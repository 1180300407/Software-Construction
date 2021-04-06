package multiimplement;

import java.util.ArrayList;
import java.util.List;

import Location.Location;
import multidimension.TwoLocationEntry;

/**
 * ʵ��TwoLocationEntry�ӿڣ�ʵ�ֶ�һ��λ�öԵĴ����ɱ���
 * @author 123
 *
 */

public class TwoLocationEntryImpl implements TwoLocationEntry{
	private Location start;//���
	private Location end;//�յ�
	private boolean setbefore=false;//�Ƿ��Ѿ��趨��λ��
	//Abstraction function:
	//	AF(start,end,setbefore)=�趨��λ�ã���setbeforeΪfalse��֤��δ���趨λ��
	//	��setbeforeΪtrue����λ���Ѿ��趨���������޸�
	//Representation invariant:
	//	��setbeforeΪtrueʱ�����ٸı�start��end
	//	�����յ㲻Ӧ��ͬ
	//Safety from rep exposure:
	//	��Ա������private�ģ�Ϊ���ɱ����ͣ�ʹ�÷���ʽ�����������ڱ�ʾй¶
	private void checkRep() {
		assert !start.equals(end);

	}
	
	@Override
	public List<Location> getLocation() {
		List<Location> locations=new ArrayList<Location>();
		locations.add(start);
		locations.add(end);
		return locations;
	}
	
	@Override
	public Location getStart() {
		Location copyLocation=new Location(start.getLongitude(), start.getLatitude(), start.getName(), start.isshareable());
		return copyLocation;
	}
	
	@Override
	public Location getEnd() {
		Location copyLocation=new Location(end.getLongitude(), end.getLatitude(), end.getName(), end.isshareable());
		return copyLocation;
	}
	
	@Override
	public void setLocations(Location start,Location end) {
		if(setbefore) {
			System.out.println("λ���Ѿ��趨���������ٽ����޸ģ�");
			return;
		}
		Location copyLocation1=new Location(start.getLongitude(), start.getLatitude(), start.getName(), start.isshareable());
		Location copyLocation2=new Location(end.getLongitude(), end.getLatitude(), end.getName(), end.isshareable());
		this.start=copyLocation1;
		this.end=copyLocation2;
		checkRep();
		this.setbefore=true;
	}
}
