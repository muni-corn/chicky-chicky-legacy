
package musicaflight.chick;

import musicaflight.avianutils.*;
import musicaflight.avianutils.AvianEase.Ease;
import musicaflight.chick.sound.Audio;
import musicaflight.chick.world.World;

public class Game {

	private static World w;

	public static boolean inventory;
	public static AvianEase invAnim = new AvianEase(.25f, Ease.CUBIC);

	static AvianRectangle r = new AvianRectangle();
	public static AvianLine l = new AvianLine();

	static AvianColor invColor = new AvianColor(AvianMath.randomInt(255), AvianMath.randomInt(255), AvianMath.randomInt(255));

	public Game() {
		w = new World("temp");
		AvianApp.addKeyListener(new AvianKeyboard() {

			@Override
			public void type(char text) {
				// TODO Auto-generated method stub

			}

			@Override
			public void repeat(int key) {
				// TODO Auto-generated method stub

			}

			@Override
			public void release(int key) {
				if (key != AvianInput.KEY_F)
					return;
				if (inventory = !inventory) {
					Audio.openInv.play();
				} else {
					Audio.closeInv.play();
				}

			}

			@Override
			public void press(int key) {
				// TODO Auto-generated method stub

			}
		});
	}

	void logic() {
		w.logic();
		if (inventory) {
			invAnim.forward();
		} else {
			invAnim.rewind();
		}
	}

	void render() {
		w.render();
		l.set((AvianApp.getWidth() - 30), (AvianApp.getHeight() - 30), (AvianApp.getWidth() - 30) + (AvianMath.sin((w.getWeather().getHours() / 12f) * 360) * 10f), (AvianApp.getHeight() - 30) - (AvianMath.cos((w.getWeather().getHours() / 12f) * 360) * 10f));
		l.render(1f);

		l.set((AvianApp.getWidth() - 30), (AvianApp.getHeight() - 30), (AvianApp.getWidth() - 30) + (AvianMath.sin(w.getWeather().getHours() * 360) * 20f), (AvianApp.getHeight() - 30) - (AvianMath.cos((w.getWeather().getHours()) * 360) * 20f));
		l.render(1f);

		float invY = (float) (AvianApp.getHeight() - (AvianApp.getHeight() * .75f * invAnim.normalizedResult()));

		AvianColor bright = AvianColor.get(invColor.getR() + 50, invColor.getG() + 50, invColor.getB() + 50);
		//		bright.setA(invAnim.normalizedResult()*255);
		//		invColor.setA(invAnim.normalizedResult()*255);

		if (invAnim.getPhase() > 0) {
			r.set(0, invY, AvianApp.getWidth(), AvianApp.getHeight() * .75f);
			r.render(bright, bright, invColor, invColor);

			//			for (int i = 0; i <= AvianAppCore.getWidth() / 64 + 1; i++) {
			//				for (int j = 0; j <= AvianApp.getHeight() * .75 / 64; j++) {
			//					Images.stripes.render(i * 64 + stripes, j * 64 + invY, 64, 64);
			//				}
			//			}
		}
	}

	public static World getWorld() {
		return w;
	}
}
