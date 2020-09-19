//adapted from http://www.codejava.net/coding/how-to-play-back-audio-in-java-with-examples

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * This is an example program that demonstrates how to play back an audio file
 * using the Clip in Java Sound API.
 * @author www.codejava.net
 *
 */
public class AudioPlayer implements LineListener {

	public static boolean stopAll = false;

	Thread loop = null;
	private boolean playCompleted = true;
	private boolean stop = false; 

	public boolean isPlayCompleted() {
		return playCompleted;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public static void setStopAll(boolean stopAll) {
		AudioPlayer.stopAll = stopAll;
	}

	/**
	 * Listens to the START and STOP events of the audio line.
	 */
	@Override
	public void update(LineEvent event) {
		LineEvent.Type type = event.getType();

		if (type == LineEvent.Type.STOP) {
			playCompleted = true;
		}

	}

	public void playAsynchronous(String file)
	{	    	
		stop = false;
		loop = new Thread()
		{
			public void run()
			{
				playCompleted = false;
				play(file);
				playCompleted = true;
			}
		};
		loop.start();

		loop = null;

	}	 

	void play(String audioFilePath) {
		File audioFile = new File(audioFilePath);

		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

			AudioFormat format = audioStream.getFormat();

			DataLine.Info info = new DataLine.Info(Clip.class, format);

			Clip audioClip = (Clip) AudioSystem.getLine(info);

			audioClip.addLineListener(this);

			audioClip.open(audioStream);

			audioClip.start();

			playCompleted = false;

			while (!playCompleted) {
				// wait for the playback completes
				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				if (stop || stopAll) {
					audioClip.stop();
					playCompleted = true;
				}

			}

			audioClip.close();

		} catch (UnsupportedAudioFileException ex) {
			System.out.println("The specified audio file is not supported.");
			ex.printStackTrace();
		} catch (LineUnavailableException ex) {
			System.out.println("Audio line for playing back is unavailable.");
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println("Error playing the audio file.");
			ex.printStackTrace();
		}

	}
}
