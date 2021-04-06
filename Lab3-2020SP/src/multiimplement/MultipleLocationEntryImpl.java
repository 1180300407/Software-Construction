package multiimplement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Location.Location;
import multidimension.MultipleLocationEntry;

/**
 * MultipleLocationEntry�ӿڵ�ʵ�֣��Զ��λ�ý��д����ɱ���
 * @author 123
 *
 */

public class MultipleLocationEntryImpl implements MultipleLocationEntry{
	private List<Location> locations=new ArrayList<Location>();
	private boolean setbefore=false;
	//Abstraction function:
	//	AF(locations,sebefore)=�趨��λ�ã���setbeforeΪfalse��֤��δ���趨λ��
	//	��setbeforeΪtrue����λ���Ѿ��趨���������޸�
	//Representation invariant:
	//	��setbeforeΪtrueʱ�����ٸı���һλ��
	//	locations������Ӧ���ڵ���2�����ٰ��������յ�
	//Safety from rep exposure:
	//	��Ա������private�ģ�Ϊ���ɱ����ͣ�����ֵ�ͳ�Ա������ֵʱ��ת��Ϊ���ɱ����ͣ������ڱ�ʾй¶
	
	private void checkRep() {
		assert locations.size()>=2;
	}
	
	@Override
	public List<Location> getLocation(){
		return Collections.unmodifiableList(locations);
	}
	
	public boolean getsetbefore() {
		return setbefore;
	}
	
	@Override
	public void setLocations(List<Location> locs) {
		if(setbefore) {
			System.out.println("λ���Ѿ��趨���������ٽ����޸ģ�");
			return;
		}
		
		locations=new ArrayList<Location>();
		for(Location location:locs) {
			locations.add(location);
		}
		checkRep();
		setbefore=true;
	}
}
