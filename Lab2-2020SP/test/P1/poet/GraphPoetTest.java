/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.poet;

import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import P1.graph.Graph;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {
    
    // Testing strategy
    //   GraphPoet(File corpus)--Creator(file corpus)
	//		1.Both uppercase and lowercase are in the file;  Just one of them in the file
	//		2.included repeated words ;  words are not repeated
	//	 poem()
	//		1.input String with words that can insert the bridge ; no bridge can  be inserted
	//		2.no word pair has multiple bridge words ; multiple bridge words
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // TODO tests
    
    
    @Test
    public void getgraphTest()throws IOException {
    	Set<String> test=new HashSet<String>();
    	File file=new File("test/P1/poet/empty.txt");
    	GraphPoet graphPoet=new GraphPoet(file);
    	assertEquals(test, graphPoet.getgraph().vertices());
    }
    
    @Test
    public void CreatorTest() throws IOException{
    	File file=new File("test/P1/poet/test1.txt");
    	GraphPoet graphPoet=new GraphPoet(file);
    	BufferedReader reader=null;
    	reader=new BufferedReader(new FileReader(file));
    	String templine=reader.readLine();  
    	templine=templine.toLowerCase();
    	String[] firstline=templine.split(" ");
    	reader.close();
    	int len=firstline.length;
    	Graph<String> testGraph=Graph.empty();
    	for(int i=0;i<len;i++) {
    		testGraph.add(firstline[i]);
    	}
    	for(int i=0;i<len-1;i++) {
    		testGraph.set(firstline[i], firstline[i+1], 1);
    	}
    	
    	//测试顶点集合是否相同
    	assertEquals(testGraph.vertices(), graphPoet.getgraph().vertices());
    	
    	//逐一验证边集，此处逐一验证是为了避免重写equals方法带来不必要的风险
    	for(String v:testGraph.vertices()) {
    		assertEquals(testGraph.targets(v), graphPoet.getgraph().targets(v));
    	}
    	
    	File file2=new File("test/P1/poet/test2.txt");
    	GraphPoet graphPoet2=new GraphPoet(file2);
    	Graph<String> testGraph2=Graph.empty();
    	testGraph2.set("to", "explore", 1);
    	testGraph2.set("to", "seek", 1);
    	testGraph2.set("explore", "strange", 1);
    	testGraph2.set("seek", "out", 1);
    	testGraph2.set("strange", "new", 1);
    	testGraph2.set("out", "new", 1);
    	testGraph2.set("new", "life", 1);
    	testGraph2.set("new", "civilizations", 1);
    	testGraph2.set("new", "worlds", 1);
    	testGraph2.set("worlds", "to", 1);
    	testGraph2.set("life", "and", 1);
    	testGraph2.set("and", "new", 1);
    	
    	//测试顶点集合是否相同
    	assertEquals(testGraph2.vertices(), graphPoet2.getgraph().vertices());
    	
    	//验证边集，此处之所以这样逐一验证是为了避免重写equals方法带来不必要的风险
    	assertEquals(testGraph2.targets("to"), graphPoet2.getgraph().targets("to"));
    	assertEquals(testGraph2.targets("explore"), graphPoet2.getgraph().targets("explore"));
    	assertEquals(testGraph2.targets("seek"), graphPoet2.getgraph().targets("seek"));
    	assertEquals(testGraph2.targets("strange"), graphPoet2.getgraph().targets("strange"));
    	assertEquals(testGraph2.targets("out"), graphPoet2.getgraph().targets("out"));
    	assertEquals(testGraph2.targets("new"), graphPoet2.getgraph().targets("new"));
    	assertEquals(testGraph2.targets("life"), graphPoet2.getgraph().targets("life"));
    	assertEquals(testGraph2.targets("and"), graphPoet2.getgraph().targets("and"));
    	assertEquals(testGraph2.targets("civilizations"), graphPoet2.getgraph().targets("civilizations"));
    	assertEquals(testGraph2.targets("worlds"), graphPoet2.getgraph().targets("worlds"));
    }
    
    @Test
    public void toStringtest() throws IOException {
    	File file =new File("test/P1/poet/empty.txt");
    	GraphPoet graphPoet1=new GraphPoet(file);
    	assertEquals("Graph is empty", graphPoet1.toString());
    	File file2=new File("test/P1/poet/teststring.txt");
    	GraphPoet graphPoet2=new GraphPoet(file2);
    	assertEquals("summer->winter:1\n", graphPoet2.toString());
    }
    
    @Test
    public void poemTest() throws IOException{
    	File file=new File("test/P1/poet/test1.txt");
    	GraphPoet graphPoet=new GraphPoet(file);
    	String input="friends talk to each other";
    	assertEquals("friends talk to see each other", graphPoet.poem(input));//insert one bridge word
    	
    	String input2="friends talk to see each other";
    	assertEquals(input2, graphPoet.poem(input2));//no bridge to insert
    	
    	File file2=new File("test/P1/poet/test2.txt");
    	GraphPoet graphPoet2=new GraphPoet(file2);
    	String input3="Look out and explore worlds";
    	assertEquals(input3, graphPoet2.poem(input3));//all indeterminacy with more than one bridge words
    }
    
    
    //Test hasbridge:no bridge word ; one deterministic bridge word ; more than one bridge words
    @Test
    public void hasbridgeTest() throws IOException{
    	File file=new File("test/P1/poet/test3.txt");
    	GraphPoet graphPoet=new GraphPoet(file);
    	
    	assertEquals("new", graphPoet.hasbridge("new", "worlds"));
    	assertEquals("new", graphPoet.hasbridge("new", "worlds"));
    	assertNotEquals("new", graphPoet.hasbridge("new", "and"));
    }
}
