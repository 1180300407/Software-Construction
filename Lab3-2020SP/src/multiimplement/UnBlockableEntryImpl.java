package multiimplement;

import multidimension.UnBlockableEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Timeslot.Timeslot;;

/**
 * UnBlockableEntry接口的实现，可变类
 * @author 123
 *
 */
public class UnBlockableEntryImpl implements UnBlockableEntry{
	private List<Timeslot> timeslot=new ArrayList<Timeslot>();
	private boolean setbefore=false;
	//	AF(timeslot,sebefore)=设定时间为timeslot的计划项，若setbefore为false，证明未曾设定时间
	//	若setbefore为true，则时间已经设定，不可再修改
	//Representation invariant:
	//	在setbefore为true时不可再改变时间,timeslot个数为1
	//Safety from rep exposure:
	//	成员变量是private的，为不可变类型，不存在表示泄露
	private void checkRep() {
		assert timeslot.size()==1;
	}
	
	@Override
	public List<Timeslot> getTime() {
		return Collections.unmodifiableList(timeslot);
	}
	
	@Override
	public void setTime(List<Timeslot> timeslot) {
		if(setbefore)
			return;
		if(timeslot.size()!=1)
			return;
		this.timeslot.add(timeslot.get(0));
		checkRep();
		setbefore=true;
	}
}
