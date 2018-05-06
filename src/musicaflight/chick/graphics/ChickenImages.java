
package musicaflight.chick.graphics;

import musicaflight.avianutils.AvianImage;
import musicaflight.avianutils.ImageBank;

public class ChickenImages implements ImageBank {

	public static AvianImage walk, stand, squat, /*onyourmark,*/run, jump,
			sprint, sleep, push, dead, fight, tired;

	@Override
	public void initImages() {
		sprint = new AvianImage("/res/photos/chicken/sprint.png");
		walk = new AvianImage("/res/photos/chicken/walk.png");
		tired = new AvianImage("/res/photos/chicken/tired.png");
		stand = new AvianImage("/res/photos/chicken/stand.png");
		squat = new AvianImage("/res/photos/chicken/squat.png");
		//		onyourmark = new AvianImage("/res/photos/chicken/onyourmark.png");
		//		sleep = new AvianImage("/res/photos/chicken/sleep.png");
		push = new AvianImage("/res/photos/chicken/push.png");
		jump = new AvianImage("/res/photos/chicken/jump.png");
	}

}
