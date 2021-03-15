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
public class ConcreteVerticesGraph<L> implements Graph<L> {
    
    private final List<Vertex<L>> vertices = new ArrayList<>();
    
    // Abstraction function:
    //   represents graph:different vertices and their adjacent out edges
    // Representation invariant:
    //   vertices different from each other
    // Safety from rep exposure:
    //   field is private and final and change the return value to unmodifiable type when needed to return changeable fields
    
    // TODO constructor
    
    // TODO checkRep
    public void checkRep() {
    	Set<Vertex<L>> checkset=new HashSet<Vertex<L>>(vertices);
		assert checkset.size()==vertices.size();
    }
    
    @Override public boolean add(L vertex) {
        //throw new RuntimeException("not implemented");
    	for(Vertex<L> v:vertices) {
    		if(v.getlabel().equals(vertex))
    			return false;
    	}
    	
    	Vertex<L> ver=new Vertex<L>(vertex);
    	vertices.add(ver);
    	checkRep();
    	return true;
    }
    
    @Override public int set(L source, L target, int weight) {
        //throw new RuntimeException("not implemented");
    	int oldweight=0;
    	int index=-1;//标记source在列表中位置
    	for(Vertex<L> v:vertices) {//遍历列表中顶点
    		if(v.getlabel().equals(source)) {//找到源顶点
    			index=vertices.indexOf(v);
    			if(v.getedge().containsKey(target)) {//图中原来存在对应边
    				oldweight=v.getedge().get(target);//返回旧权重
    				v.removeedge(target, oldweight);
    			}
    		}
    	}
    	
    	if(index==-1) {//不存在source顶点，加入列表中
    		Vertex<L> v=new Vertex<L>(source);
    		vertices.add(v);
    		index=vertices.size()-1;//index标记加入后source位置
    	}
    	
    	if(weight!=0) {//不是删除操作
    		vertices.get(index).addedge(target, weight);//无论原来是否存在旧边都已经删除了，只需加入新边
    	}
    	
    	add(target);//将target顶点加入
    	checkRep();
    	return oldweight;
    }
    
    @Override public boolean remove(L vertex) {
        //throw new RuntimeException("not implemented");
    	boolean flag=false;
    	for(Vertex<L> v:vertices) {
    		if(v.getlabel().equals(vertex)) {//将vertex为起点的顶点与边删除
    			vertices.remove(v);
    			flag=true;
    			break;
    		}
    	}
    	
    	for(Vertex<L> v:vertices) {
    		if(v.getedge().containsKey(vertex)) {//将以vertex为终点的边删除
    			v.removeedge(vertex, v.getedge().get(vertex));
    			flag=true;
    		}
    	}
    	
    	return flag;
    }
    
    @Override public Set<L> vertices() {
        //throw new RuntimeException("not implemented");
    	Set<L> labels=new HashSet<L>();
    	for(Vertex<L> v:vertices) {
    		labels.add(v.getlabel());
    	}
    	
    	return Collections.unmodifiableSet(labels);
    }
    
    @Override public Map<L, Integer> sources(L target) {
        //throw new RuntimeException("not implemented");
    	Map<L, Integer> sourceMap=new HashMap<L, Integer>();
    	for(Vertex<L> v:vertices) {//target是终点，需要在顶点的边集中寻找
    		if(v.getedge().containsKey(target)) {
    			sourceMap.put(v.getlabel(), v.getedge().get(target));
    		}
    	}
    	
    	return Collections.unmodifiableMap(sourceMap);
    }
    
    @Override public Map<L, Integer> targets(L source) {
        //throw new RuntimeException("not implemented");
    	Map<L, Integer> targetMap=new HashMap<L, Integer>();
    	for(Vertex<L> v:vertices) {
    		if(v.getlabel().equals(source)) {//label是各个起点的标签，只需在label中寻找
    			targetMap=v.getedge();
    			break;
    		}
    	}
    	
    	return Collections.unmodifiableMap(targetMap);
    }
    
    // TODO toString()
    @Override public String toString() {
    	if(vertices.isEmpty()) {
    		return "Graph is empty";
    	}
    	else {
    		String information="";
    		for(Vertex<L> v:vertices) {
    			information=information.concat(v.toString());
    		}
    		return information;
    	}
    }
}



/**
 * TODO specification
 * Mutable.
 * This class is internal to the rep of ConcreteVerticesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 * Describe the graph with Vertex class,used to assist in the function such as add,set
 */
class Vertex<L> {
    
    // TODO fields
	private L label;//顶点的标签
	private Map<L, Integer> edges=new HashMap<L, Integer>();//存储有向加权边，每一个元素为target-weight键值对
    
    // Abstraction function:
    //   represents one vertex and its adjacent out edges of graph
    // Representation invariant:
    //   only one edge between two vertices
	//	 Label非null
    // Safety from rep exposure:
    //   All fields are private and change the return value to unmodifiable type when needed to return changeable fields
    
    // TODO constructor
	/**
	 * construct new vertex with parameter label
	 * @param label of the vertex
	 */
	public Vertex(L label) {
		this.label=label;
	}
	
    
    // TODO checkRep
	public void checkRep() {
		Set<L> tarSet=new HashSet<L>();
		for(Map.Entry<L, Integer> entry : edges.entrySet()) {
			tarSet.add(entry.getKey());
		}
		assert tarSet.size()==edges.size();
	}
    
    // TODO methods
	
	/**
	 * get the label of vertex
	 * @return the label of vertex
	 */
	public L getlabel() {
		return this.label;
	}
	
	/**
	 * get the edges of the vertex
	 * @return all the edges which make vertex as the starting point
	 */
	public Map<L, Integer> getedge(){
		return Collections.unmodifiableMap(this.edges);
	}
	
	/**
	 * set the label of vertex with the parameter
	 * @param label for the vertex
	 */
	public void setlabel(L label) {
		this.label=label;
	}
	
	/**
	 * add a new edge for the vertex
	 * @param label label of the new edge,and don't exist before
	 * @param value weight of the new edge
	 */
	public void addedge(L label,Integer value) {
		if(!edges.containsKey(label))
			this.edges.put(label, value);
		checkRep();
	}
	
	/**
	 * remove one edge that already exist in the graph
	 * @param target target label of the edge
	 * @param value weight of the target edge
	 */
	public void removeedge(L target,Integer value) {
		if(edges.containsKey(target)) {
			Integer weight=edges.get(target);
			if(weight.equals(value))
				edges.remove(target, value);
		}
		checkRep();
	}
	
    // TODO toString()
    @Override public String toString() {
    	String information="";
    	if(this.getedge().isEmpty()) {
    		return information;
    	}
    	for(Map.Entry<L, Integer> entry:this.getedge().entrySet()) {
    		information=information.concat(this.getlabel()+"->"+entry.getKey()+":"+entry.getValue()+"\n");
    	}
    	return information;
    }
}
