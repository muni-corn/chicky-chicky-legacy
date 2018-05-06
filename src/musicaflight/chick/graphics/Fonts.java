
package musicaflight.chick.graphics;

import musicaflight.avianutils.AvianFont;
import musicaflight.avianutils.FontBank;

public class Fonts implements FontBank {

	public static AvianFont Sanitrixie15, Sanitrixie20;

	@Override
	public void initFonts() {
		Sanitrixie15 = new AvianFont("/res/fonts/SANITRIXIE.TTF", 15);
		Sanitrixie20 = new AvianFont("/res/fonts/SANITRIXIE.TTF", 20);
	}

}
