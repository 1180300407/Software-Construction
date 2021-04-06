package common;
import java.util.List;

import Exceptions.PlanEntryStateNotMatchException;
import Location.Location;
import State.*;
import Timeslot.Timeslot;

/**
 * 接口PlanningEntry的实现类，存储计划项的名字以及状态,是抽象类
 * @author 123
 * @param <R>
 *
 * @param <R> 泛型参数R，表示该计划项占有的资源类型
 */

public abstract class CommonPlanningEntry<R> implements PlanningEntry<R> {
	private String name;
	private String type;
	private EntryState state=new WaitingState();
	
	//Abstraction function:
	//	AF(name,state,type)=一个名称为name，状态为state,类型为type的计划项
	//Representation invariant:
	//	true
	//Safety from rep exposure:
	//	成员变量全是private的
	
	/**
	 * 构造函数
	 * @param name 计划项名称
	 */
	
	public CommonPlanningEntry (String name) {
		this.name = name;
		this.type="PlanningEntry";
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public void start() {
		try {
			state.start(this);
		} catch (PlanEntryStateNotMatchException e) {
			System.out.println(e.getErrorMessage());
		}
	}
	
	@Override
	public void cancel() {
		try {
			state.cancel(this);
		} catch (PlanEntryStateNotMatchException e) {
			System.out.println(e.getErrorMessage());
		}
	}
	
	@Override
	public void end() {
		try {
			state.end(this);
		} catch (PlanEntryStateNotMatchException e) {
			System.out.println(e.getErrorMessage());
		}
	}
	
	@Override
	public String getStateName() {
		return state.getStateName();
	}
	
	/**
	 * 设置状态
	 * @param es 目标状态
	 */
	public void setState(EntryState es) {
		this.state=es;
	}

	@Override
	public String getType() {
		return type;
	}
	
	//因为Type是用来区分子类型的，在子类型内部确定，因此设置为protected
	protected void setType(String type) {
		this.type=type;
	}
	@Override
	public abstract List<R> getResource();
	@Override
	public abstract void allocateResource(List<R> reource);
	
	@Override
	public abstract List<Location> getLocation(); 
	
	@Override
	public abstract List<Timeslot> getTime();
}
