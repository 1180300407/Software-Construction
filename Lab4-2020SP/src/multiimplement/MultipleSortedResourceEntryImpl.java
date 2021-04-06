package multiimplement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import LogFile.MyFormatter;
import multidimension.MultipleSortedResourceEntry;

/**
 * 实现MultipleSortedResourceEntry<R>接口，实现对多个可区分资源的处理
 * 如果考虑全部属性，是可变类
 * 但其实是用布尔变量setbefore的不同值来维护resources的不可变
 * @author 123
 *
 * @param <R>
 */

public class MultipleSortedResourceEntryImpl<R> implements MultipleSortedResourceEntry<R>{
	private List<R> resources=new ArrayList<>();
	private boolean setbefore=false;
	private static Logger myLogger=Logger.getLogger("MultipleSortedResourceLog");
	//	AF(resources,sebefore)=分配资源为resources的计划项，若setbefore为false，证明未曾分配资源
	//	若setbefore为true，则资源已经分配，不可再改变
	//Representation invariant:
	//	在setbefore为true时不可再改变资源
	//	分配的资源中不能有重复元素
	//Safety from rep exposure:
	//	成员变量是private的，返回值和成员变量赋值时均转换为不可变类型
	private void checkRep() {
		Set<R> resourceSet=new HashSet<>(resources);
		assert resourceSet.size()==resources.size();
		//日志记录
		myLogger.setLevel(Level.INFO);
		myLogger.setUseParentHandlers(false);
		//写入文件
		FileHandler handler;
		try {
			handler = new FileHandler("src/LogFile/MultipleSortedResourceLog.log");
			handler.setFormatter(new MyFormatter());//采用固定格式
			handler.setLevel(Level.INFO);
			myLogger.addHandler(handler);
			myLogger.info("进行不变量检查");
			handler.close();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<R> getResource(){
		return Collections.unmodifiableList(resources);
	}
	
	@Override
	public void allocateResource(List<R> rs) {
		if(!setbefore) {
			resources=new ArrayList<>();//防御式拷贝
			for(R resource:rs) {
				resources.add(resource);
			}
			resources=Collections.unmodifiableList(resources);
			checkRep();
			setbefore=true;
		}
	}
}
