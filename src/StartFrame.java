/*
 * This frame is responsible for displaying the 
 * respective descriptions of user controls and 
 * their icons. It will also have a tool bar at 
 * the top where the user can start the game.
 * 
 * The list of upgrades can be found in the comment
 * header of the Level Frame.
 */

//import the classes used later in the program
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//used to play the audio
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class StartFrame extends JFrame implements ActionListener{
	
	//animated gif as background
	//background label can have same name as the one in the 
	//level frame since they are both private
	private JLabel bgLabel;
	
	//title label to display name of the game
	private JLabel titleLabel;
	//display a button with a mario gif
	//if the user presses it, an audio track will play
	//this is a hidden easter egg so it will not be included
	//in the instructions
	private JButton marioButton;
	
	//header of the controls section - not included in array
	//since the font sizes are different
	private JLabel infoLabel;
	//tells the user about the controls 
	//size of array is double the total button count
	//		- one for description, one for displaying 
	//		  the button image
	static JLabel[] labelArray = new JLabel[14];
	//I have declared this as static since I will need
	//to use this in the LevelFrame for my play and pause buttons
	//This way, I only need to declare the image icon once. 
	static ImageIcon[] buttonImage = {new ImageIcon("images/home.png"),
			new ImageIcon("images/controls.png"),new ImageIcon("images/clear.png"),
			new ImageIcon("images/play.png"),new ImageIcon("images/pause.png"),
			new ImageIcon("images/Exit.png"),
			new ImageIcon("images/leaderboard.png"),
			new ImageIcon("images/keyA.png"),new ImageIcon("images/keyD.png"),
			new ImageIcon("images/keySpace.png"),};
	//declare a string array that contains the descriptions of buttons
	//so that I can display the corresponding text using the index in the for loop
	static String[] description = {
			"Home Button\n\t- Return to the Starting Screen",
			"Controls Button\n\t- Pauses the game and displays the control details",
			"Clear Button\n\t- Clears current high score",
			"Play Button\n\t- Resume the game and start the timer again after pausing",
			"Pausing Button\n\t- Stop the timer (Don't abuse this feature.."+
			"Bowser is watching you!)",
			"Exit Button\n\t- Quit the game and start being productive ;)",
			"Leader Board Button\n\t- Check the highest score and the name of the player",
			"A key\n\t- Move the character to the left",
			"D key\n\t- Move the character to the right",
			"Space Bar\n\t- Make the character jump"};
	//tool bar with buttons
	private JToolBar ButtonBar;
	//I will not make a JButton array since there will only be
	//3 buttons on the button bar. 
	private JButton startButton;
	private JButton exitButton;
	//lb short for leader board
	private JButton lbButton;
	
	//check if it is the initial run so that the audio won't be 
	//played again and overlap with the one that is currently looped 
	private boolean initial = true;
	
	public StartFrame() {
		//set the layout manager
		setLayout(new BorderLayout());
		
		//play the theme song
		//-note: mp3 files are not supported, so we need to convert 
		//it to a wav audio file
		//Source: https://www.geeksforgeeks.org/play-audio-file-using-java/
		//check if this is the user's initial run (this frame will be called after
		//the end of every round in case the user wants to play again)
		if (initial) {
			AudioInputStream audioInputStream;
			try {
				//
				audioInputStream = AudioSystem.getAudioInputStream(new File("audio/marioTheme.wav").getAbsoluteFile());
				Clip clip = AudioSystem.getClip();
				//
				clip.open(audioInputStream);
				//lower the volume since I just want it to run in the background
				//https://stackoverflow.com/questions/953598/audio-volume-control-increase-or-decrease-in-java
				FloatControl volume = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
				//reduce volume by 20 decibels
				volume.setValue(-20.0f);
				//start playing the clip
				clip.start();
				//loop the audio clip continuously
				//since I want it to play in the background as long as 
				//the game is still active
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			} //the catch prevents the program from being terminated even
			//if there is an error
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		//make the button bar
		//create the tool bar
		//tool bar
		//https://docs.oracle.com/javase/tutorial/uiswing/components/toolbar.html
		ButtonBar = new JToolBar();
		
		//set the properties of the buttons
		//initialize the buttons
		startButton = new JButton();
		exitButton = new JButton();
		lbButton = new JButton();
		//set the image icon
		startButton.setIcon(new ImageIcon("images/start.png"));
		exitButton.setIcon(new ImageIcon("images/Exit.png"));
		lbButton.setIcon(new ImageIcon("images/leaderboard.png"));
		//add the action listener
		startButton.addActionListener(this);
		exitButton.addActionListener(this);
		lbButton.addActionListener(this);
		////set focusable to false so that the space bar will not click the buttons
		startButton.setFocusable(false);
		exitButton.setFocusable(false);
		lbButton.setFocusable(false);
		//finally, add the button to the JToolBar
		ButtonBar.add(startButton);
		ButtonBar.add(exitButton);
		ButtonBar.add(lbButton);
		
		//set the properties of the tool bar
		//Set the tool bar to rollover mode so there is
		//a visual indicator on the buttons
		ButtonBar.setRollover(true);
		//Prevent the tool bar from being moved/dragged
		ButtonBar.setFloatable(false);
		//set the background of the tool bar so that it matches the
		//color scheme of the gif
		ButtonBar.setBackground(new Color(65,36,8));
		//Add the tool bar at the top of the page
		//It will stay at the top of the page even as you scroll down
		add(ButtonBar, BorderLayout.PAGE_START);
		
		//
		titleLabel = new JLabel("Santa Mario");
		titleLabel.setForeground(new Color(231,210,156));
		titleLabel.setFont(new Font("Serif", Font.BOLD, 40));
		titleLabel.setBounds(300,100,300,50);
		
		//
		bgLabel = new JLabel(new ImageIcon("images/StartBg.gif"));
		bgLabel.add(titleLabel);
		
		//create the mario button and add the image
		marioButton = new JButton(new ImageIcon("images/StartingMario.gif"));
		//add the action listener
		marioButton.addActionListener(this);
		//set focusable to false so that the space bar will not click the buttons
		marioButton.setFocusable(false);
		
		//Panel for holding the information about the controls
		JPanel infoPanel = new JPanel();
		//Box layout is able to arrange components vertically 
		//Source: https://docs.oracle.com/javase/tutorial/uiswing/layout/box.html
		infoPanel.setLayout(new BoxLayout(infoPanel,BoxLayout.Y_AXIS));
		//set the size of the panel
		//Source: https://stackoverflow.com/questions/1594423/
		//setting-the-size-of-panels
		//The height should be half the length of the label array
		//times 80 pixels since for each iteration in the for loop,
		//there is one controlsPanel (50 pixels in height) and
		//and a rigid area (20 pixels in height). I also don't want
		//it to look too packed, so I added an extra 10 pixels.
		infoPanel.setPreferredSize(new Dimension(875,
									labelArray.length/2*80));
		//set the background to dark brown, which will show in the 
		//rigid areas (source linked in the for loop). I will switch
		//between dark and light brown to mimic the wooden floor in 
		//the gif since I want the theme to be consistent.
		infoPanel.setBackground(new Color(65,36,8));
		
		//Information label: use html to format the text
		//		- I learned some html in the BTT2O1 course last year
		//Source(adding html to JLabel): 
		//		https://stackoverflow.com/questions/6635730/
		//		how-do-i-put-html-in-a-jlabel-in-java
		infoLabel= new JLabel("<html><h1>How To Play - Controls</h1>"+
				//add a horizontal line and add an empty line in between
				"<hr><br><br>" +
				//tell the user how to start the timer here since there
				//isn't a button for it
				"-> Press 'A', 'D' or the Space Bar to start"+
				" the timer after entering the game!</html>");
		//
		infoLabel.setForeground(new Color(231,210,156));
		infoLabel.setFont(new Font("Serif", Font.ITALIC, 20));
		//place the infoLabel in the center
		//Source: https://docs.oracle.com/javase/tutorial/
		//uiswing/layout/box.html
		infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		//I will add the info label to the main panel later
		
		//Need to increment by two since there are two
		//labels for each button: one for the description
		for(int i = 0; i < labelArray.length; i += 2) {
			//set the image of the button as the first label
			//I need to divide i by 2 since it is incrementing by 2 
			//every iteration. The size of labelArray is double that 
			//of the buttonImages.
			labelArray[i]= new JLabel(buttonImage[i/2]);
			//i is also divided by 2 here for the same reason
			labelArray[i+1] = new JLabel(description[i/2]);
			labelArray[i+1].setForeground(new Color(231,210,156));
			labelArray[i+1].setFont(new Font("TimesRoman", Font.PLAIN, 20));
			//Panel that stores the components
			JPanel controlsPanel = new JPanel(new BorderLayout());
			
			//set the layout of the panel and add the components
			controlsPanel.add(labelArray[i], BorderLayout.WEST);
			controlsPanel.add(labelArray[i+1],  BorderLayout.CENTER);
			
			//set the size of the panel to fit the button images
			//Source: https://stackoverflow.com/questions/1594423/
			//setting-the-size-of-panels
			controlsPanel.setPreferredSize(new Dimension(200,50));
			//set the background to lighter brown
			//		- mimic the wood floor in the gif
			controlsPanel.setBackground(new Color(115,68,33));
			
			//add the panel to the center of the info panel
			//by setting its alignment
			//Source: https://docs.oracle.com/javase/tutorial/
			//uiswing/layout/box.html
			controlsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
			infoPanel.add(controlsPanel);
			//Create a rigid area in between the panels so that
			//they have a bit of space between them
			//Source: https://docs.oracle.com/javase/tutorial/
			//uiswing/layout/box.html
			infoPanel.add(Box.createRigidArea(new Dimension(0,20)));
		}
		
		//create the panel that holds all the components
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(bgLabel, BorderLayout.NORTH);
		//
		mainPanel.add(marioButton,BorderLayout.WEST);
		mainPanel.add(infoLabel, BorderLayout.CENTER);
		//
		mainPanel.add(infoPanel,BorderLayout.SOUTH);
		
		//set the background to the darker brown
		mainPanel.setBackground(new Color(65,36,8));
		//Scroll bar:
		//Create a new instance of the JScrollPane class and make
		//it a vertical scroll pane. Attach the main panel and the 
		//scroll pane together.
		//		Source: https://docs.oracle.com/javase/
		//				tutorial/uiswing/components/scrollpane.html
		JScrollPane scrollPane = new JScrollPane(mainPanel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//Add the scroll pane to the frame 
		//(also adds the panel that is attached to it)
		add(scrollPane,BorderLayout.CENTER);
		//make the scroll speed faster - better user experience
		//Source: https://stackoverflow.com/questions/
		//5583495/how-do-i-speed-up-the-scroll-speed-
		//in-a-jscrollpane-when-using-the-mouse-wheel
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		
		//------------- set the properties of the frame ---------------
		//set the title
		setTitle("Starting Screen");
		//Adjust the size to ensure all panels are visible
		//Use setPreferredSize to make sure that the frame is
		//always displayed in these dimensions
		//Source: https://stackoverflow.com/questions/
		//1594423/setting-the-size-of-panels
		setPreferredSize(new Dimension(875,500));
		//Close the frame/application on exit
		//		- ensure that it doesn't keep running in the background
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Automatically size the frame to fit components with pack()
		pack();
		//Center the frame on the user’s screen
		setLocationRelativeTo(null);
		//Make the frame visible
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//check if buttons on the button bar are clicked
		//the start button opens a new level frame
		if(e.getSource() == startButton) {
			//close the current frame
			this.dispose();
			//open the customize frame
			new CustomizeFrame();
		}
		
		else if (e.getSource() == exitButton) {
			//user wants to exit so terminate the program
			System.exit(0);
		}
		
		else if(e.getSource() == marioButton) {
			//play the countdown sound effect
			//Source: https://www.geeksforgeeks.org/play-audio-file-using-java/
			//
			AudioInputStream audioInputStream;
			try {
				//set the audio file to the audio stream
				audioInputStream = AudioSystem.getAudioInputStream(new File(
						"audio/marioIntro.wav").getAbsoluteFile());
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				//start playing the clip
				clip.start();
			//cannot reuse the parameters so I changed it to e2
			} catch (Exception e2) {
				e2.printStackTrace();
			}//end of catch
		}
		
		else if (e.getSource()==lbButton) {
			//don't need to dispose current frame
			//open the leader board frame
			new LeaderBoardFrame();
		}
		
	}

}
