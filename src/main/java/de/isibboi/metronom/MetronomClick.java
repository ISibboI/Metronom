package de.isibboi.metronom;

import java.util.HashMap;
import java.util.Map;

public abstract class MetronomClick {
	private final float frequency;
	private final Map<Integer, float[]> lines = new HashMap<>();

	public MetronomClick(float frequency) {
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
			time %= 1;
			
			line[i] = createSample(time, ((float)i) / (line.length - 1));
		}
		
		lines.put(sampleAmount, line);
		return line;
	}
	
	public float getFrequency() {
		return frequency;
	}
	
	protected abstract float createSample(float positionInWave, float positionInLine);
}