package P3;

/**
 * two-dimension-position in the board 
 * x is in the horizontal position,y is in the vertical position
 * @author 123
 *
 */

public class Position {
	private final int x;
	private final int y;

	//Abstraction function:
	//	AF(x,y)=two-dimension-position (x,y)
	//Representation invariant:
	//  coordinate should be integer number other than whole real number
	//Safety from rep exposure:
	//  All fields are private and final ;All immutable values
	
	public Position(int x,int y) {
		this.x=x;
		this.y=y;
	}
	/**
	 * @return x-coordinate of the position
	 */
	public int X() {
		return x;
	}

	
	/**
	 * @return y-coordinate of the position
	 */

	public int Y() {
		return y;
	}

}
