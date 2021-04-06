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
 * PlanningEntryAPIs的第一种实现方式(指findPreEntryPerResource函数)
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
		String endtime=entries.get(min).getTime().get(entries.get(min).getTime().size()-1).getEndtime();//终止时间
		Date minDate=sdf.parse(endtime);
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		String starttime=entries.get(min).getTime().get(0).getStarttime();
		Timeslot timeslot=new Timeslot(starttime, endtime);
		timeslots.add(timeslot);
		String objectstarttime=e.getTime().get(0).getStarttime();
		Date startDate=sdf.parse(objectstarttime);
		for(int i=index+1;i<entries.size();i++) {//找到结束时间最晚的但是在指定计划项开始之前就结束的计划项
			if(entries.get(i).getResource()==null) {
				continue;
			}
			if(!entries.get(i).getResource().contains(r)) {
				continue;
			}
			String endtimei=entries.get(i).getTime().get(entries.get(i).getTime().size()-1).getEndtime();//终止时间
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
	 * 找到一组计划项entries中第一个与e占用同一资源r并在e开始之前结束的计划项
	 * @param <R> 占用资源类型
	 * @param r 共同占用的资源
	 * @param e 指定计划项
	 * @param entries 一组计划项
	 * @return entries中的位置；如果没有这样的计划项，返回-1
	 * @throws ParseException 未能将时间转化为标准格式
	 */
	private <R> int findFirstPreEntry(R r,PlanningEntry<R> e,List<? extends PlanningEntry<R>> entries) throws ParseException{
		for(int i=0;i<entries.size();i++) {
			if(entries.get(i).getResource()==null) {
				continue;
			}
			if(entries.get(i).getResource().contains(r)) {
				String endtime=entries.get(i).getTime().get(entries.get(i).getTime().size()-1).getEndtime();//计划项的终止时间
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
