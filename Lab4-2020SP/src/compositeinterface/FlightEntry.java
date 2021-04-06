package compositeinterface;

import java.util.List;

import Location.Location;
import common.CommonPlanningEntry;
import multiimplement.SingleSortedResourceEntryImpl;
import multiimplement.TwoLocationEntryImpl;
import multiimplement.UnBlockableEntryImpl;
import Resources.Plane;
import State.AllocatedState;
import Timeslot.Timeslot;;
/**
 * FlightPlanningEntry的具体实现，一个航班，可变类
 * @author 123
 *
 * @param <R> 占用资源类型
 */
public class FlightEntry<R> extends CommonPlanningEntry<Plane> implements FlightPlanningEntry<Plane>{
	private TwoLocationEntryImpl tle=new TwoLocationEntryImpl();
	private SingleSortedResourceEntryImpl<Plane> ssre=new SingleSortedResourceEntryImpl<Plane>();
	private UnBlockableEntryImpl ube=new UnBlockableEntryImpl();
	//Abstraction function:
	//	AF(name,tle,ssrle)=一个符合两个位置、单资源、不可阻塞的航班计划项
	//Representation invariant:
	//	true(由每个维度的成员变量保证)
	//Safety from rep exposure:
	//	成员变量全是private的，并由各个成员变量的对于表示泄露的安全性进行保证
	
	/**
	 * 构造函数
	 * @param name 航班名称
	 */
	public FlightEntry(String name) {
		super(name);
		super.setType("FlightPlanningEntry");
	}
	
	@Override
	public Location getStart() {
		return tle.getStart();
	}
	
	@Override
	public Location getEnd() {
		return tle.getEnd();
	}
	
	@Override
	public List<Location> getLocation(){
		return tle.getLocation();
	}
	
	@Override
	public void setLocations(Location start,Location end) {
		tle.setLocations(start, end);
	}
	
	@Override
	public List<Plane> getResource() {
		return ssre.getResource();
	}
	
	//添加状态判断
	@Override
	public void allocateResource(List<Plane> resource) {
		if(this.getStateName().equals("Waiting")) {
			ssre.allocateResource(resource);
			super.setState(new AllocatedState());
		}
			
		else 
			System.out.println("航班目前状态为:"+this.getStateName()+",无法分配飞机!");
	}
	
	@Override
	public List<Timeslot> getTime() {
		return ube.getTime();
	}
	
	@Override
	public void setTime(List<Timeslot> timeslot) {
		ube.setTime(timeslot);
	}
	
}
