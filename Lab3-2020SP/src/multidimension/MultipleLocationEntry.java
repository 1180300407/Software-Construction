package multidimension;

import java.util.List;

import Location.Location;

/**
 * 表示可设定多个位置的计划项，设定后不可更改
 * @author 123
 *
 */

public interface MultipleLocationEntry {
	
	/**
	 * @return 计划项经过的所有位置
	 */
	public List<Location> getLocation();
	
	/**
	 * 设定所有位置，至少包含起点，终点两个位置，只可设定一次
	 * @param locs 待设定的位置，位置个数应该>=2，至少包含起点和终点
	 */
	public void setLocations(List<Location> locs);
}
