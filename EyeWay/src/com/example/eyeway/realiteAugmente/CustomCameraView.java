package com.example.eyeway.realiteAugmente;



import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CustomCameraView extends SurfaceView implements
SurfaceHolder.Callback {
	
	private Camera camera;
	private SurfaceHolder holder;
	private Parameters parameters;
	private static final String TAG = "ERREUR";
	
	public CustomCameraView(Context context, AttributeSet attrs, int defStyle) {

		super(context);
		
		Log.d("Etape","Initialisation Camera");
	
		
		holder = getHolder();
		holder.addCallback(this);

		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		// désactiver le vérouillage de l’écran
		setKeepScreenOn(true);

	}

	public CustomCameraView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder _holder) {


		try {
			camera = Camera.open();

			camera.setPreviewDisplay(_holder);
			camera.startPreview();
		} catch (Exception e) {
			
			Log.d(TAG, "Error setting camera preview: " + e.getMessage());
		}

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		try {
			parameters = camera.getParameters();
			parameters.setPreviewSize(800, 480);
			camera.setParameters(parameters);
			camera.setPreviewDisplay(holder);
			camera.startPreview();
			Log.d("Etape","Camera chargée");

		} catch (Exception e) {

			Log.d(TAG, "Error setting camera preview: " + e.getMessage());

		}
	}

	

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		camera.release();
	}

}