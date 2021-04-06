package compositeinterface;

import Resources.*;
import State.AllocatedState;
import Timeslot.Timeslot;

import java.util.List;

import Location.Location;
import common.CommonPlanningEntry;
import multiimplement.SingleLocationEntryImpl;
import multiimplement.SingleSortedResourceEntryImpl;
import multiimplement.UnBlockableEntryImpl;
/**
 * CoursePlanningEntry�ľ���ʵ�֣�һ���γ̣��ɱ���
 * @author 123
 *
 * @param <R> ռ����Դ����
 */
public class CourseEntry<R> extends CommonPlanningEntry<Teacher> implements CoursePlanningEntry<Teacher>{
	private SingleLocationEntryImpl sle=new SingleLocationEntryImpl();
	private SingleSortedResourceEntryImpl<Teacher> ssre=new SingleSortedResourceEntryImpl<Teacher>();
	private UnBlockableEntryImpl ube=new UnBlockableEntryImpl();
	
	//Abstraction function:
	//	AF(sle,ssre,ube)=һ�����ϵ�λ�á�����Դ�����������Ŀγ̼ƻ���
	//Representation invariant:
	//	true(��ÿ��ά�ȵĳ�Ա������֤)
	//Safety from rep exposure:
	//	��Ա����ȫ��private�ģ����ɸ�����Ա�����Ķ��ڱ�ʾй¶�İ�ȫ�Խ��б�֤
	
	/**
	 * ���캯��
	 * @param name �γ�����
	 */
	public CourseEntry(String name) {
		super(name);
		super.setType("CoursePlanningEntry");
	}
	
	@Override
	public List<Location> getLocation() {
		return sle.getLocation();
	}
	
	@Override
	public void setLocation(List<Location> location) {
		sle.setLocation(location);
	}
	
	@Override
	public List<Teacher> getResource() {
		return ssre.getResource();
	}
	
	@Override
	public void allocateResource(List<Teacher> resource) {
		if(this.getStateName().equals("Waiting")) {
			ssre.allocateResource(resource);
			super.setState(new AllocatedState());
		}
			
		else
			System.out.println("�γ�Ŀǰ״̬Ϊ:"+this.getStateName()+",�޷������ʦ!");
	}
	
	@Override
	public List<Timeslot> getTime() {
		return ube.getTime();
	}
	
	@Override
	public void setTime(List<Timeslot> timeslot) {
		ube.setTime(timeslot);
	}

	@Override
	public String getName() {
		return super.getName();
	}
}
