package multidimension;
import java.util.List;

import Location.Location;

/**
 * ��ʾռ��λ����ʽΪ���+�յ�λ�öԵļƻ���趨�󲻿ɸ���
 * @author 123
 *
 */

public interface TwoLocationEntry {
	
	/**
	 * @return λ�öԵ����
	 */
	public Location getStart();
	
	/**
	 * @return λ�öԵ��յ�
	 */
	public Location getEnd();
	
	/**
	 * @return ���������յ��һ��λ�ö�,�����ǰ���յ��ں󣬴�СΪ2
	 */
	public List<Location> getLocation();
	
	/**
	 * ��λ�öԽ����趨��λ�ö�ֻ���趨һ��
	 * @param start ���λ��
	 * @param end �յ�λ��
	 */
	public void setLocations(Location start,Location end);
	
}
