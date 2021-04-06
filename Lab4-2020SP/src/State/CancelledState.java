package State;

import Exceptions.PlanEntryStateNotMatchException;
import common.CommonPlanningEntry;

public class CancelledState implements EntryState{
	@Override
	public String getStateName() {
		return "Cancelled";
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
	 * 非法状态转换
	 * @throws PlanEntryStateNotMatchException 非法状态转换
	 */
	@Override
	public <R> void end(CommonPlanningEntry<R> cpe) throws PlanEntryStateNotMatchException {
		throw new PlanEntryStateNotMatchException(getStateName());
	}
	
	/**
	 * 已取消
	 */
	@Override
	public <R> void cancel(CommonPlanningEntry<R> cpe) {
		
	}
}
