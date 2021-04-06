package API;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import common.PlanningEntry;

/**
 * PlanningEntryAPIs�ĵڶ���ʵ�ַ�ʽ(ָfindPreEntryPerResource����)
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
		String objectstarttime=e.getTime().get(0).getStarttime();//Ŀ��ƻ������ʼʱ��
		Date startDate=sdf.parse(objectstarttime);
		for(PlanningEntry<R> entry:entries) {//�����н���ʱ����e��ʼ֮ǰ�ļƻ�����뵽pes��
			if(entry.getResource()==null) {
				continue;
			}
			if(!entry.getResource().contains(r)) {
				continue;
			}
			String endtime=entry.getTime().get(entry.getTime().size()-1).getEndtime();//�ƻ���entry����ֹʱ��
			Date date=sdf.parse(endtime);
			if(date.before(startDate))
				pes.add(entry);
		}
		if(pes.isEmpty())
			return null;
			for(int j=0;j<pes.size()-1;j++) {//���ݽ���ʱ�����ð������ĵ�һ��
				String endtime1=pes.get(j).getTime().get(pes.get(j).getTime().size()-1).getEndtime();
				String endtime2=pes.get(j+1).getTime().get(pes.get(j+1).getTime().size()-1).getEndtime();
				Date date1=sdf.parse(endtime1);
				Date date2=sdf.parse(endtime2);
				if(date1.after(date2)) {//���Ԫ�ز��Ϻ��ƣ�һֱ�����һ��Ԫ��
					PlanningEntry<R> p=pes.get(j);
					pes.set(j, pes.get(j+1));
					pes.set(j+1, p);
				}
			
			}
		return pes.get(pes.size()-1);//������ĩβ
	}
}
