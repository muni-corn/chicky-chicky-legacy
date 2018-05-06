
package musicaflight.chick.world;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;

import musicaflight.avianutils.*;
import musicaflight.avianutils.AvianEase.Direction;
import musicaflight.avianutils.AvianEase.Ease;
import musicaflight.chick.Game;
import musicaflight.chick.graphics.Images;
import musicaflight.chick.graphics.LightningImages;
import musicaflight.chick.sound.StormSounds;

public class Climate implements Serializable {

	private static final long serialVersionUID = 8502251806805085802L;

	public enum Weather {
		CLEAR(
				Precip.NONE),
		PARTLY_CLOUDY(
				Precip.NONE),
		MOSTLY_CLOUDY(
				Precip.NONE),
		OVERCAST(
				Precip.NONE),
		LIGHT_RAIN(
				Precip.RAIN),
		RAIN(
				Precip.RAIN),
		THUNDERSTORM(
				Precip.RAIN);
		//		HAIL(
		//				Precip.HAIL),
		//		SNOW(
		//				Precip.SNOW);

		Precip p;

		Weather(Precip p) {
			this.p = p;
		}

		public Weather next() {
			Weather[] vals = values();
			if (this.ordinal() == vals.length - 1) {
				return this;
			}
			return vals[this.ordinal() + 1];
		}

		Weather previous() {
			Weather[] vals = values();
			if (this.ordinal() == 0) {
				return this;
			}
			return vals[this.ordinal() - 1];
		}

	}

	private enum Precip {
		RAIN,
		HAIL,
		SNOW,
		NONE;
	}

	private float hours;
	float light = 1f;

	static transient AvianLine l = new AvianLine();

	static transient AvianRectangle r = new AvianRectangle();

	private transient int counter;
	private transient float stars = 0;

	Weather weather = Weather.CLEAR;

	private ArrayList<Cloud> clouds = new ArrayList<Cloud>();
	private ArrayList<Lightning> strikes = new ArrayList<Lightning>();
	private ArrayList<Raindrop> rain = new ArrayList<Raindrop>();

	private long cloudTime;
	private float cloudDarkness;

	private transient long lightningTime;
	private transient long nextStrike;

	private transient long rainTime;
	private transient int rainCapacity = 1;

	private float chanceOfWorsening = AvianMath.randomFloat();
	private float nextChange = AvianMath.randomFloat() * 500;
	private float weatherTime;

	public float getHours() {
		return hours;
	}

	public float getLight() {
		return light;
	}

	void lightRain() {
		if (cloudDarkness >= .6f) {
			rainCapacity = 10 * (AvianApp.getWidth() / 100);

			rainTime++;
			if (rainTime % 10 == 0 && rain.size() < rainCapacity) {
				rain.add(new Raindrop());
			}

			if (!StormSounds.rain.isPlaying()) {
				StormSounds.rain.play();
			}
		}
	}

	void heavyRain() {
		if (cloudDarkness >= .7f) {
			rainCapacity = 50 * (AvianApp.getWidth() / 100);

			rainTime++;
			if (rainTime % 5 == 0 && rain.size() < rainCapacity) {
				rain.add(new Raindrop());
			}

			if (!StormSounds.rain.isPlaying()) {
				StormSounds.rain.play();
			}
		}
	}

	void thunderstorm() {
		if (cloudDarkness >= .8f) {
			lightningTime++;
			if (lightningTime > nextStrike) {
				strikes.add(new Lightning());
				nextStrike += 500 + AvianMath.randomInt(2500);
			}
		}
	}

	void changeWeather() {
		float spinner = AvianMath.randomFloat();
		if (spinner < chanceOfWorsening) {
			weather = weather.next();
		} else {
			weather = weather.previous();
		}
		chanceOfWorsening = AvianMath.randomFloat();
	}

	DecimalFormat df = new DecimalFormat("00");

	public void logic() {
		//		Calendar cal = Calendar.getInstance();

		//		hours = cal.get(Calendar.HOUR_OF_DAY) + (cal.get(Calendar.MINUTE) / 60f) + (cal.get(Calendar.SECOND) / 3600f) + (cal.get(Calendar.MILLISECOND) / (1000f * 60f * 60f));
		hours += 24f / (20 * 60 * 100);
		hours %= 24;
		AvianApp.setStat("Game time: ", (int) hours + ":" + df.format((hours % 1f) * 60), false);

		weatherTime += .01f;

		if (weatherTime >= nextChange) {
			changeWeather();
			nextChange += AvianMath.randomFloat() * (500);
		}

		AvianApp.setStat("Weather is ", weather, true);
		AvianApp.setStat("", (int) (chanceOfWorsening * 100) + "%", "chance of " + weather.next().name() + " in " + (int) (nextChange - weatherTime) + " seconds", true);

		float minLight = .3f;

		float cloudCover = 0;

		Iterator<Cloud> iter = clouds.iterator();
		while (iter.hasNext()) {
			Cloud c = iter.next();
			c.logic();
			if (c.gone()) {
				iter.remove();
			} else {
				if (!c.onScreen())
					continue;
				float cloudX = c.x;
				float cloudW = c.w;
				if (cloudX + c.w > AvianApp.getWidth()) {
					cloudW = AvianApp.getWidth() - cloudX;
				} else if (cloudX < 0) {
					cloudW += cloudX;
				}
				float cloudArea = c.h * cloudW;
				cloudCover += ((cloudArea / (AvianApp.getWidth() * AvianApp.getHeight())) * (150f / 255f) * (1f - minLight)) / 5f;
			}
		}

		cloudDarkness = AvianMath.glide(cloudDarkness, cloudCover, 100f);

		int interval = 10000;

		switch (weather) {
			case LIGHT_RAIN:
				lightRain();
				interval = 300;
				break;
			case MOSTLY_CLOUDY:
				interval = 750;
				break;
			case OVERCAST:
				interval = 300;
				break;
			case PARTLY_CLOUDY:
				interval = 2000;
				break;
			case RAIN:
				interval = 200;
				heavyRain();
				break;
			case CLEAR:
				interval = 100000;
				break;
			case THUNDERSTORM:
				interval = 200;
				thunderstorm();
				heavyRain();
				break;
			default:
				break;

		}
		float volume = rain.size() / (50f * (AvianApp.getWidth() / 100f));
		StormSounds.rain.setVolume(AvianMath.glide(StormSounds.rain.getVolume(), volume, 100f));

		AvianApp.setStat("Raindrop capacity = ", rainCapacity, true);
		AvianApp.setStat("Raindrops = ", rain.size(), true);

		Iterator<Lightning> lightnIter = strikes.iterator();
		while (lightnIter.hasNext()) {
			Lightning bolt = lightnIter.next();
			bolt.logic();
			if (bolt.time > bolt.miles * 5) {
				lightnIter.remove();
			}
		}

		Iterator<Raindrop> rainIter = rain.iterator();
		while (rainIter.hasNext()) {
			Raindrop drop = rainIter.next();
			drop.logic();
			if (drop.y - 30 > AvianApp.getHeight()) {
				if ((rain.size() > rainCapacity || weather.p == Precip.NONE) && cloudTime % 10 == 0)
					rainIter.remove();
				else {
					drop.reset();
				}
			}
		}

		AvianApp.setStat("Cloud interval is ", interval, true);

		cloudTime++;

		if (cloudTime >= interval) {
			clouds.add(new Cloud());
			cloudTime = 0;
		}

		AvianApp.setStat("clouds", clouds.size(), true);

		if (hours >= 6 && hours < 12) {
			light = AvianMath.sin(((hours - 6f) / 6f) * 90f) * (1f - minLight);
		} else if (hours >= 15 && hours < 21) {
			light = AvianMath.cos(((hours - 15f) / 6f) * 90f) * (1f - minLight);
		} else if (hours >= 12 && hours < 15) {
			light = (1f - minLight);
		} else {
			light = 0;
		}
		light += minLight;
		light -= cloudDarkness;
		if (light < minLight)
			light = minLight;
		AvianApp.setStat("Light at", Math.round(light * 100000f) / 1000f + "%", true);
	}

	transient AvianColor sky = new AvianColor(0, 0, 0);

	public void renderSky() {

		stars -= AvianApp.getDeltaRenderTime() / 2f;

		if (stars < -512 * 4) {
			stars = 0;
		}

		float sunrise = 0;
		if (hours >= 6 && hours < 9) {
			sunrise = AvianMath.sin(((hours - 6f) / 3f) * 90f);
		} else if (hours >= 9 && hours <= 12) {
			sunrise = AvianMath.cos(((hours - 9f) / 3f) * 90f);
		}

		float day = 0;
		if (hours >= 9 && hours < 12) {
			day = AvianMath.sin(((hours - 9f) / 3f) * 90f);
		} else if (hours >= 15 && hours < 18) {
			day = AvianMath.cos(((hours - 15f) / 3f) * 90f);
		} else if (hours >= 12 && hours < 15) {
			day = 1f;
		}

		float sunset = 0;
		if (hours >= 15 && hours < 18) {
			sunset = AvianMath.sin(((hours - 15f) / 3f) * 90f);
		} else if (hours >= 18 && hours <= 21) {
			sunset = AvianMath.cos(((hours - 18f) / 3f) * 90f);
		}

		float night = 0;
		if (hours >= 18 && hours < 21) {
			night = AvianMath.sin(((hours - 18f) / 3f) * 90f);
		} else if (hours >= 6 && hours < 9) {
			night = AvianMath.cos(((hours - 6f) / 3f) * 90f);
		} else if (hours >= 21 || hours < 6) {
			night = 1f;
		}

		counter++;
		if (counter % 1 == 0) {
			r.set(AvianMath.randomInt(AvianApp.getWidth()), AvianMath.randomInt(AvianApp.getHeight()), AvianMath.randomInt(AvianApp.getWidth() / 4), AvianMath.randomInt(AvianApp.getHeight() / 4));
		}

		if (night != 0) {
			Images.night.render(0, 0, 512 * 4, 512 * 4, night);
		}
		r.render(0, .4f);
		if (sunrise != 0) {
			Images.sunrise.render(0, 0, AvianApp.getWidth(), AvianApp.getHeight(), sunrise);
		}
		if (day != 0) {
			Images.day.render(0, 0, AvianApp.getWidth(), AvianApp.getHeight(), day);
		}
		if (sunset != 0) {
			Images.sunset.render(0, 0, AvianApp.getWidth(), AvianApp.getHeight(), sunset);
		}
		r.set((AvianApp.getWidth() / 2) + AvianMath.cos(((hours - 7f) / 13f) * 180f) * (AvianApp.getWidth() / 2) - 40, AvianApp.getHeight() - AvianMath.sin(((hours - 7f) / 13f) * 180f) * (AvianApp.getWidth() / 2) - 40, 80, 80);

		float foo = (AvianMath.sin(((hours - 7f) / 13f) * 180f));
		AvianApp.setStat("foo =", foo, false);
		r.render(1, foo * 250f / 255f, foo * 200f / 255f);

		for (int i = 0; i < clouds.size(); i++) {
			Cloud cl = clouds.get(i);
			cl.render();
		}

		for (int i = 0; i < strikes.size(); i++) {
			strikes.get(i).render();
		}

	}

	public void renderPrecip() {
		for (int i = 0; i < rain.size(); i++) {
			rain.get(i).render();
		}
	}

	public void renderGlare() {
		float sunX = (2 * r.getX() + r.getW() - AvianApp.getWidth()) / AvianApp.getWidth();
		float sunY = (2 * r.getY() + r.getH() - AvianApp.getHeight()) / AvianApp.getHeight();
		float sunW = r.getW();
		float sunH = r.getH();

		float foo = (1 - AvianMath.sin(((hours - 7f) / 13f) * 180f));

		float newX = ((-sunX * AvianApp.getWidth()) + AvianApp.getWidth() - sunW * .5f) / 2f;
		float newY = ((-sunY * AvianApp.getHeight()) + AvianApp.getHeight() - sunH * .5f) / 2f;
		r.set(newX, newY, sunW * .5f, sunH * .5f);
		r.render(1, 1 - foo * 50f / 255f, 1 - foo * 150f / 255f, 100);

		newX = (((-sunX * (2f / 3f)) * AvianApp.getWidth()) + AvianApp.getWidth() - sunW * .5f * (2f / 3f)) / 2f;
		newY = (((-sunY * (2f / 3f)) * AvianApp.getHeight()) + AvianApp.getHeight() - sunH * .5f * (2f / 3f)) / 2f;
		r.set(newX, newY, sunW * .5f * (2f / 3f), sunH * .5f * (2f / 3f));
		r.render(255, 255 - foo * 50, 255 - foo * 150, 100);

		newX = (((sunX * (2f / 3f)) * AvianApp.getWidth()) + AvianApp.getWidth() - sunW * .5f * (2f / 3f)) / 2f;
		newY = (((sunY * (2f / 3f)) * AvianApp.getHeight()) + AvianApp.getHeight() - sunH * .5f * (2f / 3f)) / 2f;
		r.set(newX, newY, sunW * .5f * (2f / 3f), sunH * .5f * (2f / 3f));
		r.render(255, 255 - foo * 50, 255 - foo * 150, 100);

		newX = (((-sunX * (1f / 3f)) * AvianApp.getWidth()) + AvianApp.getWidth() - sunW * .5f * (1f / 3f)) / 2f;
		newY = (((-sunY * (1f / 3f)) * AvianApp.getHeight()) + AvianApp.getHeight() - sunH * .5f * (1f / 3f)) / 2f;
		r.set(newX, newY, sunW * .5f * (1f / 3f), sunH * .5f * (1f / 3f));
		r.render(255, 255 - foo * 50, 255 - foo * 150, 100);

		newX = (((sunX * (1f / 3f)) * AvianApp.getWidth()) + AvianApp.getWidth() - sunW * .5f * (1f / 3f)) / 2f;
		newY = (((sunY * (1f / 3f)) * AvianApp.getHeight()) + AvianApp.getHeight() - sunH * .5f * (1f / 3f)) / 2f;
		r.set(newX, newY, sunW * .5f * (1f / 3f), sunH * .5f * (1f / 3f));
		r.render(255, 255 - foo * 50, 255 - foo * 150, 100);
	}

	transient float wind;

	public class Cloud implements Serializable {

		private static final long serialVersionUID = 80567208716927262L;
		float x, y, w, h, xv;

		int lastW, lastH;

		float coverage;

		Cloud() {
			if (weather.p != Precip.NONE) {
				coverage = (AvianMath.randomFloat() * .3f) + .2f;
			} else {
				coverage = (AvianMath.randomFloat() * .15f) + .1f;
			}
			h = (float) Math.sqrt((coverage * AvianApp.getWidth() * AvianApp.getHeight()) / 4);
			w = h * 4f;
			x = 1;
			y = AvianMath.randomFloat();
			xv = -(AvianMath.randomFloat() * .1f) - .15f;
		}

		public void logic() {
			if (lastW != AvianApp.getWidth() || lastH != AvianApp.getHeight()) {
				h = (float) Math.sqrt((coverage * AvianApp.getWidth() * AvianApp.getHeight()) / 4);
				w = h * 4f;
			}

			lastW = AvianApp.getWidth();
			lastH = AvianApp.getHeight();

			x += xv / 1000f;
		}

		public boolean gone() {
			return x * AvianApp.getWidth() + w <= 0;
		}

		public boolean onScreen() {
			return x * AvianApp.getWidth() < AvianApp.getWidth() && x * AvianApp.getWidth() + w > 0;
		}

		public void render() {
			if (!onScreen())
				return;
			r.set(x * AvianApp.getWidth(), (y * AvianApp.getHeight()) - (h / 2f), w, h);
			r.render(light, light, light, 150 / 255f);
		}
	}

	class Lightning implements Serializable {

		private static final long serialVersionUID = -5536252198213400627L;
		float miles;
		AvianEase e = new AvianEase(.1f, Ease.ELASTIC, Direction.IN);
		float rand;

		boolean flipped, instant;
		AvianImage bolt;
		AvianSound thunder;
		float time;
		float x = AvianMath.randomFloat();
		float y = AvianMath.randomFloat();
		float w = AvianMath.randomFloat() * .5f + .5f;
		float h = AvianMath.randomFloat() * .5f + .5f;

		public Lightning() {
			int spinner = AvianMath.randomInt(10000);

			int thunderSpinner = AvianMath.randomInt(4);

			if (spinner < 4000) {
				miles = 10 + (rand = AvianMath.randomFloat()) * 5;
				switch (thunderSpinner) {
					case 0:
						thunder = StormSounds.dist1;
						break;
					case 1:
						thunder = StormSounds.dist2;
						break;
					case 2:
						thunder = StormSounds.dist3;
						break;
					case 3:
						thunder = StormSounds.dist4;
						break;
				}
			} else if (spinner >= 4000 && spinner < 8000) {
				miles = 5f + (rand = AvianMath.randomFloat()) * 5f;
				switch (thunderSpinner) {
					case 0:
						thunder = StormSounds.closer1;
						break;
					case 1:
						thunder = StormSounds.closer2;
						break;
					case 2:
						thunder = StormSounds.closer3;
						break;
					case 3:
						thunder = StormSounds.closer4;
						break;
				}
			} else if (spinner >= 8000 && spinner < 9999) {
				miles = .5f + (rand = AvianMath.randomFloat()) * 4.5f;
				switch (thunderSpinner) {
					case 0:
						thunder = StormSounds.near1;
						break;
					case 1:
						thunder = StormSounds.near2;
						break;
					case 2:
						thunder = StormSounds.near3;
						break;
					case 3:
						thunder = StormSounds.near4;
						break;
				}
			} else if (spinner == 9999) {
				miles = 0;
				instant = true;
				thunder = (thunderSpinner % 2 == 0) ? StormSounds.immed1 : StormSounds.immed2;
			}

			int boltSpinner = AvianMath.randomInt(7);
			switch (boltSpinner) {
				case 0:
					bolt = LightningImages.one;
					break;
				case 1:
					bolt = LightningImages.two;
					break;
				case 2:
					bolt = LightningImages.three;
					break;
				case 3:
					bolt = LightningImages.four;
					break;
				case 4:
					bolt = LightningImages.five;
					break;
				case 5:
					bolt = LightningImages.six;
					break;
				case 6:
					bolt = LightningImages.seven;
					break;
			}

			flipped = spinner % 2 == 0;

		}

		int ticks;

		float alpha;

		void logic() {
			ticks++;
			if (ticks % 4 == 0 && ticks < 25) {
				alpha = 1;
			} else {
				alpha = 0;
			}
			time += AvianApp.getDeltaLogicTime();
			if (time > miles * 5) {
				if (miles >= 10) {
					thunder.setVolume(1f - rand);
				} else {
					thunder.setVolume(1);
				}
				thunder.play();
			}
		}

		AvianRectangle c = new AvianRectangle();

		void render() {
			float height = bolt.getHeight() / (miles > 1 ? miles : 1);
			c.set(0, 0, AvianApp.getWidth(), AvianApp.getHeight());
			c.render((alpha * (200f / 255f)) / (miles > 1 ? miles : 1));
			bolt.render(x * AvianApp.getWidth() - bolt.getWidth() / 2, instant ? AvianApp.getHeight() - height : y * (AvianApp.getHeight() / 8), (flipped ? -1 : 1) * height * bolt.getImageAspectRatio(), height, alpha);
		}
	}

	class Raindrop implements Serializable {
		private static final long serialVersionUID = 4943829658942860684L;
		float x = AvianMath.randomInt(AvianApp.getWidth());
		float y = -AvianMath.randomInt(500);
		boolean flipped = AvianMath.randomInt(2) == 0;

		float n = AvianMath.randomFloat() * 2.5f;

		private boolean collided = false;

		void logic() {
			y += 10 + n;
		}

		void render() {
			if (collided || y <= 0)
				return;
			//			Images.rain.render(x, y - 256, flipped ? -32 : 32, 256);
			l.set(x, y, x, y - 30);
			l.render(.2f);
			collided = collision();
		}

		void reset() {
			x = AvianMath.randomInt(AvianApp.getWidth());
			y = -AvianMath.randomInt(500);
			n = AvianMath.randomFloat() * 2.5f;
			collided = false;
		}

		boolean collision() {
			if (collided)
				return true;
			float yy = this.y - 30;
			Chunk c = Game.getWorld().getLand()[(int) ((x - Game.getWorld().getXOffset()) / (32 * 64))];
			int block = (int) ((x - Game.getWorld().getXOffset()) / (64)) % 32;
			for (int i = c.getBlocks().length - 1; i >= 0; i--) {
				Block b = c.getBlocks()[i][block];
				if (b.isAir())
					continue;
				if (yy >= b.getY() + Game.getWorld().getYOffset() && yy <= b.getY() + Game.getWorld().getYOffset() + b.getH()) {
					return true;
				}
			}

			return false;
		}
	}
}
