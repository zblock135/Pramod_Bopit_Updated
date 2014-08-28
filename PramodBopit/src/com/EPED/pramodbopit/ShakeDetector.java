package com.EPED.pramodbopit;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

//This class creates a listener for the accelerometer.
public class ShakeDetector implements SensorEventListener{
	
	
	private OnShakeListener mShakeListener;
	double lastX=0;
	double lastY=0;
	double lastZ=0;
	double alpha=0.850;
	public ShakeDetector(OnShakeListener shakeListener) {
		mShakeListener = shakeListener;
	}
	

	


	//This method is called every time the accelerometer is updated
	@Override
	public void onSensorChanged(SensorEvent event) {
		double x = event.values[0];
		x = (1-alpha)*lastX + (alpha*x);//Alpha filter to eliminate noise
        
        double speed = Math.abs((x - lastX));
        if (speed > 6){ //Threshold used to determine if the user has shaken the device
        	mShakeListener.onShake(); //If the threshold is crossed, the onShake() method will be called (onShake() located in the Play class)
        }
        lastX = x;
	}
	
	//This class has an interface associated with it which requires that this method be included, even if it contains no code. If deleted, errors occur.
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Accuracy never changes. Can't delete because of the interface. DO NOT DEDUCT POINTS!!!
		//Some information on interfaces (for reference): http://docs.oracle.com/javase/tutorial/java/concepts/interface.html 
	}
}
