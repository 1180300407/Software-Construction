package multidimension;

import java.util.List;

import Location.Location;

/**
 * ��ʾ���趨���λ�õļƻ���趨�󲻿ɸ���
 * @author 123
 *
 */

public interface MultipleLocationEntry {
	
	/**
	 * @return �ƻ����������λ��
	 */
	public List<Location> getLocation();
	
	/**
	 * �趨����λ�ã����ٰ�����㣬�յ�����λ�ã�ֻ���趨һ��
	 * @param locs ���趨��λ�ã�λ�ø���Ӧ��>=2�����ٰ��������յ�
	 */
	public void setLocations(List<Location> locs);
}
