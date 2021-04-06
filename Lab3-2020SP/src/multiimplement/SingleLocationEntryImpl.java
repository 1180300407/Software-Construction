package multiimplement;

import java.util.ArrayList;
import java.util.List;

import Location.Location;
import multidimension.SingleLocationEntry;

/**
 * 实现SingleLocationEntry接口
 * 可更改位置，是可变类
 * @author 123
 *
 */

public class SingleLocationEntryImpl implements SingleLocationEntry{
	private List<Location> location=new ArrayList<Location>();
	//Abstraction function:
	//	AF(location)=设定的位置
	//Representation invariant:
	//	location的大小为1
	//Safety from rep exposure:
	//	成员变量是private的，为不可变类型，使用防御式拷贝，不存在表示泄露
	
	private void checkRep() {
		assert location.size()==1;
	}
	
	@Override
	public void setLocation(List<Location> location) {
		//防御式拷贝
		if(location.size()!=1)
			return;
		this.location=new ArrayList<Location>();
		this.location.add(location.get(0));
		checkRep();
	}

	@Override
	public List<Location> getLocation() {
		List<Location> location=new ArrayList<Location>();
		location.addAll(this.location);
		return location;
	}
}
