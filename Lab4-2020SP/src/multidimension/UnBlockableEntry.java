package multidimension;

import java.util.List;

import Timeslot.Timeslot;;

/**
 * 表示时间形式为起止时间+终止时间的时间对的，不可阻塞的计划项
 * @author 123
 *
 */

public interface UnBlockableEntry {
	/**
	 * @return 获得计划项中已设定的时间
	 */
	public List<Timeslot> getTime();
	
	/**
	 * 设定计划项时间，只能设置一次,只包含一个时间对
	 * @param timeslot 待设定时间
	 */
	public void setTime(List<Timeslot> timeslot);
}
