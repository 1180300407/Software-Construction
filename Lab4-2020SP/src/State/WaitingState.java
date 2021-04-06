package State;

import Exceptions.PlanEntryStateNotMatchException;
import common.CommonPlanningEntry;

public class WaitingState implements EntryState{
	@Override
	public String getStateName() {
		return "Waiting";
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
	 * ����״̬Ϊ����״̬
	 */
	@Override
	public <R> void allocate(CommonPlanningEntry<R> cpe) {
		cpe.setState(new AllocatedState());
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
	 * ����״̬Ϊȡ��״̬
	 */
	@Override
	public <R> void cancel(CommonPlanningEntry<R> cpe) {
		cpe.setState(new CancelledState());
	}
}
