/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.poet;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileReader;

import P1.graph.Graph;

/**
 * A graph-based poetry generator.
 * 
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 * 
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 * 
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {
    
    private final Graph<String> graph = Graph.empty();
    
    // Abstraction function:
    //   reserve a word affinity graph.Vertices in the graph are non-empty,case-insensitive strings of non-space non-newline characters.
    //	 Edges in the graph count adjacencies which are positive(promised by Graph Interface)
    // Representation invariant:
    //   Vertices are non-empty,case-insensitive strings
    // Safety from rep exposure:
    //   Field is private and final,
    
    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
    	BufferedReader reader=null;
    	reader=new BufferedReader(new FileReader(corpus));
    	String templine=reader.readLine();  
    	if(templine!=null) {
    		String lowerfirst=templine.toLowerCase();//大写转化为小写
        	String[] firstline=lowerfirst.split(" ");//先对第一行进行处理是为了在循环中处理每一行最后一个字符串的邻接关系
    		
    		graph.add(firstline[0]);
    		
    		for(int i=1;i<firstline.length;i++) {//无论顶点是否已经存在，顶点标签已经确定，直接set即可
    			if(graph.targets(firstline[i-1]).containsKey(firstline[i])) {//邻接词多次出现，权重增加
    				int oldvalue=graph.targets(firstline[i-1]).get(firstline[i]);
    				graph.set(firstline[i-1], firstline[i], oldvalue+1);
    			}
    			else
    				graph.set(firstline[i-1], firstline[i], 1);//边的起点和终点一定是输入中邻接的两个词
    		}
    		
    		String endString=firstline[firstline.length-1];//记录结尾字符串，以便建立与下一行首字符串的邻接关系
    		templine=reader.readLine();
    		
        	while(templine!=null) {
        		String lowerline=templine.toLowerCase(); //大写转化为小写  		
        		String[] line =lowerline.split(" ") ;//按空格进行分词
        		int len=line.length;
        		
        		graph.set(endString, line[0], 1);//上一行结尾与该行起点邻接
        		
        		for(int i=1;i<len;i++) {//无论顶点是否已经存在，顶点标签已经确定，直接set即可
        			if(graph.targets(line[i-1]).containsKey(line[i])) {//邻接词多次出现，权重增加
        				int oldvalue=graph.targets(line[i-1]).get(line[i]);
        				graph.set(line[i-1], line[i], oldvalue+1);
        			}
        			else
        				graph.set(line[i-1], line[i], 1);//边的起点和终点一定是输入中邻接的两个词
        		}
        		
        		endString=line[len-1];//记录结尾字符串，以便建立与下一行首字符串的邻接关系
        		templine=reader.readLine();
        	}    	
    	}
    	reader.close();
    }
    
    // TODO checkRep
    
    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
       String[] oldinput=input.split(" ");//将输入对空格分词
       String lowerString=input.toLowerCase();
       String[] oldpoem=lowerString.split(" ");//输入的全小写副本
       Set<String> verticeSet=graph.vertices();
       StringBuffer outputBuffer=new StringBuffer();
       
       for(int i=0;i<oldpoem.length-1;i++) {//遍历输入中每一个字符串  	   
    	   if(verticeSet.contains(oldpoem[i])&&verticeSet.contains(oldpoem[i+1])) {//相邻的词均在图中有对应顶点
    		   outputBuffer.append(oldinput[i]);//插入oldinput而不是oldpoem是因为输出要保留原有输入中的大小写
    		   outputBuffer.append(" ");
    		   String bridge=this.hasbridge(oldpoem[i], oldpoem[i+1]);//判断有无桥词
    		   if(!bridge.equals(oldpoem[i])) {//存在桥词，插入
    			   outputBuffer.append(bridge);
    			   outputBuffer.append(" ");
    		   }
    	   }
    	   else {//二者之一未在图中
    		   outputBuffer.append(oldinput[i]);//插入oldinput而不是oldpoem是因为输出要保留原有输入中的大小写
    		   outputBuffer.append(" ");
    	   }
       }
       
       outputBuffer.append(oldinput[oldinput.length-1]);//最后一个词不会有桥词，直接复制
       return outputBuffer.toString();//转换为字符串
    }
    
    /**
     * get the preserved graph
     * @return  preserved graph in class GraphPoet
     */
    
    public Graph<String> getgraph() {
		Graph<String> copyGraph=graph;
		return copyGraph;
	}
    
    /**
     * Get the bridge word between source vertex and target vertex
     * @param source the vertex in front of the bridge 
     * @param target the vertex behind the bridge
     * @return deterministic bridge word if there is only an deterministic bridge word between
     * 		   the source and target,or max of the bridge words if there are mare than one bridge words
     * 		   otherwise return source(the first parameter)
     */
    public String hasbridge(String source,String target) {
    	int weight=0;
    	String bridge=source;
    	Map<String, Integer> firstedge=graph.targets(source);//source的所有出边
    	
    	for(Map.Entry<String, Integer> entry:firstedge.entrySet()) {
    		String firsttarget=entry.getKey();//以出边的终点再进行遍历
    		Map<String, Integer> secondedge=graph.targets(firsttarget);
    		if(secondedge.containsKey(target))//若以此为起点找到了指向target的边
    			if(secondedge.get(target)>weight) {//确保找到最大权重边的桥词
    				bridge=firsttarget;
    			}
    	
    	}
    	return bridge;
    }
    
    
    // TODO toString()
    @Override public String toString() {
    	Set<String> vers=graph.vertices();
    	if(vers.isEmpty()) {
    		return "Graph is empty";
    	}
    	String information="";
    	for(String v:vers) {
    		Map<String, Integer> tarMap=graph.targets(v);
    		for(Map.Entry<String, Integer> entry:tarMap.entrySet()) {
    			information=information.concat(v+"->"+entry.getKey()+":"+entry.getValue()+"\n");
    		}
		}
		return information;
    }
}
