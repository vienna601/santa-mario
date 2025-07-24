/*
 * This frame prompts the user to enter their user 
 * name and preferred background theme. The user name
 * will be displayed in the leader board frame if the
 * user sets a new record for the highest score.
 * 
 * The list of upgrades can be found in the comment
 * header of the Level Frame.
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class CustomizeFrame extends JFrame implements ActionListener{
	//set a default name
	static String name = "luigi";
	static String chosenbg;
	//question labels
	private JLabel qLabel1;
	private JLabel qLabel2;
	//radio buttons for selection
	private JRadioButton rb1;
	private JRadioButton rb2;
	//button for user to confirm their input
	private JButton ConfirmButton;
	//Text fields for the user to input their answers
	private JTextField InputField;
	
	//create constructor
	public CustomizeFrame(){
		//Make question labels
		qLabel1 = new JLabel("Enter your name: ");
		//qLabel2 = new JLabel("Choose a theme for the background: ");
		//Initialize text fields for user input
		InputField = new JTextField(10);
		
		qLabel2 = new JLabel("Select a background theme: ");
		//https://www.geeksforgeeks.org/jradiobutton-java-swing/
		rb1 = new JRadioButton("Riverside");
		rb2 = new JRadioButton("Village");
		
		//make the confirm button and add the action listener
		ConfirmButton = new JButton("Confirm");
		ConfirmButton.addActionListener(this);
		//
		JPanel Panel = new JPanel();
		Panel.setLayout(new BoxLayout(Panel, BoxLayout.Y_AXIS));
		//Make components transparent so that I can see the 
		//background image
		//Source:https://stackoverflow.com/questions/258023/
		//creating-a-transparent-panel
		Panel.setOpaque(false);
		ConfirmButton.setOpaque(false);
		
		//add all components to the panel
		Panel.add(qLabel1);
		Panel.add(InputField);
		Panel.add(qLabel2);
		Panel.add(rb1);
		Panel.add(rb2);
		Panel.add(ConfirmButton);
		
		//add the main label to the center of the frame
		setLayout(new BorderLayout());
		add(Panel,BorderLayout.CENTER);
		
		//Set the JFrame properties
		//Set the title
		setTitle("Customize");
		//Use setPreferredSize() to make sure that the frame is
		//always displayed in these dimensions
		//Source: https://stackoverflow.com/questions/
		//1594423/setting-the-size-of-panels
		setPreferredSize(new Dimension(300,200));
		//Automatically size the frame to fit the components
		pack();
		//Center the frame on the user’s screen
		setLocationRelativeTo(null);
		//Make the frame visible
		setVisible(true);
	}
	
	//Action Listener
	@Override
	public void actionPerformed(ActionEvent e) {
		//check if the confirm button was clicked
		if (e.getSource() == ConfirmButton) {
			//trim extra/unnecessary spaces in the user's answer
			String user = InputField.getText().trim();
			//show an error message if the user
			//leaves a question blank
			if(user.isEmpty()||(rb1.isSelected()==false&&rb2.isSelected() == false)){
				//show an error message
				JOptionPane.showMessageDialog(this, "Please answer all questions!", 
						"Error", JOptionPane.ERROR_MESSAGE);
			}//end of if
			
			//user selects both options
			else if(rb1.isSelected()&&rb2.isSelected()) {
				//show an error message
				JOptionPane.showMessageDialog(this, "Only one background can be selected.", 
						"Error", JOptionPane.ERROR_MESSAGE);
			}
			
			//both questions were answered
			else {
				//dispose the current frame before displaying the message
				this.dispose();
				//set the user's answers to the corresponding variables
				name = user;
				//set the text of the radio buttons to the bg variable which will
				//be used in the level frame
				if(rb1.isSelected())
					chosenbg = rb1.getText();
				else//only other possibility is that the second radio 
					//button was selected
					chosenbg = rb2.getText();
				//open a new level frame, passing 1 as the argument
				//so that the user starts at the first level
				new LevelFrame(1,chosenbg);
				//play the countdown sound effect after calling the level frame
				//Source: https://www.geeksforgeeks.org/play-audio-file-using-java/
				AudioInputStream audioInputStream;
				try {
					//reference the audio file and set it to the audio input stream
					audioInputStream = AudioSystem.getAudioInputStream(
							new File("audio/startingCount.wav").getAbsoluteFile());
					//get the audio clip form the input stream
					Clip clip = AudioSystem.getClip();
					//open and play the clip
					clip.open(audioInputStream);
					clip.start();
				//prevents program from being terminated after error occurs
				} catch (Exception e1) {
					//
					e1.printStackTrace();
				}//end of catch
			}
		}
	}//end of action performed method
	
}//end of class body

