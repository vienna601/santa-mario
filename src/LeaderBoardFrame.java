/*
 * This frame displays the name and score
 * of the highest scoring player. It makes use
 * of a scanner object and delimiter to read the 
 * information in a txt file.
 * 
 * The list of upgrades can be found in the comment
 * header of the Level Frame.
 */
//for setting the size
import java.awt.Dimension;
//for setting the font of the labels
import java.awt.Font;
//for action listener
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//for scanning the txt file
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
//for the swing components
import javax.swing.*;

public class LeaderBoardFrame extends JFrame implements ActionListener{
	//JLabel that holds the background image
	private JLabel bglabel;
	//JLabel that displays the user's name and score
	private JLabel infolabel;
	//button for closing the frame
	private JButton backbutton;
	//set default name and score
	String username = "default";
	int Score = 0;
	
	//constructor
	public LeaderBoardFrame() {
		//set the layout
		setLayout(null);
		//read the information in the HighScore txt file
		//The information will be displayed in the JLabels
		try {
			//create scanner object
			Scanner inputFile = new Scanner(new File("file/HighScore.txt"));
			//Use the delimiter:
			//, : to tell java that commas are the 
			// separating factor in the txt file 
			inputFile.useDelimiter(",|\r\n");
			//store the information in the corresponding variables
			//which will be used to set the text of the labels later
			Score = inputFile.nextInt();
			username = inputFile.next();
		}//end of try
		
		//prevents program from terminating in case of error
		catch (FileNotFoundException e1){
			//print the error message in the console
			e1.printStackTrace();
		}//end of catch
		
		//initialize the JLabels
		//I will use html to format my labels
		infolabel = new JLabel("<html>"+username+"<br>"+Score+"</html>");
		//format the text
		infolabel.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		bglabel = new JLabel(new ImageIcon("images/trophy.png"));
		//make the jbutton and set the image (same image as back button in the controlsframe)
		backbutton = new JButton(new ImageIcon("images/back.png"));
		//add the action listener
		backbutton.addActionListener(this);
		
		//add the infolabel and button to the bglabel
		infolabel.setBounds(230,150,300,40);
		backbutton.setBounds(170,400,150,60);
		bglabel.add(infolabel);
		bglabel.add(backbutton);
		
		//set a size for the label and add the bglabel to the frame
		bglabel.setBounds(0,0,500,500);
		add(bglabel);
		
		//Set the JFrame properties
		//Set the title
		setTitle("LeaderBoard");
		//Use setPreferredSize() to make sure that the frame is
		//always displayed in these dimensions
		//Source: https://stackoverflow.com/questions/
		//1594423/setting-the-size-of-panels
		setPreferredSize(new Dimension(500,500));
		//Automatically size the frame to fit the components
		pack();
		//Center the frame on the user’s screen
		setLocationRelativeTo(null);
		//Make the frame visible
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//check if back button is clicked
		if (e.getSource()==backbutton)
			//dispose the current frame
			this.dispose();
		
	}

}
