package com.EPED.pramodbopit;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;


public class ShakeDetector implements SensorEventListener{
	
	
	private OnShakeListener mShakeListener;
	float lastX=0;
	float lastY=0;
	float lastZ=0;
	public ShakeDetector(OnShakeListener shakeListener) {
		mShakeListener = shakeListener;
	}
	

	


	@Override
	public void onSensorChanged(SensorEvent event) {
		float x = event.values[0];
      //  float y = event.values[1];
     //   float z = event.values[2];
        
        float speed = Math.abs((x - lastX));
        if (speed > 5){
        	mShakeListener.onShake();
        	
        }
        
        
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

}
