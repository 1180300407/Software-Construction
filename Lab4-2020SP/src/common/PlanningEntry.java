package common;

import java.util.List;

import Location.Location;
import Timeslot.Timeslot;

/**
 * 一个计划项，表示一个待执行的活动/任务，它需要配置特定类型/数量的 资源，并在特定的物理位置加以执行。 
 * 有若干个状态：未分配资源、已分配资源但未启动、已启动、已完成、已取消、挂起。 
 * @author 123
 *
 * @param <R> 泛型R表示占用的资源类型
 */

public interface PlanningEntry<R> {
	
	/**
	 * 启动计划项,仅当计划项已分配资源或处于阻塞状态时可启动成功
	 */
	public void start();
	
	/**
	 * 取消计划项,运行时无法取消,已结束的计划项无法取消
	 */
	public void cancel();
	
	/**
	 * 结束计划项,只能在运行时正确结束
	 */
	public void end();
	
	/**
	 * 获得该计划项的名字
	 * @return 该计划项的名字
	 */
	public String getName();
	
	/**
	 * @return 获取当前状态的名称,如Waiting，Running等
	 */
	public String getStateName();
	
	/**
	 * @return 获取计划项所属类型，如CoursePlanningEntry等
	 */
	public String getType();
	
	/**
	 * @return 获取计划项所有资源
	 */
	public List<R> getResource();
	
	/**
	 * 为计划项分配资源,只可分配一次，分配后不能修改
	 * @param resource 待分配资源
	 */
	public void allocateResource(List<R> resource);
	
	/** 
	 * @return 获取计划项占用的所有位置
	 */
	public List<Location> getLocation(); 
	
	/**
	 * @return 获取计划项占用的所有时间对
	 */
	public List<Timeslot> getTime();
}
