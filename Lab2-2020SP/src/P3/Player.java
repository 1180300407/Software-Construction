package P3;

public class Player {
	private final boolean color;
	private final String name;
	private Action act=new Action();
	
	//Abstraction function:
	//	represents a player with color pieces,name and all actions
	//
	
	/**
	 * construct the Player with given parameter
	 * @param color determines if he has black or white pieces
	 * @param name unique name for every player
	 */
	public Player(boolean color,String name) {
		this.color=color;
		this.name=name;
	}

	/**
	 * @return true if he has white pieces,false if he has black pieces
	 */
	public boolean getColor() {
		return this.color;
	}

	/**
	 * @return the name of Player
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the action of the player
	 */
	public Action getAct() {
		return act;
	}
}
