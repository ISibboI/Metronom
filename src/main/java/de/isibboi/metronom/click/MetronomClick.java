package de.isibboi.metronom.click;

import java.util.HashMap;
import java.util.Map;

import de.isibboi.metronom.Metronom;

public abstract class MetronomClick {
	private final float frequency;
	private final Map<Integer, float[]> lines = new HashMap<>();

	public MetronomClick(Float frequency) {
		this.frequency = frequency;
	}

	public float[] createClick(final int sampleAmount) {
		float[] line = lines.get(sampleAmount);

		if (line != null) {
			return line;
		}

		line = new float[sampleAmount];

		for (int i = 0; i < line.length; i++) {
			float time = i / Metronom.SAMPLE_RATE;
			time *= frequency;
			boolean attack = (int) time == 0;
			time %= 1;

			line[i] = createSample(time, ((float) i) / (line.length - 1));

			// Add some attack time, and especially remove the click that is
			// created by 0 to 1 changes within one sample.
			if (attack) {
				line[i] *= time;
			}
		}

		postProcess(line);
		lines.put(sampleAmount, line);
		return line;
	}

	public float getFrequency() {
		return frequency;
	}

	protected abstract float createSample(float positionInWave,
			float positionInLine);

	protected void postProcess(float[] line) {
	}
}