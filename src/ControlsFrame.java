/*
 * This frame displays all the controls and their 
 * respective descriptions in a vertical format with a
 * scroll pane.
 * 
 * The list of upgrades can be found in the comment
 * header of the Level Frame.
 */

//import action listener for the buttons
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//
import javax.swing.*;
import java.awt.*;

public class ControlsFrame extends JFrame implements ActionListener{
	//header displayed at the top
	private JLabel controlsLabel;
	
	//button that closes the frame
	private JButton backButton;
	
	//constructor
	public ControlsFrame() {
		//set the layout manager
		setLayout(new BorderLayout());
		//java swing component arrays that reference the 
		//arrays in the level frame
		ImageIcon[] buttons = StartFrame.buttonImage;
		JLabel[] labels = StartFrame.labelArray;
		String[] desc = StartFrame.description;
		
		//Panel for arranging all components
		JPanel mainPanel = new JPanel(new BorderLayout());
		//set the background of the main panel
		mainPanel.setBackground(new Color(215,111,75));
		
		//label for vertically "stacking" the control button
		//images and descriptions
		JPanel subPanel = new JPanel();
		//Box layout is able to arrange components vertically 
		//Source: https://docs.oracle.com/javase/tutorial/uiswing/layout/box.html
		subPanel.setLayout(new BoxLayout(subPanel,BoxLayout.Y_AXIS));
		subPanel.setBackground(new Color(115,68,33));
		
		//Information label: use html to format the text
		//		- I learned some html in the BTT2O1 course last year
		//Source(adding html to JLabel): 
		//		https://stackoverflow.com/questions/6635730/
		//		how-do-i-put-html-in-a-jlabel-in-java
		controlsLabel= new JLabel("<html><h1>How To Play - Controls</h1>"+
				//add a horizontal line and add an empty line in between
				"<hr><br>" +
				//tell the user how to start the timer here since there
				//isn't a button for it
				"-> Press 'A', 'D' or the Space Bar to start"+
				" the timer after entering the game!</html>");
		//customize the controlsLabel by setting the font and color
		controlsLabel.setForeground(new Color(65,36,8));
		controlsLabel.setFont(new Font("Serif", Font.ITALIC, 20));
		//place the label in the center
		//Source: https://docs.oracle.com/javase/tutorial/
		//uiswing/layout/box.html
		controlsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		//I will add the info label to the main panel later
		
		//Need to increment by two since there are two
		//labels for each button: one for the description
		for(int i = 0; i < labels.length; i += 2) {
			//Panel that stores the components
			JPanel controlsPanel = new JPanel(new BorderLayout());
			
			//
			labels[i+1].setForeground(new Color(65,36,8));
			
			//set the layout of the panel and add the components
			controlsPanel.add(labels[i], BorderLayout.WEST);
			controlsPanel.add(labels[i+1],  BorderLayout.CENTER);
			
			//set the size of the panel
			//Source: https://stackoverflow.com/questions/1594423/
			//setting-the-size-of-panels
			controlsPanel.setPreferredSize(new Dimension(200,70));
			//set the background of the panel to match the bricks
			//in the level frame 
			controlsPanel.setBackground(new Color(215,111,75));
			
			//add the panel to the center of the info panel
			//by setting its alignment
			//Source: https://docs.oracle.com/javase/tutorial/
			//uiswing/layout/box.html
			controlsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
			subPanel.add(controlsPanel);
			//Create a rigid area in between the panels so that
			//they have a bit of space between them
			//Source: https://docs.oracle.com/javase/tutorial/
			//uiswing/layout/box.html
			subPanel.add(Box.createRigidArea(new Dimension(0,10)));
		}
		
		//make the button and set the image
		backButton = new JButton(new ImageIcon("images/back.png"));
		//add the action listener
		backButton.addActionListener(this);
		
		//add the components to the main panel that will 
		//have a scroll pane attached to it
		mainPanel.add(controlsLabel, BorderLayout.NORTH);
		mainPanel.add(subPanel, BorderLayout.CENTER);
		mainPanel.add(backButton, BorderLayout.SOUTH);
		
		//Create a new instance of the JScrollPane class and make
		//it a vertical scroll pane. Attach the main panel and the 
		//scroll pane together.
		//Source: https://docs.oracle.com/javase/
		//		tutorial/uiswing/components/scrollpane.html
		JScrollPane scrollPane = new JScrollPane(mainPanel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//Add the scroll pane and main panel to the frame 
		add(scrollPane,BorderLayout.CENTER);
		//make the scroll speed faster 
		//Source: https://stackoverflow.com/questions/
		//5583495/how-do-i-speed-up-the-scroll-speed-
		//in-a-jscrollpane-when-using-the-mouse-wheel
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		
		//------------- set the properties of the frame ---------------
		//set the title
		setTitle("Controls");
		//set the size of the frame
		//Source: https://stackoverflow.com/questions/
		//1594423/setting-the-size-of-panels
		setPreferredSize(new Dimension(875,500));
		//I will not set the default close operation
		//since the user is just checking the controls.
		//They would likely want to keep playing the game afterwards.
		//Automatically size the frame to fit components
		pack();
		//Center the frame on the user’s screen
		//This way, the controls frame will perfectly overlap the 
		//user's game frame (LevelFrame)
		setLocationRelativeTo(null);
		//Make the frame visible
		setVisible(true);
	}
	
	//action listener
	@Override
	public void actionPerformed(ActionEvent e) {
		//close the frame if the user clicks the okButton
		if(e.getSource() == backButton) {
			//dispose the current frame
			this.dispose();
			//no need to open the level frame since
			//it was not closed when this frame was called
			//start the timer again
			LevelFrame.paused = false;
		}
	}

}
