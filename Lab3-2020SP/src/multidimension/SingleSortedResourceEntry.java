package multidimension;

import java.util.List;

/**
 * ��ʾռ�õ�����������Դ�ļƻ���ɶԼƻ��������Դ����
 * @author 123
 *
 * @param <R> ����R��ʾ�������Դ������
 */

public interface SingleSortedResourceEntry<R> {
	
	/**
	 * @return �ѷ���ĵ�����Դ,��Դ��Ϊ1
	 */
	public List<R> getResource(); 
	
	/**
	 * ����Դ���з��䣬ֻ�ɷ���һ��
	 * @param resource ��������Դ
	 */
	public void allocateResource(List<R> resource);
}
