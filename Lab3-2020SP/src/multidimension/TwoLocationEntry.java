package multidimension;
import java.util.List;

import Location.Location;

/**
 * 表示占用位置形式为起点+终点位置对的计划项，设定后不可更改
 * @author 123
 *
 */

public interface TwoLocationEntry {
	
	/**
	 * @return 位置对的起点
	 */
	public Location getStart();
	
	/**
	 * @return 位置对的终点
	 */
	public Location getEnd();
	
	/**
	 * @return 包含起点和终点的一组位置对,起点在前，终点在后，大小为2
	 */
	public List<Location> getLocation();
	
	/**
	 * 对位置对进行设定，位置对只可设定一次
	 * @param start 起点位置
	 * @param end 终点位置
	 */
	public void setLocations(Location start,Location end);
	
}
