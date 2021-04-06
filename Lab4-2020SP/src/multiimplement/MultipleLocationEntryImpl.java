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

import Location.Location;
import LogFile.MyFormatter;
import multidimension.MultipleLocationEntry;

/**
 * MultipleLocationEntry接口的实现，对多个位置进行处理，可变类
 * @author 123
 *
 */

public class MultipleLocationEntryImpl implements MultipleLocationEntry{
	private List<Location> locations=new ArrayList<Location>();
	private boolean setbefore=false;
	private static Logger myLogger=Logger.getLogger("MultipleLocationEntryImpl");
	//Abstraction function:
	//	AF(locations,sebefore)=设定的位置，若setbefore为false，证明未曾设定位置
	//	若setbefore为true，则位置已经设定，不可再修改
	//Representation invariant:
	//	在setbefore为true时不可再改变任一位置
	//	locations的数量应大于等于2，至少包含起点和终点,每个位置互不相同
	//Safety from rep exposure:
	//	成员变量是private的，为不可变类型，返回值和成员变量赋值时均转换为不可变类型，不存在表示泄露
	
	private void checkRep() {
		assert locations.size()>=2;
		Set<Location> locationset=new HashSet<Location>(locations);
		assert locationset.size()==locations.size();//互不相同
		//日志记录
		myLogger.setLevel(Level.INFO);
		myLogger.setUseParentHandlers(false);
		//写入文件
		FileHandler handler;
		try {
			handler = new FileHandler("src/LogFile/MultipleLocationEntryImplLog.log");
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
	public List<Location> getLocation(){
		return Collections.unmodifiableList(locations);
	}
	
	public boolean getsetbefore() {
		return setbefore;
	}
	
	@Override
	public void setLocations(List<Location> locs) {
		if(setbefore) {
			System.out.println("位置已经设定过，不可再进行修改！");
			return;
		}
		
		locations=new ArrayList<Location>();
		for(Location location:locs) {
			locations.add(location);
		}
		checkRep();
		setbefore=true;
	}
}
