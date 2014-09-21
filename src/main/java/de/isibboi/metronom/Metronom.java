package de.isibboi.metronom;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import de.isibboi.metronom.click.MetronomClick;

public class Metronom implements Runnable {
	public static final float SAMPLE_RATE = 44100;
	private static final int SAMPLE_SIZE_IN_BITS = 16;

	private SourceDataLine speaker;
	private AudioFormat audioFormat;

	private int rate;
	private MetronomClick high;
	private MetronomClick low;
	private MetronomPattern line;

	private Thread self;
	private volatile boolean stop;

	public Metronom(int rate, MetronomClick high, MetronomClick low,
			MetronomPattern line) {
		try {
			audioFormat = createAudioFormat();
			DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class,
					audioFormat);
			speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

		this.rate = rate;
		this.high = high;
		this.low = low;
		this.line = line;
	}

	public void start() {
		if (self != null) {
			return;
		}

		self = new Thread(this);
		self.start();
	}

	public void stop() {
		if (self == null) {
			return;
		}

		stop = true;
	}

	public void run() {
		stop = false;

		final int sampleCount = (int) (SAMPLE_RATE * 60 * 4 / rate);		
		final ByteBuffer audioSamples = ByteBuffer.allocate(2 * sampleCount);
		
		float[] rawLine = line.composePattern(high, low, sampleCount);
		ShortBuffer shortAudioSamples = audioSamples.asShortBuffer();
		
		for (float sample : rawLine) {
			shortAudioSamples.put((short) (sample * Short.MAX_VALUE));
		}
		
		rawLine = null;
		shortAudioSamples = null;
		
		try {
			speaker.open();
			speaker.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			return;
		}

		while (!stop) {
			speaker.write(audioSamples.array(), 0, audioSamples.array().length);
		}
		
		speaker.stop();
		speaker.close();

		self = null;
	}

	private AudioFormat createAudioFormat() {
		AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
		int channels = 1;
		int frameSize = SAMPLE_SIZE_IN_BITS / 8;
		float frameRate = SAMPLE_RATE;
		boolean bigEndian = true;
		return new AudioFormat(encoding, SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, channels,
				frameSize, frameRate, bigEndian);
	}
}