package State;

import Exceptions.PlanEntryStateNotMatchException;
import common.CommonPlanningEntry;

public class RunningState implements EntryState{
	@Override
	public String getStateName() {
		return "Running";
	}
	
	/**
	 * ������
	 */
	@Override
	public <R> void start(CommonPlanningEntry<R> cpe) {
	
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
	 * �趨״̬Ϊ����״̬
	 */
	@Override
	public <R> void end(CommonPlanningEntry<R> cpe) {
		cpe.setState(new EndedState());
	}
	
	/**
	 * �Ƿ�״̬ת��
	 * @throws PlanEntryStateNotMatchException �Ƿ�״̬ת��
	 */
	@Override
	public <R> void cancel(CommonPlanningEntry<R> cpe) throws PlanEntryStateNotMatchException {
		throw new PlanEntryStateNotMatchException(getStateName());
	}
}
