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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import API.PlanningEntryAPIs;
import API.PlanningEntryAPIsFirstImpl;
import Exceptions.*;
import Location.Location;
import LogFile.MyFormatter;
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
	private static Logger myLogger=Logger.getLogger("FlightScheduleLog");
	//Abstraction function:
	//	AF(flights,planes,locations)=һ���Ժ���flights�����зɻ�planes������locations���й����ϵͳ
	//Representation invariant:
	//	����ĺ��ࡢ�ɻ��������ж���Ӧ�����ظ�Ԫ��
	//Safety from rep exposure:
	//	��Ա����ȫ��private�ģ����ⷵ��ʱȫ��תΪ���ɱ����ͣ������ڱ�ʾй¶
	
	private void checkRep() {
		Set<Location> locationSet=new HashSet<Location>(locations);
		Set<Plane> planeSet=new HashSet<Plane>(planes);
		Set<FlightEntry<Plane>> flightEntriesSet=new HashSet<>(flights);
		assert locationSet.size()==locations.size();
		assert planeSet.size()==planes.size();
		assert flightEntriesSet.size()==flights.size();
		//��־��¼
		myLogger.setLevel(Level.INFO);
		myLogger.setUseParentHandlers(false);
		//д���ļ�
		FileHandler handler;
		try {
			handler = new FileHandler("src/LogFile/FlightScheduleLog.log");
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
	 * ����һ���ɹ�����ķɻ������ɻ��Ѵ��ڲ����ظ����
	 * @param plane ����ӵķɻ�
	 * @throws IllegalPlaneContentException ֮ǰ�÷ɻ��Ѵ�������ID������������˴β�һ��
	 */
	public void addPlane(Plane plane) throws IllegalPlaneContentException {
		for(Plane plane2:planes) {
			if(plane.getId().equals(plane2.getId())) {
				boolean flag=ifOneofTwoSameIDPlaneIllegal(plane, plane2);
				if(flag) {
					throw new IllegalPlaneContentException(plane.getId());
				}
				else
					return;
			}
		}
		planes.add(plane);//��ǰδ��ӹ��÷ɻ�
		checkRep();
	}
	
	/**
	 * ɾ������ָ��ID�ķɻ����ɻ�Ӧ���Ѵ��� ,���޺���ռ��
	 * @param ID ��ɾ���ɻ���ID
	 * @throws ResourceNotFoundException  Ҫɾ���ķɻ�������
	 * @throws PlanEntryOccupyResourceException Ҫɾ���ķɻ�������ռ����
	 */
	public void deletePlane(String ID) throws ResourceNotFoundException, PlanEntryOccupyResourceException {
		int index=-1;
		for(Plane plane:planes) {
			if(plane.getId().equals(ID)) {
				index=planes.indexOf(plane);
				break;
			}
		}
		if(index==-1) {
			throw new ResourceNotFoundException(ID);
		}
		
		boolean flag=false;
		FlightEntry<Plane> fentry=null;
		for(FlightEntry<Plane> fe:flights) {//���к���ռ�ø÷ɻ�,�Ҹú����ѷ���/������
			if(fe.getResource().contains(planes.get(index))&&(fe.getStateName().equals("Allocated")||fe.getStateName().equals("Running"))) {
				flag=true;
				fentry=fe;
				break;
			}
		}
		if(!flag) {
			planes.remove(index);
			System.out.println("ɾ���ɹ�!");
			return;
		}
		else {
			throw new PlanEntryOccupyResourceException(fentry.getName());
		}
	}
	
	/**
	 * ����һ���ɹ�����Ļ������������Ѵ��ڲ����ظ����
	 * @param location ����ӵĻ���
	 */
	public void addLocation(Location location) {
		if(!locations.contains(location)) {
			locations.add(location);
			checkRep();
		}
	}
	
	/**
	 * ɾ������ָ�����ƵĻ���������Ӧ���Ѵ��� ���޺���ռ��
	 * @param locationname �������� 
	 * @throws LocationNotFoundException Ҫɾ����λ�ò�����
	 * @throws PlanEntryOccupyLocationException Ҫɾ����λ�����ڱ��ƻ���ռ����
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
		FlightEntry<Plane> flightEntry=null;
		for(FlightEntry<Plane> fe:flights) {//���к���ռ�ø�λ��,�Ҹú���δȡ��/����
			if(fe.getLocation().contains(locations.get(index))&&!fe.getStateName().equals("Cancelled")&&!fe.getStateName().equals("Ended")) {
				flag=true;
				flightEntry=fe;
				break;
			}
		}
		if(!flag) {
			System.out.println("ɾ���ɹ�!");
			locations.remove(index);
			return;
		}
		else {
			throw new PlanEntryOccupyLocationException(flightEntry.getName());
		}
		
	}
	
	/**
	 * ����һ�����������й�����������������.�½�����ĳ�ʼ״̬ΪWaiting
	 * @param name �½���������
	 * @param start ������ɵĻ�������
	 * @param end ���ཱུ��Ļ�������
	 * @param timeslot ��ʼʱ������ֹʱ�乹�ɵ�ʱ���
	 * @return ��������ɹ�����true�����򷵻�false
	 * @throws LocationNotFoundException λ����δ����
	 * @throws InconsistentStartOrEndException ��ͬ����ŵ������յ㲻һ��
	 * @throws SameLabelException �ظ���������
	 * @throws ParseException ʱ��δ��ת��Ϊ��׼��ʽ
	 */
	public boolean createFlight(String name,String start,String end,Timeslot timeslot) throws LocationNotFoundException, InconsistentStartOrEndException, SameLabelException{
		int flag1=-1;
		int flag2=-1;
		for(Location location2:locations) {
			if(location2.getName().equals(start)) {//λ�û�δ�������
				flag1=locations.indexOf(location2);
			}
			if(location2.getName().equals(end)) {
				flag2=locations.indexOf(location2);
			}
		}
		if(flag1==-1||flag2==-1) {
			throw new LocationNotFoundException();
		}
		
		FlightEntry<Plane> newflight=FlightPlanningEntry.CreateFlight(name);
		List<Timeslot> oneTimeslot=new ArrayList<Timeslot>();
		oneTimeslot.add(timeslot);
		newflight.setTime(oneTimeslot);
		newflight.setLocations(locations.get(flag1), locations.get(flag2));
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		String newstart=timeslot.getStarttime();
		String newend=timeslot.getEndtime();
		Date newstarttime=null;
		Date newendtime=null;
		try {
			newstarttime=sdf.parse(newstart);
			newendtime = sdf.parse(newend);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		Calendar newstartCalendar=Calendar.getInstance();
		newstartCalendar.setTime(newstarttime);
		Calendar newendCalendar=Calendar.getInstance();
		newendCalendar.setTime(newendtime);
		for(FlightEntry<Plane> flight:flights) {
			if(ifTwoSameFlightName(flight.getName(), name)) {
				if(!flight.getLocation().equals(newflight.getLocation())) {
					throw new InconsistentStartOrEndException(false);
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
					return false;
				}
				Calendar oldstartCalendar=Calendar.getInstance();
				oldstartCalendar.setTime(oldstarttime);
				Calendar oldendCalendar=Calendar.getInstance();
				oldendCalendar.setTime(oldendtime);
				boolean flag3=oldendCalendar.get(0)==newendCalendar.get(0)&&oldendCalendar.get(1)==newendCalendar.get(1)&&oldendCalendar.get(6)==newendCalendar.get(6);
				boolean flag4=oldstartCalendar.get(0)==newstartCalendar.get(0)&&oldstartCalendar.get(1)==newstartCalendar.get(1)&&oldstartCalendar.get(6)==newstartCalendar.get(6);
				if(flag3&&flag4) {//�ִ�ͳ������ھ���ͬ��ͬ������ض������ٴ���,��ʱ����ͬ�����ظ���������ʱ�䲻ͬ������Ҫ��
					throw new SameLabelException(name);
				}
				else {
					if(flag3||flag4)//�ִ�ͳ�������ֻ��һ����ͬ��ͬ������ض������ٴ�������Ϊʱ�����ͬ������λ��ͬһ�죬�г�ͻ
						throw new InconsistentStartOrEndException(true);
					if(oldstartCalendar.get(Calendar.HOUR_OF_DAY)!=newstartCalendar.get(Calendar.HOUR_OF_DAY)||oldstartCalendar.get(Calendar.MINUTE)!=newstartCalendar.get(Calendar.MINUTE)||oldendCalendar.get(Calendar.HOUR_OF_DAY)!=newendCalendar.get(Calendar.HOUR_OF_DAY)||oldendCalendar.get(Calendar.MINUTE)!=newendCalendar.get(Calendar.MINUTE)) {
						throw new InconsistentStartOrEndException(true);
					}
				}
			}
		}
		flights.add(newflight);
		checkRep();
		System.out.println("�����ɹ�!");
		return true;
	}
	
	/**
	 * ͨ�������ļ���������
	 * @param path �ļ�·��
	 * @return ����ļ��е����к�������﷨��ȫ�������ɹ�,����true�������ӡ������Ϣ������false
	 * @throws FileNotFoundException δ�ҵ�ָ���ļ�
	 * @throws SameLabelException �ظ���������
	 * @throws IncorrectElementDependencyException Ԫ�ؼ�������ϵ����ȷ
	 * @throws LocationNotFoundException λ����δ����
	 * @throws UnGrammaticalWordException �ļ��������﷨����
	 * @throws PlanEntryNotCreateException ���ഴ��ʧ��
	 * @throws ResourceConflictException ������Դ�����ͻ
	 * @throws ResourceNotFoundException �������Դ������
	 */
	public boolean createFlightByFile(String path) throws FileNotFoundException, LocationNotFoundException, IncorrectElementDependencyException, SameLabelException, UnGrammaticalWordException, ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException {
		File file=new File(path);
		if(!file.exists()||!file.isFile()) {
			throw new FileNotFoundException();
		}
		InputStreamReader read;
		BufferedReader bufferedReader=null;
		int number=0;
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
					Pattern pattern0=Pattern.compile("Flight:(\\d{4}-\\d{2}-\\d{2}),(\\w+)");
					Matcher matcher=pattern0.matcher(line);
					if(!matcher.find()) {//���ڸ�ʽ����
						throw new DateFormatException();
					}
					date=matcher.group(1);
					String flightname=matcher.group(2);
					Pattern pattern01=Pattern.compile("([A-Z]{2}\\d{2,4})");
					Matcher matcher2=pattern01.matcher(flightname);
					if(!matcher2.find()) {//�������Ƹ�ʽ����
						throw new FlightNameFormatException(flightname);
					}
					information[0]=flightname;//��������
					round++;
					break;
				}
				case 2:{
					Pattern pattern2=Pattern.compile("DepartureAirport:(\\w+)");
					Matcher matcher=pattern2.matcher(line);
					if(!matcher.find()) {
						throw new IllegalCharacterForAirportNameException(line);//�������Ƴ��ַǷ��ַ�
					}
					information[1]=matcher.group(1);
					round++;
					break;
				}
				case 3:{
					Pattern pattern3=Pattern.compile("ArrivalAirport:(\\w+)");
					Matcher matcher=pattern3.matcher(line);
					if(!matcher.find()) {
						throw new IllegalCharacterForAirportNameException(line);//�������Ƴ��ַǷ��ַ�
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
						throw new DateFormatException();//���ڸ�ʽ����
					}
					String departuredate=matcher.group(1);				
					if(!departuredate.equals(date)) {///���ʱ���е����ڱ������һ�е�����һ�� 
						throw new InconsistentDateException();
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
						throw new DateFormatException();//���ڸ�ʽ����
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
							throw new DateDifferMuchException();
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
						throw new PlaneIDFormatException(line);
					}
					
					String PlaneID=matcher.group(1);
					planeinformaion[0]=PlaneID;
					round++;
					break;
				}
				case 8:{
					Pattern pattern8=Pattern.compile("^Type:(\\w+$)");
					Matcher matcher=pattern8.matcher(line);
					if(!matcher.find()) {
						throw new TypeContainsOtherSymbolException(line);//���ͳ��ַǷ��ַ�
					}
					planeinformaion[1]=matcher.group(1);
					round++;
					break;
				}
				case 9:{
					Pattern pattern9=Pattern.compile("^Seats:([1-9][0-9]*$)");
					Matcher matcher=pattern9.matcher(line);
					if(!matcher.find()) {
						throw new NonNumberException();//��λ��������
					}
					seats=Integer.valueOf(matcher.group(1));
					if(seats<50||seats>600) {
						throw new SeatsSizeOutofBoundException();
					}
					round++;
					break;
				}
				case 10:{
					Pattern pattern10=Pattern.compile("Age:([1-9]?[0-9]?.[0-9]?)");
					Matcher matcher=pattern10.matcher(line);
					if(!matcher.find()) {
						throw new NonNumberException();//���������
					}
					age=Double.valueOf(matcher.group(1));
					if(age<0||age>30) {
						throw new AgeOutofBoundException();
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
					boolean flag=createFlight(information[0], information[1], information[2], timeslot);
					List<Timeslot> timeslots=new ArrayList<Timeslot>();
					timeslots.add(timeslot);		
					if(!flag) {
						return false;
					}
					allocatePlane(information[0], planeinformaion[0], timeslots);
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
				throw new IncompleteFlightInformationException();
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
	 * @throws ResourceNotFoundException ����ķɻ�������
	 * @throws ResourceConflictException ������Դ���ڳ�ͻ
	 * @throws PlanEntryNotCreateException ָ��������δ����
	 */
	public void allocatePlane(String flightname,String planeID,List<Timeslot> timeslots) throws ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException {
		int index=-1;
		for(Plane plane:planes) {
			if(plane.getId().equals(planeID)) {//���ݷɻ������ҵ��ɻ�
				index=planes.indexOf(plane);
			}
		}
		if(index==-1) {
			throw new ResourceNotFoundException(planeID);
		}
		
		for(FlightEntry<Plane> fe:flights) {
			if(fe.getName().equals(flightname)&&fe.getTime().containsAll(timeslots)) {
				List<Plane> onePlane=new ArrayList<Plane>();
				onePlane.add(planes.get(index));
				FlightEntry<Plane> copyEntry=new FlightEntry<Plane>("copy");
				copyEntry.setLocations(fe.getStart(), fe.getEnd());
				copyEntry.setTime(timeslots);
				copyEntry.allocateResource(onePlane);//�½�һ�����࣬Ϊ������������õ��ĺ���һ�������ԣ��۲��Ƿ�������ͻ
				List<FlightEntry<Plane>> flightEntries=new ArrayList<>();
				flightEntries.add(copyEntry);//������flights�У�ֻ��һ�����Ʒ������Ƿ�ᷢ����ͻ������������������ĳ�ͻ
				flightEntries.addAll(flights);		
				PlanningEntryAPIs api=new PlanningEntryAPIsFirstImpl();
				boolean flag2=api.checkResourceExclusiveConflict(flightEntries);
				if(flag2) {
					throw new ResourceConflictException();
				}
				fe.allocateResource(onePlane);
				System.out.println("����ɹ�!");
				return;
			}
		}
		
		throw new PlanEntryNotCreateException(flightname);
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
	 * @throws PlanEntryNotCreateException ָ��������δ����
	 */
	public void departure(String flightname,List<Timeslot> timeslots) throws PlanEntryNotCreateException {
		for(FlightEntry<Plane> fe:flights) {
			if(fe.getName().equals(flightname)&&fe.getTime().containsAll(timeslots)) {//ͬ����ʱ����ܹ���Ӧ
				fe.start();
				return;
			}
		}
		
		throw new PlanEntryNotCreateException(flightname);
	}
	
	/**
	 * ȡ��ָ�����࣬����Ӧ���Ѿ���������������δ���
	 * @param flightname ָ����������
	 * @param timeslots �������ɺͽ���ʱ�䣬Ϊ�����ֲ�ͬ���ڵ�ͬ������
	 * @throws PlanEntryNotCreateException ָ��������δ����
	 */
	public void cancelFlight(String flightname,List<Timeslot> timeslots) throws PlanEntryNotCreateException {
		for(FlightEntry<Plane> fe:flights) {
			if(fe.getName().equals(flightname)&&fe.getTime().containsAll(timeslots)) {//ͬ����ʱ����ܹ���Ӧ
				fe.cancel();
				return;
			}
		}
		
		throw new PlanEntryNotCreateException(flightname);
	}
	
	/**
	 * �õ�ָ�������״̬������Ӧ�Ѵ���
	 * @param flightname ָ�����������
	 * @param timeslots �������ɺͽ���ʱ�䣬Ϊ�����ֲ�ͬ���ڵ�ͬ������
	 * @return ����ú����Ѵ������򷵻���״̬���ƣ������׳��쳣
	 * @throws PlanEntryNotCreateException ����δ����
	 */
	public String getFlightState(String flightname,List<Timeslot> timeslots) throws PlanEntryNotCreateException {
		for(FlightEntry<Plane> fe:flights) {
			if(fe.getName().equals(flightname)&&fe.getTime().containsAll(timeslots)) {//ͬ����ʱ����ܹ���Ӧ
				return fe.getStateName();
			}
		}
		
		throw new PlanEntryNotCreateException(flightname);
	}
	
	/**
	 * ����ָ�����࣬����Ӧ���Ѿ��������������Ѿ����
	 * @param flightname ָ����������
	 * @param timeslots �������ɺͽ���ʱ�䣬Ϊ�����ֲ�ͬ���ڵ�ͬ������
	 * @throws PlanEntryNotCreateException ָ��������δ����
	 */
	public void endFlight(String flightname,List<Timeslot> timeslots) throws PlanEntryNotCreateException {
		for(FlightEntry<Plane> fe:flights) {
			if(fe.getName().equals(flightname)&&fe.getTime().containsAll(timeslots)) {//ͬ����ʱ����ܹ���Ӧ
				fe.end();
				return;
			}
		}
		
		throw new PlanEntryNotCreateException(flightname);
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
	
	/**
	 * �ж������ɻ��Ƿ�ӵ����ͬ��ID������������ȴ��ͬ�����Ƿ�Ƿ�
	 * @param plane1 ����һ���ɻ�
	 * @param plane2 ��һ���ɻ�
	 * @return ��������ɻ�ID��ͬ�����������ֲ�ͬ������true�����򷵻�false
	 */
	private boolean ifOneofTwoSameIDPlaneIllegal(Plane plane1,Plane plane2) {
		if(!plane1.getId().equals(plane2.getId()))//����ID��ͬ
			return false;
		if(plane1.getPlaneage()==plane2.getPlaneage()&&plane1.getSeats()==plane2.getSeats()&&plane1.getType().equals(plane2.getType()))//���в��־���ͬ���Ϸ�
			return false;
		return true;//���ڲ�ͬ���֣��Ƿ�
	}
	
}