package de.isibboi.metronom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import de.isibboi.metronom.click.MetronomClick;

public class MetronomPattern {
	private class Click implements Comparable<Click> {
		private final boolean high;
		private final float position;

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
	}

	private List<Click> clicks = new ArrayList<>();
	private float clickLength;
	private boolean multiVoice;

	public MetronomPattern(List<Float> highPositions, List<Float> lowPositions,
			float clickLength, boolean multiVoice) {
		for (Float position : highPositions) {
			clicks.add(new Click(true, position));
		}

		for (Float position : lowPositions) {
			clicks.add(new Click(false, position));
		}
		
		Collections.sort(clicks);

		this.clickLength = clickLength;
		this.multiVoice = multiVoice;
	}

	public float[] composePattern(MetronomClick high, MetronomClick low,
			int sampleAmount) {
		float[] line = new float[sampleAmount];

		float[] highArray = high
				.createClick((int) (sampleAmount * clickLength));
		float[] lowArray = low.createClick((int) (sampleAmount * clickLength));

		if (!clicks.isEmpty()) {
			if (multiVoice) {
				composePatternMultiVoiced(line, highArray, lowArray);
			} else {
				composePatternMonoVoiced(line, highArray, lowArray);
			}
		}

		return line;
	}

	private void composePatternMonoVoiced(float[] line, float[] high, float[] low) {
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
						clicks.get(0).position);
			}

			current = next;
		}
	}

	private void composePatternMultiVoiced(float[] line, float[] high, float[] low) {
		for (Click click : clicks) {
			writeSound(click.high ? high : low, line,
					(int) (click.position * line.length), -1);
		}
		
		float max = 0;
		
		for (float f : line) {
			f = Math.abs(f);
			if (f > max) {
				max = f;
			}
		}
		
		for (int i = 0; i < line.length; i++) {
			line[i] /= max;
		}
	}

	private void writeSound(float[] source, float[] line, int start, float limit) {
		for (int sourceIndex = 0, lineIndex = start; sourceIndex < source.length
				&& lineIndex != limit; sourceIndex++, lineIndex = (lineIndex + 1)
				% line.length) {
			line[lineIndex] += source[sourceIndex];
		}
	}
}