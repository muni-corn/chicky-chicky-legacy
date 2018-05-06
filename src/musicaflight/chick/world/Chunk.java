
package musicaflight.chick.world;

import java.io.Serializable;

import musicaflight.avianutils.AvianApp;
import musicaflight.chick.Game;
import musicaflight.chick.world.Block.BlockMaterial;

public class Chunk implements Serializable {

	private static final long serialVersionUID = 1870362451720382757L;

	public static final int MAX_HEIGHT = 256;

	private Block[][] blocks = new Block[MAX_HEIGHT][32];

	private final int num;

	public Chunk(int n) {
		num = n;
		for (int i = 0; i < getBlocks().length; i++) {
			for (int j = 0; j < getBlocks()[i].length; j++) {
				if (i == Chunk.MAX_HEIGHT - 1)
					getBlocks()[i][j] = new Block(BlockMaterial.GRASS, n, i, j);

				else
					getBlocks()[i][j] = new Block(BlockMaterial.DIRT, n, i, j);

			}
		}
	}

	public int getNum() {
		return num;
	}

	public void logic() {
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[i].length; j++) {
				blocks[i][j].logic(i, j, num);
			}
		}
	}

	public void render() {
		if (getBlocks()[0][getBlocks()[0].length - 1].getX() + 64 + Game.getWorld().getXOffset() < 0 || getBlocks()[0][0].getX() + Game.getWorld().getXOffset() > AvianApp.getWidth()) {
			return;
		}
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[i].length; j++) {
				blocks[i][j].render();
			}
		}
	}

	public Block[][] getBlocks() {
		return blocks;
	}

}
