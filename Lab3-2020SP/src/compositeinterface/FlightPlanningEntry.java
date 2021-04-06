package compositeinterface;

import java.util.List;

import Location.Location;
import Resources.Plane;
import common.CommonPlanningEntry;
import multidimension.*;
/**
 * һ������ƻ���
 * @author 123
 *
 * @param <R> ռ����Դ����
 */
public interface FlightPlanningEntry<R extends Plane> extends MultipleLocationEntry,SingleSortedResourceEntry<R>,BlockableEntry<R>{
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
	
	/**
	 * λ����ֻӦ����2��3
	 */
	@Override
	public void setLocations(List<Location> locs);
	
	@Override
	public void block(CommonPlanningEntry<R> cpe);
}
