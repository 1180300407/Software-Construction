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
 * �������ι����ɶԶ������վ�Σ��������Σ��Լ����������й����ɱ���
 * @author 123
 *
 */

public class TrainSchedule {
	private List<TrainEntry<Carriage>> trains=new ArrayList<>();//��������
	private List<Carriage> carriages=new ArrayList<Carriage>();//����
	private List<Location> locations=new ArrayList<Location>();//����վ��
	private List<String> locationnames=new ArrayList<String>();//����վ�ε����ƣ�ÿ�����Ƶ�˳����locations��ͬ
	//Abstraction function:
	//	AF(trains,carriages,locations,locationnames)=һ���Ը�������trains�����г���carriages������Ϊlocationnames�ĸ���վ��locations���й����ϵͳ
	//Representation invariant:
	//	locationnames�и���վ�ε����ƣ�ÿ�����Ƶ�˳����locations��ͬ
	//Safety from rep exposure:
	//	��Ա����ȫ��private�ģ����ⷵ��ʱȫ��תΪ���ɱ����ͣ������ڱ�ʾй¶
	
	/**
	 * ����һ���ɹ�����ĳ��ᣬ�������Ѵ��ڲ����ظ����
	 * @param carriage �����ӵĳ���
	 */
	public void addCarriage(Carriage carriage) {
		if(!carriages.contains(carriage)) {
			carriages.add(carriage);
		}
	}
	
	/**
	 * ɾ������ָ��ID�ĳ��ᣬ�����᲻���ڲ��������ɾ������ ,�������ѱ��������޷�ɾ��
	 * @param ID ��ɾ������ı��
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
			System.out.println("�����ڸñ�ų���,ɾ��ʧ��!");
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
		
		System.out.println("Ŀǰ�����ѱ�������г�,ɾ��ʧ��!");
	}
	
	/**
	 * ����һ���ɹ������λ�ã���λ���Ѵ��ڲ����ظ����
	 * @param location ����ӵ�λ��
	 */
	public void addLocation(Location location) {
		if(!locations.contains(location)) {
			locations.add(location);
			locationnames.add(location.getName());
		}	
	}
	
	/**
	 * ɾ������ָ�����Ƶ�λ�ã���λ�ò����ڲ��������ɾ������ ,����λ���ѱ�ĳ�г�ռ�����޷�����
	 * @param locationname ��ɾ��λ�õ�����
	 */
	public void deleteLocation(String locationname) {
		int index=locationnames.indexOf(locationname);
		if(index==-1) {
			System.out.println("Ŀǰ�����λ���в����ڸ�λ��!ɾ��ʧ��!");
			return;
		}
		boolean flag=false;
		for(TrainEntry<Carriage> te:trains) {	
			if(te.getLocation().contains(locations.get(index))&&!te.getStateName().equals("Cancelled")&&!te.getStateName().equals("Ended")) {
				flag=true;
			}
		}
		if(flag) {
			System.out.println("Ŀǰ��λ���б��г�ռ��!ɾ��ʧ��!");
			return;
		}
		locations.remove(index);
		locationnames.remove(index);
	}
	
	/**
	 * ����һ���������ζ�����й�����������������,�г���ʼ״̬ΪWaiting
	 * @param name �������ε�����
	 * @param locations �������ξ���������վ������ƣ�Ӧ�ð������������λ��locations֮��,�ܸ���Ӧ�á�2��վ��Ӧ�������
	 * @param timeslots ����������;���о�ͣʱ��㣬��ӦΪ��,timeslots��˳��Ӧ�������location��˳���Ƕ�Ӧ��
	 * @return ��������ɹ�����true�����򷵻�false
	 */
	public boolean createTrain(String name,List<String> locationnames,List<Timeslot> timeslots) {
		if(timeslots.size()==0) {
			System.out.println("�����ʱ��Բ���Ϊ��,�����ʱ��Ժ󴴽�!");
			return false;
		}
		
		if(locationnames.size()!=timeslots.size()+1) {//����ʱ����ǲ��յģ�ʱ��Ը�����1��ͬʱҲ��֤��λ������2
			System.out.println("��ͣվ��������ֹʱ��Ը�����ƥ��,������ȷ����");
			return false;
		}
		List<Location> containlocations=new ArrayList<Location>();
		for(int i=0;i<locationnames.size();i++) {
			String locationname=locationnames.get(i);
			int index=this.locationnames.indexOf(locationname);
			if(index==-1) {
				System.out.println("�����λ��֮���д���λ��δ�������!");
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
				System.out.println("���г��Ѵ��ڣ������ظ�����");
				return false;
			}
		}
		
		trains.add(newTrain);
		return true;
	}
	
	/**
	 * Ϊ�������η��䳵�ᣬ����Ӧ�����ڹ�������г���֮��
	 * @param Trainname ָ���������ε��г�����
	 * @param carriageResourse ��������г����ID,�Ѿ�ȷ�������˳��
	 */
	public void allocateCarriage(String Trainname,List<String> carriageIDs) {
		Carriage carriage;
		List<Carriage> carriageResourse=new ArrayList<Carriage>();
		for(int i=0;i<carriageIDs.size();i++) {
			carriage=new Carriage(carriageIDs.get(i), "type", 1, "");//����Carriage��equals�е�Ψһ������ID���
			if(this.carriages.contains(carriage)) {//�õ�carriages�еĶ�Ӧ����
				int index=carriages.indexOf(carriage);
				carriageResourse.add(carriages.get(index));
			}
			else {
				System.out.println("����ĳ���֮���д��ڳ���δ�������!");
				return;
			}
		}
		
		for(TrainEntry<Carriage> te:trains) {
			if(te.getName().equals(Trainname)) {
				te.allocateResource(carriageResourse);
				PlanningEntryAPIs api=new PlanningEntryAPIsFirstImpl();
				boolean flag2=api.checkResourceExclusiveConflict(trains);
				if(flag2) {
					System.out.println("��ע��,�½����������������г�������ͻ!�뿼���Ƿ�ȡ��!");
				}
				return;
			}
		}
		
		System.out.println("ָ���г���δ�������޷����䳵��!");
	}

	/**
	 * @return �õ������Ѿ��������ĸ�������
	 */
	public List<TrainEntry<Carriage>> getTrains() {
		return Collections.unmodifiableList(trains);
	}

	/**
	 * @return �õ������Ѿ��������ĳ���
	 */
	public List<Carriage> getCarriages() {
		return Collections.unmodifiableList(carriages);
	}

	/** 
	 * @return �õ������Ѿ���������λ��
	 */
	public List<Location> getLocations() {
		return Collections.unmodifiableList(locations);
	}
	
	public List<String> getLocationNames(){
		return Collections.unmodifiableList(locationnames);
	}
	
	/**
	 * ����ָ�����Ѿ������ĸ�������
	 * @param Trainname ���������Ѿ������ĸ������ε�����
	 */
	public void startTrain(String Trainname) {
		for(TrainEntry<Carriage> te:trains) {
			if(te.getName().equals(Trainname)) {
				te.start();
				return;
			}
		}
		
		System.out.println("ָ���г���δ�������޷�����!");
	}
	
	/**
	 * ����ָ�����Ѿ������ĸ�������
	 * @param Trainname ���������Ѿ������ĸ�������
	 */
	public void blockTrain(String Trainname) {
		for(TrainEntry<Carriage> te:trains) {
			if(te.getName().equals(Trainname)) {
				te.block(te);
				return;
			}
		}
		
		System.out.println("ָ���г���δ����������ʧ��!");
	}
	
	/**
	 * ȡ��ָ�����Ѿ������ĸ�������
	 * @param Trainname ��ȡ�����Ѿ������ĸ�������
	 */
	public void cancelTrain(String Trainname) {
		for(TrainEntry<Carriage> te:trains) {
			if(te.getName().equals(Trainname)) {
				te.cancel();
				return;
			}
		}
		
		System.out.println("ָ���г���δ������ȡ��ʧ��!");
	}
	
	/**
	 * ����ָ�����Ѿ������ĸ�������
	 * @param Trainname ���������Ѿ������ĸ�������
	 */
	public void endTrain(String Trainname) {
		for(TrainEntry<Carriage> te:trains) {
			if(te.getName().equals(Trainname)) {
				te.end();
				return;
			}
		}
		
		System.out.println("ָ���г���δ����!");
	}
	
	/**
	 * ���ĳһ��������Ŀǰ��״̬
	 * @param Trainname ָ�����Ѵ����ĸ�������
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
	 * ���ռ��ָ������������г�
	 * @param carriageID ָ�������ID 
	 * @return �����ռ��ָ��������г�,���������������г������򷵻�null
	 */
	public List<TrainEntry<Carriage>> getTrainsofassignCarriage(String carriageID){
		int index=-1;
		for(Carriage carriage:carriages) {//�ҵ�ָ���ɻ�
			if(carriage.getId().equals(carriageID)) {
				index=carriages.indexOf(carriage);
			}
		}
		if(index==-1) {
			System.out.println("Ŀǰ����ĳ����в����ڸ�ID����!");
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
	 * ͨ�������ID��ö�Ӧ�ĳ���
	 * @param ID �����ID
	 * @return ��Ӧ�ĳ��ᣬδ�����򷵻�null
	 */
	public Carriage getCarriagebyID(String ID) {
		for(Carriage carriage:carriages) {
			if(carriage.getId().equals(ID))
				return carriage;
		}
		return null;
	}
	
	/**
	 * ͨ���г����ƻ�ö�Ӧ���г�����
	 * @param trainName �г�����
	 * @return ��Ӧ���г����Σ�δ�����򷵻�null
	 */
	public TrainEntry<Carriage> getTrainbyName(String trainName){
		for(TrainEntry<Carriage> te:trains) {
			if(te.getName().equals(trainName))
				return te;
		}
		return null;
	}
}
