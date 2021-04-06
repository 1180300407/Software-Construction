package multiimplement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Exceptions.PlanEntryStateNotMatchException;
import State.BlockedState;
import multidimension.BlockableEntry;
import Timeslot.Timeslot;
import common.CommonPlanningEntry;

/**
 * BlockableEntry接口的实现，可变类
 * @author 123
 *
 */
public class BlockableEntryImpl<R> implements BlockableEntry<R>{
	private List<Timeslot> timeslots=new ArrayList<Timeslot>();//个数应该大于2
	private boolean setbefore=false;
	//	AF(timeslots,sebefore)=设定时间为timeslots的计划项，若setbefore为false，证明未曾设定时间
	//	若setbefore为true，则时间已经设定，不可再修改
	//Representation invariant:
	//	在setbefore为true时不可再设定时间(只可设定一次)
	//Safety from rep exposure:
	//	成员变量是private的，返回值和成员变量赋值时均转换为不可变类型，不存在表示泄露
	
	@Override
	public List<Timeslot> getTime(){
		return Collections.unmodifiableList(timeslots);
	}
	
	@Override
	public void setTime(List<Timeslot> timeslot) {
		if(!setbefore) {
			setbefore=true;
			timeslots=new ArrayList<Timeslot>();
			for(Timeslot timeslot2:timeslot) {
				timeslots.add(timeslot2);
			}
			this.timeslots=Collections.unmodifiableList(timeslot);//设定完时间就不能再修改
		}
	}
	
	@Override
	public  void block(CommonPlanningEntry<R> cpe) throws PlanEntryStateNotMatchException{
		if(cpe.getStateName().equals("Running")) {
			cpe.setState(new BlockedState());
			return;
		}
		throw new PlanEntryStateNotMatchException();
	}
}
