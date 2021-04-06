package API;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import Location.Location;
import Timeslot.Timeslot;
import common.PlanningEntry;
/**
 * ���PlanningEntryʵ�ֵ�һ��API,������һ�鷽��
 * @author 123
 *
 */

public abstract class PlanningEntryAPIs {
	/**
	 * ���һ��ƻ���֮���Ƿ����λ�ö�ռ��ͻ
	 * @param <R> �ƻ���ռ�õ���Դ����
	 * @param entries һ������ƻ���
	 * @return ������ڳ�ͻ����true�����򷵻�false
	 */
	public <R> boolean checkLocationConflict(List<? extends PlanningEntry<R>> entries) {
		if(entries.isEmpty())
			return false;
		Map<Location, List<Timeslot>> hashlocation=new HashMap<>();//�ǹ���λ�õĹ�ϣ��ÿ��λ�ö�Ӧһ���б��洢��ռ�ø�λ�õ�����ʱ��ڵ�
		Set<Location> unshareableLocations=new HashSet<Location>();//�ǹ���λ�ü���
		for(PlanningEntry<R> pe:entries) {//�����ƻ���
			if(pe.getLocation().size()!=1)//�����ͳ�վ�ǹ����
				break;
			Location location=pe.getLocation().get(0);
			if(location.isshareable())//��������λ��
				continue;
			if(unshareableLocations.contains(location)) {//֮ǰ�Ѿ��������ü���
				hashlocation.get(location).addAll(pe.getTime());
				boolean flag=false;
				try {
					flag = checkTimeConflict(hashlocation.get(location));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(flag)
					return true;
			}
			else {
				unshareableLocations.add(location);
				List<Timeslot> ces=new ArrayList<>();
				ces.addAll(pe.getTime());
				hashlocation.put(location, ces);
			}
		}
		return false;
	}
	
	public <R> boolean checkResourceExclusiveConflict(List<? extends PlanningEntry<R>> entries) { 
		if(entries.isEmpty())
			return false;
		Map<R, List<Timeslot>> hashtable=new HashMap<>();
		Set<R> resources=new HashSet<R>();
		for(PlanningEntry<R> pe:entries) {//�����ƻ���ռ�õ�������Դ
			if(pe.getResource().isEmpty())
				continue;
			for(R r:pe.getResource()) {
				if(resources.contains(r)) {//��Դ֮ǰ����������ϣ���ж�Ӧֵ����
					String starttime=pe.getTime().get(0).getStarttime();//��ʼʱ��
					String endtime=pe.getTime().get(pe.getTime().size()-1).getEndtime();//��ֹʱ��
					Timeslot timeslot=new Timeslot(starttime, endtime);
					hashtable.get(r).add(timeslot);
					boolean flag=false;
					try {
						flag = checkTimeConflict(hashtable.get(r));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if(flag)
						return true;
				}
				else {
					resources.add(r);
					List<Timeslot> timeslots=new ArrayList<>();
					String starttime=pe.getTime().get(0).getStarttime();//��ʼʱ��
					String endtime=pe.getTime().get(pe.getTime().size()-1).getEndtime();//��ֹʱ��
					Timeslot timeslot=new Timeslot(starttime, endtime);
					timeslots.add(timeslot);
					hashtable.put(r,timeslots);
				}
			}
		}
		return false;
	}
	
	/**
	 * �ҵ�һ��ƻ���entries����eռ��ͬһ��Դr��ǰ�üƻ���
	 * @param <R> ռ����Դ������
	 * @param r ��ͬռ�õ���Դ
	 * @param e ָ���ƻ���
	 * @param entries һ��ƻ���
	 * @return ǰ�üƻ���,���û�������ļƻ������null
	 * @throws ParseException δ�ܽ�ʱ��ת��Ϊ��׼��ʽ
	 */
	public abstract <R> PlanningEntry<R> findPreEntryPerResource(R r,PlanningEntry<R> e,List<? extends PlanningEntry<R>> entries) throws ParseException;
	
	/**
	 * ���һ��ʱ���֮���Ƿ�������ͻ
	 * @param timeslots һ������ʱ���
	 * @return ���������ͻ����true�����򷵻�false
	 * @throws ParseException δ�ܽ�ʱ��ת��Ϊ��׼��ʽ
	 */
	private boolean checkTimeConflict(List<Timeslot> timeslots) throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		for(int i=1;i<timeslots.size();i++) {
			Timeslot timei=timeslots.get(i);
			Date dateistart=sdf.parse(timei.getStarttime());
			Date dateiend=sdf.parse(timei.getEndtime());
			for(int j=i-1;j>=0;j--) {
				Timeslot timej=timeslots.get(j);
				Date datejstart=sdf.parse(timej.getStarttime());
				Date datejend=sdf.parse(timej.getEndtime());
				if(dateiend.before(datejstart)||dateistart.after(datejend))
					continue;
				else
					return true;
			}
		}
		return false;
	}
}
