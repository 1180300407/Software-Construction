package compositeinterface;

import Resources.Plane;
import multidimension.*;
/**
 * 一个航班计划项
 * @author 123
 *
 * @param <R> 占用资源类型
 */
public interface FlightPlanningEntry<R extends Plane> extends TwoLocationEntry,SingleSortedResourceEntry<R>,UnBlockableEntry{
	/**
	 * 静态工厂,创造一个航班计划项的实例,初始状态为Waiting
	 * @param <R> 航班占用的资源,应是Plane类或其子类
	 * @param name 航班名称
	 * @return 一个航班计划项的实例
	 */
	public static <R> FlightEntry<R> CreateFlight(String name){
		FlightEntry<R> fe=new FlightEntry<R>(name);
		return fe;
	}
	
	/**
	 * @return 获得航班名称
	 */
	public String getName();
	
	/**
	 * 航班起飞
	 */
	public void start();
	
	/**
	 * 提前取消航班
	 */
	public void cancel();
	
	/**
	 * 航班降落
	 */
	public void end();
	
	/**
	 * @return 获得航班状态的名称
	 */
	public String getStateName();
}
