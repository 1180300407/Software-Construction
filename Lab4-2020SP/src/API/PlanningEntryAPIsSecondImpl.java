package API;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import common.PlanningEntry;

/**
 * PlanningEntryAPIs的第二种实现方式(指findPreEntryPerResource函数)
 * @author 123
 *
 */
public class PlanningEntryAPIsSecondImpl extends PlanningEntryAPIs{
	@Override
	public <R> PlanningEntry<R> findPreEntryPerResource(R r,PlanningEntry<R> e,List<? extends PlanningEntry<R>> entries) throws ParseException{
		if(entries.isEmpty())
			return null;
		List<PlanningEntry<R>> pes=new ArrayList<PlanningEntry<R>>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		String objectstarttime=e.getTime().get(0).getStarttime();//目标计划项的起始时间
		Date startDate=sdf.parse(objectstarttime);
		for(PlanningEntry<R> entry:entries) {//把所有结束时间在e开始之前的计划项都加入到pes中
			if(entry.getResource()==null) {
				continue;
			}
			if(!entry.getResource().contains(r)) {
				continue;
			}
			String endtime=entry.getTime().get(entry.getTime().size()-1).getEndtime();//计划项entry的终止时间
			Date date=sdf.parse(endtime);
			if(date.before(startDate))
				pes.add(entry);
		}
		if(pes.isEmpty())
			return null;
			for(int j=0;j<pes.size()-1;j++) {//根据结束时间进行冒泡排序的第一轮
				String endtime1=pes.get(j).getTime().get(pes.get(j).getTime().size()-1).getEndtime();
				String endtime2=pes.get(j+1).getTime().get(pes.get(j+1).getTime().size()-1).getEndtime();
				Date date1=sdf.parse(endtime1);
				Date date2=sdf.parse(endtime2);
				if(date1.after(date2)) {//最大元素不断后移，一直到最后一个元素
					PlanningEntry<R> p=pes.get(j);
					pes.set(j, pes.get(j+1));
					pes.set(j+1, p);
				}
			
			}
		return pes.get(pes.size()-1);//最大的在末尾
	}
}
