package multidimension;
import java.util.List;

import Location.*;

/**
 * 表示可设定单个位置的计划项，设定后可进行更改
 * @author 123
 *
 */

public interface SingleLocationEntry {
	
	/**
	 * @return 得到目前已经设定的位置
	 */
	public List<Location> getLocation();
	
	/**
	 *设定一个具体位置或者更改位置 
	 * @param location
	 */
	public void setLocation(List<Location> location);
	
}
