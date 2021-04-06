package compositeinterface;

import Resources.*;
import State.AllocatedState;
import Timeslot.Timeslot;

import java.util.List;

import Location.Location;
import common.CommonPlanningEntry;
import multiimplement.SingleLocationEntryImpl;
import multiimplement.SingleSortedResourceEntryImpl;
import multiimplement.UnBlockableEntryImpl;
/**
 * CoursePlanningEntry的具体实现，一个课程，可变类
 * @author 123
 *
 * @param <R> 占用资源类型
 */
public class CourseEntry<R> extends CommonPlanningEntry<Teacher> implements CoursePlanningEntry<Teacher>{
	private SingleLocationEntryImpl sle=new SingleLocationEntryImpl();
	private SingleSortedResourceEntryImpl<Teacher> ssre=new SingleSortedResourceEntryImpl<Teacher>();
	private UnBlockableEntryImpl ube=new UnBlockableEntryImpl();
	
	//Abstraction function:
	//	AF(sle,ssre,ube)=一个符合单位置、单资源、不可阻塞的课程计划项
	//Representation invariant:
	//	true(由每个维度的成员变量保证)
	//Safety from rep exposure:
	//	成员变量全是private的，并由各个成员变量的对于表示泄露的安全性进行保证
	
	/**
	 * 构造函数
	 * @param name 课程名称
	 */
	public CourseEntry(String name) {
		super(name);
		super.setType("CoursePlanningEntry");
	}
	
	@Override
	public List<Location> getLocation() {
		return sle.getLocation();
	}
	
	@Override
	public void setLocation(List<Location> location) {
		sle.setLocation(location);
	}
	
	@Override
	public List<Teacher> getResource() {
		return ssre.getResource();
	}
	
	@Override
	public void allocateResource(List<Teacher> resource) {
		if(this.getStateName().equals("Waiting")) {
			ssre.allocateResource(resource);
			super.setState(new AllocatedState());
		}
			
		else
			System.out.println("课程目前状态为:"+this.getStateName()+",无法分配教师!");
	}
	
	@Override
	public List<Timeslot> getTime() {
		return ube.getTime();
	}
	
	@Override
	public void setTime(List<Timeslot> timeslot) {
		ube.setTime(timeslot);
	}

	@Override
	public String getName() {
		return super.getName();
	}
}
