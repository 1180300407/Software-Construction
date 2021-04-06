package P3;


import java.util.ArrayList;
import java.util.List;

public class FriendshipGraph {
	int[][] Edge = new int[10000][10000];
	List<Person> PersonList = new ArrayList<>();

	public void addVertex(Person name) {// ��ͼ����Ӷ���
		for (Person p : PersonList) {
			if (name.getname().equals(p.getname())) {// �������ڹ�ϵͼ�У������ظ����
				System.out.println("The name is already in the list!");
				System.exit(0); 
			}
		}
		PersonList.add(name);
		name.initvisit();// ÿһ������ӽ�ͼ��ʱ����visit���Գ�ʼ��������֮��������
	}

	public void addEdge(Person name1, Person name2) {// ��������֮��ӱ�
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

	public int getDistance(Person name1, Person name2) {// �����������ʵ������̾���
		if ((!PersonList.contains(name1)) || (!PersonList.contains(name2))) {
			System.out.println("Please input the name that is in the graph!");
			return -1;
		}
		if (name1.equals(name2)) {// ͬһ���ˣ���Ҫ�󣬾���Ϊ0
			return 0;
		}
		int distance = 0;// �����ʼ��Ϊ0
		for (Person p : PersonList) {
			p.initvisit();
		}
		List<Person> PersonQueue = new ArrayList<Person>();// ����һ������ʵ�ֹ����������
		PersonQueue.add(name1);// ��ʼ��������
		Person elePerson;

		List<Person> nowList = new ArrayList<Person>();// �����������б�����ȷ����ʱdistanceҪ+1.nowList�������Ԫ�ش��������ͬ��һ�������ǵ��������Ѷ���friendList��
		List<Person> friendList = new ArrayList<Person>();// friendList�洢nowList�е��������ѣ���nowListΪ�պ󣬽�����friendList���Ƹ�nowList��Ϊ�µ�һ����Ȼ��friendList�ÿ�
															// ��nowListΪ��ʱ֤��������ͬ��һ���Ѿ�������ɣ���˾���+1
		nowList.add(name1);
		int size = PersonList.size();
		while (PersonQueue.size() != 0) {// ���в�����ѭ��
			if (nowList.isEmpty()) {//���뵽���µ�һ������friendList�д洢��Ԫ�ظ��Ƹ�nowList��Ϊ��һ����friendList�ÿ�
				nowList.clear();
				nowList.addAll(friendList);
				friendList.clear();
			}
			elePerson = PersonQueue.get(0);// ����
			elePerson.setvisit();
			PersonQueue.remove(0);
			nowList.remove(0);
			for (int i = 0; i < size; i++) {// �����߱�
				Person tmpperson = PersonList.get(i);
				boolean visit = tmpperson.getvisit();
				int edge = Edge[PersonList.indexOf(elePerson)][i];
				if (tmpperson.equals(name2) && (edge == 1)) {// �ҵ�Ŀ�������ʱ����Ϊ��̾���
					distance++;
					return distance;
				} else if ((edge == 1) && (!visit)) {// ������֮����ϵ�����Ѽ��������
					PersonList.get(i).setvisit();
					PersonQueue.add(PersonList.get(i));
					friendList.add(PersonList.get(i));
				}
			}
			if (nowList.isEmpty())//������ͬ��һ��������ɣ�distance++
				distance++;
		}
		return -1;// ������û����ϵ������-1;
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
