/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph<String>();
    }
    
    /*
     * Testing ConcreteVerticesGraph...
     */
    
    // Testing strategy for ConcreteVerticesGraph.toString()
    //   empty graph;normal graph with edges
    
    // TODO tests for ConcreteVerticesGraph.toString()
    @Test
    public void toStringTest() {
    	ConcreteVerticesGraph<String> graph=new ConcreteVerticesGraph<String>();
    	assertEquals("Graph is empty", graph.toString());
    	graph.set("v1", "v4", 10);
    	assertEquals("v1->v4:10\n", graph.toString());
    }
    
    /*
     * Testing Vertex...
     */
    
    // Testing strategy for Vertex
    //   call the "getter" and ""setter" function and the "addedge" function
    //	 test toString:no edges; with edges
    
    
    // TODO tests for operations of Vertex
    
    @Test
    public void getlabelTest() {
    	Vertex<String> v=new Vertex<String>("v");
    	assertEquals("v", v.getlabel());
    }
    
    @Test
    public void setlabelTest() {
    	Vertex<String> v=new Vertex<String>("v0");
    	v.setlabel("v");
    	assertEquals("v", v.getlabel());
    }
    
    @Test
    public void getedgeTest() {
    	Vertex<String> v=new Vertex<String>("v");
    	Map<String, Integer> edges=new HashMap<String, Integer>();
    	assertEquals(edges, v.getedge());
    	edges.put("v1", 1);
    	v.addedge("v1", 1);
    	assertEquals(edges, v.getedge());
    }
    
    @Test
    public void VertextoStringtest() {
    	Vertex<String> v=new Vertex<String>("v1");
    	assertEquals("", v.toString());
    	v.addedge("v2", 7);
    	assertEquals("v1->v2:7\n", v.toString());
    }
    
    @Test
    public void addedgetest() {
    	Vertex<String> v=new Vertex<String>("v");
    	Map<String, Integer> edges=new HashMap<String, Integer>();
    	edges.put("v1", 5);
    	v.addedge("v1", 5);
    	assertEquals(edges, v.getedge());
    	v.addedge("v2", 4);
    	assertNotEquals(edges, v.getedge());
    	edges.put("v2", 4);
    	assertEquals(edges, v.getedge());
    }
    
    @Test
    public void removeedgetest() {
    	Vertex<String> v=new Vertex<String>("v");
    	Map<String, Integer> edges=new HashMap<String, Integer>();
    	edges.put("v1", 5);
    	v.addedge("v1", 5);
    	v.removeedge("v2", 1);
    	assertEquals(edges, v.getedge());
    	v.addedge("v5", 7);
    	v.removeedge("v5", 7);
    	assertEquals(edges, v.getedge());
    }
}
