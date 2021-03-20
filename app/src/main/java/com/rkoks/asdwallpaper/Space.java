package com.rkoks.asdwallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class Space {
	private final int MAX_RANGE = 300;
	private final int SCREEN_RANGE = 10;
	private final int STARS_COUNT = 200;

	private int width;
	private int height;
	private int starWidth;
	private int starHeight;
	private int speed = 5;

	private Random rnd;
	private Paint starPaint;
	private Paint bgPaint;

	private Star[] stars;

	public Space() {
		setSizes(600, 800);
		starPaint = new Paint();
		starPaint.setColor(Color.WHITE);

		bgPaint = new Paint();
		bgPaint.setColor(Color.BLACK);

		rnd = new Random();

		stars = new Star[STARS_COUNT];

		initStars();
	}

	public void update(){
		for (int i = 0; i < STARS_COUNT; i++) {
			stars[i].update();
		}
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void render(Canvas canvas) {
		canvas.drawPaint(bgPaint);
		for (int i = 0; i < STARS_COUNT; i++) {
			stars[i].render(canvas);
		}
	}

	public void setSizes(int width, int height) {
		this.width = width;
		this.height = height;

		starWidth = width * MAX_RANGE / SCREEN_RANGE / 2;
		starHeight = height * MAX_RANGE / SCREEN_RANGE / 2;
	}

	private void initStars() {
		for (int i = 0; i < STARS_COUNT; i++) {
			stars[i] = new Star();
		}
	}


	private class Star{
		private int x;
		private int y;
		private int z;
		private int radius = 3;

		private int screenX;
		private int screenY;

		public Star() {
			x = starWidth / 2 - rnd.nextInt(starWidth);
			y = starHeight / 2 - rnd.nextInt(starHeight);
			z = SCREEN_RANGE + rnd.nextInt(MAX_RANGE - SCREEN_RANGE);
		}

		public void update(){
			z -= speed;

			if (z != 0) {
				screenX = width / 2 + x * SCREEN_RANGE / z;
				screenY = height / 2 + y * SCREEN_RANGE / z;
			} else {
				screenX = width / 2 + x;
				screenY = height / 2 + y;
			}

			if (screenX < 0 || screenX > width || screenY < 0 || screenY > height ||
					z < SCREEN_RANGE) {
				x = starWidth / 2 - rnd.nextInt(starWidth);
				y = starHeight / 2 - rnd.nextInt(starHeight);
				z = MAX_RANGE;
			}
		}

		public void render(Canvas canvas) {
			canvas.drawCircle(screenX, screenY, radius, starPaint);
		}
	}


}
