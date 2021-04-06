package Schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import API.PlanningEntryAPIs;
import API.PlanningEntryAPIsFirstImpl;
import Location.Location;
import Resources.Teacher;
import Timeslot.Timeslot;
import compositeinterface.CourseEntry;
import compositeinterface.CoursePlanningEntry;

/**
 * �α�����ɶԶ����ʦ�����ҡ��γ̽��й����ɱ���
 * @author 123
 *
 */

public class CourseSchedule {
	private List<CourseEntry<Teacher>> courses=new ArrayList<>();
	private List<Teacher> teachers=new ArrayList<Teacher>();
	private List<Location> locations=new ArrayList<Location>();
	//Abstraction function:
	//	AF(courses,teachers,locations)=һ���Կγ�courses�����н�ʦteachers������locations���й����ϵͳ
	//Representation invariant:
	//	true
	//Safety from rep exposure:
	//	��Ա����ȫ��private�ģ����ⷵ��ʱȫ��תΪ���ɱ����ͣ������ڱ�ʾй¶
	
	/**
	 * ����һ���ɹ�����Ľ�ʦ������ʦ�Ѵ��ڲ����ظ����
	 * @param teacher ����ӵĽ�ʦ
	 */
	public void addTeacher(Teacher teacher) {
		if(!teachers.contains(teacher))
			teachers.add(teacher); //TeacherΪ���ɱ��࣬�����ڱ�ʾй¶
	}
	
	/**
	 * ɾ������ָ��ID�Ľ�ʦ������ʦ�����ڲ��������ɾ������,��ʦ���ѷ���γ����޷�ɾ��
	 * @param ID ��ɾ���Ľ�ʦ��ID
	 */
	public void deleteTeacher(String ID) {
		int index=-1;
		for(Teacher teacher:teachers) {
			if(teacher.getId().equals(ID)) {
				index=teachers.indexOf(teacher);
				break;
			}
		}
		if(index==-1) {
			System.out.println("Ŀǰ����Ľ�ʦ�в����ڸ�ID�Ľ�ʦ!");
			return;
		}
		
		boolean flag=false;
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getResource().contains(teachers.get(index))&&(ce.getStateName().equals("Allocated")||ce.getStateName().equals("Running"))) {
				flag=true;
				break;
			}
		}
		if(!flag) {
			teachers.remove(index);
			return;
		}
		
		System.out.println("�ý�ʦ�ѷ���γ�!ɾ��ʧ��!");
	}
	
	/**
	 * ����һ���ɹ�����Ľ��ң��������Ѵ��ڲ����ظ����
	 * @param location ����ӵĽ���
	 */
	public void addLocation(Location location) {
		if(!locations.contains(location))
			locations.add(location);
	}
	
	/**
	 * ɾ������ָ�����ƵĽ��ң������Ҳ����ڲ��������ɾ������,����λ���ѱ������ĳ�γ����޷�ɾ��
	 * @param locationname ��ɾ���Ľ�������
	 */
	public void deleteLocation(String locationname) {
		int index=-1;
		for(Location location:locations) {
			if(location.getName().equals(locationname)) {
				index=locations.indexOf(location);
				break;
			}
		}
		if(index==-1) {
			System.out.println("Ŀǰ�����λ���в����ڸ�λ��!ɾ��ʧ��!");
			return;
		}
		
		boolean flag=false;
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getLocation().contains(locations.get(index))&&!ce.getStateName().equals("Cancelled")&&!ce.getStateName().equals("Ended")) {
				flag=true;
				break;
			}
		}
		if(!flag) {
			locations.remove(index);
			return;
		}
		System.out.println("Ŀǰ��λ���ѱ������ĳ�γ�!ɾ��ʧ��!");
	}
	
	/**
	 * @return �õ����й����µĿγ�
	 */
	public List<CourseEntry<Teacher>> getCourses() {
		return Collections.unmodifiableList(courses);
	}

	/**
	 * @return �õ����й����µĽ�ʦ
	 */
	public List<Teacher> getTeachers() {
		return Collections.unmodifiableList(teachers);
	}

	/**
	 * @return �õ����й����µĽ���
	 */
	public List<Location> getLocations() {
		return Collections.unmodifiableList(locations);
	}

	/**
	 * ����һ���γ̶�����й��������������γ̳���
	 * @param name �����γ���
	 * @param location �γ�ռ�õĽ�������
	 * @param timeslot �γ̵��Ͽ�ʱ����¿�ʱ�乹�ɵ�ʱ���
	 * @return �����ɹ�����true�����򷵻�false
	 */
	public boolean createCourse(String name,String location,Timeslot timeslot) {
		int flag=-1;
		for(Location location2:locations) {//�õ�������λ��
			if(location2.getName().equals(location)) {
				flag=locations.indexOf(location2);
				break;
			}
		}
		if(flag==-1) {
			System.out.println("�����λ����δ�������!");
			return false;
		}
		CourseEntry<Teacher> newcourse=CoursePlanningEntry.CreateCourse(name);
		List<Timeslot> onetimeslot=new ArrayList<Timeslot>();
		onetimeslot.add(timeslot);
		newcourse.setTime(onetimeslot);
		List<Location> oneLocation=new ArrayList<Location>();
		oneLocation.add(locations.get(flag));
		newcourse.setLocation(oneLocation);
		for(CourseEntry<Teacher> cpe:courses) {
			if(cpe.getName().contentEquals(name)) {
				System.out.println("�ÿγ��Ѵ��ڣ������ظ�����");
				return false;
			}
		}
		courses.add(newcourse);
		PlanningEntryAPIs api=new PlanningEntryAPIsFirstImpl();
		boolean flag2=api.checkLocationConflict(courses);
		if(flag2) {
			System.out.println("��ע��,�½��γ��������γ���λ�ó�ͻ!�뿼���Ƿ�ȡ��!");
		}
		return true;
	}
	
	/**
	 * Ϊ�γ̷����ʦ����ʦӦ�����ڹ�������н�ʦ֮��
	 * @param coursename ָ���Ŀγ�����
	 * @param teacher ����Ľ�ʦ�����֤��
	 */
	public void allocateTeacher(String coursename,List<String> teacherIDs) {
		List<Teacher> assignteachers=new ArrayList<Teacher>();
		for(String teacherID:teacherIDs) {
			Teacher teacher=new Teacher(teacherID, "name", true, "professtionalTitle");
			if(!teachers.contains(teacher)) {
				System.out.println("ָ����ʦδ�������!");
				return;
			}
			int index=teachers.indexOf(teacher);
			assignteachers.add(teachers.get(index));
		}
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)) {
				ce.allocateResource(assignteachers);
				PlanningEntryAPIs api=new PlanningEntryAPIsFirstImpl();
				boolean flag2=api.checkResourceExclusiveConflict(courses);
				if(flag2) {
					System.out.println("��ע��,�½��γ��������γ��н�ʦ�����ͻ!�뿼���Ƿ�ȡ��!");
				}
				return;
			}
		}
		
		System.out.println("ָ���γ̻�δ�������޷������ʦ!");
	}
	
	/**
	 * ָ��ĳһ�γ��ϿΣ��γ�Ӧ����
	 * @param coursename ָ���γ���
	 */
	public void startCourse(String coursename) {//�Ƿ���Ҫ����ʱ��???
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)) {
				ce.start();
				return;
			}
		}
		
		System.out.println("ָ���γ̲����ڣ��޷��Ͽ�!");
	}
	
	/**
	 * ��ǰȡ���γ̣��γ�Ӧ����
	 * @param coursename ָ���γ�����
	 */
	public void cancelCourse(String coursename) {
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)) {
				ce.cancel();
				return;
			}
		}
		
		System.out.println("ָ���γ̲����ڣ��޷�ȡ��!");
	}
	
	/**
	 * ���Ŀǰ�γ̵�״̬���γ�Ӧ�Ѿ�������
	 * @param coursename Ҫ�鿴�Ŀγ�
	 */
	public String getCourseState(String coursename) {
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)) { 
				return ce.getStateName();
			}
		}
		
		return "null";
	}
	
	/**
	 * ָ��ĳ�γ��¿Σ��γ�Ӧ����
	 * @param coursename ָ���γ���
	 */
	public void endCourse(String coursename) {
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)) {
				ce.end();
				return;
			}
		}
		
		System.out.println("ָ���γ̲�����!");
	}
	
	/**
	 * �����γ̵ص�,�ص�Ӧ�����Ѿ����������ģ��γ�Ӧ���Ѿ�������δ����
	 * @param coursename ָ���γ�����
	 * @param newlocation �µĽ���
	 */
	public void changeLocation(String coursename,String newlocation) {
		int flag=-1;
		for(Location location2:locations) {//�õ�������λ��
			if(location2.getName().equals(newlocation)) {
				flag=locations.indexOf(location2);
				break;
			}
		}
		if(flag==-1) {
			System.out.println("���ĵ�λ����δ�������!");
			return;
		}
		Location location=locations.get(flag);
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)&&!ce.getStateName().equals("Ended")&&!ce.getStateName().equals("Cancelled")) {//�Ͽ�ǰ���Ͽ��ж��ɸ�������
				List<Location> newLocation=new ArrayList<Location>();
				newLocation.add(location);
				ce.setLocation(newLocation);
				return;
			}
		}
	}
	
	/**
	 * ���ָ����ʦ���ڵ����пγ�
	 * @param teacherID ָ����ʦ��ID 
	 * @return �����ָ����ʦ���ڵĿγ�,�������������пγ̣����򷵻�null
	 */
	public List<CourseEntry<Teacher>> getCoursesofassignTeacher(String teacherID){
		int index=-1;
		for(Teacher teacher:teachers) {//�ҵ�ָ����ʦ
			if(teacher.getId().equals(teacherID)) {
				index=teachers.indexOf(teacher);
			}
		}
		if(index==-1) {
			System.out.println("Ŀǰ����Ľ�ʦ�в����ڸ�ID�Ľ�ʦ!");
			return null;
		}
		Teacher assignteacher=teachers.get(index);
		List<CourseEntry<Teacher>> ces=new ArrayList<CourseEntry<Teacher>>();
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getResource().contains(assignteacher))
				ces.add(ce);
		}
		if(ces.isEmpty())
			return null;
		return ces;
	}
	
	/**
	 * ͨ���γ����Ƶõ��γ�
	 * @param coursename �γ�����
	 * @return ��coursenameΪ���ƵĿγ�,δ�����򷵻�null
	 */
	public CourseEntry<Teacher> getCoursebyName(String coursename){
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().contentEquals(coursename)) {
				return ce;
			}
		}
		return null;
	}
	
	/**
	 * ͨ����ʦ���֤�ŵõ�ָ����ʦ
	 * @param teacherID ��ʦ���֤��
	 * @return ��teacherIDΪ���֤�ŵ�ָ����ʦ,δ�����򷵻�null
	 */
	public Teacher getTeacherbyID(String teacherID) {
		for(Teacher teacher:teachers) {
			if(teacher.getId().equals(teacherID))
				return teacher;
		}
		return null;
	}
	
	/**
	 * ͨ��λ�����Ƶõ���λ��
	 * @param locationName λ������
	 * @return ��locationNameΪ���Ƶ�λ��,�������򷵻�null                            
	 */
	public Location getLocationbyName(String locationName) {
		for(Location location:locations) {
			if(location.getName().contentEquals(locationName))
				return location;
		}
		
		return null;
	}
}
