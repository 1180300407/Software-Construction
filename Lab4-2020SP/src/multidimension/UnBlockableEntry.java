package multidimension;

import java.util.List;

import Timeslot.Timeslot;;

/**
 * ��ʾʱ����ʽΪ��ֹʱ��+��ֹʱ���ʱ��Եģ����������ļƻ���
 * @author 123
 *
 */

public interface UnBlockableEntry {
	/**
	 * @return ��üƻ��������趨��ʱ��
	 */
	public List<Timeslot> getTime();
	
	/**
	 * �趨�ƻ���ʱ�䣬ֻ������һ��,ֻ����һ��ʱ���
	 * @param timeslot ���趨ʱ��
	 */
	public void setTime(List<Timeslot> timeslot);
}
