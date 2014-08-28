package com.EPED.pramodbopit;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;


public class ShakeDetector implements SensorEventListener{
	
	
	private OnShakeListener mShakeListener;
	float lastX=0;
	float lastXd=0;
	int alpha = .8;
	public ShakeDetector(OnShakeListener shakeListener) {
		mShakeListener = shakeListener;
	}
	

	


	@Override
	public void onSensorChanged(SensorEvent event) {
		float x = event.values[0];
		float lastXd = (1-alpha)*lastXd+(alpha*lastX);
        
        float speed = Math.abs((x - lastXd));
        if (speed > 5){
        	mShakeListener.onShake();
        	
        }
        
        
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

}
