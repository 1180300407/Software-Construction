package multidimension;

import java.util.List;
import Timeslot.Timeslot;
import common.CommonPlanningEntry;;

/**
 * 表示可设定时间、进行阻塞的计划项
 * @author 123
 * @param <R>
 *
 */

public interface BlockableEntry<R> {
	/**
	 * @return 获得计划项包含的所有时间节点
	 */
	public List<Timeslot> getTime();
	
	/**
	 * 设定计划项包含的所有时间节点，只可设置一次
	 * @param timeslot 待设置的时间节点
	 */
	public void setTime(List<Timeslot> timeslot);
	
	/**
	 * 阻塞计划项，仅当Running状态可以阻塞成功
	 * @param cpe:待阻塞计划项
	 */
	public void block(CommonPlanningEntry<R> cpe);
}
