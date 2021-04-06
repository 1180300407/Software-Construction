package compositeinterface;

import multidimension.BlockableEntry;
import multidimension.MultipleLocationEntry;
import multidimension.MultipleSortedResourceEntry;


import Resources.Carriage;
import common.CommonPlanningEntry;
/**
 * 一个高铁计划项
 * @author 123
 *
 * @param <R> 占用资源类型
 */
public interface TrainPlanningEntry<R extends Carriage> extends MultipleLocationEntry,MultipleSortedResourceEntry<R>,BlockableEntry<R>{
	/**
	 * 静态工厂,创造一个高铁车次计划项的实例
	 * @param name 高铁车次的名称
	 * @return 一个高铁车次计划项的实例
	 */
	public static <R> TrainEntry<R> CreateTrain(String name){
		TrainEntry<R> te=new TrainEntry<R>(name);
		return te;
	}
	
	@Override
	public void block(CommonPlanningEntry<R> cpe);
	
	/**
	 * 启动高铁
	 */
	public void start();
	
	/**
	 * 结束计划项
	 */
	public void end();
	
	/**
	 * @return 获得高铁车次的名称
	 */
	public String getName();
	
	/**
	 * 取消高铁,若已分配车厢资源则无法取消
	 */
	public void cancel();
	
	/**
	 * @return 获得高铁目前的状态的名称
	 */
	public String getStateName();
}
