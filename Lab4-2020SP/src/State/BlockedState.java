package State;

import Exceptions.PlanEntryStateNotMatchException;
import common.CommonPlanningEntry;

public class BlockedState implements EntryState{
	@Override
	public String getStateName() {
		return "Blocked";
	}
	
	/**
	 * �趨״̬Ϊ����̬
	 */
	@Override
	public <R> void start(CommonPlanningEntry<R> cpe) {
		cpe.setState(new RunningState());
	}
	
	/**
	 * �Ƿ�״̬ת��
	 * @throws PlanEntryStateNotMatchException �Ƿ�״̬ת��
	 */
	@Override
	public <R> void allocate(CommonPlanningEntry<R> cpe) throws PlanEntryStateNotMatchException{
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
	 * �趨״̬Ϊȡ��״̬
	 */
	@Override
	public <R> void cancel(CommonPlanningEntry<R> cpe) {
		cpe.setState(new CancelledState());
	}
}
