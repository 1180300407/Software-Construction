package Schedule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import API.PlanningEntryAPIs;
import API.PlanningEntryAPIsFirstImpl;
import Location.Location;
import compositeinterface.*;
import Resources.Plane;
import Timeslot.Timeslot;

/**
 * ��������ɶԶ�����������࣬�ɻ����й����ɱ���
 * @author 123
 *
 */

public class FlightSchedule {
	private List<FlightEntry<Plane>> flights=new ArrayList<>();//����
	private List<Location> locations=new ArrayList<Location>();//����
	private List<Plane> planes=new ArrayList<Plane>();//�ɻ�
	private List<String> locationnames=new ArrayList<String>();//�ɻ����������ƣ�ÿ�����Ƶ�˳����locations��ͬ
	//Abstraction function:
	//	AF(flights,planes,locations)=һ���Ժ���flights�����зɻ�planes������locations���й����ϵͳ
	//Representation invariant:
	//	true
	//Safety from rep exposure:
	//	��Ա����ȫ��private�ģ����ⷵ��ʱȫ��תΪ���ɱ����ͣ������ڱ�ʾй¶
	
	/**
	 * ����һ���ɹ�����ķɻ������ɻ��Ѵ��ڲ����ظ����
	 * @param plane ����ӵķɻ�
	 */
	public void addPlane(Plane plane) {
		if(!planes.contains(plane)) {
			planes.add(plane); //PlaneΪ���ɱ��࣬�����ڱ�ʾй¶
		}
	}
	
	/**
	 * ɾ������ָ��ID�ķɻ������ɻ������ڲ��������ɾ������ ,���к���ռ�÷ɻ����޷�ɾ��
	 * @param ID ��ɾ���ɻ���ID
	 */
	public void deletePlane(String ID) {
		int index=-1;
		for(Plane plane:planes) {
			if(plane.getId().equals(ID)) {
				index=planes.indexOf(plane);
				break;
			}
		}
		if(index==-1) {
			System.out.println("Ŀǰ����ķɻ��в����ڸ�ID�ɻ�!");
			return;
		}
		
		boolean flag=false;
		for(FlightEntry<Plane> fe:flights) {//���к���ռ�ø÷ɻ�,�Ҹú����ѷ���/������
			if(fe.getResource().contains(planes.get(index))&&(fe.getStateName().equals("Allocated")||fe.getStateName().equals("Running"))) {
				flag=true;
				break;
			}
		}
		if(!flag) {
			planes.remove(index);
			System.out.println("ɾ���ɹ�!");
			return;
		}
		
		System.out.println("Ŀǰ���ں���ռ�ø�λ��!ɾ��ʧ��!");
	}
	
	/**
	 * ����һ���ɹ�����Ļ������������Ѵ��ڲ����ظ����
	 * @param location ����ӵĻ���
	 */
	public void addLocation(Location location) {
		if(!locations.contains(location)) {
			locations.add(location);
			locationnames.add(location.getName());
		}
			
	}
	
	/**
	 * ɾ������ָ�����ƵĻ����������������ڲ��������ɾ������ �����мƻ�����ռ�ã���ִ��ɾ����������ʾ
	 * @param locationname
	 */
	public void deleteLocation(String locationname) {
		
		if(locationnames.contains(locationname)) {
			int index=locationnames.indexOf(locationname);
			boolean flag=false;
			for(FlightEntry<Plane> fe:flights) {//���к���ռ�ø�λ��,�Ҹú���δȡ��/����
				if(fe.getLocation().contains(locations.get(index))&&!fe.getStateName().equals("Cancelled")&&!fe.getStateName().equals("Ended")) {
					flag=true;
					break;
				}
			}
			if(!flag) {
				locations.remove(index);
				locationnames.remove(index);
				System.out.println("ɾ���ɹ�!");
				return;
			}
			
			System.out.println("Ŀǰ���ں���ռ�ø�λ��!ɾ��ʧ��!");
		}
		else {
			System.out.println("Ŀǰ�����λ���в����ڸ�λ��!ɾ��ʧ��!");
		}
		
		
	}
	
	/**
	 * ����һ�����������й�����������������.�½�����ĳ�ʼ״̬ΪWaiting
	 * @param name �½���������
	 * @param start ������ɵĻ�������
	 * @param end ���ཱུ��Ļ�������
	 * @param timeslot ��ʼʱ������ֹʱ�乹�ɵ�ʱ���
	 * @return ��������ɹ�����true�����򷵻�false
	 * @throws ParseException ʱ��δ��ת��Ϊ��׼��ʽ
	 */
	public boolean createFlight(String name,List<String> locationnameList,List<Timeslot> timeslots){
		if(timeslots.size()==0) {
			System.out.println("�����ʱ��Բ���Ϊ��,�����ʱ��Ժ󴴽�!");
			return false;
		}
		
		if(locations.size()!=timeslots.size()+1) {//����ʱ����ǲ��յģ�ʱ��Ը�����1��ͬʱҲ��֤��λ������2
			System.out.println("��ͣվ��������ֹʱ��Ը�����ƥ��,������ȷ����");
			return false;
		}
		
		if(locationnameList.size()!=2&&locationnameList.size()!=3) {
			System.out.println("��ͣվ����ֻ��Ϊ2��3,λ����������Ҫ��,������ȷ����");
			return false;
		}
		
		List<Location> containlocations=new ArrayList<Location>();
		for(int i=0;i<locationnameList.size();i++) {
			String locationname=locationnameList.get(i);
			int index=locationnames.indexOf(locationname);
			if(index==-1) {
				System.out.println("�����λ��֮���д���λ��δ�������!");
				return false;
			}
			Location location=this.locations.get(index);//����λ�����Ƶõ���Ӧλ��
			containlocations.add(location);
		}
		
		FlightEntry<Plane> newFlight=FlightPlanningEntry.CreateFlight(name);
		newFlight.setLocations(containlocations);
		newFlight.setTime(timeslots);
		
		FlightEntry<Plane> newflight=FlightPlanningEntry.CreateFlight(name);
		newflight.setTime(timeslots);
		newflight.setLocations(containlocations);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		String newstart=timeslots.get(0).getStarttime();
		String newend=timeslots.get(timeslots.size()-1).getEndtime();
		Date newstarttime=null;
		Date newendtime=null;
		try {
			newstarttime=sdf.parse(newstart);
			newendtime = sdf.parse(newend);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar newstartCalendar=Calendar.getInstance();
		newstartCalendar.setTime(newstarttime);
		Calendar newendCalendar=Calendar.getInstance();
		newendCalendar.setTime(newendtime);
		for(FlightEntry<Plane> flight:flights) {
			if(ifTwoSameFlightName(flight.getName(), name)) {
				if(!flight.getLocation().equals(newflight.getLocation())) {
					System.out.println("ͬһ��������͵������Ӧ����ͬ!");
					return false;
				}
				String oldstart=flight.getTime().get(0).getStarttime();
				String oldend=flight.getTime().get(0).getEndtime();
				Date oldstarttime=null;
				Date oldendtime=null;
				try {
					oldstarttime = sdf.parse(oldstart);
					oldendtime=sdf.parse(oldend);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Calendar oldstartCalendar=Calendar.getInstance();
				oldstartCalendar.setTime(oldstarttime);
				Calendar oldendCalendar=Calendar.getInstance();
				oldendCalendar.setTime(oldendtime);
				if((oldendCalendar.get(0)==newendCalendar.get(0)&&oldendCalendar.get(1)==newendCalendar.get(1)&&oldendCalendar.get(6)==newendCalendar.get(6))||(oldstartCalendar.get(0)==newstartCalendar.get(0)&&oldstartCalendar.get(1)==newstartCalendar.get(1)&&oldstartCalendar.get(6)==newstartCalendar.get(6))) {
					//�ִ�ͳ������ھ���ͬ��ͬ������ض������ٴ���,��ʱ����ͬ�����ظ���������ʱ�䲻ͬ������Ҫ��
					System.out.println("�����Ѵ���!");
					return false;
				}
				else {
					if(oldstartCalendar.get(Calendar.HOUR_OF_DAY)!=newstartCalendar.get(Calendar.HOUR_OF_DAY)||oldstartCalendar.get(Calendar.MINUTE)!=newstartCalendar.get(Calendar.MINUTE)||oldendCalendar.get(Calendar.HOUR_OF_DAY)!=newendCalendar.get(Calendar.HOUR_OF_DAY)||oldendCalendar.get(Calendar.MINUTE)!=newendCalendar.get(Calendar.MINUTE)) {
						System.out.println("�����ʱ��ڵ㲻һ��!");//��ͬ���ڣ�����ʱ��͵ִ�ʱ�䶼Ӧ��ͬ
						return false;
					}
				}
			}
		}
		flights.add(newflight);
		System.out.println("�����ɹ�!");
		return true;
	}
	
	/**
	 * ͨ�������ļ���������
	 * @param path �ļ�·��
	 * @return ����ļ��е����к�������﷨��ȫ�������ɹ�,����true�������ӡ������Ϣ������false
	 */
	public boolean createFlightByFile(String path) {
		File file=new File(path);
		if(!file.exists()||!file.isFile()) {
			System.out.println("���ļ�������!");
			return false;
		}
		int number=0;
		InputStreamReader read;
		BufferedReader bufferedReader=null;
		try {
			read = new InputStreamReader(new FileInputStream(file));
			bufferedReader = new BufferedReader(read);
			String line=null;
			int round=0;//��¼���ڵ�ǰ�������Ϣ���뵽�ڼ���
			int seats=0; //��¼�ɻ���λ��
			double age=0;//��¼�ɻ�����
			String[] planeinformaion=new String[2];//��һ���ַ������鱣���﷨��ȷ�ķɻ���Ϣ����ͬ�������¸�ֵ
			String[] information=new String[5];//��һ���ַ������鱣���﷨��ȷ�ĺ��ഴ����Ϣ����ͬ�������¸�ֵ
			String date=null;//һ����������ڣ�����ʱÿ���������¸�ֵ
			while((line=bufferedReader.readLine())!=null) {
				if(line.equals(""))
					continue;
				switch (round) {
				case 0:{//Flight
					number++;
					System.out.printf("��ǰ���ڴ����%d������\n", number);
					Pattern pattern0=Pattern.compile("Flight:(\\d{4}-\\d{2}-\\d{2}),([A-Z]{2}\\d{2,4})");
					Matcher matcher=pattern0.matcher(line);
					if(!matcher.find()) {
						System.out.println("�ı���Flight��ʽ�������﷨!");
						return false;
					}
					date=matcher.group(1);
					String flightname=matcher.group(2);
					information[0]=flightname;//��������
					round++;
					break;
				}
				case 2:{
					Pattern pattern2=Pattern.compile("DepartureAirport:(\\w+)");
					Matcher matcher=pattern2.matcher(line);
					if(!matcher.find()) {
						System.out.println("�ı�����ɻ�����ʽ�������﷨!");
						return false;
					}
					information[1]=matcher.group(1);
					round++;
					break;
				}
				case 3:{
					Pattern pattern3=Pattern.compile("ArrivalAirport:(\\w+)");
					Matcher matcher=pattern3.matcher(line);
					if(!matcher.find()) {
						System.out.println("�ı��еִ������ʽ�������﷨!");
						return false;
					}
					information[2]=matcher.group(1);
					round++;
					break;
				}
				case 4:{
					String pathString="DepatureTime:(\\d{4}-\\d{2}-\\d{2})( \\d{2}:\\d{2})";
					Pattern pattern4=Pattern.compile(pathString);
					Matcher matcher=pattern4.matcher(line);
					if(!matcher.find()) {
						System.out.println("�ı������ʱ���ʽ�������﷨!");
						return false;
					}
					String departuredate=matcher.group(1);				
					if(!departuredate.equals(date)) {///���ʱ���е����ڱ������һ�е�����һ�� 
						System.out.println("�ı������ʱ���뺽��ʱ�䲻ƥ��!");
						return false;
					}
					departuredate=departuredate.concat(matcher.group(2));//����ʱ�������ʱ��
					information[3]=departuredate;
					round++;
					break;
				}
				case 5:{
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
					String pathString="ArrivalTime:(\\d{4}-\\d{2}-\\d{2})( \\d{2}:\\d{2})";
					Pattern pattern5=Pattern.compile(pathString);
					Matcher matcher=pattern5.matcher(line);
					if(!matcher.find()) {
						System.out.println("�ı��еִ�ʱ���ʽ�������﷨!");
						return false;
					}
					String reachdate=matcher.group(1);
					try {
						Date timedate=sdf.parse(date);
						Date reachtimedate=sdf.parse(reachdate);
						Calendar timeCalendar=Calendar.getInstance();
						timeCalendar.setTime(timedate);
						Calendar reachCalendar=Calendar.getInstance();
						reachCalendar.setTime(reachtimedate);
						if(reachCalendar.get(0)==timeCalendar.get(0)&&reachCalendar.get(1)==timeCalendar.get(1)&&reachCalendar.get(6)==timeCalendar.get(6)) {
							information[4]=reachdate.concat(matcher.group(2));//��ͬһ��
							round++;
						}
						else if(reachCalendar.get(0)==timeCalendar.get(0)&&reachCalendar.get(1)==timeCalendar.get(1)&&reachCalendar.get(6)==timeCalendar.get(6)+1) {
							information[4]=reachdate.concat(matcher.group(2));//�ں�һ��
							round++;
						}
						else {
							System.out.println("�ı��еִ�ʱ���뺽��ʱ�䲻ƥ��!");
							return false;
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
					break;
				}
				case 6:{
					Pattern pattern6=Pattern.compile("Plane:([BN]{1}\\d{4})");
					Matcher matcher=pattern6.matcher(line);
					if(!matcher.find()) {
						System.out.println("�ı���Plane��ʽ�������﷨!");
						return false;
					}
					
					String PlaneID=matcher.group(1);
					planeinformaion[0]=PlaneID;
					round++;
					break;
				}
				case 8:{
					Pattern pattern8=Pattern.compile("Type:([A-Za-z0-9]+)");
					Matcher matcher=pattern8.matcher(line);
					if(!matcher.find()) {
						System.out.println("�ı��зɻ���Type��ʽ�������﷨!");
						return false;
					}
					planeinformaion[1]=matcher.group(1);
					round++;
					break;
				}
				case 9:{
					Pattern pattern9=Pattern.compile("Seats:([0-9]+)");
					Matcher matcher=pattern9.matcher(line);
					if(!matcher.find()) {
						System.out.println("�ı��зɻ�����λ����ʽ�������﷨!");
						return false;
					}
					seats=Integer.valueOf(matcher.group(1));
					if(seats<50||seats>600) {
						System.out.println("�ı��зɻ���λ����С������Ҫ��!");
						return false;
					}
					round++;
					break;
				}
				case 10:{
					Pattern pattern10=Pattern.compile("Age:([1-9]?[0-9]?.[0-9]?)");
					Matcher matcher=pattern10.matcher(line);
					if(!matcher.find()) {
						System.out.println("�ı��л����ʽ�������﷨!");
						return false;
					}
					age=Double.valueOf(matcher.group(1));
					if(age<0||age>30) {
						System.out.println("�ı��зɻ������С������Ҫ��!");
						return false;
					}
					round++;
					break;
				}
				case 12:{
					round=0;
					Plane plane=new Plane(planeinformaion[0], planeinformaion[1], seats, age);
					addPlane(plane);
					Timeslot timeslot=new Timeslot(information[3], information[4]);	
					Location start=new Location("10W", "35N", information[1], true);
					Location end=new Location("10W", "35N", information[2], true);
					addLocation(start);
					addLocation(end);
					List<String> locationnames=new ArrayList<String>();
					locationnames.add(information[1]);
					locationnames.add(information[2]);
					List<Timeslot> timeslots=new ArrayList<Timeslot>();
					timeslots.add(timeslot);
					boolean flag=createFlight(information[0], locationnames, timeslots);
					allocatePlane(information[0], planeinformaion[0], timeslots);
					if(!flag) {
						return false;
					}
					break;
				}
				default:{
					round++;
					break;
				}
				}
			}
			
			bufferedReader.close();
			if(round!=0) {
				System.out.println("�ı��������﷨!");
				return false;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(bufferedReader!=null) {
				try {
					bufferedReader.close();
				}catch (IOException e1) {
				}
			}
		}
		return true;
	}
	
	/**
	 * Ϊ�������ɻ����ɻ�Ӧ�����ڹ�������зɻ�֮��
	 * @param flightname ָ���ĺ�������
	 * @param plane ������ķɻ�����
	 * @param timeslots ��������ʱ��ͽ���ʱ�䣬Ϊ�����ֲ�ͬ���ڵ�ͬ������
	 */
	public void allocatePlane(String flightname,String planeID,List<Timeslot> timeslots) {
		int index=-1;
		for(Plane plane:planes) {
			if(plane.getId().equals(planeID)) {//���ݷɻ������ҵ��ɻ�
				index=planes.indexOf(plane);
			}
		}
		if(index==-1) {
			System.out.println("����ķɻ���δ�������!");
			return;
		}
		
		for(FlightEntry<Plane> fe:flights) {
			if(fe.getName().equals(flightname)&&fe.getTime().containsAll(timeslots)) {
				List<Plane> onePlane=new ArrayList<Plane>();
				onePlane.add(planes.get(index));
				fe.allocateResource(onePlane);
				System.out.println("����ɹ�!");
				PlanningEntryAPIs api=new PlanningEntryAPIsFirstImpl();
				boolean flag2=api.checkResourceExclusiveConflict(flights);
				if(flag2) {
					System.out.println("��ע��,�½����������������зɻ������ͻ!�뿼���Ƿ�ȡ��!");
				}
				return;
			}
		}
		
		System.out.println("ָ�����໹δ�������޷�����ɻ�!");
	}
	
	/**
	 * @return �õ����й����µĺ���
	 */
	public List<FlightEntry<Plane>> getFlights() {
		return Collections.unmodifiableList(flights);
	}
	
	/**
	 * @return �õ����й����µĻ���
	 */
	public List<Location> getLocations() {
		return Collections.unmodifiableList(locations);
	}

	/**
	 * @return �õ����й���֮�µķɻ�
	 */
	public List<Plane> getPlanes() {
		return Collections.unmodifiableList(planes);
	}

	/**
	 * ָ�����������ɣ�����Ӧ�Ѿ�������,�����Ѿ������˷ɻ�
	 * @param flightname ָ����������
	 * @param timeslots �������ɺͽ���ʱ�䣬Ϊ�����ֲ�ͬ���ڵ�ͬ������
	 */
	public void departure(String flightname,List<Timeslot> timeslots) {
		for(FlightEntry<Plane> fe:flights) {
			if(fe.getName().equals(flightname)&&fe.getTime().containsAll(timeslots)) {//ͬ����ʱ����ܹ���Ӧ
				fe.start();
				return;
			}
		}
		
		System.out.println("ָ�����಻���ڣ��޷����!");
	}
	
	/**
	 * ȡ��ָ�����࣬����Ӧ���Ѿ���������������δ���
	 * @param flightname ָ����������
	 * @param timeslots �������ɺͽ���ʱ�䣬Ϊ�����ֲ�ͬ���ڵ�ͬ������
	 */
	public void cancelFlight(String flightname,List<Timeslot> timeslots) {
		for(FlightEntry<Plane> fe:flights) {
			if(fe.getName().equals(flightname)&&fe.getTime().containsAll(timeslots)) {//ͬ����ʱ����ܹ���Ӧ
				fe.cancel();
				return;
			}
		}
		
		System.out.println("ָ�����಻���ڣ��޷�ȡ��!");
	}
	
	/**
	 * �õ�ָ�������״̬
	 * @param flightname ָ�����������
	 * @param timeslots �������ɺͽ���ʱ�䣬Ϊ�����ֲ�ͬ���ڵ�ͬ������
	 * @return ����ú����Ѵ������򷵻���״̬���ƣ����򷵻�һ�� ��null���ַ���
	 */
	public String getFlightState(String flightname,List<Timeslot> timeslots) {
		for(FlightEntry<Plane> fe:flights) {
			if(fe.getName().equals(flightname)&&fe.getTime().containsAll(timeslots)) {//ͬ����ʱ����ܹ���Ӧ
				return fe.getStateName();
			}
		}
		
		return "null";
	}
	
	/**
	 * ����ָ�����࣬����Ӧ���Ѿ��������������Ѿ����
	 * @param flightname ָ����������
	 * @param timeslots �������ɺͽ���ʱ�䣬Ϊ�����ֲ�ͬ���ڵ�ͬ������
	 */
	public void endFlight(String flightname,List<Timeslot> timeslots) {
		for(FlightEntry<Plane> fe:flights) {
			if(fe.getName().equals(flightname)&&fe.getTime().containsAll(timeslots)) {//ͬ����ʱ����ܹ���Ӧ
				fe.end();
				return;
			}
		}
		
		System.out.println("ָ�����಻����!");
	}
	
	/**
	 * ���ռ��ָ���ɻ������к���
	 * @param planeID ָ���ɻ���ID 
	 * @return �����ռ��ָ���ɻ��ĺ���,�������������к��ࣻ���򷵻�null
	 */
	public List<FlightEntry<Plane>> getFlightssofassignPlane(String planeID){
		int index=-1;
		for(Plane plane:planes) {//�ҵ�ָ���ɻ�
			if(plane.getId().equals(planeID)) {
				index=planes.indexOf(plane);
			}
		}
		if(index==-1) {
			System.out.println("Ŀǰ����ķɻ��в����ڸ�ID�ɻ�!");
			return null;
		}
			
		Plane assignplane=planes.get(index);
		List<FlightEntry<Plane>> fes=new ArrayList<>();
		for(FlightEntry<Plane> fe:flights) {
			if(fe.getResource().contains(assignplane))
				fes.add(fe);
		}
		if(fes.isEmpty())
			return null;
		return fes;
	}
	
	/**
	 * ͨ���ɻ���ID�õ���Ӧ�ķɻ�
	 * @param planeID �ɻ�ID
	 * @return ��Ӧ�ķɻ�,δ�����򷵻�null
	 */
	public Plane getPlanebyID(String planeID) {
		for(Plane plane:planes) {
			if(plane.getId().equals(planeID))
				return plane;
		}
		return null;
	}
	
	/**
	 * ͨ��λ�����Ƶõ���Ӧ��λ��
	 * @param locationName λ�õ�����
	 * @return ��Ӧ��λ��,δ�����򷵻�null
	 */
	public Location getLocationbyName(String locationName) {
		for(Location location:locations) {
			if(location.getName().equals(locationName))
				return location;
		}
		return null;
	}
	
	/**
	 * ͨ���������ƻ�ö�Ӧ����
	 * @param flightName ��������
	 * @param timeslots ��������ʱ��ͽ���ʱ�䣬Ϊ�����ֲ�ͬ���ڵ�ͬ������
	 * @return ��Ӧ�ĺ���,δ�����򷵻�null
	 */
	public FlightEntry<Plane> getFlightbyName(String flightName,List<Timeslot> timeslots){
		for(FlightEntry<Plane> fe:flights) {
			if(fe.getName().equals(flightName)&&fe.getTime().containsAll(timeslots)) {
				return fe;
			}
		}
		return null;
	}
	
	/**
	 * ����ָ�����Ѿ������ĺ���
	 * @param Flightname ���������Ѿ������ĺ���
	 * @param timeslots ָ����������ʱ��ͽ���ʱ��,Ϊ�����ֲ�ͬ���ڵ�ͬ������
	 */
	public void blockFlight(String Flightname,List<Timeslot> timeslots) {
		for(FlightEntry<Plane> fe:flights) {
			if(fe.getName().equals(Flightname)&&fe.getTime().containsAll(timeslots)) {
				fe.block(fe);
				return;
			}
		}
		
		System.out.println("ָ�����໹δ����������ʧ��!");
	}
	
	/**
	 * �ж��������������Ƿ������ͬһ������
	 * @param flightname1 ����һ����������
	 * @param flightname2 ��һ����������
	 * @return �������ͬһ�������򷵻�true�����򷵻�false
	 */
	public boolean ifTwoSameFlightName(String flightname1,String flightname2) {
		if(flightname1.equals(flightname2))
			return true;
		Pattern pattern=Pattern.compile("([A-Z]{2})(\\d{2,4})");
		Matcher matcher1=pattern.matcher(flightname1);
		Matcher matcher2=pattern.matcher(flightname2);
		if(matcher1.find()&&matcher2.find()) {//ͨ���ļ����ɵı�׼��ʽ�ж���CA0001,CA001,CA01�ȿ�����ͬ���ж�
			String string1=matcher1.group(1);
			String string2=matcher2.group(1);
			int num1=Integer.valueOf(matcher1.group(2));
			int num2=Integer.valueOf(matcher2.group(2));
			if(string1.equals(string2)&&num1==num2)//ǰ����ĸ��ͬ���������ֵ�ֵ��ͬ������ΪҲ��ͬһ������
				return true;
		}
		
		return false;
	}
	
}