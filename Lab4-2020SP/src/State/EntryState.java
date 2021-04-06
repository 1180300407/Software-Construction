package State;

import Exceptions.PlanEntryStateNotMatchException;
import common.CommonPlanningEntry;

/**
 * ��ʾ�ƻ����һ��״̬����ͬ״̬��ɽ���ת��
 * @author 123
 *
 */

public interface EntryState {
	/**
	 * @return ���״̬����
	 */
	public String getStateName();
	
	public <R> void start(CommonPlanningEntry<R> cpe) throws PlanEntryStateNotMatchException;
	
	public <R> void allocate(CommonPlanningEntry<R> cpe) throws PlanEntryStateNotMatchException;
	
	public <R> void cancel(CommonPlanningEntry<R> cpe) throws PlanEntryStateNotMatchException;
	
	public <R> void end(CommonPlanningEntry<R> cpe) throws PlanEntryStateNotMatchException;
}
