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
		if(this.vertices().contains(p)) {//��������
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
		if(this.targets(p1).containsKey(p2)) {//�ظ��ߴ���
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
		if(p1.equals(p2)) {//��Ҫ��ͬһ�˾���Ϊ0
			return 0;
		}
		for(Person p:this.vertices()) {
			p.initvisit();
		}
		
		int distance=0;//�����ʼΪ0
		List<Person> queue=new ArrayList<Person>();//���г�ʼ��
		queue.add(p1);
		Person elePerson;
		
		List<Person> nowList = new ArrayList<Person>();// �����������б�����ȷ����ʱdistanceҪ+1.nowList�������Ԫ�ش��������ͬ��һ�������ǵ��������Ѷ���friendList��
		List<Person> friendList = new ArrayList<Person>();// friendList�洢nowList�е��������ѣ���nowListΪ�պ󣬽�����friendList���Ƹ�nowList��Ϊ�µ�һ����Ȼ��friendList�ÿ�
															// ��nowListΪ��ʱ֤��������ͬ��һ���Ѿ�������ɣ���˾���+1
		nowList.add(p1);
		
		while (!queue.isEmpty()) {// ���в�����ѭ��
			if (nowList.isEmpty()) {//���뵽���µ�һ������friendList�д洢��Ԫ�ظ��Ƹ�nowList��Ϊ��һ����friendList�ÿ�
				nowList.clear();
				nowList.addAll(friendList);
				friendList.clear();
			}
			elePerson = queue.get(0);// ����
			elePerson.setvisit();
			queue.remove(0);
			nowList.remove(0);
			
			if(this.targets(elePerson).containsKey(p2)) {//�ҵ���Ŀ�궥��
				distance++;
				return distance;
			}
			else {//�����ڽӶ��㣬��δ���ʹ��ļ������
				for(Map.Entry<Person, Integer> entry : this.targets(elePerson).entrySet()) {
					if(!entry.getKey().getvisit()) {
						entry.getKey().setvisit();
						queue.add(entry.getKey());	
						friendList.add(entry.getKey());
					}
				}
			}
			
			if (nowList.isEmpty())//������ͬ��һ��������ɣ�distance++
				distance++;
		}
		
		return -1;
	}
	
	public static void main(String[] args) {//ʵ��ָ���еĲ�������
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
