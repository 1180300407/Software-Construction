package multiimplement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import LogFile.MyFormatter;
import multidimension.SingleSortedResourceEntry;

/**
 * 实现SingleSortedResourceEntry<R>接口，对单一可区分资源的处理，可变类
 * @author 123
 *
 * @param <R> 资源类型
 */

public class SingleSortedResourceEntryImpl<R> implements SingleSortedResourceEntry<R>{
	private List<R> resource=new ArrayList<>();
	private boolean setbefore=false;
	private static Logger myLogger=Logger.getLogger("SingleSortedResourceEntryImplLog");
	//Abstraction function:
	//	AF(resource,sebefore)=分配的资源，若setbefore为false，证明未曾分配过
	//	若setbefore为true，则资源已经分配，不可再修改
	//Representation invariant:
	//	在setbefore为true时不可再进行分配
	//	resource总数为1
	//Safety from rep exposure:
	//	成员变量是private的，返回值和成员变量赋值时均转换为不可变类型，不存在表示泄露
	private void checkRep() {
		assert resource.size()==1;
		//日志记录
		myLogger.setLevel(Level.INFO);
		myLogger.setUseParentHandlers(false);
		//写入文件
		FileHandler handler;
		try {
			handler = new FileHandler("src/LogFile/SingleSortedResourceEntryImplLog.log");
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
	public List<R> getResource() {
		return resource;
	}
	
	@Override
	public void allocateResource(List<R> resource) {
		if(!setbefore) {
			if(resource.size()==1) {
				this.resource=new ArrayList<>();
				this.resource.add(resource.get(0));
				setbefore=true;
				checkRep();
			}
		}
	}
}
