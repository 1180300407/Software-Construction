/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {
    
    // Testing strategy
    //  Test add: add new vertex;add vertex that has existed 
	//	Test set: 1.both vertices are in the graph;one is in the graph and another is not;neither is in the graph
    //			  2.reset the edge is in the graph; edge is not in the graph;
	//			  3.weight=0; weight>0; weight<0;
	//	Test remove: 1.vertex is in the graph; vertex is not in the graph;
	//				 2.remove vertex with edges; remove vertex without edges;
	//	Test vertices: empty graph;graph with n vertices(n>0)
	//	Test sources: empty graph;graph with n vertices(n>0)
	//  Test targets: empty graph;graph with n vertices(n>0)
	
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testInitialVerticesEmpty() {
        // TODO you may use, change, or remove this test
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }
    
    //Test add
    @Test
    public void addTest() {
    	Graph<String> graph=emptyInstance();
    	assertTrue(graph.add("test"));
    	assertFalse(graph.add("test"));
    }
    
    //Test set
    @Test
    public void setTest() {
    	Graph<String> graph=emptyInstance();
    	assertEquals(0, graph.set("v1", "v2", 1)); //see if the edge is new or not
    	assertFalse(graph.add("v1"));//if set is correct then the new vertex is already in the graph or not
    	
    	assertEquals(1, graph.set("v1", "v2", 0)); //see the weight=0 situation
    	assertEquals(0, graph.set("v1", "v2", 4)); //both vertices,see if the remove action is correct or not 
    	
    	assertEquals(4, graph.set("v1", "v2", 2)); //see the reset action
    	
    	assertEquals(0, graph.set("v5", "v1", 1)); //one is in the graph,and another is not
    }
    
    //Test remove
    @Test
    public void removeTest() {
    	Graph<String> graph=emptyInstance();
    	assertFalse(graph.remove("lo")); //vertex is not in the graph
    	
    	graph.add("test");
    	assertTrue(graph.remove("test"));//vertex is in the graph without edges
    	
    	graph.set("v0", "v5", 3);
    	assertTrue(graph.remove("v0"));//vertex with edge
    	assertEquals(0, graph.set("v0", "v5", 2));
    }
    
    //Test Set
    @Test
    public void verticesTest() {
		Graph<String> graph=emptyInstance();
		Set<String> tarSet=new HashSet<String>();
		assertTrue(graph.vertices().isEmpty());//empty graph
		
		tarSet.add("test1");
		tarSet.add("test2");
		graph.add("test1");
		graph.add("test2");
		assertTrue(tarSet.equals(graph.vertices()));//n vertices
	}
    
    //Test source
    @Test
    public void sourcesTest() {
    	Graph<String> graph=emptyInstance();
    	Map<String, Integer> tarmap=new HashMap<String, Integer>();
    	assertTrue(graph.sources("0").isEmpty());//empty graph
    	
    	graph.set("v1", "v2", 2);
    	graph.set("v5", "v2", 5);
    	tarmap.put("v1", 2);
    	tarmap.put("v5", 5);
    	assertTrue(tarmap.equals(graph.sources("v2")));//n vertices
    }
    
    //Test targets
    @Test
    public void targetsTest() {
    	Graph<String> graph=emptyInstance();
    	Map<String, Integer> tarmap=new HashMap<String, Integer>();
    	assertTrue(graph.sources("0").isEmpty());//empty graph
    	
    	graph.set("v1", "v2", 2);
    	graph.set("v1", "v5", 5);
    	tarmap.put("v2", 2);
    	tarmap.put("v5", 5);
    	assertTrue(tarmap.equals(graph.targets("v1")));//n vertices
    }
}
