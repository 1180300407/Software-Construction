package multiimplement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Location.Location;
import multidimension.MultipleLocationEntry;

/**
 * MultipleLocationEntry接口的实现，对多个位置进行处理，可变类
 * @author 123
 *
 */

public class MultipleLocationEntryImpl implements MultipleLocationEntry{
	private List<Location> locations=new ArrayList<Location>();
	private boolean setbefore=false;
	//Abstraction function:
	//	AF(locations,sebefore)=设定的位置，若setbefore为false，证明未曾设定位置
	//	若setbefore为true，则位置已经设定，不可再修改
	//Representation invariant:
	//	在setbefore为true时不可再改变任一位置
	//	locations的数量应大于等于2，至少包含起点和终点
	//Safety from rep exposure:
	//	成员变量是private的，为不可变类型，返回值和成员变量赋值时均转换为不可变类型，不存在表示泄露
	
	private void checkRep() {
		assert locations.size()>=2;
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
