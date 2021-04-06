package Schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import API.PlanningEntryAPIs;
import API.PlanningEntryAPIsFirstImpl;
import Exceptions.LocationConflictException;
import Exceptions.LocationNotFoundException;
import Exceptions.PlanEntryNotCreateException;
import Exceptions.PlanEntryOccupyLocationException;
import Exceptions.PlanEntryOccupyResourceException;
import Exceptions.PlanEntryStateNotMatchException;
import Exceptions.ResourceConflictException;
import Exceptions.ResourceNotFoundException;
import Exceptions.SameLabelException;
import Location.Location;
import LogFile.MyFormatter;
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
	private static Logger myLogger=Logger.getLogger("CourseScheduleLog");
	//Abstraction function:
	//	AF(courses,teachers,locations)=һ���Կγ�courses�����н�ʦteachers������locations���й����ϵͳ
	//Representation invariant:
	//	����Ŀγ̡���ʦ�������ж���Ӧ�����ظ�Ԫ��
	//Safety from rep exposure:
	//	��Ա����ȫ��private�ģ����ⷵ��ʱȫ��תΪ���ɱ����ͣ������ڱ�ʾй¶
	
	private void checkRep() {
		Set<CourseEntry<Teacher>> courseSet=new HashSet<>(courses);
		Set<Teacher> teacherSet=new HashSet<Teacher>(teachers);
		Set<Location> locationSet=new HashSet<Location>(locations);
		assert locationSet.size()==locations.size();
		assert teacherSet.size()==teachers.size();
		assert courseSet.size()==courses.size();
		//��־��¼
		myLogger.setLevel(Level.INFO);
		myLogger.setUseParentHandlers(false);
		//д���ļ�
		FileHandler handler;
		try {
			handler = new FileHandler("src/LogFile/CourseScheduleLog.log");
			handler.setFormatter(new MyFormatter());//���ù̶���ʽ
			handler.setLevel(Level.INFO);
			myLogger.addHandler(handler);
			myLogger.info("���в��������");
			handler.close();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * ����һ���ɹ�����Ľ�ʦ������ʦ�Ѵ��ڲ����ظ����
	 * @param teacher ����ӵĽ�ʦ
	 */
	public void addTeacher(Teacher teacher) {
		if(!teachers.contains(teacher))
			teachers.add(teacher); //TeacherΪ���ɱ��࣬�����ڱ�ʾй¶
		checkRep();
	}
	
	/**
	 * ɾ������ָ��ID�Ľ�ʦ��ɾ���Ľ�ʦӦ���Ѿ��������֮��,��δ����γ�
	 * @param ID ��ɾ���Ľ�ʦ��ID
	 * @throws ResourceNotFoundException Ҫɾ���Ľ�ʦ������ 
	 * @throws PlanEntryOccupyResourceException Ҫɾ���Ľ�ʦ�ѷ���γ�
	 */
	public void deleteTeacher(String ID) throws ResourceNotFoundException, PlanEntryOccupyResourceException {
		int index=-1;
		for(Teacher teacher:teachers) {
			if(teacher.getId().equals(ID)) {
				index=teachers.indexOf(teacher);
				break;
			}
		}
		if(index==-1) {
			throw new ResourceNotFoundException(ID);
		}
		
		boolean flag=false;
		CourseEntry<Teacher> courseEntry=null;
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getResource().contains(teachers.get(index))&&(ce.getStateName().equals("Allocated")||ce.getStateName().equals("Running"))) {
				flag=true;
				courseEntry=ce;
				break;
			}
		}
		if(!flag) {
			teachers.remove(index);
			return;
		}
		else
		 throw new PlanEntryOccupyResourceException(courseEntry.getName());
	}
	
	/**
	 * ����һ���ɹ�����Ľ��ң��������Ѵ��ڲ����ظ����
	 * @param location ����ӵĽ���
	 */
	public void addLocation(Location location) {
		if(!locations.contains(location))
			locations.add(location);
		checkRep();
	}
	
	/**
	 * ɾ������ָ�����ƵĽ��ң�����Ӧ���Ѵ���,��δ��������κογ�
	 * @param locationname ��ɾ���Ľ�������
	 * @throws LocationNotFoundException Ҫɾ���Ľ��Ҳ�����
	 * @throws PlanEntryOccupyLocationException ���ұ�ռ��
	 */
	public void deleteLocation(String locationname) throws LocationNotFoundException, PlanEntryOccupyLocationException {
		int index=-1;
		for(Location location:locations) {
			if(location.getName().equals(locationname)) {
				index=locations.indexOf(location);
				break;
			}
		}
		if(index==-1) {
			throw new LocationNotFoundException(locationname);
		}
		
		boolean flag=false;
		CourseEntry<Teacher> courseEntry=null;
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getLocation().contains(locations.get(index))&&!ce.getStateName().equals("Cancelled")&&!ce.getStateName().equals("Ended")) {
				flag=true;
				courseEntry=ce;
				break;
			}
		}
		if(!flag) {
			locations.remove(index);
			return;
		}
		else 
		 throw new PlanEntryOccupyLocationException(courseEntry.getName());
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
	 * @return ��������ɹ�����true�����򷵻�false
	 * @throws LocationNotFoundException ����δ����
	 * @throws SameLabelException �����γ�
	 * @throws LocationConflictException �����γ̻����λ�ó�ͻ
	 */
	public boolean createCourse(String name,String location,Timeslot timeslot) throws LocationNotFoundException, SameLabelException, LocationConflictException {
		int flag=-1;
		for(Location location2:locations) {//�õ�������λ��
			if(location2.getName().equals(location)) {
				flag=locations.indexOf(location2);
				break;
			}
		}
		if(flag==-1) {
			throw new LocationNotFoundException(location);
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
				throw new SameLabelException(name);
			}
		}
		CourseEntry<Teacher> courseEntry=new CourseEntry<Teacher>("");
		courseEntry.setLocation(oneLocation);
		List<CourseEntry<Teacher>> courseEntries=new ArrayList<>();
		courseEntries.add(courseEntry);
		courseEntries.addAll(courses);
		PlanningEntryAPIs api=new PlanningEntryAPIsFirstImpl();
		boolean flag2=api.checkLocationConflict(courseEntries);
		if(flag2) {
			throw new LocationConflictException();
		}
		courses.add(newcourse);
		checkRep();
		return true;
	}
	
	/**
	 * Ϊ�γ̷����ʦ����ʦӦ�����ڹ�������н�ʦ֮��
	 * @param coursename ָ���Ŀγ�����
	 * @param teacher ����Ľ�ʦ�����֤��
	 * @throws ResourceNotFoundException ��ʦ��δ�������
	 * @throws PlanEntryNotCreateException �γ���δ����
	 * @throws ResourceConflictException �����ʦ�������ͻ
	 */
	public void allocateTeacher(String coursename,String teacherID) throws ResourceNotFoundException, PlanEntryNotCreateException, ResourceConflictException {
		int index=-1;
		for(Teacher teacher:teachers) {
			if(teacher.getId().equals(teacherID))
			{
				index=teachers.indexOf(teacher);
			}
		}
		if(index==-1) {
			throw new ResourceNotFoundException(teacherID);
		}
		Teacher teacher=teachers.get(index);
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)) {
				List<Teacher> oneTeacher=new ArrayList<Teacher>();
				oneTeacher.add(teacher);
				CourseEntry<Teacher> courseEntry=new CourseEntry<Teacher>("null");//�½�һ���γ̣�Ϊ������������õ��Ŀγ�һ�������ԣ��۲��Ƿ�������ͻ
				courseEntry.setLocation(ce.getLocation());
				courseEntry.setTime(ce.getTime());
				courseEntry.allocateResource(oneTeacher);
				List<CourseEntry<Teacher>> courseEntries=new ArrayList<>();//������courses�У�ֻ��һ�����Ʒ������Ƿ�ᷢ����ͻ������������������ĳ�ͻ
				courseEntries.add(courseEntry);
				courseEntries.addAll(courses);
				PlanningEntryAPIs api=new PlanningEntryAPIsFirstImpl();
				boolean flag2=api.checkResourceExclusiveConflict(courseEntries);
				if(flag2) {
					throw new ResourceConflictException(teacherID);
				}
				ce.allocateResource(oneTeacher);
				return;
			}
		}
		
		throw new PlanEntryNotCreateException(coursename);
	}
	
	/**
	 * ָ��ĳһ�γ��ϿΣ��γ�Ӧ����
	 * @param coursename ָ���γ���
	 * @throws PlanEntryNotCreateException �γ���δ����
	 */
	public void startCourse(String coursename) throws PlanEntryNotCreateException {
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)) {
				ce.start();
				return;
			}
		}
		
		throw new PlanEntryNotCreateException(coursename);
	}
	
	/**
	 * ��ǰȡ���γ̣��γ�Ӧ����
	 * @param coursename ָ���γ�����
	 * @throws PlanEntryNotCreateException �γ���δ����
	 */
	public void cancelCourse(String coursename) throws PlanEntryNotCreateException {
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)) {
				ce.cancel();
				return;
			}
		}
		
		throw new PlanEntryNotCreateException(coursename);
	}
	
	/**
	 * ���Ŀǰ�γ̵�״̬���γ�Ӧ�Ѿ�������
	 * @param coursename Ҫ�鿴�Ŀγ�
	 * @return �γ̵�ǰ״̬
	 * @throws PlanEntryNotCreateException �γ�δ����
	 */
	public String getCourseState(String coursename) throws PlanEntryNotCreateException {
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)) { 
				return ce.getStateName();
			}
		}
		
		throw new PlanEntryNotCreateException(coursename);
	}
	
	/**
	 * ָ��ĳ�γ��¿Σ��γ�Ӧ����
	 * @param coursename ָ���γ���
	 * @throws PlanEntryNotCreateException �γ���δ����
	 */
	public void endCourse(String coursename) throws PlanEntryNotCreateException {
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)) {
				ce.end();
				return;
			}
		}
		
		throw new PlanEntryNotCreateException(coursename);
	}
	
	/**
	 * �����γ̵ص�,�ص�Ӧ�����Ѿ����������ģ��γ�Ӧ���Ѿ�������δ����
	 * @param coursename ָ���γ�����
	 * @param newlocation �µĽ���
	 * @throws LocationNotFoundException λ����δ����
	 * @throws PlanEntryNotCreateException �γ���δ����
	 * @throws PlanEntryStateNotMatchException �γ���ȡ����������޷�����λ��s
	 */
	public void changeLocation(String coursename,String newlocation) throws LocationNotFoundException, PlanEntryNotCreateException, PlanEntryStateNotMatchException {
		int flag=-1;
		for(Location location2:locations) {//�õ�������λ��
			if(location2.getName().equals(newlocation)) {
				flag=locations.indexOf(location2);
				break;
			}
		}
		if(flag==-1) {
			throw new LocationNotFoundException(newlocation);
		}
		Location location=locations.get(flag);
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)) {
				if(!ce.getStateName().equals("Ended")&&!ce.getStateName().equals("Cancelled")) {//�Ͽ�ǰ���Ͽ��ж��ɸ�������
					List<Location> newLocation=new ArrayList<Location>();
					newLocation.add(location);
					ce.setLocation(newLocation);
					return;
				}
				throw new PlanEntryStateNotMatchException(coursename);
			}
		}
		throw new PlanEntryNotCreateException(coursename);
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
	 * @return ��locationNameΪ���Ƶ�λ��                            
	 */
	public Location getLocationbyName(String locationName) {
		for(Location location:locations) {
			if(location.getName().contentEquals(locationName))
				return location;
		}
		
		return null;
	}
}
