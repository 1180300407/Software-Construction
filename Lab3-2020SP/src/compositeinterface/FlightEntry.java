package compositeinterface;

import java.util.List;

import Location.Location;
import common.CommonPlanningEntry;
import multiimplement.BlockableEntryImpl;
import multiimplement.MultipleLocationEntryImpl;
import multiimplement.SingleSortedResourceEntryImpl;
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
	private MultipleLocationEntryImpl mle=new MultipleLocationEntryImpl();
	private SingleSortedResourceEntryImpl<Plane> ssre=new SingleSortedResourceEntryImpl<Plane>();
	private BlockableEntryImpl<Plane> be=new BlockableEntryImpl<Plane>();
	//Abstraction function:
	//	AF(name,tle,ssrle)=一个符合两个位置、单资源、不可阻塞的航班计划项
	//Representation invariant:
	//	位置个数只能为2和3
	//Safety from rep exposure:
	//	成员变量全是private的，并由各个成员变量的对于表示泄露的安全性进行保证
	private void checkRep() {
		assert mle.getLocation().size()==2||mle.getLocation().size()==3;
	}
	/**
	 * 构造函数
	 * @param name 航班名称
	 */
	public FlightEntry(String name) {
		super(name);
		super.setType("FlightPlanningEntry");
	}
	
	@Override
	public List<Location> getLocation(){
		return mle.getLocation();
	}
	
	@Override
	public void setLocations(List<Location> locs) {
		if(locs.size()==2||locs.size()==3) {
			mle.setLocations(locs);
			checkRep();
		}	
		else 
			System.out.println("分配的位置个数不符合要求!分配位置失败!");
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
		return be.getTime();
	}
	
	@Override
	public void setTime(List<Timeslot> timeslot) {
		be.setTime(timeslot);
	}
	
	@Override
	public void block(CommonPlanningEntry<Plane> cpe) {
		be.block(this);
	}
}
