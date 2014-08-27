package com.EPED.pramodbopit;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class Play extends Activity{
	Handler handler = new Handler(); //This takes care of updating UI
	boolean shake = false; //This variable keeps track of whether the device has been shaken
	int score = 0;
	long timer = 2000;	//This variable will determine the time the user has to complete an action
	volatile boolean keep_playing = true;	//This is how the game tells whether to stop
	boolean actionComplete = false;
	String string = "";
	int count = 1;
	boolean touch = false;
	private boolean upPressed = false;
	private boolean downPressed = false;
	private boolean yell = false;
	private boolean lost = false;
	private int prev_num;

	
	private TextView text;
	private TextView scoreText;

	
	private SoundMeter micCheck;
	private ShakeDetector mShakeDetector;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	
	private MediaPlayer shakeMP;
	private MediaPlayer tapMP;
	private MediaPlayer downMP;
	private MediaPlayer upMP;
	private MediaPlayer yellMP;
	private MediaPlayer stopMP;
	
//	private Settings setting;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play);
		
		text = (TextView)findViewById(R.id.command);
		scoreText = (TextView)findViewById(R.id.score);

		
		shakeMP = MediaPlayer.create(this, R.raw.shake);
		tapMP = MediaPlayer.create(this, R.raw.tap);
		downMP = MediaPlayer.create(this, R.raw.down);
		upMP = MediaPlayer.create(this, R.raw.up);
		yellMP = MediaPlayer.create(this, R.raw.yell);
		stopMP = MediaPlayer.create(this, R.raw.stop);
		
		micCheck = new SoundMeter();
		//ShakeDetector initialization
				mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
				mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
				mShakeDetector = new ShakeDetector(new OnShakeListener() {
					@Override
					public void onShake(){
						shake = true;
					
					}
				});
				View fullView = (View)findViewById(R.id.fullView);
				fullView.setOnTouchListener(new OnTouchListener(){
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						touch = true;
						v.performClick();
						if (lost){
							onResume();
							lost = false;
						}
						return false;
					}
				});
					
			
				
			}
	
	
	
			@Override
		    protected void onResume() {
		        super.onResume();
		      //The following timer will display the Ready, set, go text before the game starts.
				//After, it calls the session method which contains the core functionality of the game.
		        
				new CountDownTimer(3100, 1500){
					
					@Override
					public void onTick(long millisUntilFinished) {
						if(count==2){
							string = "Set?";
							handler.post(setString);
							count = count + 1;
							}
							
						if (count==1){
							string = "Ready?";
							handler.post(setString);
							count = count + 1;
						}
						
					}

					@Override
					public void onFinish() {
						Session();
						
						count = 1;
					}
					
				}.start();
				
			
		        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
		    }

		    @Override
		    protected void onPause() {
		        super.onPause();
		        mSensorManager.unregisterListener(mShakeDetector);
		        shakeMP.stop(); upMP.stop(); downMP.stop(); yellMP.stop(); tapMP.stop(); stopMP.stop();
		    }   	
	
	
	public void onStart(){
		super.onStart();
		
		
	}
	

	
	
	
	
	
	
	
	
	public void Session() {
		Thread t = new Thread(){
			public void run(){
				micCheck.start();
				while (keep_playing==true){
					int num = random();
					switch (num){
					case 0: shakeAction(); break;
					
					case 1: tapAction(); break; //Tap it action
					
					case 2: turnUpAction(); break; //Turn up action
					
					case 3: turnDownAction(); break;  //Turn down action
					
					case 4: yellAction(); break; //Yell it action
					
					case 5: stopAction(); break;//Stop it action
					}
					try {
						string = "";
						handler.post(setString);
						Thread.sleep(600);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				string = ("YOU LOSE! Your score was: " + score);
				handler.post(setString);
				micCheck.stop();
				loser();
			}
		}; t.start();
		
		
		
		
	}
	
	Runnable setString = new Runnable(){
		public void run(){
		
		text.setText(string);
		}
	};
	
	
	//The following method will create a random number that will be used to select the required action
	public int random(){
		Random r = new Random();
		int num = r.nextInt(6);
		if (num==prev_num){
			num = (num + r.nextInt(6)) % 6;
		}
		prev_num = num;
		return num;
		
	}
	
	
	
	
	
	
	
	
	
	//This is the shake action, it uses the class shakeDetection to determine if the phone has been shaken.
	//If shaken, it increases your score and resets the shake variable to its default value.
	//If the action isn't completed, or another action is completed, it changes the keep_playing variable to false, thus ending the game loop
	public void shakeAction() {
			resetAll();
			string = "Shake It!";
			handler.post(setString);
			shakeMP.start();
			long time = System.currentTimeMillis();
			long curTime = time;
			while(curTime<(time+timer)){
				curTime = System.currentTimeMillis();
				
				if (shake){
					score = score+1;
					timer = timer*.98; // causes the game to go 2% faster with each itneration
					shakeMP.pause();
					actionComplete = true;
					handler.post(setScore);
					break;
				}
				else{
					if(touch || upPressed || downPressed || yell){
						shakeMP.pause();
						break;
					}
				}
			}
			if (actionComplete == false){
			keep_playing = false;
			resetAll();
			}
			else{
				if (actionComplete==true){
					actionComplete=false;
					resetAll();
				}
			}
			
		}
	
	public void tapAction(){
		resetAll();
		string = "Tap It!";
		handler.post(setString);
		tapMP.start();
		long time = System.currentTimeMillis();
		long curTime = time;
		while(curTime<(time+timer)){
			curTime = System.currentTimeMillis();
			
			if (touch){
				score = score+1;
				timer = timer*.98; // causes the game to go 2% faster with each itneration
				tapMP.pause(); 
				actionComplete = true;
				handler.post(setScore);
				break;
			}
			else{
				if(shake || upPressed || downPressed || yell){
					tapMP.pause(); 
					break;
				}
			}
		}
		if (actionComplete == false){
		keep_playing = false;
		resetAll();
		}
		else{
			if (actionComplete==true){
				actionComplete=false;
				resetAll();
			}
		}
		
	}
	
	public void turnUpAction(){
		resetAll();
		string = "Turn Up!";
		handler.post(setString);
		upMP.start();
		long time = System.currentTimeMillis();
		long curTime = time;
		while(curTime<(time+timer)){
			curTime = System.currentTimeMillis();
			
			
			if (upPressed){
				score = score+1;
				timer = timer*.98; // causes the game to go 2% faster with each itneration
				actionComplete = true;
				handler.post(setScore);
				upMP.pause();
				break;
			}
			else{
				if(touch || shake || downPressed || yell){
					upMP.pause();
					break;
				}
			}
		}
		if (actionComplete == false){
		keep_playing = false;
		resetAll();
		}
		else{
			if (actionComplete==true){
				actionComplete=false;
				resetAll();
			}
		}
		
	}
	
	public void turnDownAction(){
		resetAll();
		string = "Turn Down (For What)!";
		handler.post(setString);
		downMP.start();
		long time = System.currentTimeMillis();
		long curTime = time;
		while(curTime<(time+timer)){
			curTime = System.currentTimeMillis();
			
			
			if (downPressed){
				score = score+1;
				timer = timer*.98; // causes the game to go 2% faster with each itneration
				actionComplete = true;
				handler.post(setScore);
				downMP.pause();
				break;
			}
			else{
				if(touch || upPressed || shake || yell){
					downMP.pause();
					break;
				}
			}
		}
		if (actionComplete == false){
		keep_playing = false;
		resetAll();
		}
		else{
			if (actionComplete==true){
				actionComplete=false;
				resetAll();
			}
		}
		
	}
	
	public void yellAction(){
		resetAll();
		string = "Yell It!";
		handler.post(setString);
		yellMP.start();
		long time = System.currentTimeMillis();
		long curTime = time;
		
		while(curTime<(time+timer)){
			curTime = System.currentTimeMillis();
			if (!yellMP.isPlaying()){
			Yell();
			}
			if (yell){
				score = score+1;
				timer = timer*.98; // causes the game to go 2% faster with each itneration
				actionComplete = true;
				handler.post(setScore);
				yellMP.pause();
				break;
			}
			else{
				if(touch || upPressed || downPressed || shake){
					yellMP.pause();
					break;
				}
			}
		}
		
		if (actionComplete == false){
		keep_playing = false;
		resetAll();
		}
		else{
			if (actionComplete==true){
				actionComplete=false;
				resetAll();
			}
		}
		
	}
	
	public void stopAction(){
		resetAll();
		string = "Stop It!";
		handler.post(setString);
		stopMP.start();
		long time = System.currentTimeMillis();
		long curTime = time;
		
		while(curTime<(time+timer)){
			
			curTime = System.currentTimeMillis();
			Yell();
			
			if (touch || upPressed || downPressed || shake){
				actionComplete = true;
				stopMP.pause();
				break;
			}
			
			
		}
		
		if (actionComplete == false){
			score = score+1;
			timer = timer*.98; // causes the game to go 2% faster with each itneration
			actionComplete = true;
			handler.post(setScore);
			stopMP.pause();
		
		resetAll();
		}
		else{
			if (actionComplete==true){
				actionComplete=false;
				keep_playing = false;
				resetAll();
			}
		}
		
	}
	
	
	Runnable setScore = new Runnable(){
		public void run(){
		scoreText.setText("" + score);
		}
	};
	
	public void loser(){
		keep_playing = true;
		score = 0;
		timer = 2000;
		lost = true;
		
	}
	//This method is called every time a physical button has been pressed
	public boolean onKeyDown(int keyCode, KeyEvent ev){
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
			upPressed = true;
		}
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
			downPressed = true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		
		return true;
		
	}
	//This method checks if the mic registers a sound over a certain amplitude
	public void Yell(){
		double soundLevel = micCheck.getAmplitude();
		if (soundLevel>=8.9){
			yell=true;
		}
	}
	
	public void resetAll(){
		yell = false;
		touch = false;
		upPressed = false;
		downPressed = false;
		shake = false;
		shakeMP.seekTo(0);
		downMP.seekTo(0);
		upMP.seekTo(0);
		tapMP.seekTo(0);
		yellMP.seekTo(0);
		stopMP.seekTo(0);
	}
	
	
	
	public void changeTimer(){
		if(timer>1000){
			
			timer = 2000 - ((1000)/((100)))*score;
		}
	}
	
}
	
	








