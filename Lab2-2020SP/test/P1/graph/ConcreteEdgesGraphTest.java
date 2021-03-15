/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteEdgesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph<String>();
    }
    
    /*
     * Testing ConcreteEdgesGraph...
     */
    
    // Testing strategy for ConcreteEdgesGraph.toString()
    //   empty graph;normal graph with edges
    
    // TODO tests for ConcreteEdgesGraph.toString()
    @Test
    public void toStringTest() {
    	ConcreteEdgesGraph<String> graph=new ConcreteEdgesGraph<String>();
    	assertEquals("Graph is empty", graph.toString());
    	graph.set("v1", "v2", 5);
    	assertEquals("v1->v2:5\n", graph.toString());
    }
    
    /*
     * Testing Edge...
     */
    
    // Testing strategy for Edge
    //   Test the "getter" function:initial an Edge object and call "getter" function
    //	 Test toString
    
    // TODO tests for operations of Edge
    @Test
    public void getsourceTest() {
    	Edge<String> e=new Edge<String>("v1", "v2", 3);
    	assertEquals("v1", e.getsource());
    }
    
    @Test
    public void gettargetTest() {
    	Edge<String> e=new Edge<String>("v1", "v5", 6);
    	assertEquals("v5", e.gettarget());
    }
    
    @Test
    public void getweightTest() {
    	Edge<String> e=new Edge<String>("v0", "v2", 10);
    	assertEquals(10, e.getweight());
    }
    
    @Test
    public void EdgetoStringtest() {
    	Edge<String> e=new Edge<String>("v1", "v2", 1);
    	assertEquals("v1->v2:1", e.toString());
    }
}
