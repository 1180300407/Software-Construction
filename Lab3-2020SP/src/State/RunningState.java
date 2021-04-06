package State;

import common.CommonPlanningEntry;

public class RunningState implements EntryState{
	@Override
	public String getStateName() {
		return "Running";
	}
	
	/**
	 * 非法状态转换
	 */
	@Override
	public <R> void start(CommonPlanningEntry<R> cpe) {
		System.out.println("该计划项已经启动,请勿重复操作!");
	}
	
	/**
	 * 非法状态转换
	 */
	@Override
	public <R> void allocate(CommonPlanningEntry<R> cpe) {
		System.out.println("该计划项正在运行,分配失败!");
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
	 */
	@Override
	public <R> void cancel(CommonPlanningEntry<R> cpe) {
		System.out.println("该计划项正在运行,取消失败!");
	}
}
