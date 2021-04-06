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
 * 针对PlanningEntry实现的一组API,仅包括一组方法
 * @author 123
 *
 */

public abstract class PlanningEntryAPIs {
	/**
	 * 检测一组计划项之间是否存在位置独占冲突
	 * @param <R> 计划项占用的资源类型
	 * @param entries 一组待检测计划项
	 * @return 如果存在冲突返回true，否则返回false
	 */
	public <R> boolean checkLocationConflict(List<? extends PlanningEntry<R>> entries) {
		if(entries.isEmpty())
			return false;
		Map<Location, List<Timeslot>> hashlocation=new HashMap<>();//非共享位置的哈希表，每个位置对应一个列表，存储着占用该位置的所有时间节点
		Set<Location> unshareableLocations=new HashSet<Location>();//非共享位置集合
		for(PlanningEntry<R> pe:entries) {//遍历计划项
			if(pe.getLocation().size()!=1)//机场和车站是共享的
				break;
			Location location=pe.getLocation().get(0);
			if(location.isshareable())//跳过共享位置
				continue;
			if(unshareableLocations.contains(location)) {//之前已经遍历过该集合
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
		for(PlanningEntry<R> pe:entries) {//遍历计划项占用的所有资源
			if(pe.getResource().isEmpty())
				continue;
			for(R r:pe.getResource()) {
				if(resources.contains(r)) {//资源之前遍历过，哈希表中对应值更新
					String starttime=pe.getTime().get(0).getStarttime();//起始时间
					String endtime=pe.getTime().get(pe.getTime().size()-1).getEndtime();//终止时间
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
					String starttime=pe.getTime().get(0).getStarttime();//起始时间
					String endtime=pe.getTime().get(pe.getTime().size()-1).getEndtime();//终止时间
					Timeslot timeslot=new Timeslot(starttime, endtime);
					timeslots.add(timeslot);
					hashtable.put(r,timeslots);
				}
			}
		}
		return false;
	}
	
	/**
	 * 找到一组计划项entries中与e占用同一资源r的前置计划项
	 * @param <R> 占用资源的类型
	 * @param r 共同占用的资源
	 * @param e 指定计划项
	 * @param entries 一组计划项
	 * @return 前置计划项,如果没有这样的计划项，返回null
	 * @throws ParseException 未能将时间转化为标准格式
	 */
	public abstract <R> PlanningEntry<R> findPreEntryPerResource(R r,PlanningEntry<R> e,List<? extends PlanningEntry<R>> entries) throws ParseException;
	
	/**
	 * 检测一组时间对之间是否会产生冲突
	 * @param timeslots 一组待检测时间对
	 * @return 如果产生冲突返回true，否则返回false
	 * @throws ParseException 未能将时间转化为标准格式
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
