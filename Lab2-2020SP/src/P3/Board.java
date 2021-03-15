package P3;


import java.util.ArrayList;
import java.util.List;

/**
 * A square board for the game
 * gochess determines from the kind of game,true means go and false means chess 
 * size shows the side length of the square board
 * pieces reserve the pieces that already on the board
 * @author 123
 *
 */
public class Board {
	private final boolean gochess;
	private final int size;
	private List<Piece> pieces=new ArrayList<Piece>();
	private final int gosize=19;//the board length for go game
	private final int chesssize=8;//the board length for chess game
	
	/**
	 * construct the board with given parameters and initial the board
	 * according to the kind of game
	 * @param gochess the kind of game/board
	 * @param size side length of board
	 */
	public Board(boolean gochess,int size) {
		this.gochess=gochess;
		this.size=size;
		if(!gochess) {//国际象棋棋盘初始化
			for(int i=0;i<chesssize;i++) { //摆兵
				Position p1=new Position(1,i);
				Position p2=new Position(6, i);
				Piece chess1=new Piece(true, p1, 7);
				Piece chess2=new Piece(false, p2, 7);
				pieces.add(chess1);
				pieces.add(chess2);
			}
			for(int i=0;i<chesssize/2;i++) {//依序加入车、马、象、后、王
				Position p11=new Position(0, i);
				Position p21=new Position(7, i);
				Position p12=new Position(0, 7-i);
				Position p22=new Position(7, 7-i);
				Piece chess11=new Piece(true, p11, i+2);
				Piece chess21=new Piece(false, p21, i+2);
				Piece chess12=new Piece(true, p12, i+2);
				Piece chess22=new Piece(false, p22, i+2);
				pieces.add(chess11);
				pieces.add(chess12);
				pieces.add(chess21);
				pieces.add(chess22);
			}
		}
	}

	/**
	 * @return the gochess which shows the kind of board
	 */
	public boolean getGochess() {
		return this.gochess;
	}

	/**
	 * @return the side length of board
	 */
	public int getSize() {
		return this.size;
	}
	
	/**
	 * @return the pieces that already on the board
	 */
	public List<Piece> getPieces(){
		/*List<Piece>copypiece=new ArrayList<Piece>();
		copypiece*/
		return this.pieces;
	}
	
	/**
	 * judge if the piece is on the board already
	 * @param p piece needed to judge
	 * @return true when the piece is on the board otherwise false
	 */
	public boolean haspiece(Piece p) {
		if(this.pieces.contains(p)) {
			return true;
		}
		return false;
	}
	
	/**
	 * find the piece with given x-coordinate and y-coordinate
	 * @param x x-coordinate of target position
	 * @param y y-coordinate of target position
	 * @return the piece in the target position it it does exist 
	 * 		   otherwise one piece whose kind is zero
	 */
	public Piece poshaspiece(int x,int y) {
		Position position=new Position(x, y);
		Position initposition=new Position(-1, -1);
		Piece errorpiece=new Piece(false, initposition, 0);
		if(this.gochess) {
			if(x<0||y<0||x>=gosize||y>=gosize) {
				return errorpiece;
			}
			else {
				for(Piece p:this.getPieces()) {
					if(p.getPosition().equals(position)) {
						Piece returnPiece=new Piece(p.getcolor(), p.getPosition(), p.getKind());
						return returnPiece;
					}				
				}
			}
		}
		else {
			if(x<0||y<0||x>=chesssize||y>=chesssize) {
				return errorpiece;
			}
			else {
				for(Piece p:this.getPieces()) {
					if(p.getPosition().equals(position)) {
						Piece returnPiece=new Piece(p.getcolor(), p.getPosition(), p.getKind());
						return returnPiece;
					}					
				}
			}
		}
		return errorpiece;
	}
	
	/**
	 * print whole board on the terminal
	 */
	public void printboard() {
		if(this.gochess) {
			for(int i=0;i<gosize;i++) {
				for(int j=0;j<gosize;j++) {
					Piece piece=poshaspiece(i, j);
					if(piece.getKind()==0) {
						System.out.print("empty     "+"\t");
					}
					else if(piece.getcolor()) {
						System.out.print("P1's White"+"\t");
					}
					else {
						System.out.print("P2's Black"+"\t");
					}
				}
				System.out.print("\n");
			}
		}
		else {
			for(int i=0;i<chesssize;i++) {
				for(int j=0;j<chesssize;j++) {
					Piece piece=this.poshaspiece(i, j);
					switch (piece.getKind()) {
					case 0:
						System.out.print("empty      "+"\t");
						break;
					case 2:{
						if(piece.getcolor())
							System.out.print("P1's Rook  "+"\t");
						else
							System.out.print("P2's Rook  "+"\t");
						break;
					}
					case 3:{
						if(piece.getcolor())
							System.out.print("P1's Knight"+"\t");
						else
							System.out.print("P2's Knight"+"\t");
						break;
					}
					case 4:{
						if(piece.getcolor())
							System.out.print("P1's Bishop"+"\t");
						else
							System.out.print("P2's Bishop"+"\t");
						break;
					}
					case 5:{
						if(piece.getcolor())
							System.out.print("P1's Queen "+"\t");
						else
							System.out.print("P2's Queen "+"\t");
						break;
					}
					case 6:{
						if(piece.getcolor())
							System.out.print("P1's King  "+"\t");
						else
							System.out.print("P2's King  "+"\t");
						break;
					}
					case 7:{
						if(piece.getcolor())
							System.out.print("P1's Pawn  "+"\t");
						else
							System.out.print("P2's Pawn  "+"\t");
						break;
					}
					default:
						break;
					}
				}
				System.out.print("\n");
		}
	}
}
}
