package multidimension;

import java.util.List;

/**
 * ��ʾռ�ö��������Դ�ļƻ���ɶԼƻ��������Դ����
 * @author 123
 *
 * @param <R> ռ����Դ������
 */

public interface MultipleSortedResourceEntry<R> {
	/**
	 * @return �ѷ������Դ
	 */
	public List<R> getResource(); 
	
	/**
	 * ����Դ���з��䣬ֻ�ɷ���һ��,��Դ�����˳��Ӧ���Ѿ�ȷ��
	 * @param rs ��������Դ
	 */
	public void allocateResource(List<R> rs);
}
