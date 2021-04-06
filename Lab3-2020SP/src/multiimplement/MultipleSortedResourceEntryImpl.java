package multiimplement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import multidimension.MultipleSortedResourceEntry;

/**
 * ʵ��MultipleSortedResourceEntry<R>�ӿڣ�ʵ�ֶԶ����������Դ�Ĵ���
 * �������ȫ�����ԣ��ǿɱ���
 * ����ʵ���ò�������setbefore�Ĳ�ֵͬ��ά��resources�Ĳ��ɱ�
 * @author 123
 *
 * @param <R>
 */

public class MultipleSortedResourceEntryImpl<R> implements MultipleSortedResourceEntry<R>{
	private List<R> resources=new ArrayList<>();
	private boolean setbefore=false;
	//	AF(resources,sebefore)=������ԴΪresources�ļƻ����setbeforeΪfalse��֤��δ��������Դ
	//	��setbeforeΪtrue������Դ�Ѿ����䣬�����ٸı�
	//Representation invariant:
	//	��setbeforeΪtrueʱ�����ٸı���Դ
	//Safety from rep exposure:
	//	��Ա������private�ģ�����ֵ�ͳ�Ա������ֵʱ��ת��Ϊ���ɱ�����
	
	@Override
	public List<R> getResource(){
		return Collections.unmodifiableList(resources);
	}
	
	@Override
	public void allocateResource(List<R> rs) {
		if(!setbefore) {
			resources=new ArrayList<>();//����ʽ����
			for(R resource:rs) {
				resources.add(resource);
			}
			resources=Collections.unmodifiableList(resources);
			setbefore=true;
		}
	}
}
