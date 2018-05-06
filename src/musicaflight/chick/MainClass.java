
package musicaflight.chick;

import static org.lwjgl.opengl.GL11.*;

import musicaflight.avianutils.*;
import musicaflight.chick.graphics.*;
import musicaflight.chick.sound.Audio;
import musicaflight.chick.sound.StormSounds;

public class MainClass extends AvianApp {

	@Override
	public void construct() {
		g = new Game();
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
				// TODO Auto-generated method stub

			}

			@Override
			public void press(int key) {
			}
		});
	}

	@Override
	public void setupGL() {
		glEnable(GL_TEXTURE_2D);
		//		glEnable(GL_STENCIL_TEST);
		glShadeModel(GL_SMOOTH);
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_LIGHTING);

		glLightModelfv(GL_LIGHT_MODEL_AMBIENT, AvianUtils.asFlippedFloatBuffer(0f, 0f, 0f, 1f));

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	static Game g;

	public void keyboard() {

	}

	public void mouse() {

	}

	@Override
	public void logic() {
		g.logic();
	}

	@Override
	public void render() {
		getCam().applyTranslations();
		getCam().applyAvianOrthoMatrix();

		g.render();
	}

	@Override
	public String customTitle() {
		return "Chicky Chicky";
	}

	public void checkAudio() {
	}

	public static void main(String[] args) {
		MainClass main = new MainClass();
		main.setAppNameAndVersion("Chicky Chicky", "");
		main.addImageBank(new ChickenImages());
		main.addImageBank(new Images());
		main.addImageBank(new LightningImages());
		main.addAudioBank(new Audio());
		main.addAudioBank(new StormSounds());
		main.addFontBank(new Fonts());
		main.setResizable(true);
		main.setSplash("/res/photos/splash.png");
		//		main.setIcons("/res/photos/icon16.png", "/res/photos/icon32.png");
		AvianApp.addShutdownTask(new ShutdownTask() {

			@Override
			public void run() {
				Game.getWorld().save();
			}
		});
		main.start();
	}

}
