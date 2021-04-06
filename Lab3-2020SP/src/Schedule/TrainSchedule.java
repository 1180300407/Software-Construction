package Schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import API.PlanningEntryAPIs;
import API.PlanningEntryAPIsFirstImpl;
import Location.Location;
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
	//Abstraction function:
	//	AF(trains,carriages,locations,locationnames)=一个对高铁车次trains，所有车厢carriages，名称为locationnames的高铁站次locations进行管理的系统
	//Representation invariant:
	//	locationnames中高铁站次的名称，每个名称的顺序与locations相同
	//Safety from rep exposure:
	//	成员变量全是private的，对外返回时全部转为不可变类型，不存在表示泄露
	
	/**
	 * 增加一个可供管理的车厢，若车厢已存在不会重复添加
	 * @param carriage 待增加的车厢
	 */
	public void addCarriage(Carriage carriage) {
		if(!carriages.contains(carriage)) {
			carriages.add(carriage);
		}
	}
	
	/**
	 * 删除具有指定ID的车厢，若车厢不存在并不会进行删除操作 ,若车厢已被分配则无法删除
	 * @param ID 待删除车厢的编号
	 */
	public void deleteCarriage(String ID) {
		int index=-1;
		for(Carriage carriage:carriages) {
			if(carriage.getId()==ID) {
				index=carriages.indexOf(carriage);
				break;
			}
		}
		if(index==-1) {
			System.out.println("不存在该编号车厢,删除失败!");
			return;
		}
		boolean flag=false;
		for(TrainEntry<Carriage> te:trains) {
			if(te.getResource().contains(carriages.get(index))&&!te.getStateName().equals("Cancelled")&&!te.getStateName().equals("Ended")&&!te.getStateName().equals("Waiting")) {
				flag=true;
			}
		}
		
		if(!flag) {
			carriages.remove(index);
			return;
		}
		
		System.out.println("目前车厢已被分配给列车,删除失败!");
	}
	
	/**
	 * 增加一个可供管理的位置，若位置已存在不会重复添加
	 * @param location 待添加的位置
	 */
	public void addLocation(Location location) {
		if(!locations.contains(location)) {
			locations.add(location);
			locationnames.add(location.getName());
		}	
	}
	
	/**
	 * 删除具有指定名称的位置，若位置不存在并不会进行删除操作 ,若该位置已被某列车占用则无法分配
	 * @param locationname 待删除位置的名称
	 */
	public void deleteLocation(String locationname) {
		int index=locationnames.indexOf(locationname);
		if(index==-1) {
			System.out.println("目前管理的位置中不存在该位置!删除失败!");
			return;
		}
		boolean flag=false;
		for(TrainEntry<Carriage> te:trains) {	
			if(te.getLocation().contains(locations.get(index))&&!te.getStateName().equals("Cancelled")&&!te.getStateName().equals("Ended")) {
				flag=true;
			}
		}
		if(flag) {
			System.out.println("目前该位置中被列车占用!删除失败!");
			return;
		}
		locations.remove(index);
		locationnames.remove(index);
	}
	
	/**
	 * 增加一个高铁车次对其进行管理，不允许重名车次,列车初始状态为Waiting
	 * @param name 高铁车次的名称
	 * @param locations 高铁车次经过的所有站点的名称，应该包含在所管理的位置locations之中,总个数应该≥2，站点应是有序的
	 * @param timeslots 高铁车次中途所有经停时间点，不应为空,timeslots的顺序应该与各个location的顺序是对应的
	 * @return 如果创建成功返回true，否则返回false
	 */
	public boolean createTrain(String name,List<String> locationnames,List<Timeslot> timeslots) {
		if(timeslots.size()==0) {
			System.out.println("传入的时间对参数为空,请添加时间对后创建!");
			return false;
		}
		
		if(locationnames.size()!=timeslots.size()+1) {//由于时间对是不空的，时间对个数≥1，同时也保证了位置数≥2
			System.out.println("经停站点数与起止时间对个数不匹配,不能正确创建");
			return false;
		}
		List<Location> containlocations=new ArrayList<Location>();
		for(int i=0;i<locationnames.size();i++) {
			String locationname=locationnames.get(i);
			int index=this.locationnames.indexOf(locationname);
			if(index==-1) {
				System.out.println("分配的位置之中尚存在位置未纳入管理!");
				return false;
			}
			Location location=this.locations.get(index);
			containlocations.add(location);
		}
		
		TrainEntry<Carriage> newTrain=TrainPlanningEntry.CreateTrain(name);
		newTrain.setLocations(containlocations);
		newTrain.setTime(timeslots);
		for(TrainEntry<Carriage> tpe:trains) {
			if(tpe.getName().equals(name)) {
				System.out.println("该列车已存在，不能重复创建");
				return false;
			}
		}
		
		trains.add(newTrain);
		return true;
	}
	
	/**
	 * 为高铁车次分配车厢，车厢应包含在管理的所有车厢之中
	 * @param Trainname 指定高铁车次的列车名称
	 * @param carriageResourse 分配的所有车厢的ID,已经确定其相对顺序
	 */
	public void allocateCarriage(String Trainname,List<String> carriageIDs) {
		Carriage carriage;
		List<Carriage> carriageResourse=new ArrayList<Carriage>();
		for(int i=0;i<carriageIDs.size();i++) {
			carriage=new Carriage(carriageIDs.get(i), "type", 1, "");//利用Carriage的equals中的唯一条件是ID相等
			if(this.carriages.contains(carriage)) {//得到carriages中的对应车厢
				int index=carriages.indexOf(carriage);
				carriageResourse.add(carriages.get(index));
			}
			else {
				System.out.println("分配的车厢之中尚存在车厢未纳入管理!");
				return;
			}
		}
		
		for(TrainEntry<Carriage> te:trains) {
			if(te.getName().equals(Trainname)) {
				te.allocateResource(carriageResourse);
				PlanningEntryAPIs api=new PlanningEntryAPIsFirstImpl();
				boolean flag2=api.checkResourceExclusiveConflict(trains);
				if(flag2) {
					System.out.println("请注意,新建车次与其他车次有车厢分配冲突!请考虑是否取消!");
				}
				return;
			}
		}
		
		System.out.println("指定列车还未创建，无法分配车厢!");
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
	 */
	public void startTrain(String Trainname) {
		for(TrainEntry<Carriage> te:trains) {
			if(te.getName().equals(Trainname)) {
				te.start();
				return;
			}
		}
		
		System.out.println("指定列车还未创建，无法启动!");
	}
	
	/**
	 * 阻塞指定的已经创建的高铁车次
	 * @param Trainname 待阻塞的已经创建的高铁车次
	 */
	public void blockTrain(String Trainname) {
		for(TrainEntry<Carriage> te:trains) {
			if(te.getName().equals(Trainname)) {
				te.block(te);
				return;
			}
		}
		
		System.out.println("指定列车还未创建，阻塞失败!");
	}
	
	/**
	 * 取消指定的已经创建的高铁车次
	 * @param Trainname 待取消的已经创建的高铁车次
	 */
	public void cancelTrain(String Trainname) {
		for(TrainEntry<Carriage> te:trains) {
			if(te.getName().equals(Trainname)) {
				te.cancel();
				return;
			}
		}
		
		System.out.println("指定列车还未创建，取消失败!");
	}
	
	/**
	 * 结束指定的已经创建的高铁车次
	 * @param Trainname 待结束的已经创建的高铁车次
	 */
	public void endTrain(String Trainname) {
		for(TrainEntry<Carriage> te:trains) {
			if(te.getName().equals(Trainname)) {
				te.end();
				return;
			}
		}
		
		System.out.println("指定列车还未创建!");
	}
	
	/**
	 * 输出某一高铁车次目前的状态
	 * @param Trainname 指定的已创建的高铁车次
	 */
	public String getTrainState(String Trainname) {
		for(TrainEntry<Carriage> te:trains) {
			if(te.getName().equals(Trainname)) {
				return te.getStateName();
			}
		}
		
		return "null";
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
