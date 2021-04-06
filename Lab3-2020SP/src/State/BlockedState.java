package State;

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
	 */
	@Override
	public <R> void allocate(CommonPlanningEntry<R> cpe) {
		System.out.println("该计划项已经阻塞,请勿分配资源!");
	}
	
	/**
	 * 非法状态转换
	 */
	@Override
	public <R> void end(CommonPlanningEntry<R> cpe) {
		System.out.println("该计划项已经阻塞,结束失败!");
	}
	
	/**
	 * 设定状态为取消状态
	 */
	@Override
	public <R> void cancel(CommonPlanningEntry<R> cpe) {
		cpe.setState(new CancelledState());
	}
}
