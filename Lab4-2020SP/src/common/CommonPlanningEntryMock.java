package common;

import java.util.ArrayList;
import java.util.List;

import Location.Location;
import State.AllocatedState;
import Timeslot.Timeslot;

/**
 * 为了测试CommonPlanningEntry和PlanningEntry里面的函数而实现的专用于测试的子类
 * @author 123
 *
 */

public class CommonPlanningEntryMock extends CommonPlanningEntry<String>{
	private boolean hassetresource=false;
	private List<String> resources=new ArrayList<String>();
	private List<Location> locations=new ArrayList<>();
	private List<Timeslot> timeslots=new ArrayList<Timeslot>();
	//Abstraction function:
	//	AF(resources,locations,timeslots)=一个占用资源为resources,占用位置为locations
	//	占用时间为timeslots的计划项，hassetresource标识是否分配过资源
	//Representation invariant:
	//	true
	//Safety from rep exposure:
	//	成员变量全是private的
	
	public CommonPlanningEntryMock(String name) {
		super(name);
	}
	
	@Override
	public void allocateResource(List<String> resource) {
		if(hassetresource)
			return;
		for(String s:resource) {
			resources.add(s);
			hassetresource=true;
			super.setState(new AllocatedState());
		}
	}
	
	@Override
	public List<String> getResource(){
		return resources;
	}
	
	@Override
	public List<Location> getLocation(){
		locations.add(null);
		return locations;
	}
	
	@Override
	public List<Timeslot> getTime(){
		timeslots.add(null);
		return timeslots;
	}
}
