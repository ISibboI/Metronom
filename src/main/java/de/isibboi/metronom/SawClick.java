package de.isibboi.metronom;

import java.util.HashMap;
import java.util.Map;

public class SawClick extends MetronomClick {
	public SawClick(float frequency) {
		super(frequency);
	}

	@Override
	protected float createSample(float positionInWave, float positionInLine) {
		return positionInWave * (1 - positionInLine);
	}
}