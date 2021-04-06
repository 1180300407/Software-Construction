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
 * �������ι����ɶԶ������վ�Σ��������Σ��Լ����������й����ɱ���
 * @author 123
 *
 */

public class TrainSchedule {
	private List<TrainEntry<Carriage>> trains=new ArrayList<>();//��������
	private List<Carriage> carriages=new ArrayList<Carriage>();//����
	private List<Location> locations=new ArrayList<Location>();//����վ��
	private List<String> locationnames=new ArrayList<String>();//����վ�ε����ƣ�ÿ�����Ƶ�˳����locations��ͬ
	private static Logger myLogger=Logger.getLogger("TrainScheduleLog");
	//Abstraction function:
	//	AF(trains,carriages,locations,locationnames)=һ���Ը�������trains�����г���carriages������Ϊlocationnames�ĸ���վ��locations���й����ϵͳ
	//Representation invariant:
	//	locationnames�и���վ�ε����ƣ�ÿ�����Ƶ�˳����locations��ͬ
	//	����Ŀγ̡���ʦ�������ж���Ӧ�����ظ�Ԫ��
	//Safety from rep exposure:
	//	��Ա����ȫ��private�ģ����ⷵ��ʱȫ��תΪ���ɱ����ͣ������ڱ�ʾй¶
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
		//��־��¼
		myLogger.setLevel(Level.INFO);
		myLogger.setUseParentHandlers(false);
		//д���ļ�
		FileHandler handler;
		try {
			handler = new FileHandler("src/LogFile/TrainScheduleLog.log");
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
	 * ����һ���ɹ�����ĳ��ᣬ�������Ѵ��ڲ����ظ����
	 * @param carriage �����ӵĳ���
	 */
	public void addCarriage(Carriage carriage) {
		if(!carriages.contains(carriage)) {
			carriages.add(carriage);
			checkRep();
		}
	}
	
	/**
	 * ɾ������ָ��ID�ĳ��ᣬ����Ӧ���Ѵ��� ,��δ������
	 * @param ID ��ɾ������ı��
	 * @throws ResourceNotFoundException ������δ����
	 * @throws PlanEntryOccupyResourceException �����ѱ�����
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
	 * ����һ���ɹ������λ�ã���λ���Ѵ��ڲ����ظ����
	 * @param location ����ӵ�λ��
	 */
	public void addLocation(Location location) {
		if(!locations.contains(location)) {
			locations.add(location);
			locationnames.add(location.getName());
			checkRep();
		}	
	}
	
	/**
	 * ɾ������ָ�����Ƶ�λ�ã�λ��Ӧ�Ѵ��� ,��δ��ռ��
	 * @param locationname ��ɾ��λ�õ�����
	 * @throws LocationNotFoundException ��ɾ��λ����δ����
	 * @throws PlanEntryOccupyLocationException ��ɾ��λ����δ����
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
	 * ����һ���������ζ�����й�����������������
	 * @param name �������ε�����
	 * @param locations �������ξ���������վ������ƣ�Ӧ�ð������������λ��locations֮��,�ܸ���Ӧ�á�2��վ��Ӧ�������
	 * @param timeslots ����������;���о�ͣʱ��㣬��ӦΪ��,timeslots��˳��Ӧ�������location��˳���Ƕ�Ӧ��
	 * @return ��������ɹ�����true�����򷵻�false,�����ɹ����г���ʼ״̬ΪWaiting
	 * @throws LocationNotFoundException λ���д���δ����λ��
	 * @throws SameLabelException ��������
	 */
	public boolean createTrain(String name,List<String> locationnames,List<Timeslot> timeslots) throws LocationNotFoundException, SameLabelException {
		if(timeslots==null||locationnames==null||locationnames.size()==0||timeslots.size()==0) {
			System.out.println("����ղ���,����Ӻ󴴽�!");
			return false;
		}
		
		if(locationnames.size()!=timeslots.size()+1) {//ʱ��Ը�����λ����ƥ��
			System.out.println("��ͣվ��������ֹʱ��Ը�����ƥ��,������ȷ����");
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
	 * Ϊ�������η��䳵�ᣬ����Ӧ�����ڹ�������г���֮��
	 * @param Trainname ָ���������ε��г�����
	 * @param carriageResourse ��������г����ID,�Ѿ�ȷ�������˳��
	 * @throws ResourceNotFoundException �����д���δ��������
	 * @throws PlanEntryNotCreateException ����δ����
	 * @throws ResourceConflictException ���������ڳ�ͻ
	 */
	public void allocateCarriage(String Trainname,List<String> carriageIDs) throws ResourceNotFoundException, PlanEntryNotCreateException, ResourceConflictException {
		Carriage carriage;
		List<Carriage> carriageResourse=new ArrayList<Carriage>();
		for(int i=0;i<carriageIDs.size();i++) {
			carriage=new Carriage(carriageIDs.get(i), "type", 1, "");//����Carriage��equals�е�Ψһ������ID���
			if(this.carriages.contains(carriage)) {//�õ�carriages�еĶ�Ӧ����
				int index=carriages.indexOf(carriage);
				carriageResourse.add(carriages.get(index));
			}
			else {
				throw new ResourceNotFoundException(carriage.getId());
			}
		}
		
		for(TrainEntry<Carriage> te:trains) {
			if(te.getName().equals(Trainname)) {
				TrainEntry<Carriage> trainEntry=new TrainEntry<Carriage>("null");//�½�һ���г���Ϊ������������õ����г�һ�������ԣ��۲��Ƿ�������ͻ
				trainEntry.setLocations(te.getLocation());
				trainEntry.setTime(te.getTime());
				trainEntry.allocateResource(carriageResourse);
				List<TrainEntry<Carriage>> trainEntries=new ArrayList<>();
				trainEntries.add(trainEntry);//������trains�У�ֻ��һ�����Ʒ������Ƿ�ᷢ����ͻ������������������ĳ�ͻ
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
	 * @throws PlanEntryNotCreateException ������δ����
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
	 * ����ָ�����Ѿ������ĸ�������
	 * @param Trainname ���������Ѿ������ĸ�������
	 * @throws PlanEntryNotCreateException ������δ����
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
	 * ȡ��ָ�����Ѿ������ĸ�������
	 * @param Trainname ��ȡ�����Ѿ������ĸ�������
	 * @throws PlanEntryNotCreateException ������δ����
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
	 * ����ָ�����Ѿ������ĸ�������
	 * @param Trainname ���������Ѿ������ĸ�������
	 * @throws PlanEntryNotCreateException ������δ����
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
	 * ���ĳһ��������Ŀǰ��״̬
	 * @param Trainname ָ�����Ѵ����ĸ�������
	 * @return �г���ǰ״̬
	 * @throws PlanEntryNotCreateException �г�δ����
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
