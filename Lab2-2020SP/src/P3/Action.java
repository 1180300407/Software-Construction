package P3;


/**
 * imitate the action of player,including put the piece,
 * move piece to another position,remove enemy's piece
 * @author 123
 *
 */
public class Action {
	
	//Abstraction function:
	//	Consist of several actions player may take
	//Representation invariant:
	//	true
	//Safety from rep exposure:
	//	All return values are unmodifiable values
	
	
	/**
	 * put one piece that not on the board to target position
	 * @param color whom the piece belongs to
	 * @param x x-coordinate of the target position
	 * @param y y-coordinate of the target position
	 * @param board board of the game
	 * @param kind the kind of piece
	 * @return true if the piece is truly put on the board,otherwise false 
	 */
	public boolean put(boolean color, int x,int y,Board board,int kind) {
		if(x<0||y<0||x>=board.getSize()||y>=board.getSize()) {//指定位置越界
			System.out.println("The target position goes over the board size!");
			return false;
		}
		Position targetPosition=new Position(x, y);
		for(Piece piece:board.getPieces()) {
			if(piece.getPosition().equals(targetPosition)) {//指定位置已有棋子
				System.out.println("There is already one piece in the targetposition!");
				return false;
			}
		}
		Piece p=new Piece(color, targetPosition, kind);
		board.getPieces().add(p);
		return true;
	}
	
	/**
	 * move one piece from origin position to target position
	 * @param player shows whom the piece belongs to
	 * @param oldx x-coordinate of the origin position
	 * @param oldy y-coordinate of the origin position
	 * @param targetx x-coordinate of the target position
	 * @param targety y-coordinate of the target position
	 * @param board board of the game
	 * @return true if the move action truly succeed otherwise false
	 */
	public boolean move(boolean player,int oldx,int oldy,int targetx,int targety,Board board) {
		if(oldx<0||oldy<0||oldx>=board.getSize()||oldy>=board.getSize()) {//给定初始位置越界
			System.out.println("The original coordinate goes over the board size!");
			return false;
		}
		if(targetx<0||targety<0||targetx>=board.getSize()||targety>=board.getSize()) {//给定目标位置越界
			System.out.println("The target coordinate goes over the board size!");
			return false;
		}
		if(oldx==targetx&&oldy==targety) {
			System.out.println("The origin position is the same as target position!");
			return false;
		}
		Position originalPosition=new Position(oldx, oldy);
		Position targetPosition=new Position(targetx, targety);
		int findold=-1;
		for(Piece p:board.getPieces()) {
			if(p.getPosition().equals(targetPosition)) {//目标位置已有棋子
				System.out.println("There is already one piece in the original place");
				return false;
			}
			if(p.getPosition().equals(originalPosition)) {
				if(p.getcolor()==player) {//初始位置确实找到了可以移动的棋子
					findold=board.getPieces().indexOf(p);
				}
				else {//初始位置处棋子是对方的棋子
					System.out.println("The origin piece is not your piece!");
					return false;
				}
			}
		}
		
		if(findold==-1) {//初始位置无棋子可供移动
			System.out.println("There is no piece to move!");
			return false;
		}
		//初始位置有自己棋子，目标位置无棋子
		board.getPieces().get(findold).setPosition(targetPosition);
		return true;
	}
	
	/**
	 * pick up the piece from the target position
	 * @param player shows whom the piece belongs to
	 * @param x x-coordinate of the target position
	 * @param y y-coordinate of the target position
	 * @param board board of the game
	 * @return true if the pickup action truly succeed otherwise false
	 */
	public boolean pickup(boolean player,int x,int y,Board board) {
		if (x<0||y<0||x>=board.getSize()||y>=board.getSize()) {//位置越界
			System.out.println("The target position goes over the board size!");
			return false;
		}
		Position position=new Position(x, y);
		boolean find=false;
		for(Piece p:board.getPieces()) {
			if(p.getPosition().equals(position)) {//目标处存在棋子
				if(p.getcolor()!=player) {//棋子为对方，可提取
					find=true;
					board.getPieces().remove(p);
					break;
				}
				else {//棋子为己方，不能提取
					System.out.println("The piece you want to pick up is your own piece,not others'!");
					return false;
				}
			}
		}
		
		if(!find)//目标处无棋子可提取
			System.out.println("There is no piece to pick up!");
		
		return find;
	}
	
	/**
	 * use one piece to eat another piece of the target position
	 * @param player shows whom the piece belongs to
	 * @param oldx x-coordinate of the origin position
	 * @param oldy y-coordinate of the origin position
	 * @param targetx x-coordinate of the target position
	 * @param targety y-coordinate of the target position
	 * @param board board of the game
	 * @return true if the eat action truly succeed otherwise false
	 */
	public boolean eatchess(boolean player,int oldx,int oldy,int targetx,int targety,Board board) {
		boolean remove=pickup(player, targetx, targety, board);
		boolean mov=move(player, oldx, oldy, targetx, targety, board);
		if(remove&&mov)
			return true;
		else {
			System.out.println("Fail to eatchess!");
			return false;
		}
	}
}
