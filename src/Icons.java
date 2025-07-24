/*
 * template class for icons
 * project header here
 */

import javax.swing.ImageIcon;

public class Icons {
	//
	public static final ImageIcon WALL = new ImageIcon(
			"images/Brick.png");
	public static final ImageIcon PRESENT = new ImageIcon(
			"images/present.gif");
	//there are different versions of Mario in image folder
	//facing different directions
	//hint: store four images in array and call them at a 
	//particular index
	public static final ImageIcon[] MARIO = {
			new ImageIcon("images/Idle.png"),
			new ImageIcon("images/Runningright.gif"),
			new ImageIcon("images/Runningleft.gif"),
			new ImageIcon("images/Jumping.gif")};
	//
	public static final ImageIcon STAR = new ImageIcon(
			"images/star.png");
	public static final ImageIcon SPIKE = new ImageIcon(
			"images/spikedBall.png");
	public static final ImageIcon MUSHROOM = new ImageIcon(
			"images/Mushroom.png");
	public static final ImageIcon FOG = new ImageIcon(
			"images/fog.png");
}
