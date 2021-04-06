package P3;


import java.util.ArrayList;
import java.util.List;

public class FriendshipGraph {
	int[][] Edge = new int[10000][10000];
	List<Person> PersonList = new ArrayList<>();

	public void addVertex(Person name) {// 向图中添加顶点
		for (Person p : PersonList) {
			if (name.getname().equals(p.getname())) {// 该人已在关系图中，不能重复添加
				System.out.println("The name is already in the list!");
				System.exit(0); 
			}
		}
		PersonList.add(name);
		name.initvisit();// 每一个人添加进图中时都对visit属性初始化，便于之后计算距离
	}

	public void addEdge(Person name1, Person name2) {// 两个顶点之间加边
		if (name1.getname().equals(name2.getname())) {
			System.out.println("There shouldn't be two same names!");
			System.exit(0);
		} else if ((!PersonList.contains(name1)) || (!PersonList.contains(name2))) {
			System.out.println("There are not names like these!");
			System.exit(0);
		} else {
			int i1 = PersonList.indexOf(name1);
			int i2 = PersonList.indexOf(name2);
			Edge[i1][i2] = 1;
		}
	}

	public int getDistance(Person name1, Person name2) {// 广度优先搜索实现求最短距离
		if ((!PersonList.contains(name1)) || (!PersonList.contains(name2))) {
			System.out.println("Please input the name that is in the graph!");
			return -1;
		}
		if (name1.equals(name2)) {// 同一个人，按要求，距离为0
			return 0;
		}
		int distance = 0;// 距离初始化为0
		for (Person p : PersonList) {
			p.initvisit();
		}
		List<Person> PersonQueue = new ArrayList<Person>();// 创建一个队列实现广度优先搜索
		PersonQueue.add(name1);// 起始点加入队列
		Person elePerson;

		List<Person> nowList = new ArrayList<Person>();// 建立两个新列表，用来确定何时distance要+1.nowList里的所有元素代表距离相同的一级，它们的所有朋友都在friendList中
		List<Person> friendList = new ArrayList<Person>();// friendList存储nowList中的所有朋友，当nowList为空后，将现有friendList复制给nowList成为新的一级，然后friendList置空
															// 当nowList为空时证明距离相同的一级已经遍历完成，因此距离+1
		nowList.add(name1);
		int size = PersonList.size();
		while (PersonQueue.size() != 0) {// 队列不空则循环
			if (nowList.isEmpty()) {//距离到了新的一级，将friendList中存储的元素复制给nowList成为新一级，friendList置空
				nowList.clear();
				nowList.addAll(friendList);
				friendList.clear();
			}
			elePerson = PersonQueue.get(0);// 出队
			elePerson.setvisit();
			PersonQueue.remove(0);
			nowList.remove(0);
			for (int i = 0; i < size; i++) {// 遍历边表
				Person tmpperson = PersonList.get(i);
				boolean visit = tmpperson.getvisit();
				int edge = Edge[PersonList.indexOf(elePerson)][i];
				if (tmpperson.equals(name2) && (edge == 1)) {// 找到目标人物，此时距离为最短距离
					distance++;
					return distance;
				} else if ((edge == 1) && (!visit)) {// 否则将与之相联系的朋友加入队列中
					PersonList.get(i).setvisit();
					PersonQueue.add(PersonList.get(i));
					friendList.add(PersonList.get(i));
				}
			}
			if (nowList.isEmpty())//距离相同的一级遍历完成，distance++
				distance++;
		}
		return -1;// 两个人没有联系，返回-1;
	}

	public static void main(String[] args) {//实验指导中的测试用例
		FriendshipGraph graph = new FriendshipGraph();
		Person rachel = new Person("Rachel");
		Person ross = new Person("Ross");
		Person ben = new Person("Ben");
		Person kramer = new Person("Kramer");
		graph.addVertex(rachel);
		graph.addVertex(ross);
		graph.addVertex(ben);
		graph.addVertex(kramer);
		graph.addEdge(rachel, ross);
		graph.addEdge(ross, rachel);
		graph.addEdge(ross, ben);
		graph.addEdge(ben, ross);
		System.out.println(graph.getDistance(rachel, ross));
		// should print 1
		System.out.println(graph.getDistance(rachel, ben));
		// should print 2
		System.out.println(graph.getDistance(rachel, rachel));
		// should print 0
		System.out.println(graph.getDistance(rachel, kramer));
		// should print -1
	}
}
