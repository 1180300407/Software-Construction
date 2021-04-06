package State;

import Exceptions.PlanEntryStateNotMatchException;
import common.CommonPlanningEntry;

public class CancelledState implements EntryState{
	@Override
	public String getStateName() {
		return "Cancelled";
	}
	
	/**
	 * �Ƿ�״̬ת��
	 * @throws PlanEntryStateNotMatchException �Ƿ�״̬ת��
	 */
	@Override
	public <R> void start(CommonPlanningEntry<R> cpe) throws PlanEntryStateNotMatchException {
		throw new PlanEntryStateNotMatchException(getStateName());
	}
	
	/**
	 * �Ƿ�״̬ת��
	 * @throws PlanEntryStateNotMatchException �Ƿ�״̬ת��
	 */
	@Override
	public <R> void allocate(CommonPlanningEntry<R> cpe) throws PlanEntryStateNotMatchException {
		throw new PlanEntryStateNotMatchException(getStateName());
	}
	
	/**
	 * �Ƿ�״̬ת��
	 * @throws PlanEntryStateNotMatchException �Ƿ�״̬ת��
	 */
	@Override
	public <R> void end(CommonPlanningEntry<R> cpe) throws PlanEntryStateNotMatchException {
		throw new PlanEntryStateNotMatchException(getStateName());
	}
	
	/**
	 * ��ȡ��
	 */
	@Override
	public <R> void cancel(CommonPlanningEntry<R> cpe) {
		
	}
}
