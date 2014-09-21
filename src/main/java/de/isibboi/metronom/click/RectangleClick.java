package de.isibboi.metronom.click;

import de.isibboi.metronom.AudioUtil;

public class RectangleClick extends MetronomClick {
	public RectangleClick(Float frequency) {
		super(frequency);
	}

	@Override
	protected float createSample(float positionInWave, float positionInLine) {
		return positionInWave < 0.5 ? -1 + positionInLine
				: (1 - positionInLine);
	}
	
	@Override
	protected void postProcess(float[] line) {
		AudioUtil.lowPassFilter(line);
	}
}