package P3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MyChessAndGoGame {
	public static void main(String[] args) throws IOException {
		boolean gamekind;
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));  
		System.out.println("��ѡ����Ϸ���ͣ�chess����������壬go����Χ��");
		String input=null;
		input = bf.readLine();
		while(!input.equals("chess")&&!input.equals("go")) {//����Ƿ�����
			System.out.println("���벻�淶�����������룡");				
			input=bf.readLine();
		}
		if(input.equals("go")) {//Χ��
			gamekind=true;
		}	
		else{//��������
			gamekind=false;
		}
		System.out.println("���������1(ִ����)������:");
		input=bf.readLine();
		Player p1=new Player(true, input);
		System.out.println("���������2(ִ����)������:");
		input=bf.readLine();
		Player p2=new Player(false, input);
		Game game=new Game(p1, p2, gamekind);//����һ����Ϸ
		
		input=null;
		if(gamekind==true) {
			System.out.println("�淨��ʾ"+"\n"+"����:put x,y /*x,yΪ[0,18]��������*/");
			System.out.println("����:pick x,y /*x,yΪ[0,18]��������*/");
			System.out.println("��ѯ:query x,y /*x,yΪ[0,18]��������*/");
			System.out.println("����:skip");
			System.out.println("��ʾ����:print");
			System.out.println("������Ϸ:end");
			System.out.println("���غ�ִ���֣�"+p1.getName());
			System.out.println("����������:");
			input=bf.readLine();
			boolean turn=true;
			while(!input.equals("end")) {//ֻ������end�Ž�����Ϸ
				String[] instruction=input.split(" ");
				if(instruction[0].equals("skip")) {//skip����
					turn=!turn;//����ִ�з�
					if(turn)
						System.out.println("���غ�ִ���֣�"+p1.getName());
					else
						System.out.println("���غ�ִ���֣�"+p2.getName());
					System.out.println("����������:");
					input=bf.readLine();
				}
				else if(instruction[0].equals("print")) {//print����
					game.getBoard().printboard();
					System.out.println("����������:");
					input=bf.readLine();
				}
				else if(instruction[0].equals("query")) {//query����
					String[] xy=instruction[1].split(",");
					int x=Integer.valueOf(xy[0]);
					int y=Integer.valueOf(xy[1]);
					Position queryPosition=new Position(x, y);
					Piece queryPiece=new Piece(turn, queryPosition, 1);
					if(game.getBoard().haspiece(queryPiece)) {
						System.out.println("��λ��������һö����");
					}
					else {
						System.out.println("��λ��û����������");
					}
					System.out.println("����������:");
					input=bf.readLine();
				}
				else if(instruction[0].equals("put")) {//put����
					String[] xy=instruction[1].split(",");
					int x=Integer.valueOf(xy[0]);
					int y=Integer.valueOf(xy[1]);
					if(turn) {
						boolean flag= game.getP1().getAct().put(game.getP1().getColor(), x, y, game.getBoard(), 1);	
						if(!flag) {//���ӷǷ����
							System.out.println("ָ��ִ��ʧ�ܣ�����������:");
							input=bf.readLine();
						}
						else {
							turn=!turn;
							if(turn)
								System.out.println("���غ�ִ���֣�"+p1.getName());
							else
								System.out.println("���غ�ִ���֣�"+p2.getName());
							System.out.println("����������:");
							input=bf.readLine();
						}
					}
					else {
						boolean flag= game.getP2().getAct().put(game.getP1().getColor(), x, y, game.getBoard(), 1);	
						if(!flag) {//���ӷǷ����
							System.out.println("ָ��ִ��ʧ�ܣ�����������:");
							input=bf.readLine();
						}
						else {
							turn=!turn;
							if(turn)
								System.out.println("���غ�ִ���֣�"+p1.getName());
							else
								System.out.println("���غ�ִ���֣�"+p2.getName());
							System.out.println("����������:");
							input=bf.readLine();
						}
					}
				}
				else if(instruction[0].equals("pick")) {//pick����
					String[] xy=instruction[1].split(",");
					int x=Integer.valueOf(xy[0]);
					int y=Integer.valueOf(xy[1]);
					if(turn) {
						boolean flag= game.getP1().getAct().pickup(game.getP1().getColor(), x, y, game.getBoard());	
						if(!flag) {//���ӷǷ����
							System.out.println("ָ��ִ��ʧ�ܣ�����������:");
							input=bf.readLine();
						}
						else {
							turn=!turn;
							if(turn)
								System.out.println("���غ�ִ���֣�"+p1.getName());
							else
								System.out.println("���غ�ִ���֣�"+p2.getName());
							System.out.println("����������:");
							input=bf.readLine();
						}
					}
					else {
						boolean flag= game.getP2().getAct().pickup(game.getP1().getColor(), x, y, game.getBoard());	
						if(!flag) {//���ӷǷ����
							System.out.println("ָ��ִ��ʧ�ܣ�����������:");
							input=bf.readLine();
						}
						else {
							turn=!turn;
							if(turn)
								System.out.println("���غ�ִ���֣�"+p1.getName());
							else
								System.out.println("���غ�ִ���֣�"+p2.getName());
							System.out.println("����������:");
							input=bf.readLine();
						}
					}
				}
				else {//����֮��������Ϊ�Ƿ�����
					System.out.println("��������Ϸ��������������������:");
					input=bf.readLine();
				}
			}
		}
		else {
			System.out.println("�淨��ʾ");
			System.out.println("�ƶ�����:move x1,y1 x2,y2 /*x1,y1.x2,y2Ϊ[0,7]��������,x1,y1Ϊ��ʼ�����꣬x2,y2ΪĿ�ĵ�����*/");
			System.out.println("����:eat x,y /*x,yΪ[0,7]��������*/");
			System.out.println("��ѯ:query x,y /*x,yΪ[0,7]��������*/");
			System.out.println("����:skip");
			System.out.println("��ʾ����:print");
			System.out.println("������Ϸ:end");
			System.out.println("���غ�ִ���֣�"+p1.getName());
			System.out.println("����������:");
			input=bf.readLine();
			boolean turn=true;
			while(!input.equals("end")) {//ֻ������end�Ž�����Ϸ
				String[] instruction=input.split(" ");
				if(instruction[0].equals("skip")) {//skip����
					turn=!turn;//����ִ�з�
					if(turn)
						System.out.println("���غ�ִ���֣�"+p1.getName());
					else
						System.out.println("���غ�ִ���֣�"+p2.getName());
					System.out.println("����������:");
					input=bf.readLine();
				}
				else if(instruction[0].equals("print")) {//print����
					game.getBoard().printboard();
					System.out.println("����������:");
					input=bf.readLine();
				}
				else if(instruction[0].equals("query")) {//query����
					String[] xy=instruction[1].split(",");
					int x=Integer.valueOf(xy[0]);
					int y=Integer.valueOf(xy[1]);
					Position queryPosition=new Position(x, y);
					Piece queryPiece=new Piece(turn, queryPosition, 1);
					if(game.getBoard().haspiece(queryPiece)) {
						System.out.println("��λ��������һö����");
					}
					else {
						System.out.println("��λ��û����������");
					}
					System.out.println("����������:");
					input=bf.readLine();
				}
				else if(instruction[0].equals("move")) {//put����
					String[] xy1=instruction[1].split(",");
					int x1=Integer.valueOf(xy1[0]);
					int y1=Integer.valueOf(xy1[1]);
					String[] xy2=instruction[2].split(",");
					int x2=Integer.valueOf(xy2[0]);
					int y2=Integer.valueOf(xy2[1]);
					if(turn) {
						boolean flag= game.getP1().getAct().move(turn, x1, y1, x2, y2, game.getBoard());	
						if(!flag) {//���ӷǷ����
							System.out.println("ָ��ִ��ʧ�ܣ�����������:");
							input=bf.readLine();
						}
						else {
							turn=!turn;
							if(turn)
								System.out.println("���غ�ִ���֣�"+p1.getName());
							else
								System.out.println("���غ�ִ���֣�"+p2.getName());
							System.out.println("����������:");
							input=bf.readLine();
						}
					}
					else {
						boolean flag= game.getP2().getAct().move(turn, x1, y1, x2, y2, game.getBoard());	
						if(!flag) {//���ӷǷ����
							System.out.println("ָ��ִ��ʧ�ܣ�����������:");
							input=bf.readLine();
						}
						else {//ִ�гɹ�
							turn=!turn;
							if(turn)
								System.out.println("���غ�ִ���֣�"+p1.getName());
							else
								System.out.println("���غ�ִ���֣�"+p2.getName());
							System.out.println("����������:");
							input=bf.readLine();
						}
					}
				}
				else if(instruction[0].equals("eat")) {//pick����
					String[] xy1=instruction[1].split(",");
					int x1=Integer.valueOf(xy1[0]);
					int y1=Integer.valueOf(xy1[1]);
					String[] xy2=instruction[2].split(",");
					int x2=Integer.valueOf(xy2[0]);
					int y2=Integer.valueOf(xy2[1]);
					if(turn) {
						boolean flag= game.getP1().getAct().eatchess(turn, x1, y1, x2, y2, game.getBoard());	
						if(!flag) {//���ӷǷ����
							System.out.println("ָ��ִ��ʧ�ܣ�����������:");
							input=bf.readLine();
						}
						else {
							turn=!turn;
							if(turn)
								System.out.println("���غ�ִ���֣�"+p1.getName());
							else
								System.out.println("���غ�ִ���֣�"+p2.getName());
							System.out.println("����������:");
							input=bf.readLine();
						}
					}
					else {
						boolean flag= game.getP2().getAct().eatchess(turn, x1, y1, x2, y2, game.getBoard());	
						if(!flag) {//���ӷǷ����
							System.out.println("ָ��ִ��ʧ�ܣ�����������:");
							input=bf.readLine();
						}
						else {
							turn=!turn;
							if(turn)
								System.out.println("���غ�ִ���֣�"+p1.getName());
							else
								System.out.println("���غ�ִ���֣�"+p2.getName());
							System.out.println("����������:");
							try {
								input=bf.readLine();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				else {//����֮��������Ϊ�Ƿ�����
					System.out.println("��������Ϸ��������������������:");
					input=bf.readLine();
				}
		}
	}
}
}
