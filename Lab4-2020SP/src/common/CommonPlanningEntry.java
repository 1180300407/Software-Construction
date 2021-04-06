package common;
import java.util.List;

import Exceptions.PlanEntryStateNotMatchException;
import Location.Location;
import State.*;
import Timeslot.Timeslot;

/**
 * �ӿ�PlanningEntry��ʵ���࣬�洢�ƻ���������Լ�״̬,�ǳ�����
 * @author 123
 * @param <R>
 *
 * @param <R> ���Ͳ���R����ʾ�üƻ���ռ�е���Դ����
 */

public abstract class CommonPlanningEntry<R> implements PlanningEntry<R> {
	private String name;
	private String type;
	private EntryState state=new WaitingState();
	
	//Abstraction function:
	//	AF(name,state,type)=һ������Ϊname��״̬Ϊstate,����Ϊtype�ļƻ���
	//Representation invariant:
	//	true
	//Safety from rep exposure:
	//	��Ա����ȫ��private��
	
	/**
	 * ���캯��
	 * @param name �ƻ�������
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
	 * ����״̬
	 * @param es Ŀ��״̬
	 */
	public void setState(EntryState es) {
		this.state=es;
	}

	@Override
	public String getType() {
		return type;
	}
	
	//��ΪType���������������͵ģ����������ڲ�ȷ�����������Ϊprotected
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
