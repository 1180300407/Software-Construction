package API;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Timeslot.Timeslot;
import common.PlanningEntry;
/**
 * PlanningEntryAPIs�ĵ�һ��ʵ�ַ�ʽ(ָfindPreEntryPerResource����)
 * @author 123
 *
 */

public class PlanningEntryAPIsFirstImpl extends PlanningEntryAPIs{
	@Override
	public <R> PlanningEntry<R> findPreEntryPerResource(R r,PlanningEntry<R> e,List<? extends PlanningEntry<R>> entries) throws ParseException{
		if(entries.isEmpty())
			return null;
		int index=findFirstPreEntry(r, e, entries);
		if(index==-1)
			return null;
		int min=index;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		String endtime=entries.get(min).getTime().get(entries.get(min).getTime().size()-1).getEndtime();//��ֹʱ��
		Date minDate=sdf.parse(endtime);
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		String starttime=entries.get(min).getTime().get(0).getStarttime();
		Timeslot timeslot=new Timeslot(starttime, endtime);
		timeslots.add(timeslot);
		String objectstarttime=e.getTime().get(0).getStarttime();
		Date startDate=sdf.parse(objectstarttime);
		for(int i=index+1;i<entries.size();i++) {//�ҵ�����ʱ������ĵ�����ָ���ƻ��ʼ֮ǰ�ͽ����ļƻ���
			if(entries.get(i).getResource()==null) {
				continue;
			}
			if(!entries.get(i).getResource().contains(r)) {
				continue;
			}
			String endtimei=entries.get(i).getTime().get(entries.get(i).getTime().size()-1).getEndtime();//��ֹʱ��
			Date date=sdf.parse(endtime);
			if(date.before(startDate)) {
				String starttimei=entries.get(i).getTime().get(0).getStarttime();
				Timeslot timeslot2=new Timeslot(starttimei, endtimei);
				timeslots.add(timeslot2);
				if(date.after(minDate)) {
					minDate=date;
					min=i;
				}
			}
		}
		PlanningEntry<R> pEntry=entries.get(min);
		return pEntry;
	}
	
	/**
	 * �ҵ�һ��ƻ���entries�е�һ����eռ��ͬһ��Դr����e��ʼ֮ǰ�����ļƻ���
	 * @param <R> ռ����Դ����
	 * @param r ��ͬռ�õ���Դ
	 * @param e ָ���ƻ���
	 * @param entries һ��ƻ���
	 * @return entries�е�λ�ã����û�������ļƻ������-1
	 * @throws ParseException δ�ܽ�ʱ��ת��Ϊ��׼��ʽ
	 */
	private <R> int findFirstPreEntry(R r,PlanningEntry<R> e,List<? extends PlanningEntry<R>> entries) throws ParseException{
		for(int i=0;i<entries.size();i++) {
			if(entries.get(i).getResource()==null) {
				continue;
			}
			if(entries.get(i).getResource().contains(r)) {
				String endtime=entries.get(i).getTime().get(entries.get(i).getTime().size()-1).getEndtime();//�ƻ������ֹʱ��
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
				Date date=sdf.parse(endtime);
				String objectstarttime=e.getTime().get(0).getStarttime();
				Date startDate=sdf.parse(objectstarttime);
				if(date.before(startDate))
					return i;
			}
		}
		return -1;
	}
}
