/*
 * This frame is responsible for displaying the animation
 * that corresponds to the power up collected by the user.
 * 
 * The list of upgrades can be found in the comment
 * header of the Level Frame.
 */

//for setting the layout
import java.awt.BorderLayout;
//for setting the size
import java.awt.Dimension;
//for action listener
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//for swing components
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

//I will destroy this frame after playing the animation
public class PowerUpFrame extends JFrame implements ActionListener{
	//there will only be one swing component, which is the JLabel
	//that holds the gif
	//I will initialize it here so that its default is a blank label
	//instead of a null object
	private JLabel gifLabel = new JLabel();
	
	//timer for self-destruct
	//wait 3.5 seconds so that the animation plays
	private Timer timer = new Timer(3500, this);
	//create constructor
	public PowerUpFrame(String effect) {
		//stop the timer as the animation also takes up time
		LevelFrame.paused = true;
		//set layout manager
		setLayout(new BorderLayout());
		
		//Display corresponding gif
		//I will use .equals since strings cannot be compared with ==
		//unless you want to compare their memory addresses. I do not
		//need to use .equalsIgnoreCase since I can control the argument
		//that is passed.
		if (effect.equals("star")) {
			//set the animation label to the star gif
			gifLabel.setIcon(new ImageIcon("images/collectStar.gif"));
			//set the corresponding title
			setTitle("You got a star!");
		}
		//if the user collected a mushroom
		else if (effect.equals("mushroom")) {
			gifLabel.setIcon(new ImageIcon("images/eatMushroom.gif"));
			//set the corresponding title
			setTitle("You got a mushroom!");
		}
		//if the character hit a spike
		else if (effect.equals("spike")) {
			gifLabel.setIcon(new ImageIcon("images/bowser.gif"));
			//set the corresponding title
			setTitle("Oh no! You hit a spike ball!");
		}
		
		//set the size of the label
		gifLabel.setPreferredSize(new Dimension(300,300));
		//add the label to the frame
		add(gifLabel,BorderLayout.CENTER);
		
		//------------- set the properties of the frame ---------------
		//set the size of the frame
		//Source: https://stackoverflow.com/questions/
		//1594423/setting-the-size-of-panels
		setPreferredSize(new Dimension(300,300));
		//I will not set the default close operation
		//since this frame will be disposed automatically.
		//The user will not have to close it.
		//Automatically size the frame to fit components
		pack();
		//Center the frame on the user’s screen
		//This way, the controls frame will perfectly overlap the 
		//user's game frame (LevelFrame)
		setLocationRelativeTo(null);
		//Make the frame visible
		setVisible(true);
		
		//start the timer
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//stop the timer
		timer.stop();
		//dispose current frame
		this.dispose();
		
	}

}
