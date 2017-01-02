package com.libcv;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.libcv.Main;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

public class AndroidLauncher extends AndroidApplication implements CameraBridgeViewBase.CvCameraViewListener2, View.OnTouchListener{

	CameraBridgeViewBase cameraSurface;

	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
				case LoaderCallbackInterface.SUCCESS: {
					cameraSurface.enableView();
					cameraSurface.setOnTouchListener(AndroidLauncher.this);
				}
				break;
				default: {
					super.onManagerConnected(status);
				}
				break;
			}
		}
	};

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useAccelerometer = true;
		cfg.useCompass = true;

		cfg.r = 8;
		cfg.g = 8;
		cfg.b = 8;
		cfg.a = 8;

		//initialize(new CameraDemo(this), cfg);
		initialize(new Main(), cfg);

		if (graphics.getView() instanceof SurfaceView) {
			SurfaceView glView = (SurfaceView) graphics.getView();
			glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		}

		cameraSurface = new JavaCameraView(this, -1);
		cameraSurface.setCvCameraViewListener(this);
		addContentView(cameraSurface, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	}

	@Override
	public void onPause() {
		super.onPause();
		if (cameraSurface != null)
			cameraSurface.disableView();
	}

	@Override
	public void onResume() {
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_10, this, mLoaderCallback);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (cameraSurface != null)
			cameraSurface.disableView();
	}

	@Override
	public void onCameraViewStarted(int width, int height) {

	}

	@Override
	public void onCameraViewStopped() {

	}

	@Override
	public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
		return inputFrame.rgba();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}
}
