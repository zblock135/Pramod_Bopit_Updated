/**
 * Pramod Bopit: Section 64, Group 8
 * Developed by:
 * Zachary Block
 * Benjamin Schienberg
 * Aaron Brown
 * 
 */

package com.EPED.pramodbopit;

//This is where we import all of the classes/methods that are used in this class
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
	
	//##############################################################
	//Variable Initialization
	//##############################################################
	
	Handler handler = new Handler(); //This takes care of updating UI
	
	boolean shake = false; //This variable keeps track of whether the device has been shaken
	
	int score = 0; //This variable keeps track of the score
	
	long timer = 2000;	//This variable will determine the time the user has to complete an action
	
	volatile boolean keep_playing = true;	//This is how the game tells whether to stop
	
	boolean actionComplete = false; //This variable keeps track of whether an action has been completed
	
	String string = ""; //Initializes the string variable, used later to change the text on screen
	
	int count = 1; //Initializes the count variable, used during the countdown section
	
	boolean touch = false; //This variable keeps track of whether the screen has been touched
	
	private boolean upPressed = false; //This variable keeps track of whether the volume up button has been pressed
	
	private boolean downPressed = false; //This variable keeps track of whether the volume down button has been pressed
	
	private boolean yell = false; //This variable keeps track of whether any noise has passed the threshold value
	
	private boolean lost = false; //This variable keeps track of whether you have lost the game
	
	private int prev_num; //This variable is used in the random number generation

	//##############################################################
	//Object Initialization
	//##############################################################
	
	//The following two objects represent two text fields in the UI that are updated upon completed actions
	private TextView text;
	private TextView scoreText;
	//The following four objects represent sensors, which are used to determine if actions have been completed
	private SoundMeter micCheck;
	private ShakeDetector mShakeDetector;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	//The following 6 objects represent all the sound clips that are played throughout the game
	private MediaPlayer shakeMP;
	private MediaPlayer tapMP;
	private MediaPlayer downMP;
	private MediaPlayer upMP;
	private MediaPlayer yellMP;
	private MediaPlayer stopMP;
	
	//##############################################################
	//CORE/ADVANCED METHODS AND FUNCTIONALITY
	//##############################################################
	
	//Below is the onCreate method, which is called when this activity first starts. It is used to initialize various objects and UI elements
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play);	//Sets the UI as the play layout .xml file
		//The following 2 lines link the objects with their respective text fields
		text = (TextView)findViewById(R.id.command);
		scoreText = (TextView)findViewById(R.id.score);

		//The following 6 lines link the objects with their respective sound files
		shakeMP = MediaPlayer.create(this, R.raw.shake);
		tapMP = MediaPlayer.create(this, R.raw.tap);
		downMP = MediaPlayer.create(this, R.raw.down);
		upMP = MediaPlayer.create(this, R.raw.up);
		yellMP = MediaPlayer.create(this, R.raw.yell);
		stopMP = MediaPlayer.create(this, R.raw.stop);
		
		micCheck = new SoundMeter(); //Initializing the micCheck object
		
		//ShakeDetector initialization:
				mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
				mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
				//The following object mShakeDetector is being used as a listener, which will listen for the desired action
				//to be completed, which in this case is a shake
				mShakeDetector = new ShakeDetector(new OnShakeListener() {
					//The following method, onShake(), is called when the accelerometer senses the device being shaken
					@Override
					public void onShake(){
						shake = true; //If the device has been shaken, set shake equal to true
					
					}
				});
				//Touch Screen Listener Initialization:
				View fullView = (View)findViewById(R.id.fullView); 
				//The following lines set up the object fullView as a listener, which "listens" for touches on the screen
				fullView.setOnTouchListener(new OnTouchListener(){
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						touch = true; //If the screen is touched, set touch to true
						v.performClick();
						if (lost){ //If you touch the screen after you lose, the game will reset itself
							onResume();
							lost = false;
						}
						return false;
					}
				});
			}
	
//###############################################################################################################################	
	
			//The following method onResume() is called when the activity is active
			@Override
		    protected void onResume() {
		        super.onResume();
		      //The following timer will display the Ready, set text before the game starts.
			//After, it calls the session method which contains the core functionality of the game.
		        
				new CountDownTimer(3100, 1500){
					//The onTick method is called every time the timer passes the 2nd parameter (in this case 1500)
					@Override
					public void onTick(long millisUntilFinished) {
						if(count==2){ 
							string = "Set?";
							handler.post(setString); //Used to change the center text on the play UI
							}
							
						if (count==1){
							string = "Ready?";
							handler.post(setString);//Used to change the center text on the play UI
							count = count + 1;
						}
						
					}
					//The onFinish method is called when the timer reaches its final value
					@Override
					public void onFinish() {
						Session(); //The session method contains the main loop for our game
						count = 1;
					}
					
				}.start(); //Starts the timer
				//The line of code below starts the accelerometer's collection of data
		        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
		    }
			
//###############################################################################################################################
			
			//The onPause method is called when the app is exited, or the screen is turned off
		    @Override
		    protected void onPause() {
		        super.onPause();
		        mSensorManager.unregisterListener(mShakeDetector); //Will stop the accelerometer from collecting data
		        shakeMP.stop(); upMP.stop(); downMP.stop(); yellMP.stop(); tapMP.stop(); stopMP.stop(); //Will stop playing any sound clips
		    }
		    
//###############################################################################################################################	
		 
	//The Session method contains the main loop of our game	    
	public void Session() {
		Thread t = new Thread(){ //Create a new thread, t, that will run parallel to the UI thread (needed due to while loop)
			public void run(){
				micCheck.start(); //initializes the mic
				while (keep_playing==true){
					int num = random(); //calls the random method, which generates a random number and assigns the value to the variable num
					switch (num){ //This switch uses the num variable to determine which action to ask for
					
					case 0: shakeAction(); break; //Shake it action
					
					case 1: tapAction(); break; //Tap it action
					
					case 2: turnUpAction(); break; //Turn up action
					
					case 3: turnDownAction(); break;  //Turn down action
					
					case 4: yellAction(); break; //Yell it action
					
					case 5: stopAction(); break;//Stop it action
					}
					try {
						string = ""; //Resets the central text field after every run of the while loop (eliminates a bug)
						handler.post(setString);//Used to change the center text on the play UI
						Thread.sleep(600);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				//This part of code is run once the while loop is exited (because the keep_playing variable = false)
				string = ("YOU LOSE! Your score was: " + score);
				handler.post(setString);//Used to change the center text on the play UI
				micCheck.stop();
				loser(); //Calls the loser method, which sets variables to reflect that the game was lost
			}
		}; t.start(); //Starts the thread t
	}
	
//###############################################################################################################################	
	
	//This runnable is called to change the central text in the play UI
	Runnable setString = new Runnable(){
		public void run(){
		text.setText(string);//Used to change the center text on the play UI
		}
	};
	
//###############################################################################################################################	
	
	//The following method will create a random number that will be used to select the required action
	public int random(){
		Random r = new Random(); //Creates the Random object r, which is used to create random numbers
		int num = r.nextInt(6);//sets num to equal a random integer from [0,6) (including 0, excluding 6)
		//The following code makes repeating actions harder to come by.
		if (num==prev_num){ //If the current num value equals the previous num value, create a new random number and add it to the first
			num = (num + r.nextInt(6)) % 6; //then take the modulus to determine your new num value
		}
		prev_num = num;
		return num;
		
	}
	
//###############################################################################################################################	
	
	//This is the shake action, it uses the class shakeDetection to determine if the phone has been shaken.
	//If shaken, it increases your score and resets the shake variable to its default value.
	//If the action isn't completed, or another action is completed, it changes the keep_playing variable to false, thus ending the game loop
	public void shakeAction() {
			resetAll();//The resetAll method resets all the game variables to their original values
			string = "Shake It!";
			handler.post(setString);//Used to change the center text on the play UI
			shakeMP.start(); //Start playing the shake sound
			long time = System.currentTimeMillis(); //sets the variable time to equal the current time (in milliseconds)
			long curTime = time;
			//Will continue to check if the device has been shaken until the timer has run out
			while(curTime<(time+timer)){
				curTime = System.currentTimeMillis();
				//The code below is reused in the following 4 methods to determine if the action has been completed
				if (shake){
					score = score+1; //updates the score
					shakeMP.pause(); //pauses the sound clip
					actionComplete = true; //action has been completed
					handler.post(setScore); 
					break;
				}
				else{
					//If any other action is completed, the game will end
					if(touch || upPressed || downPressed || yell){
						shakeMP.pause(); //pauses the sound clip
						break;
					}
				}
			}
			if (actionComplete == false){
			keep_playing = false;
			resetAll(); //The resetAll method resets all the game variables to their original values
			}
			else{
				if (actionComplete==true){
					actionComplete=false;
					resetAll();//The resetAll method resets all the game variables to their original values
				}
			}
			
		}
	
//###############################################################################################################################
	
	//The tapAction method asks the user to tap the screen. It checks if the tap variable = true, indicating the screen has been touched
	public void tapAction(){
		resetAll();//The resetAll method resets all the game variables to their original values
		string = "Tap It!";
		handler.post(setString);
		tapMP.start(); //start playing the tap sound
		long time = System.currentTimeMillis();
		long curTime = time;
		while(curTime<(time+timer)){
			curTime = System.currentTimeMillis();
			
			if (touch){
				score = score+1;
				tapMP.pause(); //pause the tap sound clip
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
	
//###############################################################################################################################
	
	//The turnUpAction method asks the user to press the volume up button.
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
	
//###############################################################################################################################
	
	//The turnDownAction method asks the user to press the volume down button.
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
	
//###############################################################################################################################
	
	//The yellAction method asks the user to yell into the microphone.
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
	
//###############################################################################################################################
	
	//The stopAction method asks the user not to complete any action.
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
	
//###############################################################################################################################
	
	//The following runnable "setScore" changes the text field in the top right corner to reflect the user's score
	Runnable setScore = new Runnable(){
		public void run(){
		scoreText.setText("" + score); //sets the upper right text field in the play UI
		}
	};
	
//###############################################################################################################################
	
	//The loser method is called when the user loses the game. It resets some of the game variables to their original values
	public void loser(){
		keep_playing = true;
		score = 0;
		timer = 2000; //Resets the timer to the original 2 seconds
		lost = true;
	}
	
//###############################################################################################################################
	
	//This method is called every time a physical button has been pressed
	public boolean onKeyDown(int keyCode, KeyEvent ev){
		//The following if statements check which button was pressed
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
			upPressed = true;
		}
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
			downPressed = true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK){
			finish(); //The finish method stops the current activity, and returns the user to the previous activity
		}
		
		return true;
	}
	
//###############################################################################################################################
	
	//This method checks if the mic registers a sound over a certain amplitude threshold
	public void Yell(){
		double soundLevel = micCheck.getAmplitude(); //Uses the micCheck object to get the amplitude of the sound coming into the mic
		if (soundLevel>=8.9){
			yell=true;
		}
	}
	
//###############################################################################################################################
	
	//The resetAll method is called to ensure that the trigger variables and sounds are reset after each completed action
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
	
//###############################################################################################################################
	
	//The changeTimer method is called after each completed action, and changes the time the user has to complete an action
	public void changeTimer(){
		if(timer>1000){
			timer = timer - 10*score*(Settings.difficulty);
		}
	}
}
	
	








