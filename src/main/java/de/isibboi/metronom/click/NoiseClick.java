package de.isibboi.metronom.click;

import de.isibboi.metronom.AudioUtil;

public class NoiseClick extends MetronomClick {
	public NoiseClick(Float frequency) {
		super(frequency);
	}

	@Override
	protected float createSample(float positionInWave, float positionInLine) {
		return (float) (Math.random() * 2 - 1) * (1 - positionInLine);
	}
	
	@Override
	protected void postProcess(float[] line) {
		AudioUtil.highPassFilter(line);
	}
}