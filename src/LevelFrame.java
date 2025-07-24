//responsible for setting up user interface,
//loading map, icons and characters
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
//imports for the components
import javax.swing.*;
import java.awt.*;
//imports for action listener
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
//import key listener to read key board input
import java.awt.event.KeyListener;
import java.util.ArrayList;
//import scanner for reading the txt files
import java.util.Scanner;
//for delaying the time (in play button action listener)
//https://stackoverflow.com/questions/24104313/how-do-i-make-a-delay-in-java
import java.util.concurrent.TimeUnit;
//imports for reading audio and txt files
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
//will be used later to format the time 
//Source:https://stackoverflow.com/questions/
//28337718/java-swing-timer-countdown
import java.text.SimpleDateFormat;

public class LevelFrame extends JFrame implements KeyListener, ActionListener{
	//2D array that holds the map
	//size: 875 x 500
	public static JLabel[][] boardArray = new JLabel[20][35];
	//create a separate array to overlay the original board
	public static JLabel[][] fogArray = new JLabel[20][35];
	//needs to be accessible in character class for fog removal
	static JPanel lvl1Panel = new JPanel();
	public static Character mario = new Character(Icons.MARIO, 
			//keys that control the movement of the character
									new String[] {"a","d"," "});
	
	//label for holding background image
	private JLabel bgLabel;
	
	//label for displaying timer
	private JLabel timerLabel = new JLabel("00:00:000");
	//Since I want the timer to update every tenth of a
	//second, I will make the delay 100 milliseconds
	private Timer gameTimer = new Timer(100,this);
	/* note: I do not need to constrain the user's movement
	 * while the timer is 'paused' since the timeElapsed will
	 * immediately start accumulating after the user moves again
	 * - the timer is not actually paused, it's just that the 
	 *   timeElapsed variable will stop increasing, which will
	 *   have the same effect as a stopped timer with less 
	 *   complications.
	*/
	static boolean paused = true;
	//set the initial time elapsed as 0 seconds
	private long timeElapsed = 0;
	
	//label for displaying the current score
	//this will be updated with the collectCoin method
	private JLabel scoreLabel;
	//declare a static score variable that can be changed in 
	//other classes
	//it will be initialized at 0 since the user should start
	//with 0 points
	static int score = 0;
	
	//buttons at the top - I will not do a button array
	//since the distance between each button is not constant
	//		- I cannot use a for loop to declare and set bounds
	private JButton[] topButtons = new JButton[6];
	
	//this will be used to modify the txt file
	private ArrayList <String> newInfo = new ArrayList< >();
	
	//parameter: load appropriate level
	//sets size of the game based on the size of the map
	//set up key bindings
	public LevelFrame(int level, String chosenbg) {
		//start the timer
		gameTimer.start();
		
		//set the default value and add it to the array
		newInfo.add(0," ");
		
		//set up the level by filling the board array
		loadLevel(level,chosenbg);
		
		//create the panel by setting the bounds and
		//adding components to the lvl1Panel
		createLvPanel();
		
		//bind the keys to actions in the key action class
		setupKeyBindings();
		
		//add key listener to the panel since we know that 
		//we will use the a and d keys\
		lvl1Panel.addKeyListener(this);
		
		//set size of the frame according to the size of the array
		//I added 60 pixels to the height since the buttons are 
		//50 pixels in height and I want to leave a bit of space
		//between the game and the buttons
		setPreferredSize(new Dimension (25 * boardArray[0].length, 
				25 * boardArray.length+60));
		setLayout(null);
		setResizable(false);
		//program will terminate when the user closes this frame
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//make sure frame fits components
		pack();
		//Center the frame on the user’s screen
		setLocationRelativeTo(null);
		//set visibility to true
		setVisible(true);
	}

	//set up the board array
	//parameters: level number and the chosen background (from the
	//customize frame)
	public void loadLevel(int level, String chosenbg) {
		// use try catch block
		//catches error if one occurs 
		//(instead of having it displayed in the console)
		try {
			//background - will set bounds in the create lv method
			//set the image according to user input
			if(chosenbg.equals("Riverside"))
				bgLabel = new JLabel(new ImageIcon("images/Bg1.png"));
			else
				bgLabel = new JLabel(new ImageIcon("images/Bg2.png"));
			
			//score label - set the default score to 0
			scoreLabel = new JLabel("Score: "+ score);
			
			//multiple levels: use level number variable
			Scanner inputFile = new Scanner(new File("file/Level" + level + ".txt"));
			//
			for(int row = 0; row < boardArray.length; row++) {
				//store each line into the line array
				char[] lineArray = inputFile.next().toCharArray();
				//check each individual character on each row
				for(int col = 0; col < lineArray.length; col++) {
					//read through the line, if the character equals 'B':
					if (lineArray[col] == 'B') {
						//change it to the wall icon
						boardArray[row][col] = new JLabel(Icons.WALL);
					}
					//do the same for the other letters
					else if (lineArray[col] == 'C') {
						//change it to the present icon
						boardArray[row][col] = new JLabel(Icons.PRESENT);
					}
					else if (lineArray[col] == 'S') {
						//change it to the star icon
						boardArray[row][col] = new JLabel(Icons.STAR);
					}
					//I will use the second letter in "spike" since
					//the star icon has already taken the 'S' character.
					//I will not use 's' since I might mix up the two.
					else if (lineArray[col] == 'P') {
						//
						boardArray[row][col] = new JLabel(Icons.SPIKE);
					}
					else if (lineArray[col] == 'M') {
						//
						boardArray[row][col] = new JLabel(Icons.MUSHROOM);
					}
					else {
						//if it is not a special icon, set it to an empty JLabel
						boardArray[row][col] = new JLabel();
					}
				}//end of nested for loop
			}//end of outer for loop
			//close scanner
			inputFile.close();
		}//end of try
		catch (FileNotFoundException e){
			//avoid terminating program in case of error
			e.printStackTrace();
		}
		
	}

	//this is where I set the bounds and add the components to the panel
	public void createLvPanel() {
		//customize the timer label
		//change the background so that the user can easily see it
		timerLabel.setBackground(new Color(220,194,224));
		//since the default opacity of a JLabel is false, I need
		//to set it to true in order to see the background
		//Source:https://stackoverflow.com/questions/2380314/
		//		 how-do-i-set-a-jlabels-background-color
		timerLabel.setOpaque(true);
		//set the font and font size
		timerLabel.setFont(new Font("TimesRoman", Font.BOLD, 25));
		//set a size for the label so that the size does not change
		//when a second has passed (the zeros representing the tenth,
		//hundredth and thousandth of a second will disappear, making
		//the label shorter and shifting all the buttons at the top)
		timerLabel.setPreferredSize(new Dimension(110,50));
		
		//JPanel for holding the components at the top 
		//(buttons and the timer label)
		//set layout to flow layout (helps arrange components in a row)
		//Source: https://docs.oracle.com/javase/tutorial/
		//		  uiswing/layout/flow.html
		JPanel topPanel = new JPanel(new FlowLayout());
		//set how the components are arranged - from left to right
		topPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		//I will use the length of the topButtons array in the second argument
		//instead of the buttonImage array from the starting screen since that
		//array also includes the A, D keys and the space bar.
		for(int i = 0; i < topButtons.length; i++) {
			//set the image icon with reference to the static image icon 
			//array in the start frame class. I have arranged the elements
			//so that the indices match up the order they appear from left 
			//to right
			topButtons[i] = new JButton(StartFrame.buttonImage[i]);
			topButtons[i].setAlignmentY(Component.TOP_ALIGNMENT);
			//add action listener to buttons
			topButtons[i].addActionListener(this);
			//
			//https://stackoverflow.com/questions/14095018/
			//setfocusable-method-or-focusing-components-java
			topButtons[i].setFocusable(false);
			//
			topPanel.add(topButtons[i]);
			if (i == 2)
				//add the timer label in the middle
				topPanel.add(timerLabel);
		}
		//after adding all the other components, I will put the 
		//score label on the very left
		//it will have the same font as the timer label for consistency
		scoreLabel.setFont(new Font("TimesRoman", Font.BOLD, 25));
		//since the background of the timer label is purple, I will use other
		//colors (blue and light yellow) from the background image for the 
		//font color and background color
			//the default opacity for JLabels are false, so I have to set it 
			//to true in order to be able to see the background color
		scoreLabel.setOpaque(true);
		//set the background color
		scoreLabel.setBackground(new Color(249,229,197));
		//set the font color
		scoreLabel.setForeground(new Color(118,140,189));
		//set a size for the score label so that it does not 
		//change when the number of digits increase
		scoreLabel.setPreferredSize(new Dimension(130,30));
		//add the score label to the panel after customizing it
		topPanel.add(scoreLabel);
		
		//set background colour for top panel
		topPanel.setBackground(new Color(215,111,75));
		//set bounds for the panel
		topPanel.setBounds(0,0,875,80);
		
		
		//I will be placing components with coordinates so 
		//I can set the layout manager to null
		lvl1Panel.setLayout(null);
		//set the bounds for the label that holds the background image
		//I will add this to the panel later
		bgLabel.setBounds(0, 0, 25 * boardArray[0].length, 
				25 * boardArray.length + 60);
		setFog();
		//loop through the board array and set the bounds for the
		//JLaels that have icons
		for(int row = 0; row < boardArray.length; row++) {
			//loop through each row and column
			for(int col = 0; col < boardArray[0].length; col++) {
				//check if the current JLabel is the wall icon
				if(boardArray[row][col].getIcon() == Icons.WALL) {
					//set the location
					//multiply by 25: each box is 25x25 pixels
					//+60: because of the button panel at the top
					boardArray[row][col].setBounds(col * 25, row * 25 + 60, 25,25);
					//add it to the panel 
					lvl1Panel.add(boardArray[row][col]);
				}
				//check if the current JLabel is the present icon
				else if(boardArray[row][col].getIcon() == Icons.PRESENT) {
					//multiply by 25: each box is 25x25 pixels
					//add 70 to the rows since the buttons in the button bar are
					//70x70 pixels and are placed at the top
					boardArray[row][col].setBounds(col * 25, row * 25 + 60, 25,25);
					lvl1Panel.add(boardArray[row][col]);
				}
				//do the same for the other icons
				else if(boardArray[row][col].getIcon() == Icons.STAR) {
					boardArray[row][col].setBounds(col * 25, row * 25 + 60, 25,25);
					lvl1Panel.add(boardArray[row][col]);
				}
				else if(boardArray[row][col].getIcon() == Icons.SPIKE) {
					boardArray[row][col].setBounds(col * 25, row * 25 + 60, 25,25);
					lvl1Panel.add(boardArray[row][col]);
				}
				else if(boardArray[row][col].getIcon() == Icons.MUSHROOM) {
					boardArray[row][col].setBounds(col * 25, row * 25 + 60, 25,25);
					lvl1Panel.add(boardArray[row][col]);
				}
				else
					;//do nothing
			}//end of nested for loop
		}//end of outer for loop
		
		//add the top panel to the background
		bgLabel.add(topPanel);
		
		//add the character
		//set the default static image
		mario.setIcon(Icons.MARIO[0]);
		mario.setBounds(25,485,25,25);
		lvl1Panel.add(mario);
		//set bounds of panel
		lvl1Panel.setBounds(0, 0, 25 * boardArray[0].length, 
							25 * boardArray.length + 60);
		//add the background label to the panel
		lvl1Panel.add(bgLabel);
		
		//add the panel to the frame
		add(lvl1Panel);
		
	}//end of createLvPanel method

	//fog
	public void setFog() {
		for(int row = 0; row < fogArray.length; row++) {
			//loop through each row and column
			for(int col = 0; col < fogArray[0].length; col++) {
				fogArray[row][col] = new JLabel(Icons.FOG);
				fogArray[row][col].setBounds(col * 25, row * 25 + 60, 25,25);
				lvl1Panel.add(fogArray[row][col]);
			}
		}
	}
	
	//binds keys to corresponding actions
	public void setupKeyBindings() {
		//associate/bind key pressed to an action
		//create action map to map a key identifier to an action
		ActionMap actionMap;
		//map a key stroke to a key identifier
		InputMap inputMap;
		//
		inputMap = lvl1Panel.getInputMap();
		//
		actionMap = lvl1Panel.getActionMap();
		
		//0th index: 'a' key
		//map this key with the left action
		inputMap.put(KeyStroke.getKeyStroke(
				mario.getKey()[0].toCharArray()[0]),"left");
		//input map works in conjunction with action map
		//pass argument to key action class
		actionMap.put("left", new KeyAction("left"));
		
		//same for other actions
		//1st index is the 'd' key
		inputMap.put(KeyStroke.getKeyStroke(
				mario.getKey()[1].toCharArray()[0]),"right");
		actionMap.put("right", new KeyAction("right"));
		
		//index 2: space bar
		inputMap.put(KeyStroke.getKeyStroke(
				mario.getKey()[2].toCharArray()[0]),"jump");
		actionMap.put("jump", new KeyAction("jump"));
	}
	
	//action listener that gets called every tenth of a second
	//so that the tenths of a second are updated and displayed on the 
	//timer Label. This method also handles the actions triggered by the 
	//buttons at the top.
	@Override
	public void actionPerformed(ActionEvent e) {
		//for bonus effects
		//increment the time (unit is seconds so I will set the increase as 0.1)
		Character.starTime += 0.1;
		Character.mushroomTime += 0.1;
		
		//check if timer is paused
		if (paused)
			timeElapsed += 0;
		else
			//add 0.1 seconds to the total amount of time elapsed
			timeElapsed += 100;
		
		//Set the text format so that it looks more similar to a stop watch
		//https://stackoverflow.com/questions/28337718/java-swing-timer-countdown
        SimpleDateFormat df=new SimpleDateFormat("mm:ss:S");
        timerLabel.setText(df.format(timeElapsed));
        
        //=============== Exceeded time limit(30s): game ends ===============
        //30 seconds is equal to 30000 milliseconds
        if(timeElapsed >= 30000){
        	//stop the game and get the score
        	//play the end game sound effect
        	//Source: https://www.geeksforgeeks.org/play-audio-file-using-java/
			AudioInputStream audioInputStream;
			try {
				//set the file to the audio input stream
				audioInputStream = AudioSystem.getAudioInputStream(
						new File("audio/levelFail.wav").getAbsoluteFile());
				//get the audio file from the audio system and set it to
				//a clip variable
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				//start playing the clip
				clip.start();
			//cannot reuse the parameters so I changed it to e1
			} catch (Exception e1) {
				//
				e1.printStackTrace();
			}//end of catch
			
			//I can initialize this here since I won't be using this 
			//variable anywhere else
			int finalScore = score;
			
			try {
				//create new scanner object to read the information in the txt file
				Scanner inputFile = new Scanner(new File("file/HighScore.txt"));
				//Use the delimiter:
				//, : to tell java that commas are the 
				// separating factor in the txt file 
				inputFile.useDelimiter(",|\r\n");
				//set the current number in the txt file as the highest score
				int Highest = inputFile.nextInt();
				//compare the user's current score with the highest score
				if (finalScore > Highest) {
					//set the 0th index in the array list to the user's score
					//and add a comma to separate the score and the user name
					//Sources:
					//https://docs.oracle.com/javase/tutorial/essential/io/file.html
					//https://stackoverflow.com/questions/37263631/editing-existing-txt-file-in-java
					newInfo.set(0,finalScore + "," + CustomizeFrame.name);
					//change the information in the txt file to the information stored
					//in the array list
					Files.write(Paths.get("File/HighScore.txt"), newInfo);
					//tell the user their score in case they were too focused on the game and missed
					//their final score
					JOptionPane.showMessageDialog(null, "Congratulations! You set a new record!"+
													"\nYour score was: " + finalScore);
				}
				//lower or equal to highest score
				else {
					JOptionPane.showMessageDialog(null, "The current highest score is: "+
	        				Highest + "\nYou're almost there! Try again next time!");
				}
				
				//close scanner
				inputFile.close();
			}//end of try
			//prevent program from being terminated
			catch (FileNotFoundException e1){
				//print the error message
				e1.printStackTrace();
			} catch (IOException e1) {
				//display error message in the console
				e1.printStackTrace();
			}
			
            //reset the time elapsed and score
			timeElapsed = 0;
			score = 0;
			//dispose current frame and open the home screen
			this.dispose();
			new StartFrame();
        }//end of game ended if statement
        
        else{//there is still time left, update the score
        	//if the player exceeds the time limit, no score will
        	//be added when they collect the coin afterwards.
        	//update the score with the static score variable
        	//that will be updated in the collectCoin method in the
            //character class
            scoreLabel.setText("Score: " + score);
        }
        	
        //check if buttons at the top were pressed
        //check from first index, which will be the
        //leftmost button(because of flow layout)
        if (e.getSource() == topButtons[0]){
        	//home button
        	//dispose current frame
        	this.dispose();
        	//open the starting screen
        	new StartFrame();
        }
        //controls button
        //no need to dispose the current frame since the
    	//player just wants to check the controls description
        else if (e.getSource() == topButtons[1]){
        	//stop the timer
        	paused = true;
        	//open the controls screen
        	new ControlsFrame();
        }
        
        //clear high score
        else if (e.getSource() == topButtons[2]){
        	//set the information to the default
			newInfo.set(0,"default");
			try {
				//
				Files.write(Paths.get("File/HighScore.txt"), newInfo);
			} catch (IOException e1) {
				//the catch prevents the program from being terminated even
				//if there is an error
				e1.printStackTrace();
			}
        }
        
        //start timer
        else if (e.getSource() == topButtons[3]){
        	try {
				//0.5 second delay before the timer
				//is started again so that the player
				//can get ready (I have tested 1 second but it felt too slow)
				//https://stackoverflow.com/questions/24104313/
				//how-do-i-make-a-delay-in-java
				TimeUnit.SECONDS.sleep((long) 0.5);
        	} 
			catch (InterruptedException e1) {
				//display error message in console
				e1.printStackTrace();
			}
        	//start the timer
			paused = false;
        }
        
        //pause button
        else if (e.getSource() == topButtons[4]){
        	//pause the timer
        	paused = true;
        }
        
        //exit button
        else if (e.getSource() == topButtons[5]){
        	//I need to dispose the frame here instead of 
        	//directly using System.exit(0)since I want the 
        	//message box to appear after the level frame is disposed
        	this.dispose();
        	JOptionPane.showMessageDialog(null, 
        			"Thank you for playing Santa Mario!\nMerry Christmas!");
        	//terminate the program
        	System.exit(0);
        }
	}
	
	//-------------------------- Key listeners --------------------------
	//not using,  but needed for using the interface
	@Override
	public void keyTyped(KeyEvent e) {
	}
	//not using,  but needed for using the interface
	@Override
	public void keyPressed(KeyEvent e) {
	}

	//check if user has released their keys
	@Override
	public void keyReleased(KeyEvent e) {
		//sets the horizontal velocity to 0 when the 'a' or 'd'
		//keys are released. We don't want mario to slow down 
		//(he should come to a complete stop)
		if(e.getKeyChar() =='a' || e.getKeyChar() =='d'|| e.getKeyChar() == ' ') {
			//set the change in x-coordinates to 0
			mario.setdX(0);
			//stopped moving: set to idle image
			mario.setIcon(Icons.MARIO[0]);
		}
		
	}//end of key released

}
