package multidimension;

import java.util.List;
import Timeslot.Timeslot;
import common.CommonPlanningEntry;;

/**
 * ��ʾ���趨ʱ�䡢���������ļƻ���
 * @author 123
 * @param <R>
 *
 */

public interface BlockableEntry<R> {
	/**
	 * @return ��üƻ������������ʱ��ڵ�
	 */
	public List<Timeslot> getTime();
	
	/**
	 * �趨�ƻ������������ʱ��ڵ㣬ֻ������һ��
	 * @param timeslot �����õ�ʱ��ڵ�
	 */
	public void setTime(List<Timeslot> timeslot);
	
	/**
	 * �����ƻ������Running״̬���������ɹ�
	 * @param cpe:�������ƻ���
	 */
	public void block(CommonPlanningEntry<R> cpe);
}
