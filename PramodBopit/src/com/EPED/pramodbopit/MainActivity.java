package com.EPED.pramodbopit;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends ActionBarActivity {
	
	
	//This method is run when the activity first opens. It is responsible for setting up the UI.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	//If the PLAY button is pressed, the Play activity is started
	public void startPlay(View view){
		startActivity(new Intent(this, Play.class));
	}
	//If the SETTINGS button is pressed, the Settings activity is started
	public void startSettings(View view){
		startActivity(new Intent(this, Settings.class));
	}
	//If the CREDITS button is pressed, the Credits activity is started
	public void startCredits(View view){
		startActivity(new Intent(this, Credits.class));
	}
}
