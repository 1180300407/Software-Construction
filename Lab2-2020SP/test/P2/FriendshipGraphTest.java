package P2;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class FriendshipGraphTest {

	//addVertex
	/**
	 * Test strategy
	 * According to The number of vertex:0,1,n(n>1)
	 */
	@Test
	public void addVertexTest() {
		FriendshipGraph graph=new FriendshipGraph();
		Person p1=new Person("Ross"); 
		Person p2=new Person("Tom");
		Person p3=new Person("Jack");
		Set<Person> Person = new HashSet<Person>();
		
		assertEquals(Person,graph.vertices());
		
		graph.addVertex(p1);
		Person.add(p1);
		
		assertEquals(Person,graph.vertices());
		
		graph.addVertex(p2);
		Person.add(p2);
		
		graph.addVertex(p3);
		Person.add(p3);
		
		assertEquals(Person,graph.vertices());
	}
	
	//addEdge
	/**
	 * Test strategy
	 * According to The number of edge:0,2,n(n>2)
	 */
	@Test
	public void addEdgeTest(){
		FriendshipGraph graph=new FriendshipGraph();
		Person p1=new Person("Ross");
		Person p2=new Person("Tom");
		Person p3=new Person("Jack");
		graph.addVertex(p1);
		graph.addVertex(p2);
		graph.addVertex(p3);
		Map<Person, Integer> Edge=new HashMap<Person, Integer>();
		assertEquals(Edge, graph.targets(p1));
		assertEquals(Edge, graph.targets(p2));
		assertEquals(Edge, graph.targets(p3));
		
		graph.addEdge(p1, p2);
		graph.addEdge(p2, p1);
		
		Edge.put(p2, 1);
		assertEquals(Edge,graph.targets(p1));
		Edge.clear();
		Edge.put(p1,1);
		assertEquals(Edge, graph.targets(p2));
		
		graph.addEdge(p2, p3);
		graph.addEdge(p3, p2);
		Edge.put(p3, 1);
		assertEquals(Edge,graph.targets(p2));
		Edge.clear();
		Edge.put(p2, 1);
		assertEquals(Edge,graph.targets(p3));
	}
	
	//getDistance
	/**Test strategy
	 * 1.According to The Connectivity of Graphs: non-side graph,normal unconnected graph,connected graph;
	 * 2.According to The number of vertex:1,2,n(n>2)
	 * 3.According to The result of distance:-1,0,n(n>0)
	 */
		
	//The Connectivity of Graphs(including the vertex number:n=8)
	@Test
	public void connecttest() {
		FriendshipGraph graph=new FriendshipGraph();
		Person p1=new Person("a");
		Person p2=new Person("b");
		Person p3=new Person("c");
		Person p4=new Person("d");
		Person p5=new Person("e");
		Person p6=new Person("f");
		Person p7=new Person("g");
		Person p8=new Person("h");
		graph.addVertex(p1);
		graph.addVertex(p2);
		graph.addVertex(p3);
		graph.addVertex(p4);
		graph.addVertex(p5);
		graph.addVertex(p6);
		graph.addVertex(p7);
		graph.addVertex(p8);
		
		//non-side graph
		assertEquals(-1, graph.getDistance(p3, p6));
		
		graph.addEdge(p1, p3);
		graph.addEdge(p3, p1);
		graph.addEdge(p3, p5);
		graph.addEdge(p5, p3);
		graph.addEdge(p7, p5);
		graph.addEdge(p5, p7);
		
		//normal unconnected graph
		assertEquals(-1, graph.getDistance(p3, p6));
		assertEquals(1, graph.getDistance(p3, p5));
		assertEquals(2, graph.getDistance(p3, p7));
			
		graph.addEdge(p1, p2);
		graph.addEdge(p2, p1);
		graph.addEdge(p2, p4);
		graph.addEdge(p4, p2);
		graph.addEdge(p4, p6);
		graph.addEdge(p6, p4);
		graph.addEdge(p6, p8);
		graph.addEdge(p8, p6);
		
		//connected graph
		assertEquals(2, graph.getDistance(p2, p6));
		assertEquals(5, graph.getDistance(p3, p8));
	}
	
	//Test the vertex n=1 and n=2 case 
	@Test
	public void Graphvertextest() {
		FriendshipGraph graph=new FriendshipGraph();
		Person a=new Person("a");
		Person b=new Person("b");
		
		graph.addVertex(a);
		assertEquals(0, graph.getDistance(a, a));
		
		graph.addVertex(b);
		assertEquals(-1, graph.getDistance(a, b));
		
		graph.addEdge(a, b);
		assertEquals(1, graph.getDistance(a, b));
	}
}
