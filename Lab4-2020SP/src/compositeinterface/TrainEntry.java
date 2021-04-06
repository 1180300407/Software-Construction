package compositeinterface;

import java.util.List;

import Exceptions.PlanEntryStateNotMatchException;
import Location.Location;
import common.CommonPlanningEntry;
import multiimplement.BlockableEntryImpl;
import multiimplement.MultipleLocationEntryImpl;
import multiimplement.MultipleSortedResourceEntryImpl;
import Resources.Carriage;
import State.AllocatedState;
import Timeslot.Timeslot;
/**
 * TrainPlanningEntry��ʵ�֣�һ�˸������ɱ���
 * @author 123
 *
 * @param <R>
 */
public class TrainEntry<R> extends CommonPlanningEntry<Carriage> implements TrainPlanningEntry<Carriage>{
	private MultipleLocationEntryImpl mle=new MultipleLocationEntryImpl();
	private MultipleSortedResourceEntryImpl<Carriage> msre=new MultipleSortedResourceEntryImpl<Carriage>();
	private BlockableEntryImpl<Carriage> be=new BlockableEntryImpl<Carriage>();
	//Abstraction function:
	//	AF(name,tle,ssrle)=һ�����϶�λ�á�����Դ���������ĸ����ƻ���
	//Representation invariant:
	//	true(��ÿ��ά�ȵĳ�Ա������֤)
	//Safety from rep exposure:
	//	��Ա����ȫ��private�ģ����ɸ�����Ա�����Ķ��ڱ�ʾй¶�İ�ȫ�Խ��б�֤
	
	/**
	 * ���캯��
	 * @param name �г�����
	 */
	public TrainEntry(String name) {
		super(name);
		super.setType("TrainPlanningEntry");
	}
	
	@Override
	public List<Location> getLocation() {
		return mle.getLocation();
	}
	
	@Override
	public void setLocations(List<Location> locs) {
		mle.setLocations(locs);
	}
	
	@Override
	public List<Carriage> getResource() {
		return msre.getResource();
	}
	
	@Override
	public void allocateResource(List<Carriage> resource) {
		if(this.getStateName().equals("Waiting")) {
			msre.allocateResource(resource);
			super.setState(new AllocatedState());
		}
			
		else 
			System.out.println("�г�Ŀǰ״̬Ϊ:"+this.getStateName()+",�޷����䳵��!");
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
	public void block(CommonPlanningEntry<Carriage> cpe) {
		try {
			be.block(this);
		} catch (PlanEntryStateNotMatchException e) {
			System.out.println(e.getErrorMessage());
		}
	}
}
