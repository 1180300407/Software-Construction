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
import Exceptions.LocationNotFoundException;
import Exceptions.PlanEntryNotCreateException;
import Exceptions.PlanEntryOccupyLocationException;
import Exceptions.PlanEntryOccupyResourceException;
import Exceptions.ResourceConflictException;
import Exceptions.ResourceNotFoundException;
import Exceptions.SameLabelException;
import Location.Location;
import LogFile.MyFormatter;
import Resources.Carriage;
import Timeslot.Timeslot;
import compositeinterface.TrainEntry;
import compositeinterface.TrainPlanningEntry;

/**
 * 高铁车次管理，可对多个高铁站次，高铁车次，以及多个车厢进行管理，可变类
 * @author 123
 *
 */

public class TrainSchedule {
	private List<TrainEntry<Carriage>> trains=new ArrayList<>();//高铁车次
	private List<Carriage> carriages=new ArrayList<Carriage>();//车厢
	private List<Location> locations=new ArrayList<Location>();//高铁站次
	private List<String> locationnames=new ArrayList<String>();//高铁站次的名称，每个名称的顺序与locations相同
	private static Logger myLogger=Logger.getLogger("TrainScheduleLog");
	//Abstraction function:
	//	AF(trains,carriages,locations,locationnames)=一个对高铁车次trains，所有车厢carriages，名称为locationnames的高铁站次locations进行管理的系统
	//Representation invariant:
	//	locationnames中高铁站次的名称，每个名称的顺序与locations相同
	//	管理的课程、教师、教室中都不应含有重复元素
	//Safety from rep exposure:
	//	成员变量全是private的，对外返回时全部转为不可变类型，不存在表示泄露
	private void checkRep() {
		assert locationnames.size()==locations.size();
		for(int i=0;i<locationnames.size();i++) {
			assert locationnames.get(i).equals(locations.get(i).getName());
		}
		Set<String> strings=new HashSet<String>(locationnames); 
		Set<Carriage> carriageSet=new HashSet<Carriage>(carriages);
		Set<TrainEntry<Carriage>> trainEntrieSet=new HashSet<>(trains);
		assert strings.size()==locationnames.size();
		assert carriageSet.size()==carriages.size();
		assert trainEntrieSet.size()==trains.size();
		//日志记录
		myLogger.setLevel(Level.INFO);
		myLogger.setUseParentHandlers(false);
		//写入文件
		FileHandler handler;
		try {
			handler = new FileHandler("src/LogFile/TrainScheduleLog.log");
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
	 * 增加一个可供管理的车厢，若车厢已存在不会重复添加
	 * @param carriage 待增加的车厢
	 */
	public void addCarriage(Carriage carriage) {
		if(!carriages.contains(carriage)) {
			carriages.add(carriage);
			checkRep();
		}
	}
	
	/**
	 * 删除具有指定ID的车厢，车厢应该已存在 ,且未被分配
	 * @param ID 待删除车厢的编号
	 * @throws ResourceNotFoundException 车厢尚未创建
	 * @throws PlanEntryOccupyResourceException 车厢已被分配
	 */
	public void deleteCarriage(String ID) throws ResourceNotFoundException, PlanEntryOccupyResourceException {
		int index=-1;
		for(Carriage carriage:carriages) {
			if(carriage.getId()==ID) {
				index=carriages.indexOf(carriage);
				break;
			}
		}
		if(index==-1) {
			throw new ResourceNotFoundException(ID);
		}
		boolean flag=false;
		TrainEntry<Carriage> trainEntry=null;
		for(TrainEntry<Carriage> te:trains) {
			if(te.getResource().contains(carriages.get(index))&&!te.getStateName().equals("Cancelled")&&!te.getStateName().equals("Ended")&&!te.getStateName().equals("Waiting")) {
				flag=true;
				trainEntry=te;
				break;
			}
		}
		
		if(!flag) {
			carriages.remove(index);
			return;
		}
		
		throw new PlanEntryOccupyResourceException(trainEntry.getName());
	}
	
	/**
	 * 增加一个可供管理的位置，若位置已存在不会重复添加
	 * @param location 待添加的位置
	 */
	public void addLocation(Location location) {
		if(!locations.contains(location)) {
			locations.add(location);
			locationnames.add(location.getName());
			checkRep();
		}	
	}
	
	/**
	 * 删除具有指定名称的位置，位置应已创建 ,且未被占用
	 * @param locationname 待删除位置的名称
	 * @throws LocationNotFoundException 待删除位置尚未创建
	 * @throws PlanEntryOccupyLocationException 待删除位置尚未创建
	 */
	public void deleteLocation(String locationname) throws LocationNotFoundException, PlanEntryOccupyLocationException {
		int index=locationnames.indexOf(locationname);
		if(index==-1) {
			throw new LocationNotFoundException(locationname);
		}
		boolean flag=false;
		TrainEntry<Carriage> trainEntry=null;
		for(TrainEntry<Carriage> te:trains) {	
			if(te.getLocation().contains(locations.get(index))&&!te.getStateName().equals("Cancelled")&&!te.getStateName().equals("Ended")) {
				flag=true;
				trainEntry=te;
				break;
			}
		}
		if(flag) {
			throw new PlanEntryOccupyLocationException(trainEntry.getName());
		}
		locations.remove(index);
		locationnames.remove(index);
		checkRep();
	}
	
	/**
	 * 增加一个高铁车次对其进行管理，不允许重名车次
	 * @param name 高铁车次的名称
	 * @param locations 高铁车次经过的所有站点的名称，应该包含在所管理的位置locations之中,总个数应该≥2，站点应是有序的
	 * @param timeslots 高铁车次中途所有经停时间点，不应为空,timeslots的顺序应该与各个location的顺序是对应的
	 * @return 如果创建成功返回true，否则返回false,创建成功的列车初始状态为Waiting
	 * @throws LocationNotFoundException 位置中存在未创建位置
	 * @throws SameLabelException 重名车次
	 */
	public boolean createTrain(String name,List<String> locationnames,List<Timeslot> timeslots) throws LocationNotFoundException, SameLabelException {
		if(timeslots==null||locationnames==null||locationnames.size()==0||timeslots.size()==0) {
			System.out.println("传入空参数,请添加后创建!");
			return false;
		}
		
		if(locationnames.size()!=timeslots.size()+1) {//时间对个数与位置数匹配
			System.out.println("经停站点数与起止时间对个数不匹配,不能正确创建");
			return false;
		}
		List<Location> containlocations=new ArrayList<Location>();
		for(int i=0;i<locationnames.size();i++) {
			String locationname=locationnames.get(i);
			int index=this.locationnames.indexOf(locationname);
			if(index==-1) {
				throw new LocationNotFoundException(locationname);
			}
			Location location=this.locations.get(index);
			containlocations.add(location);
		}
		
		TrainEntry<Carriage> newTrain=TrainPlanningEntry.CreateTrain(name);
		newTrain.setLocations(containlocations);
		newTrain.setTime(timeslots);
		for(TrainEntry<Carriage> tpe:trains) {
			if(tpe.getName().equals(name)) {
				throw new SameLabelException(name);
			}
		}
		
		assert newTrain.getStateName().equals("Waiting")==true;
		trains.add(newTrain); 
		checkRep();
		return true;
	}
	
	/**
	 * 为高铁车次分配车厢，车厢应包含在管理的所有车厢之中
	 * @param Trainname 指定高铁车次的列车名称
	 * @param carriageResourse 分配的所有车厢的ID,已经确定其相对顺序
	 * @throws ResourceNotFoundException 车厢中存在未创建车厢
	 * @throws PlanEntryNotCreateException 车次未创建
	 * @throws ResourceConflictException 车厢分配存在冲突
	 */
	public void allocateCarriage(String Trainname,List<String> carriageIDs) throws ResourceNotFoundException, PlanEntryNotCreateException, ResourceConflictException {
		Carriage carriage;
		List<Carriage> carriageResourse=new ArrayList<Carriage>();
		for(int i=0;i<carriageIDs.size();i++) {
			carriage=new Carriage(carriageIDs.get(i), "type", 1, "");//利用Carriage的equals中的唯一条件是ID相等
			if(this.carriages.contains(carriage)) {//得到carriages中的对应车厢
				int index=carriages.indexOf(carriage);
				carriageResourse.add(carriages.get(index));
			}
			else {
				throw new ResourceNotFoundException(carriage.getId());
			}
		}
		
		for(TrainEntry<Carriage> te:trains) {
			if(te.getName().equals(Trainname)) {
				TrainEntry<Carriage> trainEntry=new TrainEntry<Carriage>("null");//新建一个列车，为其分配与搜索得到的列车一样的属性，观察是否会产生冲突
				trainEntry.setLocations(te.getLocation());
				trainEntry.setTime(te.getTime());
				trainEntry.allocateResource(carriageResourse);
				List<TrainEntry<Carriage>> trainEntries=new ArrayList<>();
				trainEntries.add(trainEntry);//不加入trains中，只是一个替代品，检测是否会发生冲突，避免真正分配带来的冲突
				trainEntries.addAll(trains);
				PlanningEntryAPIs api=new PlanningEntryAPIsFirstImpl();
				boolean flag2=api.checkResourceExclusiveConflict(trainEntries);
				if(flag2) {
					throw new ResourceConflictException();
				}
				te.allocateResource(carriageResourse);
				return;
			}
		}
		
		throw new PlanEntryNotCreateException(Trainname);
	}

	/**
	 * @return 得到所有已经纳入管理的高铁车次
	 */
	public List<TrainEntry<Carriage>> getTrains() {
		return Collections.unmodifiableList(trains);
	}

	/**
	 * @return 得到所有已经纳入管理的车厢
	 */
	public List<Carriage> getCarriages() {
		return Collections.unmodifiableList(carriages);
	}

	/** 
	 * @return 得到所有已经纳入管理的位置
	 */
	public List<Location> getLocations() {
		return Collections.unmodifiableList(locations);
	}
	
	public List<String> getLocationNames(){
		return Collections.unmodifiableList(locationnames);
	}
	
	/**
	 * 启动指定的已经创建的高铁车次
	 * @param Trainname 待启动的已经创建的高铁车次的名称
	 * @throws PlanEntryNotCreateException 车次尚未创建
	 */
	public void startTrain(String Trainname) throws PlanEntryNotCreateException {
		for(TrainEntry<Carriage> te:trains) {
			if(te.getName().equals(Trainname)) {
				te.start();
				return;
			}
		}
		
		throw new PlanEntryNotCreateException(Trainname);
	}
	
	/**
	 * 阻塞指定的已经创建的高铁车次
	 * @param Trainname 待阻塞的已经创建的高铁车次
	 * @throws PlanEntryNotCreateException 车次尚未创建
	 */
	public void blockTrain(String Trainname) throws PlanEntryNotCreateException {
		for(TrainEntry<Carriage> te:trains) {
			if(te.getName().equals(Trainname)) {
				te.block(te);
				return;
			}
		}
		
		throw new PlanEntryNotCreateException(Trainname);
	}
	
	/**
	 * 取消指定的已经创建的高铁车次
	 * @param Trainname 待取消的已经创建的高铁车次
	 * @throws PlanEntryNotCreateException 车次尚未创建
	 */
	public void cancelTrain(String Trainname) throws PlanEntryNotCreateException {
		for(TrainEntry<Carriage> te:trains) {
			if(te.getName().equals(Trainname)) {
				te.cancel();
				return;
			}
		}
		
		throw new PlanEntryNotCreateException(Trainname);
	}
	
	/**
	 * 结束指定的已经创建的高铁车次
	 * @param Trainname 待结束的已经创建的高铁车次
	 * @throws PlanEntryNotCreateException 车次尚未创建
	 */
	public void endTrain(String Trainname) throws PlanEntryNotCreateException {
		for(TrainEntry<Carriage> te:trains) {
			if(te.getName().equals(Trainname)) {
				te.end();
				return;
			}
		}
		
		throw new PlanEntryNotCreateException(Trainname);
	}
	
	/**
	 * 输出某一高铁车次目前的状态
	 * @param Trainname 指定的已创建的高铁车次
	 * @return 列车当前状态
	 * @throws PlanEntryNotCreateException 列车未创建
	 */
	public String getTrainState(String Trainname) throws PlanEntryNotCreateException {
		for(TrainEntry<Carriage> te:trains) {
			if(te.getName().equals(Trainname)) {
				return te.getStateName();
			}
		}
		
		throw new PlanEntryNotCreateException(Trainname);
	}
	
	/**
	 * 获得占用指定车厢的所有列车
	 * @param carriageID 指定车厢的ID 
	 * @return 如果有占用指定车厢的列车,返回这样的所有列车；否则返回null
	 */
	public List<TrainEntry<Carriage>> getTrainsofassignCarriage(String carriageID){
		int index=-1;
		for(Carriage carriage:carriages) {//找到指定飞机
			if(carriage.getId().equals(carriageID)) {
				index=carriages.indexOf(carriage);
			}
		}
		if(index==-1) {
			System.out.println("目前管理的车厢中不存在该ID车厢!");
			return null;
		}
			
		Carriage assigncarriage=carriages.get(index);
		List<TrainEntry<Carriage>> tes=new ArrayList<>();
		for(TrainEntry<Carriage> te:trains) {
			if(te.getResource().contains(assigncarriage))
				tes.add(te);
		}
		if(tes.isEmpty())
			return null;
		return tes;
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
	 * 通过车厢的ID获得对应的车厢
	 * @param ID 车厢的ID
	 * @return 对应的车厢，未创建则返回null
	 */
	public Carriage getCarriagebyID(String ID) {
		for(Carriage carriage:carriages) {
			if(carriage.getId().equals(ID))
				return carriage;
		}
		return null;
	}
	
	/**
	 * 通过列车名称获得对应的列车车次
	 * @param trainName 列车名称
	 * @return 对应的列车车次，未创建则返回null
	 */
	public TrainEntry<Carriage> getTrainbyName(String trainName){
		for(TrainEntry<Carriage> te:trains) {
			if(te.getName().equals(trainName))
				return te;
		}
		return null;
	}
	
}
