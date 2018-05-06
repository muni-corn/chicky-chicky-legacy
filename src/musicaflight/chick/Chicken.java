
package musicaflight.chick;

import java.io.Serializable;
import java.text.DecimalFormat;

import musicaflight.avianutils.*;
import static musicaflight.avianutils.AvianInput.*;
import musicaflight.chick.graphics.ChickenImages;
import musicaflight.chick.sound.Audio;
import musicaflight.chick.world.*;

public class Chicken implements Serializable {

	private static final long serialVersionUID = 6894435185816891545L;

	public static enum ChickenPose {

		STAND(
				ChickenImages.stand,
				1,
				500),
		WALK(
				ChickenImages.walk,
				4,
				170),
		JUMP(
				ChickenImages.jump,
				2,
				50),
		SQUAT(
				ChickenImages.squat),
		//		ONYOURMARK(
		//				ChickenImages.onyourmark),
		SPRINT(
				ChickenImages.sprint,
				4,
				150),
		SLEEP(
				ChickenImages.sleep,
				2,
				2000),
		FIGHT(
				ChickenImages.fight,
				2,
				500),
		PUSH(
				ChickenImages.push,
				4,
				500),
		DEAD(
				ChickenImages.dead),
		TIRED(
				ChickenImages.tired,
				2,
				500);

		private AvianImage img;
		private int phases, currentPhase;
		private long time = System.currentTimeMillis();
		private int msBetweenPhases;

		ChickenPose(AvianImage spriteSheet, int phases, int msBetweenPhases) {
			img = spriteSheet;
			this.phases = phases;
			this.msBetweenPhases = msBetweenPhases;
			time = System.currentTimeMillis();
		}

		ChickenPose(AvianImage sprite) {
			img = sprite;
		}

		public void logic() {
			if (phases <= 0)
				return;
			if ((System.currentTimeMillis() - time) >= msBetweenPhases) {
				time = System.currentTimeMillis();
				currentPhase++;
				if (currentPhase % 2 == 0 && (this == WALK || this == SPRINT || this == PUSH) && yv == 0) {
					playWalkSound();
				}
			}
			currentPhase %= phases;
		}

		public void playWalkSound() {
			switch (collidingBlock.getBlockMaterial()) {
				case AIR:
					break;
				case DIRT:
					Audio.grass.playWithRandomPitch(1, 1.5f);
					break;
				case GRASS:
					Audio.grass.playWithRandomPitch(1, 1.5f);
					break;
				case STONE:
					break;
				default:
					break;

			}
		}

		public AvianImage getImage() {
			if (phases > 1)
				img.crop(currentPhase * (img.getWidth() / phases), 0, (img.getWidth() / phases), img.getHeight());
			return img;
		}
	}

	transient boolean moveLeft;
	transient boolean moving;
	transient boolean sprint;
	transient boolean squat;
	transient boolean grounded;

	float health, food, xp;
	float hMax, fMax, xMax;

	public Chicken() {
		resetPosition();
		AvianApp.addMouseListener(new AvianMouse() {

			@Override
			public void press(int button, float x, float y) {
				// TODO Auto-generated method stub

			}

			@Override
			public void release(int button, float x, float y) {
				// TODO Auto-generated method stub

			}

			@Override
			public void move(float x, float y) {
				crosshairAlpha = 1;
			}

			@Override
			public void scroll(int count) {
				// TODO Auto-generated method stub

			}

		});
	}

	private void resetPosition() {
		y = ((Chunk.MAX_HEIGHT * 64)) + 15 + 64;
		x = 64;
		yv = xv = 0;
		pose = ChickenPose.JUMP;
		grounded = false;
	}

	public void keyboard() {
		if (isKeyDown(KEY_R)) {
			resetPosition();
			return;
		}
		boolean keyA = isKeyDown(KEY_A);
		boolean keyD = isKeyDown(KEY_D);
		sprint = isKeyDown(KEY_W);
		squat = isKeyDown(KEY_S);
		if (keyA && !keyD) {
			moveLeft = true;
		} else if (keyD && !keyA) {
			moveLeft = false;
		}
		moving = ((keyA && !keyD) || (keyD && !keyA)) && !squat;

		if (isKeyDown(KEY_SPACE) && yv == 0 && grounded) {
			yv = 5.37f;
			grounded = false;
		}
	}

	transient ChickenPose pose = ChickenPose.JUMP;

	private float xv;
	static float yv;
	private static float lastYV;
	private float x = 64, y = AvianApp.getHeight();

	public float getX() {
		return x;
	}

	public float getY() {
		return AvianApp.getHeight() - y;
	}

	DecimalFormat df = new DecimalFormat("0.#");

	transient int time = 0;
	transient float d = 0;
	static transient Block collidingBlock;

	public void logic() {
		keyboard();
		time--;
		if (!AvianInput.isMouseButtonDown(0)) {
			time = 0;
		}
		float walkVel = .6f;
		float sprintVel = 1.4f;
		if (squat) {
			//			if (isKeyDown(KEY_D) || isKeyDown(KEY_A)) {
			//				pose = ChickenPose.ONYOURMARK;
			//			} else {
			pose = ChickenPose.SQUAT;
			//			}
			xv = 0;
		} else if (moving) {
			xv = sprint ? sprintVel : walkVel;
			pose = sprint ? ChickenPose.SPRINT : ChickenPose.WALK;
		} else {
			xv = 0;
			pose = ChickenPose.STAND;
		}
		AvianApp.setStat("x = ", df.format(x), false);
		AvianApp.setStat("y = ", df.format(y), false);

		x += xv * (moveLeft ? -1 : 1);
		yv += World.gravity;
		y += yv;

		Chunk ch = Game.getWorld().getLand()[Game.getWorld().getLand().length - 1];

		if (x < 0) {
			x = 0;
			pose = ChickenPose.PUSH;
		} else if (x + 48 > ch.getBlocks()[0][ch.getBlocks()[0].length - 1].getX() + ch.getBlocks()[0][ch.getBlocks()[0].length - 1].getW()) {
			x = (ch.getBlocks()[0][ch.getBlocks()[0].length - 1].getX() + ch.getBlocks()[0][ch.getBlocks()[0].length - 1].getW()) - 48;
			pose = ChickenPose.PUSH;
		}
		collidingBlock = null;

		int plot = (int) (x / (32 * 64));
		for (int i = plot - 1; i < plot + 2; i++) {
			if (i < 0 || i >= Game.getWorld().getLand().length)
				continue;
			Chunk chunk = Game.getWorld().getLand()[i];
			for (int j = chunk.getBlocks().length - 1; j >= 0; j--) {
				for (int k = 0; k < chunk.getBlocks()[j].length; k++) {
					checkHover(chunk.getBlocks()[j][k]);
					fixCollision(chunk.getBlocks()[j][k]);
				}
			}
		}

		if (yv == 0 && lastYV != 0) {
			pose.playWalkSound();
		}

		lastYV = yv;

		if (yv != 0 && xv == 0) {
			pose = ChickenPose.JUMP;
		}

		pose.logic();

		crosshairAlpha -= 3 / 255f;
		if (crosshairAlpha < 50 / 255f) {
			crosshairAlpha = 50 / 255f;
		}

		d = AvianMath.glide(d, 10, 10f);
	}

	static transient AvianCircle c = new AvianCircle();

	transient float crosshairAlpha = 1f;

	private transient float aimX, aimY;

	public void render() {
		float l = Game.getWorld().getWeather().getLight();
		pose.getImage().render((int) (x + (moveLeft ? 48 : 0) + Game.getWorld().getXOffset()), (int) (AvianApp.getHeight() - y + Game.getWorld().getYOffset()), moveLeft ? -48 : 48, 52, l, l, l);

	}

	static transient AvianArc a = new AvianArc();

	public void renderGUI() {
		c.setDiameter(d);
		float dist = 24 + 60;
		float mx = AvianInput.getMouseX();
		float my = AvianInput.getMouseY();
		float cx = Game.getWorld().getXOffset() + x + 24;
		float cy = (Game.getWorld().getYOffset() + AvianApp.getHeight() - y) + 26;
		float xDist = (mx - cx) * (mx - cx);
		float yDist = (my - cy) * (my - cy);
		float mouseDist = (float) Math.sqrt(xDist + yDist);
		if (mouseDist < dist) {
			dist = mouseDist;
		}
		float angle = Math.abs(AvianMath.arctan((cx - mx) / (cy - my)));
		if (AvianInput.getMouseX() < Game.getWorld().getXOffset() + x + 24) {
			aimX = cx - AvianMath.sin(angle) * dist;
		} else {
			aimX = cx + AvianMath.sin(angle) * dist;
		}
		if (AvianInput.getMouseY() < cy) {
			aimY = cy - AvianMath.cos(angle) * dist;
		} else {
			aimY = cy + AvianMath.cos(angle) * dist;
		}
		c.setX(aimX);
		c.setY(aimY);
		c.render(true, crosshairAlpha);
		//		a.setLineWidth(1);
		//		a.setIterations(64);
		//		a.set(AvianAppCore.getWidth() / 2, AvianAppCore.getHeight() + 200, 250, 0, 360);
		//		a.render(AvianColor.get(.8f * 255f, 0, .055f * 255f));

	}

	public boolean isColliding(Block b) {
		if (b.isAir())
			return false;
		return (Math.abs((x + 48 / 2) - (b.getX() + b.getW() / 2)) * 2 < (48 + b.getW())) && (Math.abs(((AvianApp.getHeight() - y) + 52 / 2f) - (b.getY() + b.getH() / 2f)) * 2 < (52 + b.getH()));
	}

	public boolean fixCollision(Block b) {
		if (isColliding(b)) {
			float bottomPenetration = -((b.getY() - (AvianApp.getHeight() - y)) - 52);
			float topPenetration = -(((AvianApp.getHeight() - y) - b.getY()) - b.getH());
			float rightPenetration = -((x - b.getX()) - b.getW());
			float leftPenetration = -((b.getX() - x) - 48);

			float xPenetration = Math.min(rightPenetration, leftPenetration);
			float yPenetration = Math.min(topPenetration, bottomPenetration);
			if (xPenetration <= yPenetration) {
				if (leftPenetration < rightPenetration) {
					x -= leftPenetration;
				} else {
					x += rightPenetration;
				}
				pose = ChickenPose.PUSH;
				xv = 0;
			} else {
				if (topPenetration < bottomPenetration) {
					y -= topPenetration;
					yv = 0;
					//TODO ?!?!
				} else {
					y += bottomPenetration;
					grounded = true;
					yv = 0;
				}
			}
			if (collidingBlock == null) {
				collidingBlock = b;
			}
			return true;
		}

		return false;
	}

	private void checkHover(Block b) {
		if (!b.isAir() && aimX > b.getX() + Game.getWorld().getXOffset() && aimX < b.getX() + Game.getWorld().getXOffset() + b.getW() && aimY > b.getY() + Game.getWorld().getYOffset() && aimY < b.getY() + Game.getWorld().getYOffset() + b.getH()) {
			b.hover = true;
			if (time <= 0 && AvianInput.isMouseButtonDown(0)) {
				b.remove();
				time = 50;
				d = 3;
				crosshairAlpha = 255;
			}
		} else {
			b.hover = false;
		}
	}
}
