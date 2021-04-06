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
 * 航班管理，可对多个机场，航班，飞机进行管理，可变类
 * @author 123
 *
 */

public class FlightSchedule {
	private List<FlightEntry<Plane>> flights=new ArrayList<>();//航班
	private List<Location> locations=new ArrayList<Location>();//机场
	private List<Plane> planes=new ArrayList<Plane>();//飞机
	private static Logger myLogger=Logger.getLogger("FlightScheduleLog");
	//Abstraction function:
	//	AF(flights,planes,locations)=一个对航班flights，所有飞机planes，机场locations进行管理的系统
	//Representation invariant:
	//	管理的航班、飞机、机场中都不应含有重复元素
	//Safety from rep exposure:
	//	成员变量全是private的，对外返回时全部转为不可变类型，不存在表示泄露
	
	private void checkRep() {
		Set<Location> locationSet=new HashSet<Location>(locations);
		Set<Plane> planeSet=new HashSet<Plane>(planes);
		Set<FlightEntry<Plane>> flightEntriesSet=new HashSet<>(flights);
		assert locationSet.size()==locations.size();
		assert planeSet.size()==planes.size();
		assert flightEntriesSet.size()==flights.size();
		//日志记录
		myLogger.setLevel(Level.INFO);
		myLogger.setUseParentHandlers(false);
		//写入文件
		FileHandler handler;
		try {
			handler = new FileHandler("src/LogFile/FlightScheduleLog.log");
			handler.setFormatter(new MyFormatter());//采用固定格式
			handler.setLevel(Level.INFO);
			myLogger.addHandler(handler);
			myLogger.info("进行不变量检查");
			handler.close();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 增加一个可供管理的飞机，若飞机已存在不会重复添加
	 * @param plane 待添加的飞机
	 * @throws IllegalPlaneContentException 之前该飞机已创建，除ID外其他部分与此次不一致
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
		planes.add(plane);//此前未添加过该飞机
		checkRep();
	}
	
	/**
	 * 删除具有指定ID的飞机，飞机应该已创建 ,且无航班占用
	 * @param ID 待删除飞机的ID
	 * @throws ResourceNotFoundException  要删除的飞机不存在
	 * @throws PlanEntryOccupyResourceException 要删除的飞机被航班占用中
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
		for(FlightEntry<Plane> fe:flights) {//若有航班占用该飞机,且该航班已分配/运行中
			if(fe.getResource().contains(planes.get(index))&&(fe.getStateName().equals("Allocated")||fe.getStateName().equals("Running"))) {
				flag=true;
				fentry=fe;
				break;
			}
		}
		if(!flag) {
			planes.remove(index);
			System.out.println("删除成功!");
			return;
		}
		else {
			throw new PlanEntryOccupyResourceException(fentry.getName());
		}
	}
	
	/**
	 * 增加一个可供管理的机场，若机场已存在不会重复添加
	 * @param location 待添加的机场
	 */
	public void addLocation(Location location) {
		if(!locations.contains(location)) {
			locations.add(location);
			checkRep();
		}
	}
	
	/**
	 * 删除具有指定名称的机场，机场应该已创建 ，无航班占用
	 * @param locationname 机场名称 
	 * @throws LocationNotFoundException 要删除的位置不存在
	 * @throws PlanEntryOccupyLocationException 要删除的位置正在被计划项占用中
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
		for(FlightEntry<Plane> fe:flights) {//若有航班占用该位置,且该航班未取消/结束
			if(fe.getLocation().contains(locations.get(index))&&!fe.getStateName().equals("Cancelled")&&!fe.getStateName().equals("Ended")) {
				flag=true;
				flightEntry=fe;
				break;
			}
		}
		if(!flag) {
			System.out.println("删除成功!");
			locations.remove(index);
			return;
		}
		else {
			throw new PlanEntryOccupyLocationException(flightEntry.getName());
		}
		
	}
	
	/**
	 * 增加一个航班对其进行管理，不允许重名航班.新建航班的初始状态为Waiting
	 * @param name 新建航班名称
	 * @param start 航班起飞的机场名称
	 * @param end 航班降落的机场名称
	 * @param timeslot 起始时间与终止时间构成的时间对
	 * @return 如果创建成功返回true，否则返回false
	 * @throws LocationNotFoundException 位置尚未创建
	 * @throws InconsistentStartOrEndException 相同航班号的起点或终点不一致
	 * @throws SameLabelException 重复创建航班
	 * @throws ParseException 时间未能转化为标准格式
	 */
	public boolean createFlight(String name,String start,String end,Timeslot timeslot) throws LocationNotFoundException, InconsistentStartOrEndException, SameLabelException{
		int flag1=-1;
		int flag2=-1;
		for(Location location2:locations) {
			if(location2.getName().equals(start)) {//位置还未纳入管理
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
				if(flag3&&flag4) {//抵达和出发日期均相同的同名航班必定不能再创建,若时间相同则是重复创建，若时间不同不满足要求
					throw new SameLabelException(name);
				}
				else {
					if(flag3||flag4)//抵达和出发日期只有一个相同的同名航班必定不能再创建，因为时间点相同，而又位于同一天，有冲突
						throw new InconsistentStartOrEndException(true);
					if(oldstartCalendar.get(Calendar.HOUR_OF_DAY)!=newstartCalendar.get(Calendar.HOUR_OF_DAY)||oldstartCalendar.get(Calendar.MINUTE)!=newstartCalendar.get(Calendar.MINUTE)||oldendCalendar.get(Calendar.HOUR_OF_DAY)!=newendCalendar.get(Calendar.HOUR_OF_DAY)||oldendCalendar.get(Calendar.MINUTE)!=newendCalendar.get(Calendar.MINUTE)) {
						throw new InconsistentStartOrEndException(true);
					}
				}
			}
		}
		flights.add(newflight);
		checkRep();
		System.out.println("创建成功!");
		return true;
	}
	
	/**
	 * 通过读入文件创建航班
	 * @param path 文件路径
	 * @return 如果文件中的所有航班符合语法且全部创建成功,返回true，否则打印错误信息并返回false
	 * @throws FileNotFoundException 未找到指定文件
	 * @throws SameLabelException 重复创建航班
	 * @throws IncorrectElementDependencyException 元素间依赖关系不正确
	 * @throws LocationNotFoundException 位置尚未创建
	 * @throws UnGrammaticalWordException 文件内容有语法错误
	 * @throws PlanEntryNotCreateException 航班创建失败
	 * @throws ResourceConflictException 存在资源分配冲突
	 * @throws ResourceNotFoundException 分配的资源不存在
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
			int round=0;//记录对于当前航班的信息读入到第几行
			int seats=0; //记录飞机座位数
			double age=0;//记录飞机机龄
			String[] planeinformaion=new String[2];//用一个字符串数组保存语法正确的飞机信息，不同航班重新赋值
			String[] information=new String[5];//用一个字符串数组保存语法正确的航班创建信息，不同航班重新赋值
			String date=null;//一个航班的日期，遍历时每个航班重新赋值
			while((line=bufferedReader.readLine())!=null) {
				if(line.equals(""))
					continue;
				switch (round) {
				case 0:{//Flight
					number++;
					System.out.printf("当前正在处理第%d个航班\n", number);
					Pattern pattern0=Pattern.compile("Flight:(\\d{4}-\\d{2}-\\d{2}),(\\w+)");
					Matcher matcher=pattern0.matcher(line);
					if(!matcher.find()) {//日期格式错误
						throw new DateFormatException();
					}
					date=matcher.group(1);
					String flightname=matcher.group(2);
					Pattern pattern01=Pattern.compile("([A-Z]{2}\\d{2,4})");
					Matcher matcher2=pattern01.matcher(flightname);
					if(!matcher2.find()) {//航班名称格式错误
						throw new FlightNameFormatException(flightname);
					}
					information[0]=flightname;//航班名称
					round++;
					break;
				}
				case 2:{
					Pattern pattern2=Pattern.compile("DepartureAirport:(\\w+)");
					Matcher matcher=pattern2.matcher(line);
					if(!matcher.find()) {
						throw new IllegalCharacterForAirportNameException(line);//机场名称出现非法字符
					}
					information[1]=matcher.group(1);
					round++;
					break;
				}
				case 3:{
					Pattern pattern3=Pattern.compile("ArrivalAirport:(\\w+)");
					Matcher matcher=pattern3.matcher(line);
					if(!matcher.find()) {
						throw new IllegalCharacterForAirportNameException(line);//机场名称出现非法字符
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
						throw new DateFormatException();//日期格式错误
					}
					String departuredate=matcher.group(1);				
					if(!departuredate.equals(date)) {///起飞时间中的日期必须与第一行的日期一致 
						throw new InconsistentDateException();
					}
					departuredate=departuredate.concat(matcher.group(2));//加上时间点的起飞时间
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
						throw new DateFormatException();//日期格式错误
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
							information[4]=reachdate.concat(matcher.group(2));//在同一天
							round++;
						}
						else if(reachCalendar.get(0)==timeCalendar.get(0)&&reachCalendar.get(1)==timeCalendar.get(1)&&reachCalendar.get(6)==timeCalendar.get(6)+1) {
							information[4]=reachdate.concat(matcher.group(2));//在后一天
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
						throw new TypeContainsOtherSymbolException(line);//机型出现非法字符
					}
					planeinformaion[1]=matcher.group(1);
					round++;
					break;
				}
				case 9:{
					Pattern pattern9=Pattern.compile("^Seats:([1-9][0-9]*$)");
					Matcher matcher=pattern9.matcher(line);
					if(!matcher.find()) {
						throw new NonNumberException();//座位数非数字
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
						throw new NonNumberException();//机龄非数字
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
	 * 为航班分配飞机，飞机应包含在管理的所有飞机之中
	 * @param flightname 指定的航班名称
	 * @param plane 待分配的飞机名称
	 * @param timeslots 航班的起飞时间和降落时间，为了区分不同日期的同名航班
	 * @throws ResourceNotFoundException 分配的飞机不存在
	 * @throws ResourceConflictException 分配资源存在冲突
	 * @throws PlanEntryNotCreateException 指定航班尚未创建
	 */
	public void allocatePlane(String flightname,String planeID,List<Timeslot> timeslots) throws ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException {
		int index=-1;
		for(Plane plane:planes) {
			if(plane.getId().equals(planeID)) {//根据飞机名称找到飞机
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
				copyEntry.allocateResource(onePlane);//新建一个航班，为其分配与搜索得到的航班一样的属性，观察是否会产生冲突
				List<FlightEntry<Plane>> flightEntries=new ArrayList<>();
				flightEntries.add(copyEntry);//不加入flights中，只是一个替代品，检测是否会发生冲突，避免真正分配带来的冲突
				flightEntries.addAll(flights);		
				PlanningEntryAPIs api=new PlanningEntryAPIsFirstImpl();
				boolean flag2=api.checkResourceExclusiveConflict(flightEntries);
				if(flag2) {
					throw new ResourceConflictException();
				}
				fe.allocateResource(onePlane);
				System.out.println("分配成功!");
				return;
			}
		}
		
		throw new PlanEntryNotCreateException(flightname);
	}
	
	/**
	 * @return 得到所有管理下的航班
	 */
	public List<FlightEntry<Plane>> getFlights() {
		return Collections.unmodifiableList(flights);
	}
	
	/**
	 * @return 得到所有管理下的机场
	 */
	public List<Location> getLocations() {
		return Collections.unmodifiableList(locations);
	}

	/**
	 * @return 得到所有管理之下的飞机
	 */
	public List<Plane> getPlanes() {
		return Collections.unmodifiableList(planes);
	}

	/**
	 * 指定航班进行起飞，航班应已经创建过,并且已经分配了飞机
	 * @param flightname 指定航班名称
	 * @param timeslots 航班的起飞和降落时间，为了区分不同日期的同名航班
	 * @throws PlanEntryNotCreateException 指定航班尚未创建
	 */
	public void departure(String flightname,List<Timeslot> timeslots) throws PlanEntryNotCreateException {
		for(FlightEntry<Plane> fe:flights) {
			if(fe.getName().equals(flightname)&&fe.getTime().containsAll(timeslots)) {//同名且时间点能够对应
				fe.start();
				return;
			}
		}
		
		throw new PlanEntryNotCreateException(flightname);
	}
	
	/**
	 * 取消指定航班，航班应该已经创建过，并且尚未起飞
	 * @param flightname 指定航班名称
	 * @param timeslots 航班的起飞和降落时间，为了区分不同日期的同名航班
	 * @throws PlanEntryNotCreateException 指定航班尚未创建
	 */
	public void cancelFlight(String flightname,List<Timeslot> timeslots) throws PlanEntryNotCreateException {
		for(FlightEntry<Plane> fe:flights) {
			if(fe.getName().equals(flightname)&&fe.getTime().containsAll(timeslots)) {//同名且时间点能够对应
				fe.cancel();
				return;
			}
		}
		
		throw new PlanEntryNotCreateException(flightname);
	}
	
	/**
	 * 得到指定航班的状态，航班应已创建
	 * @param flightname 指定航班的名称
	 * @param timeslots 航班的起飞和降落时间，为了区分不同日期的同名航班
	 * @return 如果该航班已创建，则返回其状态名称，否则抛出异常
	 * @throws PlanEntryNotCreateException 航班未创建
	 */
	public String getFlightState(String flightname,List<Timeslot> timeslots) throws PlanEntryNotCreateException {
		for(FlightEntry<Plane> fe:flights) {
			if(fe.getName().equals(flightname)&&fe.getTime().containsAll(timeslots)) {//同名且时间点能够对应
				return fe.getStateName();
			}
		}
		
		throw new PlanEntryNotCreateException(flightname);
	}
	
	/**
	 * 结束指定航班，航班应该已经创建过，并且已经起飞
	 * @param flightname 指定航班名称
	 * @param timeslots 航班的起飞和降落时间，为了区分不同日期的同名航班
	 * @throws PlanEntryNotCreateException 指定航班尚未创建
	 */
	public void endFlight(String flightname,List<Timeslot> timeslots) throws PlanEntryNotCreateException {
		for(FlightEntry<Plane> fe:flights) {
			if(fe.getName().equals(flightname)&&fe.getTime().containsAll(timeslots)) {//同名且时间点能够对应
				fe.end();
				return;
			}
		}
		
		throw new PlanEntryNotCreateException(flightname);
	}
	
	/**
	 * 获得占用指定飞机的所有航班
	 * @param planeID 指定飞机的ID 
	 * @return 如果有占用指定飞机的航班,返回这样的所有航班；否则返回null
	 */
	public List<FlightEntry<Plane>> getFlightssofassignPlane(String planeID){
		int index=-1;
		for(Plane plane:planes) {//找到指定飞机
			if(plane.getId().equals(planeID)) {
				index=planes.indexOf(plane);
			}
		}
		if(index==-1) {
			System.out.println("目前管理的飞机中不存在该ID飞机!");
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
	 * 通过飞机的ID得到对应的飞机
	 * @param planeID 飞机ID
	 * @return 对应的飞机,未创建则返回null
	 */
	public Plane getPlanebyID(String planeID) {
		for(Plane plane:planes) {
			if(plane.getId().equals(planeID))
				return plane;
		}
		return null;
	}
	
	/**
	 * 通过位置名称得到对应的位置
	 * @param locationName 位置的名称
	 * @return 对应的位置,未创建则返回null
	 */
	public Location getLocationbyName(String locationName) {
		for(Location location:locations) {
			if(location.getName().equals(locationName))
				return location;
		}
		return null;
	}
	
	/**
	 * 通过航班名称获得对应航班
	 * @param flightName 航班名称
	 * @param timeslots 航班的起飞时间和降落时间，为了区分不同日期的同名航班
	 * @return 对应的航班,未创建则返回null
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
	 * 判断两个航班名称是否代表了同一个航班
	 * @param flightname1 其中一个航班名称
	 * @param flightname2 另一个航班名称
	 * @return 如果代表同一个航班则返回true，否则返回false
	 */
	public boolean ifTwoSameFlightName(String flightname1,String flightname2) {
		if(flightname1.equals(flightname2))
			return true;
		Pattern pattern=Pattern.compile("([A-Z]{2})(\\d{2,4})");
		Matcher matcher1=pattern.matcher(flightname1);
		Matcher matcher2=pattern.matcher(flightname2);
		if(matcher1.find()&&matcher2.find()) {//通过文件生成的标准格式中对于CA0001,CA001,CA01等看做相同的判断
			String string1=matcher1.group(1);
			String string2=matcher2.group(1);
			int num1=Integer.valueOf(matcher1.group(2));
			int num2=Integer.valueOf(matcher2.group(2));
			if(string1.equals(string2)&&num1==num2)//前面字母相同，后面数字的值相同，则认为也是同一个航班
				return true;
		}
		
		return false;
	}
	
	/**
	 * 判断两个飞机是否拥有相同的ID，但其他部分却不同，即是否非法
	 * @param plane1 其中一个飞机
	 * @param plane2 另一个飞机
	 * @return 如果两个飞机ID相同，且其他部分不同，返回true，否则返回false
	 */
	private boolean ifOneofTwoSameIDPlaneIllegal(Plane plane1,Plane plane2) {
		if(!plane1.getId().equals(plane2.getId()))//允许ID不同
			return false;
		if(plane1.getPlaneage()==plane2.getPlaneage()&&plane1.getSeats()==plane2.getSeats()&&plane1.getType().equals(plane2.getType()))//所有部分均相同，合法
			return false;
		return true;//存在不同部分，非法
	}
	
}