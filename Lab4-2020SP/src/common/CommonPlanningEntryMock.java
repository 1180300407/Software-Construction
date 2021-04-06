package common;

import java.util.ArrayList;
import java.util.List;

import Location.Location;
import State.AllocatedState;
import Timeslot.Timeslot;

/**
 * Ϊ�˲���CommonPlanningEntry��PlanningEntry����ĺ�����ʵ�ֵ�ר���ڲ��Ե�����
 * @author 123
 *
 */

public class CommonPlanningEntryMock extends CommonPlanningEntry<String>{
	private boolean hassetresource=false;
	private List<String> resources=new ArrayList<String>();
	private List<Location> locations=new ArrayList<>();
	private List<Timeslot> timeslots=new ArrayList<Timeslot>();
	//Abstraction function:
	//	AF(resources,locations,timeslots)=һ��ռ����ԴΪresources,ռ��λ��Ϊlocations
	//	ռ��ʱ��Ϊtimeslots�ļƻ��hassetresource��ʶ�Ƿ�������Դ
	//Representation invariant:
	//	true
	//Safety from rep exposure:
	//	��Ա����ȫ��private��
	
	public CommonPlanningEntryMock(String name) {
		super(name);
	}
	
	@Override
	public void allocateResource(List<String> resource) {
		if(hassetresource)
			return;
		for(String s:resource) {
			resources.add(s);
			hassetresource=true;
			super.setState(new AllocatedState());
		}
	}
	
	@Override
	public List<String> getResource(){
		return resources;
	}
	
	@Override
	public List<Location> getLocation(){
		locations.add(null);
		return locations;
	}
	
	@Override
	public List<Timeslot> getTime(){
		timeslots.add(null);
		return timeslots;
	}
}
