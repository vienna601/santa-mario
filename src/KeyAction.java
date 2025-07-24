/*
 * This class is responsible for calling the corresponding
 * methods from the character class and validate if the actions
 * can be executed.
 * 
 * The list of upgrades can be found in the comment
 * header of the Level Frame.
 */

import java.awt.event.ActionEvent;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.swing.text.TextAction;

public class KeyAction extends TextAction {
	//fields
	private String key;
	
	//constructor
	public KeyAction(String key) {
		//super() is used to call constructor of 
		//the text action class
		super(key);
		//
		this.key = key;
	}

	//I will check if the action can be performed, then
	//call the corresponding method from the character class
	@Override
	public void actionPerformed(ActionEvent e) {
		//make mario a local variable so that we don't have 
		//to reference the level frame class every time
		Character mario = LevelFrame.mario;
		
		//if I am pressing the key 'a' AND mario is not to the 
		//right of a wall ( .getCol()-1 ), he will move left
		if(e.getActionCommand().equals(mario.getKey()[0])&&
		LevelFrame.boardArray[mario.getRow()][mario.getCol()-1].getIcon() != Icons.WALL) {
			//need to modify if add new character
			//since it's mario.moveLeft(), not CHaracter.n=moveLeft() !
			mario.moveLeft();
			//the character is moving: set to animated gif
			mario.setIcon(Icons.MARIO[2]);
			//start the timer
			LevelFrame.paused = false;
		}
		
		//same logic for 'd' key
		//if I am pressing the key 'a' AND mario is not to the l
		//eft of a wall( .getCol()+1 ), he will move left
		else if(e.getActionCommand().equals(mario.getKey()[1])&&
		LevelFrame.boardArray[mario.getRow()][mario.getCol()+1].getIcon() != Icons.WALL) {
			mario.moveRight();
			//the character is moving: set to animated gif
			mario.setIcon(Icons.MARIO[1]);
			//start the timer
			LevelFrame.paused = false;
		}
		
		//.getRow()-1: Decreasing row number (as you go up) since the TOP of the frame is 0
		else if(e.getActionCommand().equals(mario.getKey()[2])&&
		LevelFrame.boardArray[mario.getRow()-1][mario.getCol()].getIcon() != Icons.WALL) {
			//check if character is already jumping
			if(!mario.isJumping()) {
				//trigger the jump action
				mario.jump();
				//set the character's icon to the jumping animation
				mario.setIcon(Icons.MARIO[3]);
				
				//play the jump effect
				//https://stackoverflow.com/questions/15526255/
				//best-way-to-get-sound-on-button-press-for-a-java-calculator
				AudioInputStream audioInputStream;
				try {
					//
					audioInputStream = AudioSystem.getAudioInputStream(new File("audio/marioJump.wav").getAbsoluteFile());
					Clip clip = AudioSystem.getClip();
					//
					clip.open(audioInputStream);
					//lower the volume since the jump effect was too loud and 
					//would get annoying very quickly
					//https://stackoverflow.com/questions/953598/audio-volume-control-increase-or-decrease-in-java
					FloatControl volume = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
					//reduce volume by 20 decibels
					volume.setValue(-20.0f);
					//
					clip.start();
				} //cannot reuse the parameter e so I changed it to e1
				catch (Exception e1) {
					//
					e1.printStackTrace();
				}//end of catch
				
				//start the timer
				LevelFrame.paused = false;
				
			}//end of nested if
		}//end of else if
		
	}//end of action performed

}
