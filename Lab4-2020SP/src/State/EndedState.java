package State;

import Exceptions.PlanEntryStateNotMatchException;
import common.CommonPlanningEntry;

public class EndedState implements EntryState{
	@Override
	public String getStateName() {
		return "Ended";
	}
	
	/**
	 * 非法状态转换
	 * @throws PlanEntryStateNotMatchException 非法状态转换
	 */
	@Override
	public <R> void start(CommonPlanningEntry<R> cpe) throws PlanEntryStateNotMatchException {
		throw new PlanEntryStateNotMatchException(getStateName());
	}
	
	/**
	 * 非法状态转换
	 * @throws PlanEntryStateNotMatchException 非法状态转换
	 */
	@Override
	public <R> void allocate(CommonPlanningEntry<R> cpe) throws PlanEntryStateNotMatchException {
		throw new PlanEntryStateNotMatchException(getStateName());
	}
	
	/**
	 * 已结束
	 */
	@Override
	public <R> void end(CommonPlanningEntry<R> cpe) {
		
	}
	
	/**
	 * 非法状态转换
	 * @throws PlanEntryStateNotMatchException 非法状态转换
	 */
	@Override
	public <R> void cancel(CommonPlanningEntry<R> cpe) throws PlanEntryStateNotMatchException {
		throw new PlanEntryStateNotMatchException(getStateName());
	}
}
