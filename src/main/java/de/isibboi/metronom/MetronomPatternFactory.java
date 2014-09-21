package de.isibboi.metronom;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MetronomPatternFactory {
	private static final Map<String, MetronomPattern> patterns = new HashMap<>();

	static {
		patterns.put("Quarter", createQuarterPattern());
		patterns.put("Triplet", createTripletPattern());
	}

	public static MetronomPattern createQuarterPattern() {
		List<Float> highClicks = Arrays.<Float> asList(0f);
		List<Float> lowClicks = Arrays.<Float> asList(0.25f, 0.5f, 0.75f);
		return new MetronomPattern(highClicks, lowClicks, 0.125f, false);
	}

	public static MetronomPattern createTripletPattern() {
		float third = 1 / 3f * 0.25f;
		float twoThirds = 2 / 3f * 0.25f;
		List<Float> highClicks = Arrays.<Float> asList(0f, 0.25f, 0.5f, 0.75f);
		List<Float> lowClicks = Arrays.<Float> asList(0f, 0.125f, 0.25f,
				0.25f + third, 0.25f + twoThirds, 0.5f, 0.625f, 0.75f,
				0.75f + third, 0.75f + twoThirds);
		return new MetronomPattern(highClicks, lowClicks, 0.1f, true);
	}

	public static MetronomPattern createPattern(String name) {
		MetronomPattern result = patterns.get(name);

		if (result == null) {
			throw new IllegalArgumentException("Pattern '" + name
					+ "' does not exist.");
		} else {
			return result;
		}
	}

	public static Collection<String> getPatternNames() {
		return patterns.keySet();
	}
}