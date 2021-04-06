package common;

import java.util.List;

import Location.Location;
import Timeslot.Timeslot;

/**
 * һ���ƻ����ʾһ����ִ�еĻ/��������Ҫ�����ض�����/������ ��Դ�������ض�������λ�ü���ִ�С� 
 * �����ɸ�״̬��δ������Դ���ѷ�����Դ��δ������������������ɡ���ȡ�������� 
 * @author 123
 *
 * @param <R> ����R��ʾռ�õ���Դ����
 */

public interface PlanningEntry<R> {
	
	/**
	 * �����ƻ���,�����ƻ����ѷ�����Դ��������״̬ʱ�������ɹ�
	 */
	public void start();
	
	/**
	 * ȡ���ƻ���,����ʱ�޷�ȡ��,�ѽ����ļƻ����޷�ȡ��
	 */
	public void cancel();
	
	/**
	 * �����ƻ���,ֻ��������ʱ��ȷ����
	 */
	public void end();
	
	/**
	 * ��øüƻ��������
	 * @return �üƻ��������
	 */
	public String getName();
	
	/**
	 * @return ��ȡ��ǰ״̬������,��Waiting��Running��
	 */
	public String getStateName();
	
	/**
	 * @return ��ȡ�ƻ����������ͣ���CoursePlanningEntry��
	 */
	public String getType();
	
	/**
	 * @return ��ȡ�ƻ���������Դ
	 */
	public List<R> getResource();
	
	/**
	 * Ϊ�ƻ��������Դ,ֻ�ɷ���һ�Σ���������޸�
	 * @param resource ��������Դ
	 */
	public void allocateResource(List<R> resource);
	
	/** 
	 * @return ��ȡ�ƻ���ռ�õ�����λ��
	 */
	public List<Location> getLocation(); 
	
	/**
	 * @return ��ȡ�ƻ���ռ�õ�����ʱ���
	 */
	public List<Timeslot> getTime();
}
