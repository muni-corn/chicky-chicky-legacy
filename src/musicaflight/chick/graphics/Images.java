
package musicaflight.chick.graphics;

import musicaflight.avianutils.AvianImage;
import musicaflight.avianutils.ImageBank;

public class Images implements ImageBank {

	public static AvianImage grass, dirt, stone;

	public static AvianImage sunrise, day, sunset, night;

	public static AvianImage rain;
	
	public static AvianImage stripes;

	@Override
	public void initImages() {
		grass = new AvianImage("/res/photos/blocks/grass.png");
		dirt = new AvianImage("/res/photos/blocks/dirt.png");
		stone = new AvianImage("/res/photos/blocks/stone.png");

		sunrise = new AvianImage("/res/photos/skyColor/sunrise.png");
		day = new AvianImage("/res/photos/skyColor/day.png");
		sunset = new AvianImage("/res/photos/skyColor/sunset.png");
		night = new AvianImage("/res/photos/skyColor/night.png");

		rain = new AvianImage("/res/photos/rain.png");
		
		stripes = new AvianImage("/res/photos/stripes.png");

	}

}
