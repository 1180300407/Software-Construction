package compositeinterface;

import java.util.List;

import Location.Location;
import common.CommonPlanningEntry;
import multiimplement.BlockableEntryImpl;
import multiimplement.MultipleLocationEntryImpl;
import multiimplement.SingleSortedResourceEntryImpl;
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
	private MultipleLocationEntryImpl mle=new MultipleLocationEntryImpl();
	private SingleSortedResourceEntryImpl<Plane> ssre=new SingleSortedResourceEntryImpl<Plane>();
	private BlockableEntryImpl<Plane> be=new BlockableEntryImpl<Plane>();
	//Abstraction function:
	//	AF(name,tle,ssrle)=һ����������λ�á�����Դ�����������ĺ���ƻ���
	//Representation invariant:
	//	λ�ø���ֻ��Ϊ2��3
	//Safety from rep exposure:
	//	��Ա����ȫ��private�ģ����ɸ�����Ա�����Ķ��ڱ�ʾй¶�İ�ȫ�Խ��б�֤
	private void checkRep() {
		assert mle.getLocation().size()==2||mle.getLocation().size()==3;
	}
	/**
	 * ���캯��
	 * @param name ��������
	 */
	public FlightEntry(String name) {
		super(name);
		super.setType("FlightPlanningEntry");
	}
	
	@Override
	public List<Location> getLocation(){
		return mle.getLocation();
	}
	
	@Override
	public void setLocations(List<Location> locs) {
		if(locs.size()==2||locs.size()==3) {
			mle.setLocations(locs);
			checkRep();
		}	
		else 
			System.out.println("�����λ�ø���������Ҫ��!����λ��ʧ��!");
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
		return be.getTime();
	}
	
	@Override
	public void setTime(List<Timeslot> timeslot) {
		be.setTime(timeslot);
	}
	
	@Override
	public void block(CommonPlanningEntry<Plane> cpe) {
		be.block(this);
	}
}
