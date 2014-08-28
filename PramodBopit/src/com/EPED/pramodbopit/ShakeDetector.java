package com.EPED.pramodbopit;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;


public class ShakeDetector implements SensorEventListener{
	
	
	private OnShakeListener mShakeListener;
	double lastX=0;
	double lastY=0;
	double lastZ=0;
	double alpha=0.850;
	public ShakeDetector(OnShakeListener shakeListener) {
		mShakeListener = shakeListener;
	}
	

	


	@Override
	public void onSensorChanged(SensorEvent event) {
		double x = event.values[0];
		x = (1-alpha)*lastX + (alpha*x);
        
        double speed = Math.abs((x - lastX));
        if (speed > 6){
        	mShakeListener.onShake();
        	
        }
        lastX = x;
        
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

}
