package P3;

/**
 * A piece in the game
 * color shows black or white which determines the player of piece
 * position shows the two-dimension-position of piece in the board
 * kind can determine it belongs to chess or go,and the concrete kind of piece of chess
 * @author 123
 *
 */

public class Piece {
	private final boolean color;
	private Position position;
	private final int kind;
	
	//Abstraction function:
	//	AF(color,position,kind)=one piece belongs to color with position and kind
	//Representation invariant:
	//	
	//Safety from rep exposure:
	//	All fields are private,defensive copy
	
	/**
	 * construct the Piece with parameters
	 * @param color
	 * @param position
	 * @param kind
	 */
	public Piece(boolean color,Position position,int kind) {
		this.color=color;
		this.setPosition(position);
		this.kind=kind;
	}

	/**
	 * @return color of the piece,true means white,false means black
	 */
	public boolean getcolor() {
		return color;
	}

	/**
	 * @return two-dimension-position of the piece
	 */
	public Position getPosition() {
		return new Position(this.position.X(), this.position.Y());
	}

	/**
	 * move the piece to another position
	 * @param position target position the piece will go next
	 */
	public void setPosition(Position position) {
		this.position = new Position(position.X(), position.Y());
	}

	/**
	 * @return the kind of Piece,1 means go,2 to 7 means chess
	 * 		   and different concrete numbers shows different kinds in chess
	 * 		   2:车；3：马；4：象；5：后；6：王；7：兵	
	 */
	public int getKind() {
		return kind;
	}
}
