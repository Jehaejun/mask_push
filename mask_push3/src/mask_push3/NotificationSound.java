package mask_push3;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class NotificationSound implements Sound {
	private File sound;

	public NotificationSound(File sound) {
		this.sound = sound;
	}
	
	@Override
	public void play() {
		// TODO Auto-generated method stub
		AudioInputStream stream;
		AudioFormat format;
		DataLine.Info info;
		Clip clip;

		try {
			stream = AudioSystem.getAudioInputStream(sound);
			format = stream.getFormat();
			info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
			clip.start();

		} catch (Exception e) {
			System.out.println("err : " + e);
		}
	}

}
