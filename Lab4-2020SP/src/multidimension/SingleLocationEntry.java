package multidimension;
import java.util.List;

import Location.*;

/**
 * ��ʾ���趨����λ�õļƻ���趨��ɽ��и���
 * @author 123
 *
 */

public interface SingleLocationEntry {
	
	/**
	 * @return �õ�Ŀǰ�Ѿ��趨��λ��
	 */
	public List<Location> getLocation();
	
	/**
	 *�趨һ������λ�û��߸���λ�� 
	 * @param location
	 */
	public void setLocation(List<Location> location);
	
}
