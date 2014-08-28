Pramod_Bopit_Updated
====================

App for EPED II

This is an Android application game.  The game is loosely based off of the children's toy 'Bop It' where the user obeys voice commands in order to score points. 

Core Functionality:
This app utilizes the accelerometer as one of the user commands. The accelerometer is toggled on and off by the app. It also utilizes threshold detetection and alpha filtering. 

Advanced Functionality:

Advanced User Interface - Clicking the buttons on the opening MainActivity will take the user to different pages with                             separate functions.

Advanced Algorithms     - The app uses a random number generator to determine which actions to choose. In addition, the                           code makes reoccuring actions (having the same action twice) unlikely.

Advanced Visualizations - A text field and sound clips are used to tell you which action to complete. In addition, a                              logo was designed and is used in the game. 

Multiple Sensors        - In addition to the accelerometer, our app uses multiple sensors during the course of the game.                           Some actions use physical buttons on the phone (Turn up and Turn down). The tap it action                               makes use of the touch screen, and the yell it function make use of the microphone.

Different Language      -The most important advanced feature is the fact that the app was programmed in Java, rather                             than MatLAB. This allows the app to be used on any android device.

Threading & Handlers    -Unlike MatLAB, when programming for Android, any code (inside the main "UI" thread) that takes                          a while to run (like a while loop) will cause your program to lag, and your UI will not display                          correctly. The solution, which is incorporated into our app, is threading. You basically create                          a peice of code that runs in the background which is totally separated from the main "UI"                               thread. Since the two threads are totally separate, you have to use a handler to communicate                            between the two threads. Our code uses a handler that takes commands from the background thread                          and sends them to the main "UI" thread to be executed.

Directions:

Below are the 6 possible actions the game will ask you to perform. You must perform them within the alotted time to recieve points. If you do not complete the action, or complete another action that was not asked for, you will lose the game. In order to restart the game once you lose, simply tap the screen and the game will restart.

Shake it: Shake the phone. Only a slight shake is needed.

Tap it: Tap the screen once.

Turn Up: Click the volume up button on your phone once. (Physical Button)

Turn Down: Click the volume down button on your phone once. (Physical Button)

Yell it: Make some noise! Either yell or snap your fingers next to the mic.

Stop it: Do not complete any action. Try to hold the phone as still as possible.

