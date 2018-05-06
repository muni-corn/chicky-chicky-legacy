
package musicaflight.chick.world;

import java.io.Serializable;

import musicaflight.avianutils.*;
import musicaflight.chick.Game;
import musicaflight.chick.Item;
import musicaflight.chick.graphics.Images;

public class Block implements Serializable {

	private static final long serialVersionUID = -4770242089624519625L;

	public static enum BlockMaterial implements Serializable {
		GRASS(
				Images.grass,
				5),
		DIRT(
				Images.dirt,
				4),
		STONE(
				Images.stone,
				250),
		AIR(
				null,
				0,
				(Item[]) null);

		AvianImage img;
		int hp;
		Item[] drops;

		BlockMaterial(AvianImage img, int hp, Item... drops) {
			this.img = img;
		}
	}

	private BlockMaterial type;
	private int row, col;
	public transient boolean hover;

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public Block(BlockMaterial type, int chunk, int row, int col) {
		this.type = type;
		this.row = row;
		this.col = col;
		x = col * 64 + (chunk * 32 * 64);
		y = 64 + (row * 64);
	}

	transient float yv = 0;
	transient float alpha;

	public BlockMaterial getBlockMaterial() {
		return type;
	}

	public void logic(int r, int c, int chunk) {
		this.row = r;
		this.col = c;
		x = AvianMath.glide(x, c * 64 + (chunk * 32 * 64), 10f);
		float fy = 64 + (r * 64);
		if (fy > y) {
			y = AvianMath.glide(y, fy, 10f);
			yv = 0;
		} else {
			yv += World.gravity;
			y += yv;
			if (y < fy) {
				y = fy;
				yv = 0;
			}
		}
		alpha = AvianMath.glide(alpha, hover ? 20 / 255f : 0, 10f);
	}

	private float x, y;

	public float getX() {
		return x;
	}

	public float getY() {
		return AvianApp.getHeight() - y;
	}

	public float getW() {
		return 64;
	}

	public float getH() {
		return 64;
	}

	public boolean onScreen() {
		return (x + Game.getWorld().getXOffset() >= -64 && x + Game.getWorld().getXOffset() <= AvianApp.getWidth()) && (getY() + Game.getWorld().getYOffset() > -64 && getY() + Game.getWorld().getYOffset() < AvianApp.getHeight());
	}

	static transient AvianRectangle w = new AvianRectangle();

	public void render() {
		if (!onScreen() || type.img == null)
			return;
		float l = Game.getWorld().getWeather().getLight();
		type.img.render((int) (x + Game.getWorld().getXOffset()), (int) (AvianApp.getHeight() - y + Game.getWorld().getYOffset()), 64, 64, l, l, l);
		if (alpha >= 1) {
			w.set((int) (x + Game.getWorld().getXOffset()), (int) (AvianApp.getHeight() - y + Game.getWorld().getYOffset()), 64, 64);
			w.render(alpha);
		}
	}

	public boolean isAir() {
		return type == BlockMaterial.AIR;
	}

	public void remove() {
		if (!hover)
			return;
		type = BlockMaterial.AIR;
	}

	//	public Block blockAbove() {
	//		
	//	}

}
