package State;

import common.CommonPlanningEntry;

public class CancelledState implements EntryState{
	@Override
	public String getStateName() {
		return "Cancelled";
	}
	
	/**
	 * 非法状态转换
	 */
	@Override
	public <R> void start(CommonPlanningEntry<R> cpe) {
		System.out.println("该计划项已经取消,启动失败!");
	}
	
	/**
	 * 非法状态转换
	 */
	@Override
	public <R> void allocate(CommonPlanningEntry<R> cpe) {
		System.out.println("该计划项已经取消,分配失败!");
	}
	
	/**
	 * 非法状态转换
	 */
	@Override
	public <R> void end(CommonPlanningEntry<R> cpe) {
		System.out.println("该计划项已经取消,结束失败!");
	}
	
	/**
	 * 非法状态转换
	 */
	@Override
	public <R> void cancel(CommonPlanningEntry<R> cpe) {
		System.out.println("该计划项已经取消,请勿重复操作!");
	}
}
