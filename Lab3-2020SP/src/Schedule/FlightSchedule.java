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
 * 航班管理，可对多个机场，航班，飞机进行管理，可变类
 * @author 123
 *
 */

public class FlightSchedule {
	private List<FlightEntry<Plane>> flights=new ArrayList<>();//航班
	private List<Location> locations=new ArrayList<Location>();//机场
	private List<Plane> planes=new ArrayList<Plane>();//飞机
	private List<String> locationnames=new ArrayList<String>();//飞机机场的名称，每个名称的顺序与locations相同
	//Abstraction function:
	//	AF(flights,planes,locations)=一个对航班flights，所有飞机planes，机场locations进行管理的系统
	//Representation invariant:
	//	true
	//Safety from rep exposure:
	//	成员变量全是private的，对外返回时全部转为不可变类型，不存在表示泄露
	
	/**
	 * 增加一个可供管理的飞机，若飞机已存在不会重复添加
	 * @param plane 待添加的飞机
	 */
	public void addPlane(Plane plane) {
		if(!planes.contains(plane)) {
			planes.add(plane); //Plane为不可变类，不存在表示泄露
		}
	}
	
	/**
	 * 删除具有指定ID的飞机，若飞机不存在并不会进行删除操作 ,若有航班占用飞机，无法删除
	 * @param ID 待删除飞机的ID
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
			System.out.println("目前管理的飞机中不存在该ID飞机!");
			return;
		}
		
		boolean flag=false;
		for(FlightEntry<Plane> fe:flights) {//若有航班占用该飞机,且该航班已分配/运行中
			if(fe.getResource().contains(planes.get(index))&&(fe.getStateName().equals("Allocated")||fe.getStateName().equals("Running"))) {
				flag=true;
				break;
			}
		}
		if(!flag) {
			planes.remove(index);
			System.out.println("删除成功!");
			return;
		}
		
		System.out.println("目前存在航班占用该位置!删除失败!");
	}
	
	/**
	 * 增加一个可供管理的机场，若机场已存在不会重复添加
	 * @param location 待添加的机场
	 */
	public void addLocation(Location location) {
		if(!locations.contains(location)) {
			locations.add(location);
			locationnames.add(location.getName());
		}
			
	}
	
	/**
	 * 删除具有指定名称的机场，若机场不存在并不会进行删除操作 ，若有计划项在占用，不执行删除操作并提示
	 * @param locationname
	 */
	public void deleteLocation(String locationname) {
		
		if(locationnames.contains(locationname)) {
			int index=locationnames.indexOf(locationname);
			boolean flag=false;
			for(FlightEntry<Plane> fe:flights) {//若有航班占用该位置,且该航班未取消/结束
				if(fe.getLocation().contains(locations.get(index))&&!fe.getStateName().equals("Cancelled")&&!fe.getStateName().equals("Ended")) {
					flag=true;
					break;
				}
			}
			if(!flag) {
				locations.remove(index);
				locationnames.remove(index);
				System.out.println("删除成功!");
				return;
			}
			
			System.out.println("目前存在航班占用该位置!删除失败!");
		}
		else {
			System.out.println("目前管理的位置中不存在该位置!删除失败!");
		}
		
		
	}
	
	/**
	 * 增加一个航班对其进行管理，不允许重名航班.新建航班的初始状态为Waiting
	 * @param name 新建航班名称
	 * @param start 航班起飞的机场名称
	 * @param end 航班降落的机场名称
	 * @param timeslot 起始时间与终止时间构成的时间对
	 * @return 如果创建成功返回true，否则返回false
	 * @throws ParseException 时间未能转化为标准格式
	 */
	public boolean createFlight(String name,List<String> locationnameList,List<Timeslot> timeslots){
		if(timeslots.size()==0) {
			System.out.println("传入的时间对参数为空,请添加时间对后创建!");
			return false;
		}
		
		if(locations.size()!=timeslots.size()+1) {//由于时间对是不空的，时间对个数≥1，同时也保证了位置数≥2
			System.out.println("经停站点数与起止时间对个数不匹配,不能正确创建");
			return false;
		}
		
		if(locationnameList.size()!=2&&locationnameList.size()!=3) {
			System.out.println("经停站点数只能为2或3,位置数不符合要求,不能正确创建");
			return false;
		}
		
		List<Location> containlocations=new ArrayList<Location>();
		for(int i=0;i<locationnameList.size();i++) {
			String locationname=locationnameList.get(i);
			int index=locationnames.indexOf(locationname);
			if(index==-1) {
				System.out.println("分配的位置之中尚存在位置未纳入管理!");
				return false;
			}
			Location location=this.locations.get(index);//根据位置名称得到对应位置
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
					System.out.println("同一航班出发和到达机场应该相同!");
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
					//抵达和出发日期均相同的同名航班必定不能再创建,若时间相同则是重复创建，若时间不同不满足要求
					System.out.println("航班已创建!");
					return false;
				}
				else {
					if(oldstartCalendar.get(Calendar.HOUR_OF_DAY)!=newstartCalendar.get(Calendar.HOUR_OF_DAY)||oldstartCalendar.get(Calendar.MINUTE)!=newstartCalendar.get(Calendar.MINUTE)||oldendCalendar.get(Calendar.HOUR_OF_DAY)!=newendCalendar.get(Calendar.HOUR_OF_DAY)||oldendCalendar.get(Calendar.MINUTE)!=newendCalendar.get(Calendar.MINUTE)) {
						System.out.println("航班的时间节点不一致!");//不同日期，出发时间和抵达时间都应相同
						return false;
					}
				}
			}
		}
		flights.add(newflight);
		System.out.println("创建成功!");
		return true;
	}
	
	/**
	 * 通过读入文件创建航班
	 * @param path 文件路径
	 * @return 如果文件中的所有航班符合语法且全部创建成功,返回true，否则打印错误信息并返回false
	 */
	public boolean createFlightByFile(String path) {
		File file=new File(path);
		if(!file.exists()||!file.isFile()) {
			System.out.println("该文件不存在!");
			return false;
		}
		int number=0;
		InputStreamReader read;
		BufferedReader bufferedReader=null;
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
					Pattern pattern0=Pattern.compile("Flight:(\\d{4}-\\d{2}-\\d{2}),([A-Z]{2}\\d{2,4})");
					Matcher matcher=pattern0.matcher(line);
					if(!matcher.find()) {
						System.out.println("文本中Flight格式不符合语法!");
						return false;
					}
					date=matcher.group(1);
					String flightname=matcher.group(2);
					information[0]=flightname;//航班名称
					round++;
					break;
				}
				case 2:{
					Pattern pattern2=Pattern.compile("DepartureAirport:(\\w+)");
					Matcher matcher=pattern2.matcher(line);
					if(!matcher.find()) {
						System.out.println("文本中起飞机场格式不符合语法!");
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
						System.out.println("文本中抵达机场格式不符合语法!");
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
						System.out.println("文本中起飞时间格式不符合语法!");
						return false;
					}
					String departuredate=matcher.group(1);				
					if(!departuredate.equals(date)) {///起飞时间中的日期必须与第一行的日期一致 
						System.out.println("文本中起飞时间与航班时间不匹配!");
						return false;
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
						System.out.println("文本中抵达时间格式不符合语法!");
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
							information[4]=reachdate.concat(matcher.group(2));//在同一天
							round++;
						}
						else if(reachCalendar.get(0)==timeCalendar.get(0)&&reachCalendar.get(1)==timeCalendar.get(1)&&reachCalendar.get(6)==timeCalendar.get(6)+1) {
							information[4]=reachdate.concat(matcher.group(2));//在后一天
							round++;
						}
						else {
							System.out.println("文本中抵达时间与航班时间不匹配!");
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
						System.out.println("文本中Plane格式不符合语法!");
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
						System.out.println("文本中飞机的Type格式不符合语法!");
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
						System.out.println("文本中飞机的座位数格式不符合语法!");
						return false;
					}
					seats=Integer.valueOf(matcher.group(1));
					if(seats<50||seats>600) {
						System.out.println("文本中飞机座位数大小不符合要求!");
						return false;
					}
					round++;
					break;
				}
				case 10:{
					Pattern pattern10=Pattern.compile("Age:([1-9]?[0-9]?.[0-9]?)");
					Matcher matcher=pattern10.matcher(line);
					if(!matcher.find()) {
						System.out.println("文本中机龄格式不符合语法!");
						return false;
					}
					age=Double.valueOf(matcher.group(1));
					if(age<0||age>30) {
						System.out.println("文本中飞机机龄大小不符合要求!");
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
				System.out.println("文本不符合语法!");
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
	 * 为航班分配飞机，飞机应包含在管理的所有飞机之中
	 * @param flightname 指定的航班名称
	 * @param plane 待分配的飞机名称
	 * @param timeslots 航班的起飞时间和降落时间，为了区分不同日期的同名航班
	 */
	public void allocatePlane(String flightname,String planeID,List<Timeslot> timeslots) {
		int index=-1;
		for(Plane plane:planes) {
			if(plane.getId().equals(planeID)) {//根据飞机名称找到飞机
				index=planes.indexOf(plane);
			}
		}
		if(index==-1) {
			System.out.println("分配的飞机尚未纳入管理!");
			return;
		}
		
		for(FlightEntry<Plane> fe:flights) {
			if(fe.getName().equals(flightname)&&fe.getTime().containsAll(timeslots)) {
				List<Plane> onePlane=new ArrayList<Plane>();
				onePlane.add(planes.get(index));
				fe.allocateResource(onePlane);
				System.out.println("分配成功!");
				PlanningEntryAPIs api=new PlanningEntryAPIsFirstImpl();
				boolean flag2=api.checkResourceExclusiveConflict(flights);
				if(flag2) {
					System.out.println("请注意,新建航班与其他航班有飞机分配冲突!请考虑是否取消!");
				}
				return;
			}
		}
		
		System.out.println("指定航班还未创建，无法分配飞机!");
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
	 */
	public void departure(String flightname,List<Timeslot> timeslots) {
		for(FlightEntry<Plane> fe:flights) {
			if(fe.getName().equals(flightname)&&fe.getTime().containsAll(timeslots)) {//同名且时间点能够对应
				fe.start();
				return;
			}
		}
		
		System.out.println("指定航班不存在，无法起飞!");
	}
	
	/**
	 * 取消指定航班，航班应该已经创建过，并且尚未起飞
	 * @param flightname 指定航班名称
	 * @param timeslots 航班的起飞和降落时间，为了区分不同日期的同名航班
	 */
	public void cancelFlight(String flightname,List<Timeslot> timeslots) {
		for(FlightEntry<Plane> fe:flights) {
			if(fe.getName().equals(flightname)&&fe.getTime().containsAll(timeslots)) {//同名且时间点能够对应
				fe.cancel();
				return;
			}
		}
		
		System.out.println("指定航班不存在，无法取消!");
	}
	
	/**
	 * 得到指定航班的状态
	 * @param flightname 指定航班的名称
	 * @param timeslots 航班的起飞和降落时间，为了区分不同日期的同名航班
	 * @return 如果该航班已创建，则返回其状态名称，否则返回一个 “null”字符串
	 */
	public String getFlightState(String flightname,List<Timeslot> timeslots) {
		for(FlightEntry<Plane> fe:flights) {
			if(fe.getName().equals(flightname)&&fe.getTime().containsAll(timeslots)) {//同名且时间点能够对应
				return fe.getStateName();
			}
		}
		
		return "null";
	}
	
	/**
	 * 结束指定航班，航班应该已经创建过，并且已经起飞
	 * @param flightname 指定航班名称
	 * @param timeslots 航班的起飞和降落时间，为了区分不同日期的同名航班
	 */
	public void endFlight(String flightname,List<Timeslot> timeslots) {
		for(FlightEntry<Plane> fe:flights) {
			if(fe.getName().equals(flightname)&&fe.getTime().containsAll(timeslots)) {//同名且时间点能够对应
				fe.end();
				return;
			}
		}
		
		System.out.println("指定航班不存在!");
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
	 * 阻塞指定的已经创建的航班
	 * @param Flightname 待阻塞的已经创建的航班
	 * @param timeslots 指定航班的起飞时间和降落时间,为了区分不同日期的同名航班
	 */
	public void blockFlight(String Flightname,List<Timeslot> timeslots) {
		for(FlightEntry<Plane> fe:flights) {
			if(fe.getName().equals(Flightname)&&fe.getTime().containsAll(timeslots)) {
				fe.block(fe);
				return;
			}
		}
		
		System.out.println("指定航班还未创建，阻塞失败!");
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
	
}