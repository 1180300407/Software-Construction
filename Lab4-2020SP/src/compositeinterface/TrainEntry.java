package compositeinterface;

import java.util.List;

import Exceptions.PlanEntryStateNotMatchException;
import Location.Location;
import common.CommonPlanningEntry;
import multiimplement.BlockableEntryImpl;
import multiimplement.MultipleLocationEntryImpl;
import multiimplement.MultipleSortedResourceEntryImpl;
import Resources.Carriage;
import State.AllocatedState;
import Timeslot.Timeslot;
/**
 * TrainPlanningEntry的实现，一趟高铁，可变类
 * @author 123
 *
 * @param <R>
 */
public class TrainEntry<R> extends CommonPlanningEntry<Carriage> implements TrainPlanningEntry<Carriage>{
	private MultipleLocationEntryImpl mle=new MultipleLocationEntryImpl();
	private MultipleSortedResourceEntryImpl<Carriage> msre=new MultipleSortedResourceEntryImpl<Carriage>();
	private BlockableEntryImpl<Carriage> be=new BlockableEntryImpl<Carriage>();
	//Abstraction function:
	//	AF(name,tle,ssrle)=一个符合多位置、多资源、可阻塞的高铁计划项
	//Representation invariant:
	//	true(由每个维度的成员变量保证)
	//Safety from rep exposure:
	//	成员变量全是private的，并由各个成员变量的对于表示泄露的安全性进行保证
	
	/**
	 * 构造函数
	 * @param name 列车名称
	 */
	public TrainEntry(String name) {
		super(name);
		super.setType("TrainPlanningEntry");
	}
	
	@Override
	public List<Location> getLocation() {
		return mle.getLocation();
	}
	
	@Override
	public void setLocations(List<Location> locs) {
		mle.setLocations(locs);
	}
	
	@Override
	public List<Carriage> getResource() {
		return msre.getResource();
	}
	
	@Override
	public void allocateResource(List<Carriage> resource) {
		if(this.getStateName().equals("Waiting")) {
			msre.allocateResource(resource);
			super.setState(new AllocatedState());
		}
			
		else 
			System.out.println("列车目前状态为:"+this.getStateName()+",无法分配车厢!");
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
	public void block(CommonPlanningEntry<Carriage> cpe) {
		try {
			be.block(this);
		} catch (PlanEntryStateNotMatchException e) {
			System.out.println(e.getErrorMessage());
		}
	}
}
