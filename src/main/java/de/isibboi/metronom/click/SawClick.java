package de.isibboi.metronom.click;

import de.isibboi.metronom.AudioUtil;

public class SawClick extends MetronomClick {
	public SawClick(Float frequency) {
		super(frequency);
	}

	@Override
	protected float createSample(float positionInWave, float positionInLine) {
		return (positionInWave * 2 - 1) * (1 - positionInLine);
	}
	
	@Override
	protected void postProcess(float[] line) {
		AudioUtil.lowPassFilter(line);
	}
}