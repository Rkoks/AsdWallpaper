package com.rkoks.asdwallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;


public class MyWallpaperService extends WallpaperService {
	@Override
	public Engine onCreateEngine() {
		return new MyWallpaperEngine();
	}

	private class MyWallpaperEngine extends Engine {
		private final int FPS = 60;
		private final long FRAME_DURATION = 1000 / FPS;

		private Handler handler;
		private SurfaceHolder surfaceHolder;
		private int width;
		private int height;
		private boolean visible;

		private Space space;

		private Runnable drawer = new Runnable() {
			@Override
			public void run() {
				update();
				render();
			}
		};

		public void update(){
			//calculate here
			space.update();
		}

		public void render(){
			if (visible) {
				Canvas canvas = null;
				try{
					canvas = surfaceHolder.lockCanvas();
					if (canvas != null) {
						canvas.save();
						//draw begin
						space.render(canvas);
						//draw end
						canvas.restore();
					}
				} finally {
					if (canvas != null) {
						surfaceHolder.unlockCanvasAndPost(canvas);
						handler.removeCallbacks(drawer);
						handler.postDelayed(drawer, FRAME_DURATION);
					}
				}
			}
		}

		public MyWallpaperEngine() {
			handler = new Handler();
		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			this.surfaceHolder = surfaceHolder;
			setTouchEventsEnabled(true);
			space = new Space();
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);
			this.width = width;
			this.height = height;
			space.setSizes(width, height);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			super.onVisibilityChanged(visible);
			this.visible = visible;
			if (visible) {
				handler.post(drawer);
			} else {
				handler.removeCallbacks(drawer);
			}
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			handler.removeCallbacks(drawer);
		}

		@Override
		public void onTouchEvent(MotionEvent event) {
			super.onTouchEvent(event);
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				space.setSpeed(30);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				space.setSpeed(5);
			}
		}
	}

}
