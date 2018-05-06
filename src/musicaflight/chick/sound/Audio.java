
package musicaflight.chick.sound;

import musicaflight.avianutils.AudioBank;
import musicaflight.avianutils.AvianSound;

public class Audio implements AudioBank {

	public static AvianSound openInv, closeInv, grass;

	@Override
	public void initAudio() {
		openInv = new AvianSound("/res/sounds/openInv.wav");
		closeInv = new AvianSound("/res/sounds/closeInv.wav");
		grass = new AvianSound("/res/sounds/walk/grass2.wav");
	}

	@Override
	public void setMusicVolume(float volume) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSFXVolume(float volume) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopAll() {
		// TODO Auto-generated method stub

	}

}
