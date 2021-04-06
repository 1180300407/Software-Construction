package compositeinterface;

import java.util.List;

import Resources.Teacher;
import multidimension.MultipleSortedResourceEntry;
import multidimension.SingleLocationEntry;
import multidimension.UnBlockableEntry;
/**
 * һ���γ̼ƻ���
 * @author 123
 *
 * @param <R> ռ����Դ����
 */
public interface CoursePlanningEntry<R extends Teacher> extends SingleLocationEntry,MultipleSortedResourceEntry<R>,UnBlockableEntry{
	/**
	 * ��̬����,����һ���γ̼ƻ����ʵ��,��ʼ״̬ΪWaiting
	 * @param <R> �γ�ռ�õ���Դ,Ӧ��Teacher���������
	 * @param name �γ�����
	 * @return �γ̼ƻ����ʵ��
	 */
	public static <R> CourseEntry<R> CreateCourse(String name) {
		CourseEntry<R> ce=new CourseEntry<R>(name);
		return ce;
	}
	
	/**
	 * �γ��Ͽ�,��ʦ�����ſ��������Ͽ�
	 */
	public void start();
	
	/**
	 * �γ���ǰȡ��,�γ�δ��ʼ�ſ���ȡ���ɹ�
	 */
	public void cancel();
	
	/**
	 * �����γ�,���¿�,�γ��ѿ�ʼ�ſ����¿�
	 */
	public void end();

	/**
	 * @return Ŀǰ�γ�����״̬������
	 */
	public String getStateName();
	
	/**
	 * @return �γ�����
	 */
	public String getName();
	
	/**
	 * Ϊ�γ̷����ʦ,��ʦ�Ĵ���Ӧ���Ѿ�ȷ��
	 */
	@Override
	public void allocateResource(List<R> resource);
	
}
