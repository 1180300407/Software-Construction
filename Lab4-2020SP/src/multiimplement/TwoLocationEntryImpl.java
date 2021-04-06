package multiimplement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import Location.Location;
import LogFile.MyFormatter;
import multidimension.TwoLocationEntry;

/**
 * 实现TwoLocationEntry接口，实现对一个位置对的处理，可变类
 * @author 123
 *
 */

public class TwoLocationEntryImpl implements TwoLocationEntry{
	private Location start;//起点
	private Location end;//终点
	private boolean setbefore=false;//是否已经设定过位置
	private static Logger myLogger=Logger.getLogger("TwoLocationEntryImplLog");
	//Abstraction function:
	//	AF(start,end,setbefore)=设定的位置，若setbefore为false，证明未曾设定位置
	//	若setbefore为true，则位置已经设定，不可再修改
	//Representation invariant:
	//	在setbefore为true时不可再改变start与end
	//	起点和终点不应相同
	//Safety from rep exposure:
	//	成员变量是private的，为不可变类型，使用防御式拷贝，不存在表示泄露
	private void checkRep() {
		assert !start.equals(end);
		//日志记录
		myLogger.setLevel(Level.INFO);
		myLogger.setUseParentHandlers(false);
		//写入文件
		FileHandler handler;
		try {
			handler = new FileHandler("src/LogFile/TwoLocationEntryImplLog.log");
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
	public List<Location> getLocation() {
		List<Location> locations=new ArrayList<Location>();
		locations.add(start);
		locations.add(end);
		return locations;
	}
	
	@Override
	public Location getStart() {
		Location copyLocation=new Location(start.getLongitude(), start.getLatitude(), start.getName(), start.isshareable());
		return copyLocation;
	}
	
	@Override
	public Location getEnd() {
		Location copyLocation=new Location(end.getLongitude(), end.getLatitude(), end.getName(), end.isshareable());
		return copyLocation;
	}
	
	@Override
	public void setLocations(Location start,Location end) {
		if(setbefore) {
			System.out.println("位置已经设定过，不可再进行修改！");
			return;
		}
		Location copyLocation1=new Location(start.getLongitude(), start.getLatitude(), start.getName(), start.isshareable());
		Location copyLocation2=new Location(end.getLongitude(), end.getLatitude(), end.getName(), end.isshareable());
		this.start=copyLocation1;
		this.end=copyLocation2;
		checkRep();
		this.setbefore=true;
	}
}
