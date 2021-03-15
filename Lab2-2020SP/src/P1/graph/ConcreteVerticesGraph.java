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
    	int index=-1;//���source���б���λ��
    	for(Vertex<L> v:vertices) {//�����б��ж���
    		if(v.getlabel().equals(source)) {//�ҵ�Դ����
    			index=vertices.indexOf(v);
    			if(v.getedge().containsKey(target)) {//ͼ��ԭ�����ڶ�Ӧ��
    				oldweight=v.getedge().get(target);//���ؾ�Ȩ��
    				v.removeedge(target, oldweight);
    			}
    		}
    	}
    	
    	if(index==-1) {//������source���㣬�����б���
    		Vertex<L> v=new Vertex<L>(source);
    		vertices.add(v);
    		index=vertices.size()-1;//index��Ǽ����sourceλ��
    	}
    	
    	if(weight!=0) {//����ɾ������
    		vertices.get(index).addedge(target, weight);//����ԭ���Ƿ���ھɱ߶��Ѿ�ɾ���ˣ�ֻ������±�
    	}
    	
    	add(target);//��target�������
    	checkRep();
    	return oldweight;
    }
    
    @Override public boolean remove(L vertex) {
        //throw new RuntimeException("not implemented");
    	boolean flag=false;
    	for(Vertex<L> v:vertices) {
    		if(v.getlabel().equals(vertex)) {//��vertexΪ���Ķ������ɾ��
    			vertices.remove(v);
    			flag=true;
    			break;
    		}
    	}
    	
    	for(Vertex<L> v:vertices) {
    		if(v.getedge().containsKey(vertex)) {//����vertexΪ�յ�ı�ɾ��
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
    	for(Vertex<L> v:vertices) {//target���յ㣬��Ҫ�ڶ���ı߼���Ѱ��
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
    		if(v.getlabel().equals(source)) {//label�Ǹ������ı�ǩ��ֻ����label��Ѱ��
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
	private L label;//����ı�ǩ
	private Map<L, Integer> edges=new HashMap<L, Integer>();//�洢�����Ȩ�ߣ�ÿһ��Ԫ��Ϊtarget-weight��ֵ��
    
    // Abstraction function:
    //   represents one vertex and its adjacent out edges of graph
    // Representation invariant:
    //   only one edge between two vertices
	//	 Label��null
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
