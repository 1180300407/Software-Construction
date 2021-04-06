package State;

import common.CommonPlanningEntry;

public class AllocatedState implements EntryState{
	@Override
	public String getStateName() {
		return "Allocated";
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
		System.out.println("该计划项已经分配资源,请勿重复分配!");
	}
	
	/**
	 * 非法状态转换
	 */
	@Override
	public <R> void end(CommonPlanningEntry<R> cpe) {
		System.out.println("该计划项还未启动,无法结束!");
	}
	
	/**
	 * 设定状态为取消状态
	 */
	@Override
	public <R> void cancel(CommonPlanningEntry<R> cpe) {
		cpe.setState(new CancelledState());
	}
}
