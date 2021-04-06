package compositeinterface;

import Resources.Plane;
import multidimension.*;
/**
 * һ������ƻ���
 * @author 123
 *
 * @param <R> ռ����Դ����
 */
public interface FlightPlanningEntry<R extends Plane> extends TwoLocationEntry,SingleSortedResourceEntry<R>,UnBlockableEntry{
	/**
	 * ��̬����,����һ������ƻ����ʵ��,��ʼ״̬ΪWaiting
	 * @param <R> ����ռ�õ���Դ,Ӧ��Plane���������
	 * @param name ��������
	 * @return һ������ƻ����ʵ��
	 */
	public static <R> FlightEntry<R> CreateFlight(String name){
		FlightEntry<R> fe=new FlightEntry<R>(name);
		return fe;
	}
	
	/**
	 * @return ��ú�������
	 */
	public String getName();
	
	/**
	 * �������
	 */
	public void start();
	
	/**
	 * ��ǰȡ������
	 */
	public void cancel();
	
	/**
	 * ���ཱུ��
	 */
	public void end();
	
	/**
	 * @return ��ú���״̬������
	 */
	public String getStateName();
}
