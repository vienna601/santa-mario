/* 
 * ====== Santa Mario ======
 * This frame sets up the game board and loads all the 
 * labels, panels and buttons. It will also bind the
 * corresponding keys with an action by passing a 
 * string argument to the key action class which handles
 * a part of character movement.
 * 
 * Some features:
 * - Get player image to Face the Proper Direction as they move
 * - Get player image to change if jumping, moving, and stopping 
 * - Accurate Timing 
 * - Pause game button 
 * - Menubar with a number of options (New Game, Quit, etc.)
 * - Separate Opening Screen before the game starts
 * - Background music and sound Effects
 * - Power-ups and pop up screen
 * - Leaderboard with a player’s initials (saved to external text file)
 */

public class SuperMarioApplication {

	public static void main(String[] args) {
		new StartFrame();
	}

}
