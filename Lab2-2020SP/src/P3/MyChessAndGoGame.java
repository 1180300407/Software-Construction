package P3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MyChessAndGoGame {
	public static void main(String[] args) throws IOException {
		boolean gamekind;
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));  
		System.out.println("请选择游戏类型：chess代表国际象棋，go代表围棋");
		String input=null;
		input = bf.readLine();
		while(!input.equals("chess")&&!input.equals("go")) {//处理非法输入
			System.out.println("输入不规范，请重新输入！");				
			input=bf.readLine();
		}
		if(input.equals("go")) {//围棋
			gamekind=true;
		}	
		else{//国际象棋
			gamekind=false;
		}
		System.out.println("请输入玩家1(执白棋)的姓名:");
		input=bf.readLine();
		Player p1=new Player(true, input);
		System.out.println("请输入玩家2(执黑棋)的姓名:");
		input=bf.readLine();
		Player p2=new Player(false, input);
		Game game=new Game(p1, p2, gamekind);//创建一盘游戏
		
		input=null;
		if(gamekind==true) {
			System.out.println("玩法提示"+"\n"+"落子:put x,y /*x,y为[0,18]区间整数*/");
			System.out.println("提子:pick x,y /*x,y为[0,18]区间整数*/");
			System.out.println("查询:query x,y /*x,y为[0,18]区间整数*/");
			System.out.println("跳过:skip");
			System.out.println("显示棋盘:print");
			System.out.println("结束游戏:end");
			System.out.println("本回合执棋手："+p1.getName());
			System.out.println("请输入命令:");
			input=bf.readLine();
			boolean turn=true;
			while(!input.equals("end")) {//只有输入end才结束游戏
				String[] instruction=input.split(" ");
				if(instruction[0].equals("skip")) {//skip命令
					turn=!turn;//调换执行方
					if(turn)
						System.out.println("本回合执棋手："+p1.getName());
					else
						System.out.println("本回合执棋手："+p2.getName());
					System.out.println("请输入命令:");
					input=bf.readLine();
				}
				else if(instruction[0].equals("print")) {//print命令
					game.getBoard().printboard();
					System.out.println("请输入命令:");
					input=bf.readLine();
				}
				else if(instruction[0].equals("query")) {//query命令
					String[] xy=instruction[1].split(",");
					int x=Integer.valueOf(xy[0]);
					int y=Integer.valueOf(xy[1]);
					Position queryPosition=new Position(x, y);
					Piece queryPiece=new Piece(turn, queryPosition, 1);
					if(game.getBoard().haspiece(queryPiece)) {
						System.out.println("该位置有您的一枚棋子");
					}
					else {
						System.out.println("该位置没有您的棋子");
					}
					System.out.println("请输入命令:");
					input=bf.readLine();
				}
				else if(instruction[0].equals("put")) {//put命令
					String[] xy=instruction[1].split(",");
					int x=Integer.valueOf(xy[0]);
					int y=Integer.valueOf(xy[1]);
					if(turn) {
						boolean flag= game.getP1().getAct().put(game.getP1().getColor(), x, y, game.getBoard(), 1);	
						if(!flag) {//棋子非法情况
							System.out.println("指令执行失败，请重新输入:");
							input=bf.readLine();
						}
						else {
							turn=!turn;
							if(turn)
								System.out.println("本回合执棋手："+p1.getName());
							else
								System.out.println("本回合执棋手："+p2.getName());
							System.out.println("请输入命令:");
							input=bf.readLine();
						}
					}
					else {
						boolean flag= game.getP2().getAct().put(game.getP1().getColor(), x, y, game.getBoard(), 1);	
						if(!flag) {//棋子非法情况
							System.out.println("指令执行失败，请重新输入:");
							input=bf.readLine();
						}
						else {
							turn=!turn;
							if(turn)
								System.out.println("本回合执棋手："+p1.getName());
							else
								System.out.println("本回合执棋手："+p2.getName());
							System.out.println("请输入命令:");
							input=bf.readLine();
						}
					}
				}
				else if(instruction[0].equals("pick")) {//pick命令
					String[] xy=instruction[1].split(",");
					int x=Integer.valueOf(xy[0]);
					int y=Integer.valueOf(xy[1]);
					if(turn) {
						boolean flag= game.getP1().getAct().pickup(game.getP1().getColor(), x, y, game.getBoard());	
						if(!flag) {//棋子非法情况
							System.out.println("指令执行失败，请重新输入:");
							input=bf.readLine();
						}
						else {
							turn=!turn;
							if(turn)
								System.out.println("本回合执棋手："+p1.getName());
							else
								System.out.println("本回合执棋手："+p2.getName());
							System.out.println("请输入命令:");
							input=bf.readLine();
						}
					}
					else {
						boolean flag= game.getP2().getAct().pickup(game.getP1().getColor(), x, y, game.getBoard());	
						if(!flag) {//棋子非法情况
							System.out.println("指令执行失败，请重新输入:");
							input=bf.readLine();
						}
						else {
							turn=!turn;
							if(turn)
								System.out.println("本回合执棋手："+p1.getName());
							else
								System.out.println("本回合执棋手："+p2.getName());
							System.out.println("请输入命令:");
							input=bf.readLine();
						}
					}
				}
				else {//除此之外的输入均为非法输入
					System.out.println("输入命令不合法，请检查输入后重新输入:");
					input=bf.readLine();
				}
			}
		}
		else {
			System.out.println("玩法提示");
			System.out.println("移动棋子:move x1,y1 x2,y2 /*x1,y1.x2,y2为[0,7]区间整数,x1,y1为起始点坐标，x2,y2为目的点坐标*/");
			System.out.println("吃子:eat x,y /*x,y为[0,7]区间整数*/");
			System.out.println("查询:query x,y /*x,y为[0,7]区间整数*/");
			System.out.println("跳过:skip");
			System.out.println("显示棋盘:print");
			System.out.println("结束游戏:end");
			System.out.println("本回合执棋手："+p1.getName());
			System.out.println("请输入命令:");
			input=bf.readLine();
			boolean turn=true;
			while(!input.equals("end")) {//只有输入end才结束游戏
				String[] instruction=input.split(" ");
				if(instruction[0].equals("skip")) {//skip命令
					turn=!turn;//调换执行方
					if(turn)
						System.out.println("本回合执棋手："+p1.getName());
					else
						System.out.println("本回合执棋手："+p2.getName());
					System.out.println("请输入命令:");
					input=bf.readLine();
				}
				else if(instruction[0].equals("print")) {//print命令
					game.getBoard().printboard();
					System.out.println("请输入命令:");
					input=bf.readLine();
				}
				else if(instruction[0].equals("query")) {//query命令
					String[] xy=instruction[1].split(",");
					int x=Integer.valueOf(xy[0]);
					int y=Integer.valueOf(xy[1]);
					Position queryPosition=new Position(x, y);
					Piece queryPiece=new Piece(turn, queryPosition, 1);
					if(game.getBoard().haspiece(queryPiece)) {
						System.out.println("该位置有您的一枚棋子");
					}
					else {
						System.out.println("该位置没有您的棋子");
					}
					System.out.println("请输入命令:");
					input=bf.readLine();
				}
				else if(instruction[0].equals("move")) {//put命令
					String[] xy1=instruction[1].split(",");
					int x1=Integer.valueOf(xy1[0]);
					int y1=Integer.valueOf(xy1[1]);
					String[] xy2=instruction[2].split(",");
					int x2=Integer.valueOf(xy2[0]);
					int y2=Integer.valueOf(xy2[1]);
					if(turn) {
						boolean flag= game.getP1().getAct().move(turn, x1, y1, x2, y2, game.getBoard());	
						if(!flag) {//棋子非法情况
							System.out.println("指令执行失败，请重新输入:");
							input=bf.readLine();
						}
						else {
							turn=!turn;
							if(turn)
								System.out.println("本回合执棋手："+p1.getName());
							else
								System.out.println("本回合执棋手："+p2.getName());
							System.out.println("请输入命令:");
							input=bf.readLine();
						}
					}
					else {
						boolean flag= game.getP2().getAct().move(turn, x1, y1, x2, y2, game.getBoard());	
						if(!flag) {//棋子非法情况
							System.out.println("指令执行失败，请重新输入:");
							input=bf.readLine();
						}
						else {//执行成功
							turn=!turn;
							if(turn)
								System.out.println("本回合执棋手："+p1.getName());
							else
								System.out.println("本回合执棋手："+p2.getName());
							System.out.println("请输入命令:");
							input=bf.readLine();
						}
					}
				}
				else if(instruction[0].equals("eat")) {//pick命令
					String[] xy1=instruction[1].split(",");
					int x1=Integer.valueOf(xy1[0]);
					int y1=Integer.valueOf(xy1[1]);
					String[] xy2=instruction[2].split(",");
					int x2=Integer.valueOf(xy2[0]);
					int y2=Integer.valueOf(xy2[1]);
					if(turn) {
						boolean flag= game.getP1().getAct().eatchess(turn, x1, y1, x2, y2, game.getBoard());	
						if(!flag) {//棋子非法情况
							System.out.println("指令执行失败，请重新输入:");
							input=bf.readLine();
						}
						else {
							turn=!turn;
							if(turn)
								System.out.println("本回合执棋手："+p1.getName());
							else
								System.out.println("本回合执棋手："+p2.getName());
							System.out.println("请输入命令:");
							input=bf.readLine();
						}
					}
					else {
						boolean flag= game.getP2().getAct().eatchess(turn, x1, y1, x2, y2, game.getBoard());	
						if(!flag) {//棋子非法情况
							System.out.println("指令执行失败，请重新输入:");
							input=bf.readLine();
						}
						else {
							turn=!turn;
							if(turn)
								System.out.println("本回合执棋手："+p1.getName());
							else
								System.out.println("本回合执棋手："+p2.getName());
							System.out.println("请输入命令:");
							try {
								input=bf.readLine();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				else {//除此之外的输入均为非法输入
					System.out.println("输入命令不合法，请检查输入后重新输入:");
					input=bf.readLine();
				}
		}
	}
}
}
