package de.isibboi.metronom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import de.isibboi.metronom.click.MetronomClick;

public class MetronomCLI {
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws IOException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		Option high = OptionBuilder
				.hasArgs(2)
				.withDescription(
						"The name and frequency of the click that should be used for up.")
				.create('u');
		Option low = OptionBuilder
				.hasArgs(2)
				.withDescription(
						"The name and frequency of the click that should be used for down.")
				.create('d');
		Option pattern = OptionBuilder.hasArg()
				.withDescription("The pattern that should be used.")
				.create('p');
		Option rateOption = OptionBuilder.hasArg()
				.withDescription("The rate of the metronome.").create('r');
		Option help = OptionBuilder.withLongOpt("help")
				.withDescription("Prints this help message.").create('h');

		Options options = new Options();
		options.addOption(high);
		options.addOption(low);
		options.addOption(pattern);
		options.addOption(rateOption);
		options.addOption(help);

		try {
			CommandLineParser parser = new GnuParser();
			CommandLine commandLine = parser.parse(options, args);

			if (hasOption(help, commandLine)) {
				new HelpFormatter().printHelp("java -jar <jarfile>", options);
			}

			MetronomClick highClick = (MetronomClick) Class
					.forName(
							"de.isibboi.metronom.click."
									+ getString(0, high, commandLine, "Saw")
									+ "Click")
					.getConstructor(Float.class)
					.newInstance(getFloat(1, high, commandLine, 1, 22_000, 880));

			MetronomClick lowClick = (MetronomClick) Class
					.forName(
							"de.isibboi.metronom.click."
									+ getString(0, low, commandLine, "Saw")
									+ "Click").getConstructor(Float.class)
					.newInstance(getFloat(1, low, commandLine, 1, 22_000, 440));

			MetronomPattern metronomPattern = MetronomPatternFactory
					.createPattern(getString(pattern, commandLine, "Quarter"));

			int rate = getInt(rateOption, commandLine, 1, 1000, 60);

			Metronom m = new Metronom(rate, highClick, lowClick,
					metronomPattern);
			m.start();
			System.out.println("Hit enter to exit");

			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			in.readLine();
			m.stop();
		} catch (ParseException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private static float getFloat(int position, Option o,
			CommandLine commandLine, float min, float max, float defaultValue)
			throws ParseException {
		String value = getString(position, o, commandLine);

		if (value == null) {
			return defaultValue;
		} else {
			try {
				return Float.parseFloat(value);
			} catch (NumberFormatException e) {
				throw new ParseException("Option " + o.getOpt() + "["
						+ position + "] has to be a float. (Is: " + value + ")");
			}
		}
	}

	private static String getString(int position, Option o,
			CommandLine commandLine, String defaultValue) {
		if (!hasOption(o, commandLine)) {
			return defaultValue;
		}

		if (commandLine.getOptionValues(o.getOpt()).length <= position) {
			return defaultValue;
		}

		return commandLine.getOptionValues(o.getOpt())[position];
	}

	private static String getString(int position, Option o,
			CommandLine commandLine) {
		return getString(position, o, commandLine, null);
	}

	@SuppressWarnings("unused")
	private static int getInt(int position, Option o, CommandLine commandLine,
			int defaultValue) throws ParseException {
		String value = getString(position, o, commandLine);

		if (value == null) {
			return defaultValue;
		} else {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException e) {
				throw new ParseException("Option " + o.getOpt() + "["
						+ position + "] has to be an integer. (Is: " + value
						+ ")");
			}
		}
	}

	private static boolean hasOption(Option o, CommandLine commandLine) {
		return commandLine.hasOption(o.getOpt());
	}

	private static String getString(Option o, CommandLine commandLine)
			throws ParseException {
		String value = commandLine.getOptionValue(o.getOpt());

		if (value == null) {
			throw new ParseException("Option " + o.getOpt() + " is missing.");
		} else {
			return value;
		}
	}

	private static String getString(Option o, CommandLine commandLine,
			String defaultValue) throws ParseException {
		if (hasOption(o, commandLine)) {
			return getString(o, commandLine);
		} else {
			return defaultValue;
		}
	}

	private static int getInt(Option o, CommandLine commandLine)
			throws ParseException {
		try {
			return Integer.parseInt(getString(o, commandLine));
		} catch (NumberFormatException e) {
			throw new ParseException("Option " + o.getOpt()
					+ " has to be an integer. (Is: "
					+ commandLine.getOptionValue(o.getOpt()) + ")");
		}
	}

	private static int getInt(Option o, CommandLine commandLine, int min,
			int max) throws ParseException {
		int value = getInt(o, commandLine);

		if (value < min) {
			throw new ParseException("Option " + o.getOpt() + " is too small: "
					+ value + " < " + min);
		}

		if (value > max) {
			throw new ParseException("Option " + o.getOpt() + " is too large: "
					+ value + " > " + max);
		}

		return value;
	}

	private static int getInt(Option o, CommandLine commandLine, int min,
			int max, int defaultValue) throws ParseException {
		if (hasOption(o, commandLine)) {
			return getInt(o, commandLine, min, max);
		} else {
			return defaultValue;
		}
	}
}