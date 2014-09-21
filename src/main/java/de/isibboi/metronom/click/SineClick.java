package de.isibboi.metronom.click;

public class SineClick extends MetronomClick {
	public SineClick(Float frequency) {
		super(frequency);
	}

	@Override
	protected float createSample(float positionInWave, float positionInLine) {
		return (float) (Math.sin(positionInWave * Math.PI) * (1 - positionInLine));
	}
}