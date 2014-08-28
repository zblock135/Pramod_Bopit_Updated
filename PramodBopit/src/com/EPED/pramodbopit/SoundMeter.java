/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.EPED.pramodbopit;

import java.io.IOException;
import android.media.MediaRecorder;
import android.util.Log;

//This class was made by Google to check for the amplitude of the sound being picked up by the mic.
public class SoundMeter {
	//Variable Initialization
	 static final private double EMA_FILTER = 0.6;
	 private static final String LOG_TAG = "AudioRecordTest";
     private MediaRecorder mRecorder = null;
     private double mEMA = 0.0;

     //This method sets up the recorder object, and starts to record audio. Since the output file = null, the audio is not saved to a file
     public void start(){
             
                 mRecorder = new MediaRecorder();
                 mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                 mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                 mRecorder.setOutputFile("/dev/null"); 
                 mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                 
                
                //Using try/catch is used to run bits of code that may throw an error.
                 try {
					mRecorder.prepare();
				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
				}
                 mRecorder.start();
                 mEMA = 0.0;
             }
     
     //This method is used to stop recording and to release the recorder object
     public void stop() {
             if (mRecorder != null) {
                     mRecorder.stop();       
                     mRecorder.release();
                     mRecorder = null;
             }
     }
    
     //This method is used to get the amplitude of incoming sound
     public double getAmplitude() {
             if (mRecorder != null) //checks to see if the recorder object exists.
                     return  (mRecorder.getMaxAmplitude()/2700.0);
             else
                     return 0;

     }

     //This method is the same as getAmplitude, except it uses an EMA filter to eliminate noise
     public double getAmplitudeEMA() {
             double amp = getAmplitude();
             mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
             return mEMA;
     }
}


