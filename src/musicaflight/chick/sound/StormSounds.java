
package musicaflight.chick.sound;

import musicaflight.avianutils.AudioBank;
import musicaflight.avianutils.AvianSound;

public class StormSounds implements AudioBank {

	public static AvianSound dist1, dist2, dist3, dist4;
	public static AvianSound closer1, closer2, closer3, closer4;
	public static AvianSound near1, near2, near3, near4;
	public static AvianSound immed1, immed2;

	public static AvianSound rain;

	@Override
	public void initAudio() {
		dist1 = new AvianSound("/res/sounds/thunder-distant1.wav");
		dist2 = new AvianSound("/res/sounds/thunder-distant2.wav");
		dist3 = new AvianSound("/res/sounds/thunder-distant3.wav");
		dist4 = new AvianSound("/res/sounds/thunder-distant4.wav");

		closer1 = new AvianSound("/res/sounds/thunder-closer1.wav");
		closer2 = new AvianSound("/res/sounds/thunder-closer2.wav");
		closer3 = new AvianSound("/res/sounds/thunder-closer3.wav");
		closer4 = new AvianSound("/res/sounds/thunder-closer4.wav");

		near1 = new AvianSound("/res/sounds/thunder-near1.wav");
		near2 = new AvianSound("/res/sounds/thunder-near2.wav");
		near3 = new AvianSound("/res/sounds/thunder-near3.wav");
		near4 = new AvianSound("/res/sounds/thunder-near4.wav");

		immed1 = new AvianSound("/res/sounds/thunder-immediate1.wav");
		immed2 = new AvianSound("/res/sounds/thunder-immediate2.wav");

		rain = new AvianSound("/res/sounds/heavy-rain.wav", true);
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
