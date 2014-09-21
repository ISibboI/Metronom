package de.isibboi.metronom;

public final class AudioUtil {
	public static void highPassFilter(float[] line) {
		float[] xv = new float[3];
		float[] yv = new float[3];

		for (int i = 0; i < line.length; i++) {
			xv[0] = xv[1];
			xv[1] = xv[2];
			xv[2] = line[i] / 1.223443107f;
			yv[0] = yv[1];
			yv[1] = yv[2];
			yv[2] = (xv[0] + xv[2]) - 2 * xv[1] + (-0.6683689946f * yv[0])
					+ (1.6010923942f * yv[1]);
			line[i] = yv[2];
		}
	}
}