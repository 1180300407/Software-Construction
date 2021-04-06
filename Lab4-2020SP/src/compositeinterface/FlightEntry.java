package compositeinterface;

import java.util.List;

import Location.Location;
import common.CommonPlanningEntry;
import multiimplement.SingleSortedResourceEntryImpl;
import multiimplement.TwoLocationEntryImpl;
import multiimplement.UnBlockableEntryImpl;
import Resources.Plane;
import State.AllocatedState;
import Timeslot.Timeslot;;
/**
 * FlightPlanningEntry�ľ���ʵ�֣�һ�����࣬�ɱ���
 * @author 123
 *
 * @param <R> ռ����Դ����
 */
public class FlightEntry<R> extends CommonPlanningEntry<Plane> implements FlightPlanningEntry<Plane>{
	private TwoLocationEntryImpl tle=new TwoLocationEntryImpl();
	private SingleSortedResourceEntryImpl<Plane> ssre=new SingleSortedResourceEntryImpl<Plane>();
	private UnBlockableEntryImpl ube=new UnBlockableEntryImpl();
	//Abstraction function:
	//	AF(name,tle,ssrle)=һ����������λ�á�����Դ�����������ĺ���ƻ���
	//Representation invariant:
	//	true(��ÿ��ά�ȵĳ�Ա������֤)
	//Safety from rep exposure:
	//	��Ա����ȫ��private�ģ����ɸ�����Ա�����Ķ��ڱ�ʾй¶�İ�ȫ�Խ��б�֤
	
	/**
	 * ���캯��
	 * @param name ��������
	 */
	public FlightEntry(String name) {
		super(name);
		super.setType("FlightPlanningEntry");
	}
	
	@Override
	public Location getStart() {
		return tle.getStart();
	}
	
	@Override
	public Location getEnd() {
		return tle.getEnd();
	}
	
	@Override
	public List<Location> getLocation(){
		return tle.getLocation();
	}
	
	@Override
	public void setLocations(Location start,Location end) {
		tle.setLocations(start, end);
	}
	
	@Override
	public List<Plane> getResource() {
		return ssre.getResource();
	}
	
	//���״̬�ж�
	@Override
	public void allocateResource(List<Plane> resource) {
		if(this.getStateName().equals("Waiting")) {
			ssre.allocateResource(resource);
			super.setState(new AllocatedState());
		}
			
		else 
			System.out.println("����Ŀǰ״̬Ϊ:"+this.getStateName()+",�޷�����ɻ�!");
	}
	
	@Override
	public List<Timeslot> getTime() {
		return ube.getTime();
	}
	
	@Override
	public void setTime(List<Timeslot> timeslot) {
		ube.setTime(timeslot);
	}
	
}
