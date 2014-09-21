package de.isibboi.metronom.click;

public class TriangleClick extends MetronomClick {
	public TriangleClick(Float frequency) {
		super(frequency);
	}

	@Override
	protected float createSample(float positionInWave, float positionInLine) {
		float result;

		if (positionInWave < 0.5) {
			result = -1 + positionInWave * 4;
		} else {
			result = 3 - positionInWave * 4;
		}

		return result * (1 - positionInLine);
	}
}