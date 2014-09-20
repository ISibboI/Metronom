package de.isibboi.metronom;

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class MetronomLine {
	private class Click implements Comparable<Click> {
		private boolean high;
		private float position;

		public Click(boolean high, Float position) {
			this.high = high;
			this.position = position;
		}

		@Override
		public int compareTo(Click o) {
			if (position < o.position) {
				return -1;
			} else if (position > o.position) {
				return 1;
			} else {
				return 0;
			}
		}

		public boolean isHigh() {
			return high;
		}

		public float getPosition() {
			return position;
		}
	}

	private SortedSet<Click> clicks = new TreeSet<>();
	private float clickLength;
	private boolean multiVoice;

	public MetronomLine(List<Float> highPositions, List<Float> lowPositions,
			float clickLength, boolean multiVoice) {
		for (Float position : highPositions) {
			clicks.add(new Click(true, position));
		}

		for (Float position : lowPositions) {
			clicks.add(new Click(false, position));
		}

		this.clickLength = clickLength;
		this.multiVoice = multiVoice;
	}

	public float[] composeLine(MetronomClick high, MetronomClick low,
			int sampleAmount) {
		float[] line = new float[sampleAmount];

		float[] highArray = high
				.createClick((int) (sampleAmount * clickLength));
		float[] lowArray = low.createClick((int) (sampleAmount * clickLength));

		if (!clicks.isEmpty()) {
			if (multiVoice) {
				composeLineMultiVoiced(line, highArray, lowArray);
			} else {
				composeLineMonoVoiced(line, highArray, lowArray);
			}
		}

		return line;
	}

	private void composeLineMonoVoiced(float[] line, float[] high, float[] low) {
		Iterator<Click> i = clicks.iterator();
		Click current = i.next();
		Click next = null;

		while (current != null) {
			if (i.hasNext()) {
				next = i.next();
				writeSound(current.high ? high : low, line,
						(int) (current.position * line.length), next.position);
			} else {
				next = null;
				writeSound(current.high ? high : low, line,
						(int) (current.position * line.length),
						clicks.first().position);
			}

			current = next;
		}
	}

	private void composeLineMultiVoiced(float[] line, float[] high, float[] low) {
		for (Click click : clicks) {
			writeSound(click.high ? high : low, line, (int)(click.position * line.length), -1);
		}
	}

	private void writeSound(float[] source, float[] line, int start, float limit) {
		for (int sourceIndex = 0, lineIndex = start; sourceIndex < source.length
				&& lineIndex != limit; sourceIndex++, lineIndex = (lineIndex + 1)
				% line.length) {
			line[lineIndex] = source[sourceIndex];
		}
	}
}