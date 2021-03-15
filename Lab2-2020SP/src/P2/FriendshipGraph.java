package P2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import P1.graph.ConcreteEdgesGraph;

public class FriendshipGraph extends ConcreteEdgesGraph<Person>{
	
	//Abstraction function
	// represents sociable relationship with graph,vertices represent people and edges represent relationship
	//Representation invariant:
	// vertices in the graph should be different,weight of edge should be positive
	//Safety from rep exposure:
	// the class extends the class ConcreteEdgesGraph and it extends its safety measure
	// additional functions either return unmodifiable values or is just void itself
	// the two functions addVertex and addEdge are based on functions from the class ConcreteEdgesGraph
	// which have taken safety measures,and getDistance function don't change or return any intern field about RI
	
	
	/**
	 * add one new person to the friendshipgraph
	 * @param p person needed to add
	 */
	public void addVertex(Person p) {
		if(this.vertices().contains(p)) {//重名处理
			System.out.println("The name is already created!");
		}
		else {
			this.add(p);
		}
	}
	
	/**
	 * create an edge between two given people who are in the graph
	 * @param p1 starting point of the edge
	 * @param p2 ending point of the person
	 */
	public void addEdge(Person p1,Person p2) {
		if(this.targets(p1).containsKey(p2)) {//重复边处理
			System.out.println("There is already an edge!");
		}
		else {
			this.set(p1, p2, 1);
		}	
	}
	
	/**
	 * compute the distance between two given people
	 * @param p1 starting point
	 * @param p2 ending point
	 * @return -1 if there is no reachable path,0 if the two points are same
	 * 		   otherwise return minimum of distance
	 */
	public int getDistance(Person p1,Person p2) {
		if(p1.equals(p2)) {//按要求，同一人距离为0
			return 0;
		}
		for(Person p:this.vertices()) {
			p.initvisit();
		}
		
		int distance=0;//距离初始为0
		List<Person> queue=new ArrayList<Person>();//队列初始化
		queue.add(p1);
		Person elePerson;
		
		List<Person> nowList = new ArrayList<Person>();// 建立两个新列表，用来确定何时distance要+1.nowList里的所有元素代表距离相同的一级，它们的所有朋友都在friendList中
		List<Person> friendList = new ArrayList<Person>();// friendList存储nowList中的所有朋友，当nowList为空后，将现有friendList复制给nowList成为新的一级，然后friendList置空
															// 当nowList为空时证明距离相同的一级已经遍历完成，因此距离+1
		nowList.add(p1);
		
		while (!queue.isEmpty()) {// 队列不空则循环
			if (nowList.isEmpty()) {//距离到了新的一级，将friendList中存储的元素复制给nowList成为新一级，friendList置空
				nowList.clear();
				nowList.addAll(friendList);
				friendList.clear();
			}
			elePerson = queue.get(0);// 出队
			elePerson.setvisit();
			queue.remove(0);
			nowList.remove(0);
			
			if(this.targets(elePerson).containsKey(p2)) {//找到了目标顶点
				distance++;
				return distance;
			}
			else {//遍历邻接顶点，将未访问过的加入队列
				for(Map.Entry<Person, Integer> entry : this.targets(elePerson).entrySet()) {
					if(!entry.getKey().getvisit()) {
						entry.getKey().setvisit();
						queue.add(entry.getKey());	
						friendList.add(entry.getKey());
					}
				}
			}
			
			if (nowList.isEmpty())//距离相同的一级遍历完成，distance++
				distance++;
		}
		
		return -1;
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
