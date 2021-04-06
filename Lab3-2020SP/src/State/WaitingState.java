package State;

import common.CommonPlanningEntry;

public class WaitingState implements EntryState{
	@Override
	public String getStateName() {
		return "Waiting";
	}
	
	/**
	 * 非法状态转换
	 */
	@Override
	public <R> void start(CommonPlanningEntry<R> cpe) {
		System.out.println("该计划项尚未分配,启动失败!");
	}
	
	/**
	 * 设置状态为分配状态
	 */
	@Override
	public <R> void allocate(CommonPlanningEntry<R> cpe) {
		cpe.setState(new AllocatedState());
	}
	
	/**
	 * 非法状态转换
	 */
	@Override
	public <R> void end(CommonPlanningEntry<R> cpe) {
		System.out.println("该计划项尚未分配,结束失败!");
	}
	
	/**
	 * 设置状态为取消状态
	 */
	@Override
	public <R> void cancel(CommonPlanningEntry<R> cpe) {
		cpe.setState(new CancelledState());
	}
}
