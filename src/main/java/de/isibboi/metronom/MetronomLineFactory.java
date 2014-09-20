package de.isibboi.metronom;

import java.util.Arrays;
import java.util.List;

public final class MetronomLineFactory {
	public static MetronomLine createQuarterLine() {
		List<Float> highClicks = Arrays.<Float>asList(0f);
		List<Float> lowClicks = Arrays.<Float>asList(0.25f, 0.5f, 0.75f);
		return new MetronomLine(highClicks, lowClicks, 0.125f, false);
	}
}