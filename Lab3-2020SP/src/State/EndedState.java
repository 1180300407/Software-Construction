package State;

import common.CommonPlanningEntry;

public class EndedState implements EntryState{
	@Override
	public String getStateName() {
		return "Ended";
	}
	
	/**
	 * 非法状态转换
	 */
	@Override
	public <R> void start(CommonPlanningEntry<R> cpe) {
		System.out.println("该计划项已经结束,启动失败!");
	}
	
	/**
	 * 非法状态转换
	 */
	@Override
	public <R> void allocate(CommonPlanningEntry<R> cpe) {
		System.out.println("该计划项已经结束,分配失败!");
	}
	
	/**
	 * 非法状态转换
	 */
	@Override
	public <R> void end(CommonPlanningEntry<R> cpe) {
		System.out.println("该计划项已经结束,请勿重复操作!");
	}
	
	/**
	 * 非法状态转换
	 */
	@Override
	public <R> void cancel(CommonPlanningEntry<R> cpe) {
		System.out.println("该计划项已经结束,取消失败!");
	}
}
