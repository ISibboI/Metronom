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
	}

	public static MetronomPattern createQuarterPattern() {
		List<Float> highClicks = Arrays.<Float> asList(0f);
		List<Float> lowClicks = Arrays.<Float> asList(0.25f, 0.5f, 0.75f);
		return new MetronomPattern(highClicks, lowClicks, 0.125f, false);
	}

	public static MetronomPattern createPattern(String name) {
		MetronomPattern result = patterns.get(name);
		
		if (result == null) {
			throw new IllegalArgumentException("Pattern '" + name + "' does not exist.");
		} else {
			return result;
		}
	}
	
	public static Collection<String> getPatternNames() {
		return patterns.keySet();
	}
}