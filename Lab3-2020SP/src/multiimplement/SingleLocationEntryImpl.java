package multiimplement;

import java.util.ArrayList;
import java.util.List;

import Location.Location;
import multidimension.SingleLocationEntry;

/**
 * ʵ��SingleLocationEntry�ӿ�
 * �ɸ���λ�ã��ǿɱ���
 * @author 123
 *
 */

public class SingleLocationEntryImpl implements SingleLocationEntry{
	private List<Location> location=new ArrayList<Location>();
	//Abstraction function:
	//	AF(location)=�趨��λ��
	//Representation invariant:
	//	location�Ĵ�СΪ1
	//Safety from rep exposure:
	//	��Ա������private�ģ�Ϊ���ɱ����ͣ�ʹ�÷���ʽ�����������ڱ�ʾй¶
	
	private void checkRep() {
		assert location.size()==1;
	}
	
	@Override
	public void setLocation(List<Location> location) {
		//����ʽ����
		if(location.size()!=1)
			return;
		this.location=new ArrayList<Location>();
		this.location.add(location.get(0));
		checkRep();
	}

	@Override
	public List<Location> getLocation() {
		List<Location> location=new ArrayList<Location>();
		location.addAll(this.location);
		return location;
	}
}
