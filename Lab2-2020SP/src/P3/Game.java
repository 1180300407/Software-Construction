package P3;

/**
 * A game for chess or go
 * two players of the game,one for white pieces and another for black
 * gochess shows if the game is go game or chess game
 * board is the whole board for the entire game
 * @author 123
 *
 */
public class Game {
	private Player p1;
	private Player p2;
	private final boolean gochess;//true means go and false means chess
	private Board board;
	private final int gosize=19;//the board length for go game
	private final int chesssize=8;//the board length for chess game
	
	/**
	 * Construct the game with given parameters
	 * @param p1 one player,has white or black pieces
	 * @param p2 anther player,has another kind of pieces
	 * @param gochess the kind of game
	 * @param board entire board of game
	 */
	public Game(Player p1,Player p2,boolean gochess) {
		this.p1=p1;
		this.p2=p2;
		this.gochess=gochess;
		if(gochess) {
			Board gameBoard=new Board(gochess, gosize);
			this.board=gameBoard;
		}
		else {
			Board gameBoard=new Board(gochess, chesssize);
			this.board=gameBoard;
		}
	}

	/**
	 * @return one of the player
	 */
	public Player getP1() {
		return p1;
	}

	/**
	 * @return another player
	 */
	public Player getP2() {
		return p2;
	}

	/**
	 * @return the kind of game
	 */
	public boolean getGochess() {
		return gochess;
	}

	/**
	 * @return entire board
	 */
	public Board getBoard() {
		return board;
	}
}
