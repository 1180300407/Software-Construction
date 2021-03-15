/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph<L> implements Graph<L> {
    
    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();
    
    // Abstraction function:
    //   represents the graph with vertices and positive directed edges
    // Representation invariant:
    //   vertices contains different graph vertices
    //	 edges has instinct direction and positive weight
    //	 edges中的每条边的顶点都在vertices中
    // Safety from rep exposure:
    //   All fields are private and final
    //	 change the return value to unmodifiable type when needed to return changeable fields
    
    // TODO constructor
    
    // TODO checkRep
    private void checkRep() {
    	for(Edge<L> e:edges) {
    		assert e.getweight()>0;
    	}
    }
    
    @Override public boolean add(L vertex) {
    	return vertices.add(vertex);
    }
    
    @Override public int set(L source, L target, int weight) {
    	vertices.add(source);//集合自身保证不重复性
    	vertices.add(target);
    	int oldweight=0;//返回的旧边值
    	Edge<L> edge=new Edge<L>(source, target, weight);//要加进去的边
    	
    	for(Edge<L> e:edges) {
    		if(e.getsource().equals(source)&&e.gettarget().equals(target)) {
    			oldweight=e.getweight();
    			edges.remove(e);//至多只能找到一个点并删除，因此此时在循环内删除一个点不会发生循环紊乱
    			break;//删除一个点后直接退出循环
    		}
    	}
    	
    	if(weight!=0)//添加新权重
    		edges.add(edge);
    	checkRep();
    	return oldweight;
    }
    
    @Override public boolean remove(L vertex) {
    	if(!vertices.remove(vertex)) {
    		return false;//图中不存在该点
    	}
    	
    	for(int i=0;i<edges.size();i++) {
    		Edge<L> e=edges.get(i);
    		if (e.getsource().equals(vertex)||e.gettarget().equals(vertex)) {
				edges.remove(i);//将该点关联边全部删除
				i--;//此处i要--，否则会有元素无法遍历到
			}
    	}
    	checkRep();
    	return true;
    }
    
    @Override public Set<L> vertices() {
    	return Collections.unmodifiableSet(vertices);
    }
    
    @Override public Map<L, Integer> sources(L target) {
    	Map<L, Integer> sourceMap=new HashMap<L, Integer>();
    	
    	for(Edge<L> e:edges) {//遍历边表
    		if(e.gettarget().equals(target)) {
    			sourceMap.put(e.getsource(), e.getweight());
    		}
    	}
    	
    	return sourceMap;
    }
    
    @Override public Map<L, Integer> targets(L source) {
    	Map<L, Integer> targetMap=new HashMap<L, Integer>();
    	
    	for(Edge<L> e:edges) {//遍历边表
    		if(e.getsource().equals(source)) {
    			targetMap.put(e.gettarget(), e.getweight());
    		}
    	}
    	
    	return targetMap;
    }
    
    
    // TODO toString()
    @Override public String toString() {
    	if(edges.isEmpty()) {
    		return "Graph is empty";
    	}
    	else {
    		String information="";
    		for(Edge<L> e:edges) {
    			information=information.concat(e.toString());
    			information=information.concat("\n");
    		}
    		return information;
    	}
    }
}

/**
 * TODO specification
 * Immutable.
 * This class is internal to the rep of ConcreteEdgesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 * 
 * Describe the graph with edge class,used to assist in the function such as add,set
 * 
 */
class Edge<L> {
    
    // TODO fields
	
    private final int weight; //the weight of the edge,positive int
    private final L sourcevertex;//the source of the direct edge;
	private final L targetvertex;//the target of the direct edge;
	
    // Abstraction function:
    //   represents an edge of graph
    // Representation invariant:
    //   weight>0
	//	 sourcevertex和targetvertex非Null
    // Safety from rep exposure:
    //   All fields are private and final
    
    // TODO constructor
	/**
	 * construct the Edge with parameters
	 * @param v1:starting point of the edge 
	 * @param v2:ending point of the edge
	 * @param weight:the weight of the edge
	 */
    public Edge(L v1,L v2,int weight) {
    	this.sourcevertex=v1;
    	this.targetvertex=v2;
    	this.weight=weight;
    }
	
    // TODO checkRep
    private void checkRep() {
    	assert weight>0;
    }
    // TODO methods
    
    /**
     * get the starting point of edge
     * @return the starting point of edge
     */
    public L getsource() {
    	L copysource=this.sourcevertex;
    	return copysource;
    }
    
    /**
     * get the ending point of edge
     * @return the ending point of edge
     */
    public L gettarget() {
    	L copysource=this.targetvertex;
    	return copysource;
    }
    
    /**
     * get the weight of edge
     * @return the weight of edge
     */
    public int getweight() {
    	checkRep();
    	return this.weight;
    }
    
    // TODO toString()
    @Override public String toString() {
    	String information="";
    	information=information.concat(this.getsource()+"->"+this.gettarget()+":"+this.getweight());
    	return information;
    }
}
