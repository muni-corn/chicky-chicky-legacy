
package musicaflight.chick.world;

import java.io.Serializable;

import musicaflight.avianutils.AvianApp;
import musicaflight.avianutils.AvianMath;
import musicaflight.chick.Chicken;
import musicaflight.chick.Game;

public class World implements Serializable {

	private static final long serialVersionUID = -4618792860766240936L;

	private Climate weather = new Climate();

	private Chicken c;
	private float x, y = Chunk.MAX_HEIGHT * 64;

	public float getXOffset() {
		return x;
	}

	public float getYOffset() {
		return y;
	}

	public static transient float gravity = -.15f;

	private Chunk[] land = new Chunk[3];

	private String name;

	//	private World() {
	//
	//	}

	public World(String name) {
		for (int i = 0; i < land.length; i++) {
			land[i] = new Chunk(i);
		}
		c = new Chicken();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void logic() {
		//		syncAudio();
		weather.logic();
		c.logic();
		int plot = (int) (c.getX() / (32 * 64));
		for (int i = plot - 1; i < plot + 2; i++) {
			if (i < 0 || i >= land.length)
				continue;
			land[i].logic();
		}

		float newX = 0;
		Chunk ch = land[land.length - 1];
		if (c.getX() + 24 > AvianApp.getWidth() / 2f) {
			newX = -(c.getX() + 24 - (AvianApp.getWidth() / 2f));
		}

		x = AvianMath.glide(x, newX, Game.inventory ? 1f : 30f);
		if (ch.getBlocks()[0][ch.getBlocks()[0].length - 1].getX() + ch.getBlocks()[0][ch.getBlocks()[0].length - 1].getW() + x < AvianApp.getWidth()) {
			x = AvianApp.getWidth() - (ch.getBlocks()[0][ch.getBlocks()[0].length - 1].getX() + ch.getBlocks()[0][ch.getBlocks()[0].length - 1].getW());
		}

		float newY = 0;
		float n = (float) ((AvianApp.getHeight() / 2f) * (1f - Game.invAnim.normalizedResult()) + (AvianApp.getHeight() / 8f) * Game.invAnim.normalizedResult());

		if (c.getY() < n) {
			newY = -(c.getY() + 26 - n);
		}

		y = AvianMath.glide(y, newY, Game.inventory ? 1f : 30f);
		if (ch.getBlocks()[0][ch.getBlocks()[0].length - 1].getY() + ch.getBlocks()[0][ch.getBlocks()[0].length - 1].getH() + y < AvianApp.getHeight()) {
			y = AvianApp.getHeight() - (ch.getBlocks()[0][ch.getBlocks()[0].length - 1].getY() + ch.getBlocks()[0][ch.getBlocks()[0].length - 1].getH());
		}

		AvianApp.setStat("World x = ", x, false);
		AvianApp.setStat("World y = ", y, false);
	}

	public void render() {
		weather.renderSky();
		c.render();
		weather.renderPrecip();

		for (int i = 0; i < land.length; i++) {
			land[i].render();
		}

		c.renderGUI();

	}

	public Climate getWeather() {
		return weather;
	}

	public Chunk[] getLand() {
		return land;
	}

	//TODO
	public void save() {
		//		String filepath = AvianAppCore.getAppDirectoryPath() + name + ".chickyworld";

		//		File f = new File(filepath);
	}

	//TODO
	public void load(String n) {
		//		String filepath = AvianAppCore.getAppDirectoryPath() + name + ".chickyworld";
		//		World w = new World();
	}

}
