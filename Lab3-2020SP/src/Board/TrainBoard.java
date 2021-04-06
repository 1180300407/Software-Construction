package Board;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.TableColumn;

import Location.Location;
import Resources.Carriage;
import Timeslot.Timeslot;
import compositeinterface.*;

public class TrainBoard implements Iterable<TrainEntry<Carriage>>{
	private final Location location;
	private Map<TrainEntry<Carriage>,Long> reachTrains=new HashMap<>();
	private Map<TrainEntry<Carriage>,Long> leaveTrains=new HashMap<>();
	private List<TrainEntry<Carriage>> trains=new ArrayList<>();
	private final int count=1;
	//Abstraction function:
	//	AF(location,reachTrains,leaveTrains,trains)=一个location处的信息板
	//	其中reachTrains包含在count小时前后的抵达车次
	//	reachTrains包含在count小时前后的发车车次
	//	trains表示所有count小时前后经过该位置的车次集合
	//Representation invariant:
	//	trains中所有列车均应已分配车厢
	//Safety from rep exposure:
	//	成员变量全是private的，防御式拷贝
	private void checkRep() {
		for(TrainEntry<Carriage> te:trains) {
			assert te.getResource()!=null;
		}
	}
	
	/**
	 * 构造函数
	 * @param location 信息板所属位置
	 * @param tpes 该位置待搜索的列车集合，所有列车均应已分配车厢
	 */
	public TrainBoard(Location location,List<TrainEntry<Carriage>> tpes,Calendar calendar) {
		this.location=location;
		this.trains=tpes;
		try {
			setRequestTrains(calendar);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		checkRep();
	}
	
	//实现迭代器
	@Override
	public Iterator<TrainEntry<Carriage>> iterator(){
		sortTrains();//先排序以实现有序迭代
		return trains.iterator();
	}
	
	/**
	 * 在Board中存储的所有列车集合中搜索得到在location位置所有在指定时间一小时前后的车次,并分别保存
	 * @param calendar 指定时刻
	 * @throws ParseException 未能将时间转化为标准格式
	 */
	public void setRequestTrains(Calendar calendar) throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		leaveTrains=new HashMap<TrainEntry<Carriage>, Long>();
		reachTrains=new HashMap<TrainEntry<Carriage>, Long>();
		Timeslot timeslot;
		for(TrainEntry<Carriage> tpe:trains) {
			if(tpe.getResource().isEmpty())
				continue;
			if(!tpe.getLocation().contains(location)) 
				continue;
			int index=tpe.getLocation().indexOf(location);
			if(index==0) {//该位置为起点，只有可能在离开车次中
				timeslot=tpe.getTime().get(0);
				Date date=sdf.parse(timeslot.getStarttime());
				Calendar cal=Calendar.getInstance();
				cal.setTime(date);
				if(cal.get(0)==calendar.get(0)&&cal.get(1)==calendar.get(1)&&cal.get(6)==calendar.get(6)) {
					if(cal.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY))//在同一小时之内，必定符合要求
						leaveTrains.put(tpe, cal.getTime().getTime());
					else if(cal.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)-count){//在前一小时，分钟大于等于指定时刻，则在一小时之内
						if(cal.get(Calendar.MINUTE)>=calendar.get(Calendar.MINUTE))
							leaveTrains.put(tpe, cal.getTime().getTime());
					}
					else if(cal.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)+count) {//在后一小时，分钟小于等于制定时刻，则在一小时之内
						if(cal.get(Calendar.MINUTE)<=calendar.get(Calendar.MINUTE))
							leaveTrains.put(tpe, cal.getTime().getTime());
					}
				}
			}
			else if(index==tpe.getLocation().size()-1) {//该位置为终点，只有可能在抵达车次中
				timeslot=tpe.getTime().get(index-1);
				Date date=sdf.parse(timeslot.getEndtime());
				Calendar cal=Calendar.getInstance();
				cal.setTime(date);
				if(cal.get(0)==calendar.get(0)&&cal.get(1)==calendar.get(1)&&cal.get(6)==calendar.get(6)) {
					if(cal.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY))//在同一小时之内，必定符合要求
						reachTrains.put(tpe, cal.getTime().getTime());
					else if(cal.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)-count){//在前一小时，分钟大于等于指定时刻，则在一小时之内
						if(cal.get(Calendar.MINUTE)>=calendar.get(Calendar.MINUTE))
							reachTrains.put(tpe, cal.getTime().getTime());
					}
					else if(cal.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)+count) {//在后一小时，分钟小于等于制定时刻，则在一小时之内
						if(cal.get(Calendar.MINUTE)<=calendar.get(Calendar.MINUTE))
							reachTrains.put(tpe, cal.getTime().getTime());
					}
				}
			}
			else {//中间站次
				Timeslot timeslot1=tpe.getTime().get(index-1);//到达第index个位置的时间
				Date date1=sdf.parse(timeslot1.getEndtime());
				Calendar cal1=Calendar.getInstance();
				cal1.setTime(date1);
				Timeslot timeslot2=tpe.getTime().get(index);
				Date date2=sdf.parse(timeslot2.getStarttime());//从第index个位置离开的时间
				Calendar cal2=Calendar.getInstance();
				cal2.setTime(date2);
				if(cal1.get(0)==calendar.get(0)&&cal1.get(1)==calendar.get(1)&&cal1.get(6)==calendar.get(6)) {//在同一天
					if(cal1.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY))//在同一小时之内，必定符合要求
						reachTrains.put(tpe, cal1.getTime().getTime());
					else if(cal1.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY-count)){//在前一小时，分钟大于等于指定时刻，则在一小时之内
						if(cal1.get(Calendar.MINUTE)>=calendar.get(Calendar.MINUTE))
							reachTrains.put(tpe, cal1.getTime().getTime());
					}
					else if(cal1.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)+count) {//在后一小时，分钟小于等于制定时刻，则在一小时之内
						if(cal1.get(Calendar.MINUTE)<=calendar.get(Calendar.MINUTE))
							reachTrains.put(tpe, cal1.getTime().getTime());
					}
				}
				if(cal2.get(0)==calendar.get(0)&&cal2.get(1)==calendar.get(1)&&cal2.get(6)==calendar.get(6)) {//在同一天
					if(cal2.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY))//在同一小时之内，必定符合要求
						leaveTrains.put(tpe, cal2.getTime().getTime());
					else if(cal1.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)-count){//在前一小时，分钟大于等于指定时刻，则在一小时之内
						if(cal1.get(Calendar.MINUTE)>=calendar.get(Calendar.MINUTE))
							leaveTrains.put(tpe, cal2.getTime().getTime());
					}
					else if(cal1.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)+count) {//在后一小时，分钟小于等于制定时刻，则在一小时之内
						if(cal1.get(Calendar.MINUTE)<=calendar.get(Calendar.MINUTE))
							leaveTrains.put(tpe, cal2.getTime().getTime());
					}
				}
			}
		}
		sortTrains();
	}
	
	/**
	 * 为reachTrains和departureTrains中的元素排序，以及trains排序
	 */
	private void sortTrains() {
		Map<TrainEntry<Carriage>, Long> sortreach=new LinkedHashMap<TrainEntry<Carriage>, Long>();
		Map<TrainEntry<Carriage>, Long> sortleave=new LinkedHashMap<TrainEntry<Carriage>, Long>();
		List<Map.Entry<TrainEntry<Carriage>, Long>> list1=new ArrayList<>();
		list1.addAll(reachTrains.entrySet());
		List<Map.Entry<TrainEntry<Carriage>, Long>> list2=new ArrayList<>();
		list2.addAll(leaveTrains.entrySet());//将map中元素复制到list中以进行排序
		Collections.sort(list1, new MapvalueComparator());
		Collections.sort(list2,new MapvalueComparator());
		for(Map.Entry<TrainEntry<Carriage>, Long> entry:list1) {//将排序后的加入到有序map中
			sortreach.put(entry.getKey(), entry.getValue());
		}
		
		for(Map.Entry<TrainEntry<Carriage>, Long> entry:list2) {//将排序后的加入到有序map中
			sortleave.put(entry.getKey(), entry.getValue());
		}
		this.reachTrains=sortreach;
		this.leaveTrains=sortleave;
		trains=new ArrayList<TrainEntry<Carriage>>();
		int i=0,j=0;
		while(i<list1.size()||j<list2.size()) {//将leaveTrains和reachTrains中所有列车按从早到晚顺序加入trains
			if(i==list1.size()) {		//使其成为从早到晚排序的有序列车集合
				trains.add(list2.get(j).getKey());//抵达列车全部加入，将排序后的出发列车直接加入
				j++;
			}
			else if(j==list2.size()) {//出发列车全部加入，将排序后的抵达列车直接加入
				trains.add(list1.get(i).getKey());
				i++;
			}
			else {//二者更小的先加入
				Long timei=list1.get(i).getValue();
				Long timej=list2.get(j).getValue();
				if(timei<=timej) {
					trains.add(list1.get(i).getKey());
					i++;
				}
				else {
					trains.add(list2.get(j).getKey());
					j++;
				}
			}
		}
	}
	
	/**
	 * Board的可视化展现
	 * @throws ParseException 时间未能转化为标准格式
	 */
	public void visualize() throws ParseException {
		Calendar calendar=Calendar.getInstance();
		setRequestTrains(calendar);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		String time=sdf.format(calendar.getTime());
		JFrame jf=new JFrame("TrainBoard");
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// 创建内容面板，使用边界布局
        JPanel panel = new JPanel(new BorderLayout());
        String[] col= {" ",time,location.getName()," "};
        Object[][] rowData=new Object[reachTrains.size()+1+leaveTrains.size()+1][4];
        int i=1;
        rowData[0][0]=" ";
        rowData[0][1]=" ";
        rowData[0][2]="抵达车次";
        rowData[0][3]=" ";
        for(Map.Entry<TrainEntry<Carriage>, Long> entry:reachTrains.entrySet()) {//将抵达车次可视化
        	TrainEntry<Carriage> tpe=entry.getKey();
        	Date date=new Date(entry.getValue());
        	sdf.format(date);
			Calendar calendar2=Calendar.getInstance();
			calendar2.setTime(date);
			int hour1=calendar2.get(Calendar.HOUR_OF_DAY);
			int minute1=calendar2.get(Calendar.MINUTE);
			String time1;//时间
			if(minute1<10)
				time1=hour1+":"+"0"+minute1;
			else 
				time1=hour1+":"+minute1;
			rowData[i][0]= time1;
			rowData[i][1]=tpe.getName();//列车名称
			int maxindex=tpe.getLocation().size()-1;
			String locationString=tpe.getLocation().get(0).getName()+"-"+tpe.getLocation().get(maxindex).getName();//起点-终点
			rowData[i][2]=locationString;
			switch (tpe.getStateName()) {//状态
			case "Running":
				rowData[i][3]="即将抵达";
				break;
			case "Cancelled":
				rowData[i][3]="已取消";
				break;
			case "Ended":
				rowData[i][3]="已到达";
				break;
			case "Blocked":
				rowData[i][3]="已到达";
				break;
			default:
				break;
			}
			i++;
        }
        rowData[i][0]=" ";
        rowData[i][1]=" ";
        rowData[i][2]="出发车次";
        rowData[i][3]=" ";
        i++;
        for(Map.Entry<TrainEntry<Carriage>, Long> entry:leaveTrains.entrySet()) {//将出发航班可视化
        	TrainEntry<Carriage> tpe=entry.getKey();
        	Date date=new Date(entry.getValue());
        	sdf.format(date);
			Calendar calendar2=Calendar.getInstance();
			calendar2.setTime(date);
			int hour1=calendar2.get(Calendar.HOUR_OF_DAY);
			int minute1=calendar2.get(Calendar.MINUTE);
			String time1;//时间
			if(minute1<10)
				time1=hour1+":"+"0"+minute1;
			else 
				time1=hour1+":"+minute1;
			rowData[i][0]= time1;
			rowData[i][1]=tpe.getName();//列车名称
			int maxindex=tpe.getLocation().size()-1;
			String locationString=tpe.getLocation().get(0).getName()+"-"+tpe.getLocation().get(maxindex).getName();//起点-终点
			rowData[i][2]=locationString;
			switch (tpe.getStateName()) {//状态
			case "Running":
				rowData[i][3]="已发车";
				break;
			case "Cancelled":
				rowData[i][3]="已取消";
				break;
			case "Allocated":
				rowData[i][3]="即将发车";
				break;
			case "Blocked":
				rowData[i][3]="即将发车";
				break;
			default:
				break;
			}
			i++;
        }
        JTable table=new JTable(rowData,col);
        table.setGridColor(Color.GRAY);
        table.setForeground(Color.BLACK);
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(100);
        TableColumn tableColumn = table.getColumnModel().getColumn(1);
        tableColumn.setWidth(600);
        tableColumn = table.getColumnModel().getColumn(2);
        tableColumn.setWidth(600);
        table.setFont(new Font(null, Font.PLAIN, 25));
        table.getTableHeader().setFont(new Font(null, Font.BOLD, 25));
        table.getColumnModel().getColumn(0).setPreferredWidth(130);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 400));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);
        
        jf.setContentPane(panel);
        jf.pack();
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	/*   //测试用的main函数
	public static void main(String[] args) throws ParseException {
		Location startlocation=new Location("10E","45N" , "哈尔滨西", true);
		Location endlocation=new Location("3E","30N" , "长春北", true);
		List<TrainEntry<Carriage>> cpes=new ArrayList<>();
		TrainEntry<Carriage> cpe1=TrainPlanningEntry.CreateTrain("T147");
		List<Location> locations=new ArrayList<Location>();
		locations.add(startlocation);
		locations.add(endlocation);
		Timeslot timeslot=new Timeslot("2020-05-14 22:00", "2020-05-15 03:20");//根据现实时间调整测试时间,日期应设置为与测试日期相同
		List<Timeslot> oneTimeslot=new ArrayList<Timeslot>();
		oneTimeslot.add(timeslot);
		cpe1.setLocations(locations);
		cpe1.setTime(oneTimeslot);
		Carriage carriage=new Carriage("4", "rt", 200, "century");
		List<Carriage> carriages=new ArrayList<>();
		carriages.add(carriage);
		cpe1.allocateResource(carriages);	
		cpe1.start();
		Timeslot timeslot2=new Timeslot("2020-05-13 10:11", "2020-05-14 22:50");//根据现实时间调整测试时间,日期应设置为与测试日期相同
		List<Timeslot> seTimeslot=new ArrayList<Timeslot>();
		seTimeslot.add(timeslot2);
		TrainEntry<Carriage> cpe2=TrainPlanningEntry.CreateTrain("G1238");
		List<Location> locations2=new ArrayList<Location>();
		locations2.add(endlocation);
		locations2.add(startlocation);
		cpe2.setLocations(locations2);
		cpe2.setTime(seTimeslot);
		cpe2.allocateResource(carriages);
		cpe2.start();
		cpes.add(cpe2);
		cpes.add(cpe1);
		Timeslot timeslot3=new Timeslot("2020-05-13 10:11", "2020-05-14 22:30");//根据现实时间调整测试时间,日期应设置为与测试日期相同
		List<Timeslot> seTimeslot2=new ArrayList<Timeslot>();
		seTimeslot2.add(timeslot3);
		TrainEntry<Carriage> cpe3=TrainPlanningEntry.CreateTrain("G1438");
		cpe3.setLocations(locations2);
		cpe3.setTime(seTimeslot2);
		cpe3.allocateResource(carriages);
		cpe3.start();
		cpes.add(cpe3);
		Calendar calendar=Calendar.getInstance();
		TrainBoard tb=new TrainBoard(startlocation, cpes,calendar);
		tb.visualize();
	}*/
	
}

	class MapvalueComparator implements Comparator<Map.Entry<TrainEntry<Carriage>, Long>>{
		
		@Override
		public int compare(Entry<TrainEntry<Carriage>, Long> e1 ,Entry<TrainEntry<Carriage>, Long> e2) {
			return e1.getValue().compareTo(e2.getValue());
		}
	}
	
	
