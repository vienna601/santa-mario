/*
 * This frame sets the fields of the character, and 
 * is responsible for the logic behind the vertical movement
 * of the character. The horizontal movement is validated in
 * the key action class. 
 * 
 * The list of upgrades can be found in the comment
 * header of the Level Frame.
 */
//for the audio
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
//for swing components and action listener
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//want the character to inherit JLabel properties
public class Character extends JLabel implements ActionListener {
	//image icon that stores the images of the character 
	//moving left, moving right, standing idle and jumping
	private ImageIcon[] iconArray;
	//string array - holds the keys which control 
	//the character's movement
	private String[] key;
	//change in coordinates
	private int dX, dY;
	
	//jumping
	//timer object keeps track of how long character should jump
	private Timer jumpTimer = new Timer(15,this);
	//keep track of the number of times the user jumped
	private int jumpCounter;
	//keep track of if the character is jumping
	private boolean jumping = false;
	
	//for every action there is an opposite action
	//falling
	private Timer fallTimer = new Timer(15,this);
	//keep track of the number of times the character fell
	private int fallCounter;
	//keep track of if the character is falling
	private boolean falling = false;
	
	private Timer moveTimer = new Timer(35,this);
	//for the star
	//default score increment is 10, but getting the
	//star will double it
	private int scoreBonus = 10;
	
	//the time counter will need to be accessed and incremented
	//in the Level Frame class
	static double starTime, mushroomTime;
	
	//speed increase for the mushroom icon
	//default is false
	private boolean speedBuff = false;
	
	//for the dynamic image
	public Character(ImageIcon[] iconArray, String[] key) {
		//reference the parent class constructor
		//Source: https://www.geeksforgeeks.org/
		//difference-between-super-and-super-in-java-with-examples/
		super();
		//set the icon array to the image icon array parameter 
		setIconArray(iconArray);
		//set the string array parameter to the key property
		this.key = key;
	}
	
	//getters and setters for everything
	public ImageIcon[] getIconArray() {
		return iconArray;
	}

	public void setIconArray(ImageIcon[] iconArray) {
		this.iconArray = iconArray;
	}


	public String[] getKey() {
		return key;
	}

	public void setKey(String[] key) {
		this.key = key;
	}

	public int getdX() {
		return dX;
	}

	public void setdX(int dX) {
		this.dX = dX;
	}

	public int getdY() {
		return dY;
	}

	public void setdY(int dY) {
		this.dY = dY;
	}

	public Timer getJumpTimer() {
		return jumpTimer;
	}

	public void setJumpTimer(Timer jumpTimer) {
		this.jumpTimer = jumpTimer;
	}

	public int getJumpCounter() {
		return jumpCounter;
	}

	public void setJumpCounter(int jumpCounter) {
		this.jumpCounter = jumpCounter;
	}

	public boolean isJumping() {
		return jumping;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public Timer getFallTimer() {
		return fallTimer;
	}

	public void setFallTimer(Timer fallTimer) {
		this.fallTimer = fallTimer;
	}

	public int getFallCounter() {
		return fallCounter;
	}

	public void setFallCounter(int fallCounter) {
		this.fallCounter = fallCounter;
	}

	public boolean isFalling() {
		return falling;
	}

	public void setFalling(boolean falling) {
		this.falling = falling;
	}
	
	//utility method for jumping
	public void jump() {
		//the character is jumping
		jumping = true;
		jumpCounter = 0;
		//start the timer
		jumpTimer.start();
		//there is no need to start the fog timer since
		//the jump timer also calls the action performed method
	}
	//another utility method for fall
	public void fall() {
		falling = true;
		fallCounter = 0;
		//start the timer
		fallTimer.start();
	}
	
	//move the character to the right
	public void moveRight() {
		moveTimer.start();
		//change x coordinate value
		if(speedBuff)
			//increase the pixel count 
			dX = 10;
		else//normal movement: move 5 pixels
			dX = 5;
		//set bounds of the label to update the image
		//y-value won't change since movement is horizontal
		setBounds(getX() + dX, getY(), 25,25);
	}
	
	//move the character to the left
	public void moveLeft() {
		moveTimer.start();
		//repeat the same steps in moveRight()
		//except that they are all negative
		if(speedBuff)
			//increase the pixel count 
			dX = -10;
		else//normal movement: move 5 pixels
			dX = -5;
		//set bounds of the label
		//y-value won't change since its the same
		setBounds(getX() + dX, getY(), 25,25);
	}
	
	//return the row and column of the 2D array
	//that represents the current location of the 
	//character
	//for collision detection for coin: add a point 
	//and remove the coin if character touches the coin
	public int getRow() {
		//divide by 25 since it is 25x25 pixels
		//minus 60 since I expanded the frame by 60 pixels
		//vertically to fit the timer label, score label and
		//the buttons 
		return (int)(getY()- 60)/25;
	}
	
	//
	public int getCol() {
		//return the an index of the boardarray according
		//to the user's current coordinates (divide by 25 since
		//the JLabels in the array are 25x25 pixels)
		return (int)getX()/25;
	}
	
	//
	public void collectCoin() {
		//assign variables to the current row and column
		int row = getRow();
		int col = getCol();
		
		//check if user has collected a present
		if (LevelFrame.boardArray[row][col].getIcon() == Icons.PRESENT) {
			//replace icon with null (remove it)
			LevelFrame.boardArray[row][col].setIcon(null);
			//increment the score according to the current score bonus
			LevelFrame.score += scoreBonus;
			//play the coin sound effect
			//https://stackoverflow.com/questions/15526255/
			//best-way-to-get-sound-on-button-press-for-a-java-calculator
			AudioInputStream audioInputStream;
			try {
				//set the audio file to the audio input stream
				audioInputStream = AudioSystem.getAudioInputStream(
						new File("audio/coin.wav").getAbsoluteFile());
				Clip clip = AudioSystem.getClip();
				//open the clip
				clip.open(audioInputStream);
				//I will not lower the volume of getting the coin since
				//the sound effect won't be constantly played
				//start playing the clip
				clip.start();
			}//if the audio file cannot be accessed
			catch (Exception e) {
				//does not terminate program if error occurs
				e.printStackTrace();
			}
		}
	}
	
	//check if user has collected a star, which doubles the score increment
	public void collectStar() {
		//repeat the same steps from the collectCoin method
		int row = getRow();
		int col = getCol();
		
		//check if player's current position is on a star
		if (LevelFrame.boardArray[row][col].getIcon() == Icons.STAR) {
			//replace icon with null to remove it from the screen
			LevelFrame.boardArray[row][col].setIcon(null);
			//
			scoreBonus = 20;
			//
			starTime = 0;
			//display the power up animation
			new PowerUpFrame("star");
			//play the star sound effect (12s)
			//https://stackoverflow.com/questions/15526255/
			//best-way-to-get-sound-on-button-press-for-a-java-calculator
			AudioInputStream audioInputStream;
			try {
				//
				audioInputStream = AudioSystem.getAudioInputStream(
						new File("audio/star.wav").getAbsoluteFile());
				Clip clip = AudioSystem.getClip();
				//
				clip.open(audioInputStream);
				clip.start();
			}//if the audio file cannot be accessed
			catch (Exception e) {
				//
				e.printStackTrace();
			}//end of catch
		}//end of if
	}
	
	//check if user ran into the spiked ball
	public void spikeBall() {
		//repeat the same steps from the collectCoin method
		//get the dimensions/location of the character
		int row = getRow();
		int col = getCol();
		
		//check if player's current position is on a spiked ball
		if (LevelFrame.boardArray[row][col].getIcon() == Icons.SPIKE) {
			//replace icon with null to remove it from the screen
			LevelFrame.boardArray[row][col].setIcon(null);
			//
			LevelFrame.score -= 10; 
			//display the power up animation
			new PowerUpFrame("spike");
			//play the sad sound effect
			//https://stackoverflow.com/questions/15526255/
			//best-way-to-get-sound-on-button-press-for-a-java-calculator
			AudioInputStream audioInputStream;
			try {
				//
				audioInputStream = AudioSystem.getAudioInputStream(
						new File("audio/spiked.wav").getAbsoluteFile());
				Clip clip = AudioSystem.getClip();
				//
				clip.open(audioInputStream);
				clip.start();
			}//if the audio file cannot be accessed
			catch (Exception e) {
				//
				e.printStackTrace();
			}//end of catch
		}//end of if
	}
	
	//check if user has collected the mushroom
	public void collectMushroom() {
		//repeat the same steps from the collectCoin method
		int row = getRow();
		int col = getCol();
		
		//check if player's current position is on a mushroom
		if (LevelFrame.boardArray[row][col].getIcon() == Icons.MUSHROOM) {
			//replace icon with null to remove it from the screen
			LevelFrame.boardArray[row][col].setIcon(null);
			//activate speed buff that increases speed
			//of horizontal movement by 2 times
			speedBuff = true;
			//I need to create a separate variable to store the starting
			//time for the mushroom since the user might collect a star before
			//the mushroom speed buff disappears. If I only use one variable,
			//the buffs will not be individually removed.
			mushroomTime = 0;
			//display the power up animation
			new PowerUpFrame("mushroom");
			//play the coin sound effect
			//https://stackoverflow.com/questions/15526255/
			//best-way-to-get-sound-on-button-press-for-a-java-calculator
			AudioInputStream audioInputStream;
			try {
				//
				audioInputStream = AudioSystem.getAudioInputStream(
						new File("audio/powerup.wav").getAbsoluteFile());
				Clip clip = AudioSystem.getClip();
				//
				clip.open(audioInputStream);
				clip.start();
			}//if the audio file cannot be accessed
			catch (Exception e) {
				//
				e.printStackTrace();
			}//end of catch
		}//end of if
	}
	
	//--------------- Fog effect (referenced PacMan project) --------------------
	//filling the map with fog
	private void fillFog() {
		for (int row = 0; row < 20; row++) { 
			for (int col = 0; col < 35; col++) {
				LevelFrame.fogArray[row][col].setIcon(Icons.FOG);
			}
		}
	}
	private void clearFog() {
		//reset the map with fog every time mario moves
		fillFog();
		//5x5 view window
		for (int row = -2; row < 3; row++) {
			for (int col = -2; col < 3; col++) {
				//check if within board array
				if (getRow() + row >= 0 && getRow() + row <= 19 &&
						getCol() + col >= 0 && getCol() + col <= 34 ) {
					//set icon to null to make it "invisible"
					LevelFrame.fogArray[getRow()+row][getCol() + col].setIcon(null);
				}
			}
		}
		//show character icon within the fog
		LevelFrame.fogArray[getRow()][getCol()].setIcon(null);
	}
	//-----------------------------------------------------------------------------
	
	/*
	 * The action performed method is responsible for 
	 * collision. It will check all possible scenarios where 
	 * the user should not be able to jump, and then set the 
	 * new coordinates for the character.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		clearFog();
		//collision with a brick
		//velocity negative: jumping upwards
		//if jumping upwards AND there is a wall on top
		if(jumping && dY < 0 && LevelFrame.boardArray[getRow()-1][getCol()].getIcon() == Icons.WALL) {
			//stop jumping
			jumping = false;
			dY = 0;
			//stop the timer
			jumpTimer.stop();
			//
			fall();
			//
			return;
		}
		
		//opposite: stop character from falling through the ground
		if(falling && dY > 0) {
			//assign next row number
			//y value increases as we descend
			int nextRow = getRow() + 1;
			//if it's at the very bottom OR if the next row is a wall
			if(nextRow >= LevelFrame.boardArray.length || 
					LevelFrame.boardArray[nextRow][getCol()].getIcon() == Icons.WALL) {
				falling = false;
				dY = 0;
				//stop the timer
				fallTimer.stop();
				return;
			}//end of nested if
		}//check if falling
		
		if (jumping) {
			jumpCounter ++;
			if (jumpCounter <= 10) {//going up
				dY = -5;
			}
			//reached maximum value (should not jump higher than board)
			else if(jumpCounter <= 20) {//going down
				dY = 5;
			}
			else {//reset and stop jumping
				jumping = false;
				dY = 0;
				jumpTimer.stop();
				fall();
			}
		}
		//
		else if (falling) {
			fallCounter++;
			//move the character 5 units down at a time
			dY = 5;
			//update the character's position
			setBounds(getX(), getY()+ dY, 25, 25);
			//check for collision with the ground
			//these two ifs could be combined (same condition)
			if (LevelFrame.boardArray[getRow()+1][getCol()].getIcon() == Icons.WALL) {
				falling = false;
				dY = 0;
				fallTimer.stop();
			}
			else if (fallCounter >= 20) {
				falling = false;
				dY = 0;
				fallTimer.stop();
			}
			//exit the method since character is falling
			return;
		}
		
		//if character has a wall to their right and tries to move right
		if(LevelFrame.boardArray[getRow()][getCol()+1].getIcon() == Icons.WALL 
				&& dX>0) {
			dX = 0;
		}
		//
		else if (LevelFrame.boardArray[getRow()][getCol()-1].getIcon() == Icons.WALL 
				&& dX < 0) {
			dX = 0;
		}
		//
		else if(LevelFrame.boardArray[getRow() - 1][getCol()].getIcon() == Icons.WALL 
				&& dY < 0) {
			dY = 0;
		}
		//
		else if (LevelFrame.boardArray[getRow() + 1][getCol()].getIcon() == Icons.WALL 
				&& dY > 0) {
			dY = 0;
		}
		
		//check if character collected any coins, power ups or 
		//ran into a spike ball
		collectCoin();
		collectStar();
		collectMushroom();
		spikeBall();
		
		//change location of character
		setBounds(getX() + dX, getY() + dY, 25, 25);
		
		//Lastly, I will check if the buffs should be removed
		//check current time
		//The action listener is called when the user moves
		//so I cannot be certain that the time passed is exactly 12
		//seconds when the user moves. Therefore, I have to use the
		//>= relational operator instead of ==.
		if (starTime >= 12) {
			//remove the bonus points effect
			scoreBonus = 10;
			//don't need to reset time variables since they
			//are reset when the power ups are collected
		}
		//use if instead of else if since both buffs may be activated
		//at the same time
		//The speed power up will last for 5 seconds
		if (mushroomTime >= 5) {
			//remove the speed buff
			speedBuff = false;
		}
	}//end of action performed method
	

}//end of class boy
