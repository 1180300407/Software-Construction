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
 * BlockableEntry�ӿڵ�ʵ�֣��ɱ���
 * @author 123
 *
 */
public class BlockableEntryImpl<R> implements BlockableEntry<R>{
	private List<Timeslot> timeslots=new ArrayList<Timeslot>();//����Ӧ�ô���2
	private boolean setbefore=false;
	//	AF(timeslots,sebefore)=�趨ʱ��Ϊtimeslots�ļƻ����setbeforeΪfalse��֤��δ���趨ʱ��
	//	��setbeforeΪtrue����ʱ���Ѿ��趨���������޸�
	//Representation invariant:
	//	��setbeforeΪtrueʱ�������趨ʱ��(ֻ���趨һ��)
	//Safety from rep exposure:
	//	��Ա������private�ģ�����ֵ�ͳ�Ա������ֵʱ��ת��Ϊ���ɱ����ͣ������ڱ�ʾй¶
	
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
			this.timeslots=Collections.unmodifiableList(timeslot);//�趨��ʱ��Ͳ������޸�
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
