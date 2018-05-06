
package musicaflight.chick.graphics;

import musicaflight.avianutils.AvianImage;
import musicaflight.avianutils.ImageBank;

public class WorldImages implements ImageBank {

	public static AvianImage farmFG, farmBG, farmClouds, hills, lake, forest,
			mountains, volcano;

	@Override
	public void initImages() {
		farmFG = new AvianImage("/res/photos/worlds/farmForeground.png");
		farmBG = new AvianImage("/res/photos/worlds/farmBackground.png");
		farmClouds = new AvianImage("/res/photos/worlds/farmClouds.png");
		hills = new AvianImage("/res/photos/worlds/rollinghills.png");
		lake = new AvianImage("/res/photos/worlds/lake.png");
		forest = new AvianImage("/res/photos/worlds/forest.png");
		mountains = new AvianImage("/res/photos/worlds/mountains.png");
		volcano = new AvianImage("/res/photos/worlds/volcano.png");

	}

}
