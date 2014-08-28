package com.EPED.pramodbopit;

import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;


public class Settings extends Activity{

public static int difficulty = 1; //This variable controls how quickly the timer approaches 1 second


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		//Initializes the Seekbar to be a volume controller (Code by Alan Moore [http://stackoverflow.com/questions/7459228/create-slider-to-change-android-volume])
		//The code below looks for updates to the seekbar, and maps the value of the bar to a volume level
		//Then, the code sets the media volume to be equal to this value, thus allowing the user to change the volume of the game
		final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);	
	    int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	    int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	    SeekBar volControl = (SeekBar)findViewById(R.id.volume);
	    volControl.setMax(maxVolume);
	    volControl.setProgress(curVolume);
	    volControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
	        @Override
	        public void onStopTrackingTouch(SeekBar arg0) {
	        }

	        @Override
	        public void onStartTrackingTouch(SeekBar arg0) {
	        }

	        @Override
	        public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
	            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, arg1, 0);
	        }
	    });
	}
	
		//This method detects when a radio button has been pressed and sets the difficulty depending on which button was pressed.
	    public void onRadioButtonClicked(View view) {
	   	  
	    	
	    	RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
	    	int id = radioGroup.getCheckedRadioButtonId(); //This gets the ID of the checked radio button
	    	if (id == -1){
	    	    //no item selected
	    	}
	    	else{
	    	    if (id == R.id.radioButton1){
	    	        difficulty = 1;
	    	    }
	    	    if (id == R.id.radioButton2){
	    	    	difficulty = 2;
	    	    }
	    	    if (id == R.id.radioButton3){
	    	    	difficulty = 3;
	    	    }
	    	}
	    }
	}

	
	

