
package musicaflight.chick.graphics;

import musicaflight.avianutils.AvianImage;
import musicaflight.avianutils.ImageBank;

public class LightningImages implements ImageBank {

	public static AvianImage one, two, three, four, five, six, seven;

	@Override
	public void initImages() {
		one = new AvianImage("/res/photos/lightning/1.png");
		two = new AvianImage("/res/photos/lightning/2.png");
		three = new AvianImage("/res/photos/lightning/3.png");
		four = new AvianImage("/res/photos/lightning/4.png");
		five = new AvianImage("/res/photos/lightning/5.png");
		six = new AvianImage("/res/photos/lightning/6.png");
		seven = new AvianImage("/res/photos/lightning/7.png");
	}

}
