package State;

import Exceptions.PlanEntryStateNotMatchException;
import common.CommonPlanningEntry;

public class BlockedState implements EntryState{
	@Override
	public String getStateName() {
		return "Blocked";
	}
	
	/**
	 * 设定状态为运行态
	 */
	@Override
	public <R> void start(CommonPlanningEntry<R> cpe) {
		cpe.setState(new RunningState());
	}
	
	/**
	 * 非法状态转换
	 * @throws PlanEntryStateNotMatchException 非法状态转换
	 */
	@Override
	public <R> void allocate(CommonPlanningEntry<R> cpe) throws PlanEntryStateNotMatchException{
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
	 * 设定状态为取消状态
	 */
	@Override
	public <R> void cancel(CommonPlanningEntry<R> cpe) {
		cpe.setState(new CancelledState());
	}
}
