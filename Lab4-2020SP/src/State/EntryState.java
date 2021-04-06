package State;

import Exceptions.PlanEntryStateNotMatchException;
import common.CommonPlanningEntry;

/**
 * 表示计划项的一种状态，不同状态间可进行转换
 * @author 123
 *
 */

public interface EntryState {
	/**
	 * @return 获得状态名称
	 */
	public String getStateName();
	
	public <R> void start(CommonPlanningEntry<R> cpe) throws PlanEntryStateNotMatchException;
	
	public <R> void allocate(CommonPlanningEntry<R> cpe) throws PlanEntryStateNotMatchException;
	
	public <R> void cancel(CommonPlanningEntry<R> cpe) throws PlanEntryStateNotMatchException;
	
	public <R> void end(CommonPlanningEntry<R> cpe) throws PlanEntryStateNotMatchException;
}
