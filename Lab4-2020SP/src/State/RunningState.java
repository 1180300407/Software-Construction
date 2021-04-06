package State;

import Exceptions.PlanEntryStateNotMatchException;
import common.CommonPlanningEntry;

public class RunningState implements EntryState{
	@Override
	public String getStateName() {
		return "Running";
	}
	
	/**
	 * 已启动
	 */
	@Override
	public <R> void start(CommonPlanningEntry<R> cpe) {
	
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
	 * 设定状态为结束状态
	 */
	@Override
	public <R> void end(CommonPlanningEntry<R> cpe) {
		cpe.setState(new EndedState());
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
