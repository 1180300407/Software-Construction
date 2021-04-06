package compositeinterface;

import multidimension.BlockableEntry;
import multidimension.MultipleLocationEntry;
import multidimension.MultipleSortedResourceEntry;


import Resources.Carriage;
import common.CommonPlanningEntry;
/**
 * һ�������ƻ���
 * @author 123
 *
 * @param <R> ռ����Դ����
 */
public interface TrainPlanningEntry<R extends Carriage> extends MultipleLocationEntry,MultipleSortedResourceEntry<R>,BlockableEntry<R>{
	/**
	 * ��̬����,����һ���������μƻ����ʵ��
	 * @param name �������ε�����
	 * @return һ���������μƻ����ʵ��
	 */
	public static <R> TrainEntry<R> CreateTrain(String name){
		TrainEntry<R> te=new TrainEntry<R>(name);
		return te;
	}
	
	@Override
	public void block(CommonPlanningEntry<R> cpe);
	
	/**
	 * ��������
	 */
	public void start();
	
	/**
	 * �����ƻ���
	 */
	public void end();
	
	/**
	 * @return ��ø������ε�����
	 */
	public String getName();
	
	/**
	 * ȡ������,���ѷ��䳵����Դ���޷�ȡ��
	 */
	public void cancel();
	
	/**
	 * @return ��ø���Ŀǰ��״̬������
	 */
	public String getStateName();
}
