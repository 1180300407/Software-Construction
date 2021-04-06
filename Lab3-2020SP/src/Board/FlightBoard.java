package Board;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.TableColumn;

import Location.Location;
import Resources.Plane;
import Timeslot.Timeslot;
import compositeinterface.*;

/**
 * 机场信息板，可变类
 * @author 123
 *
 */
public class FlightBoard implements Iterable<FlightEntry<Plane>>{
	private final Location location;
	private List<FlightEntry<Plane>> reachFlights=new ArrayList<>();
	private List<FlightEntry<Plane>> departureFlights=new ArrayList<>();
	private List<FlightEntry<Plane>> allFlights=new ArrayList<>();
	private final int count=1;
	//Abstraction function:
	//	AF(location,reachFlights,departureFlights,allFlights)=一个location处的信息板
	//	其中reachFlights包含在count小时前后的抵达航班
	//	departureFlights包含在count小时前后的起飞航班
	//	allFlights表示所有count小时前后经过该位置的航班集合
	//Representation invariant:
	//	allFlights中所有航班均应已分配飞机
	//Safety from rep exposure:
	//	成员变量全是private的，防御式拷贝
	private void checkRep() {
		for(FlightEntry<Plane> fe:allFlights) {
			assert fe.getResource()!=null;
		}
	}
	
	/**
	 * 构造函数
	 * @param location 信息板所属的具体位置
	 * @param allflights 该位置待搜索的航班集合，所有航班均应已分配飞机
	 */
	public FlightBoard(Location location,List<FlightEntry<Plane>> allflights,Calendar calendar) {
		this.location=location;
		this.allFlights=allflights;
		try {
			setRequestFlights(calendar);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		checkRep();
	}
	
	//实现迭代器
	@Override
	public Iterator<FlightEntry<Plane>> iterator(){
		try {
			sortFlights(); //先排序以实现有序迭代
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return allFlights.iterator();
	}
	
	/**
	 * 在所有航班集合中搜索得到在location位置所有在指定时间一小时前后的航班,并分别保存
	 * @param calendar 指定时刻
	 * @throws ParseException 未能将时间转化为标准格式
	 */
	public void setRequestFlights(Calendar calendar) throws ParseException {
		reachFlights=new ArrayList<FlightEntry<Plane>>();
		departureFlights=new ArrayList<FlightEntry<Plane>>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		Timeslot timeslot;
		for(FlightEntry<Plane> fpe:allFlights) {
			if(fpe.getResource()==null) {
				continue;
			}
			if(!fpe.getLocation().contains(location))
				continue;
			boolean ismiddle=false;//标识该航班是否有中间站
			int index=fpe.getLocation().indexOf(location);
			timeslot=fpe.getTime().get(0);
			Date date1=null;//降落时间
			if(fpe.getLocation().size()==2) {//没有经停站
				date1 = sdf.parse(timeslot.getEndtime());
			}
			else if(fpe.getLocation().size()==3){//有经停站
				Timeslot timeslot2=fpe.getTime().get(1);
				date1=sdf.parse(timeslot2.getEndtime());
				ismiddle=true;
			}
			else {//位置个数不标准
				continue;
			}
			Date date2 = sdf.parse(timeslot.getStarttime());//起飞时间
			
			Calendar cal1=Calendar.getInstance();
			Calendar cal2=Calendar.getInstance();
			cal1.setTime(date1);
			cal2.setTime(date2);
			if(index==fpe.getLocation().size()-1&&cal1.get(0)==calendar.get(0)&&cal1.get(1)==calendar.get(1)&&cal1.get(6)==calendar.get(6)) {//指定时刻与抵达时间在同一天
				if(cal1.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY))//在同一小时之内，必定符合要求
					reachFlights.add(fpe);
				else if(cal1.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)-count){//在前一小时，分钟大于等于指定时刻，则在一小时之内
					if(cal1.get(Calendar.MINUTE)>=calendar.get(Calendar.MINUTE))
						reachFlights.add(fpe);
				}
				else if(cal1.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)+count) {//在后一小时，分钟小于等于制定时刻，则在一小时之内
					if(cal1.get(Calendar.MINUTE)<=calendar.get(Calendar.MINUTE))
						reachFlights.add(fpe);
				}
			}
			else  if(index==0&&cal2.get(0)==calendar.get(0)&&cal2.get(1)==calendar.get(1)&&cal2.get(6)==calendar.get(6)) {//指定时刻与出发时间在同一天
				if(cal2.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY))//在同一小时之内，必定符合要求
					departureFlights.add(fpe);
				else if(cal2.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)-count){//在前一小时，分钟大于等于指定时刻，则在一小时之内
					if(cal2.get(Calendar.MINUTE)>=calendar.get(Calendar.MINUTE))
						departureFlights.add(fpe);
				}
				else if(cal2.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)+count) {//在后一小时，分钟小于等于制定时刻，则在一小时之内
					if(cal2.get(Calendar.MINUTE)<=calendar.get(Calendar.MINUTE))
						departureFlights.add(fpe);
				}
			}
			else if(index==1&&ismiddle) {//是中间站点
				Date date3 = sdf.parse(fpe.getTime().get(0).getEndtime());//抵达中间站点时间
				Date date4 = sdf.parse(fpe.getTime().get(1).getStarttime());//离开中间站点时间
				Calendar cal3=Calendar.getInstance();
				Calendar cal4=Calendar.getInstance();
				cal3.setTime(date3);
				cal4.setTime(date4);
				if(cal3.get(0)==calendar.get(0)&&cal3.get(1)==calendar.get(1)&&cal3.get(6)==calendar.get(6)) {//抵达中间点时间在同一天
					if(cal3.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY))//在同一小时之内，必定符合要求
						reachFlights.add(fpe);
					else if(cal3.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)-count){//在前一小时，分钟大于等于指定时刻，则在一小时之内
						if(cal3.get(Calendar.MINUTE)>=calendar.get(Calendar.MINUTE))
							reachFlights.add(fpe);
					}
					else if(cal3.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)+count) {//在后一小时，分钟小于等于制定时刻，则在一小时之内
						if(cal3.get(Calendar.MINUTE)<=calendar.get(Calendar.MINUTE))
							reachFlights.add(fpe);
					}
				}
				else if(cal4.get(0)==calendar.get(0)&&cal3.get(1)==calendar.get(1)&&cal3.get(6)==calendar.get(6)) {//离开中间点时间在同一天
					if(cal4.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY))//在同一小时之内，必定符合要求
						departureFlights.add(fpe);
					else if(cal4.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)-count){//在前一小时，分钟大于等于指定时刻，则在一小时之内
						if(cal4.get(Calendar.MINUTE)>=calendar.get(Calendar.MINUTE))
							departureFlights.add(fpe);
					}
					else if(cal4.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)+count) {//在后一小时，分钟小于等于制定时刻，则在一小时之内
						if(cal4.get(Calendar.MINUTE)<=calendar.get(Calendar.MINUTE))
							departureFlights.add(fpe);
					}
				}
			}
		}
		sortFlights();
	}
	
	/**
	 * 为reachFlights和departureFlights中的元素排序
	 * @throws ParseException 未能将时间转化为标准格式
	 */
	private void sortFlights() throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		for(int i=0;i<reachFlights.size()-1;i++) {//为抵达航班排序
			Timeslot timei=reachFlights.get(i).getTime().get(0);
			Date minDate=sdf.parse(timei.getEndtime());
			int min=i;
			for(int j=i+1;j<reachFlights.size();j++) {//选择排序
				Timeslot timej=reachFlights.get(j).getTime().get(0);
				Date date=sdf.parse(timej.getEndtime());//比较每个航班的终止时间
				if(date.getTime()<minDate.getTime()) {
					minDate=date;
					min=j;
				}
			}
			if(min!=i) {
				FlightEntry<Plane> fpei=reachFlights.get(i);
				FlightEntry<Plane> fpemin=reachFlights.get(min);
				reachFlights.set(i, fpemin);
				reachFlights.set(min, fpei);
			}
		}
		
		for(int i=0;i<departureFlights.size()-1;i++) {//为出发航班排序
			Timeslot timei=departureFlights.get(i).getTime().get(0);
			Date minDate=sdf.parse(timei.getStarttime());
			int min=i;
			for(int j=i+1;j<departureFlights.size();j++) {//选择排序
				Timeslot timej=departureFlights.get(j).getTime().get(0);
				Date date=sdf.parse(timej.getStarttime());//比较航班的起始时间
				if(date.getTime()<minDate.getTime()) {
					minDate=date;
					min=j;
				}
			}
			if(min!=i) {
				FlightEntry<Plane> fpei=departureFlights.get(i);
				FlightEntry<Plane> fpemin=departureFlights.get(min);
				departureFlights.set(i, fpemin);
				departureFlights.set(min, fpei);
			}
		}
		
		int i=0,j=0;
		allFlights=new ArrayList<FlightEntry<Plane>>();
		while(i<reachFlights.size()||j<departureFlights.size()) {//将allFlights改造为包括所有航班（既包括抵达又包括出发）
			if(i==reachFlights.size()) {						//的时间从早到晚排序的有序航班集合
				allFlights.add(departureFlights.get(j));//抵达航班全部加入，将排序后的出发航班直接加入
				j++;
			}
			else if(j==departureFlights.size()) {//出发航班全部加入，将排序后的抵达航班直接加入
				allFlights.add(reachFlights.get(i));
				i++;
			}
			else {//二者更小的先加入
				Timeslot timei=reachFlights.get(i).getTime().get(0);
				Timeslot timej=departureFlights.get(j).getTime().get(0);
				Date datei=sdf.parse(timei.getEndtime());
				Date datej=sdf.parse(timej.getStarttime());
				if(datei.before(datej)||datei.equals(datej)) {
					allFlights.add(reachFlights.get(i));
					i++;
				}
				else {
					allFlights.add(departureFlights.get(j));
					j++;
				}
			}
		}
	}
	
	/**
	 * Board可视化展现
	 * @throws ParseException 时间未能转化为标准格式
	 */
	public void visualize() throws ParseException {
		Calendar calendar=Calendar.getInstance();
		setRequestFlights(calendar);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		String time=sdf.format(calendar.getTime());
		JFrame jf=new JFrame("FlightBoard");
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// 创建内容面板，使用边界布局
        JPanel panel = new JPanel(new BorderLayout());
        String[] col= {" ",time,location.getName()," "};
        Object[][] rowData=new Object[reachFlights.size()+1+departureFlights.size()+1][4];
        rowData[0][0]=" ";
        rowData[0][1]=" ";
        rowData[0][2]="抵达航班";
        rowData[0][3]=" ";
        for(int i=0;i<reachFlights.size();i++) {//将抵达航班可视化
        	FlightEntry<Plane> fpe=reachFlights.get(i);
        	Timeslot timeslot=fpe.getTime().get(0);
			Date date=sdf.parse(timeslot.getEndtime());
			Calendar calendar2=Calendar.getInstance();
			calendar2.setTime(date);
			int hour1=calendar2.get(Calendar.HOUR_OF_DAY);
			int minute1=calendar2.get(Calendar.MINUTE);
			String time1;//时间
			if(minute1<10)
				time1=hour1+":"+"0"+minute1;
			else 
				time1=hour1+":"+minute1;
			rowData[i+1][0]= time1;
			rowData[i+1][1]=fpe.getName();//航班名称
			int size=fpe.getLocation().size();
			String locationString=fpe.getLocation().get(0).getName()+"-"+fpe.getLocation().get(size-1).getName();//起点-终点
			rowData[i+1][2]=locationString;
			switch (fpe.getStateName()) {//状态
			case "Running":
				rowData[i+1][3]="即将降落";
				break;
			case "Cancelled":
				rowData[i+1][3]="已取消";
				break;
			case "Ended":
				rowData[i+1][3]="已降落";
				break;
			default:
				break;
			}
        }
        rowData[reachFlights.size()+1][0]=" ";
        rowData[reachFlights.size()+1][1]=" ";
        rowData[reachFlights.size()+1][2]="出发航班";
        rowData[reachFlights.size()+1][3]=" ";
        for(int i=0;i<departureFlights.size();i++) {//将出发航班可视化
        	FlightEntry<Plane> fpe=departureFlights.get(i);
        	Timeslot timeslot=fpe.getTime().get(0);
			Date date=sdf.parse(timeslot.getStarttime());
			Calendar calendar2=Calendar.getInstance();
			calendar2.setTime(date);
			int hour1=calendar2.get(Calendar.HOUR_OF_DAY);
			int minute1=calendar2.get(Calendar.MINUTE);
			String time1;//时间
			if(minute1<10)
				time1=hour1+":"+"0"+minute1;
			else 
				time1=hour1+":"+minute1;
			rowData[i+2+reachFlights.size()][0]= time1;//时间
			rowData[i+2+reachFlights.size()][1]=fpe.getName();//航班名称
			int size=fpe.getLocation().size();
			String locationString=fpe.getLocation().get(0).getName()+"-"+fpe.getLocation().get(size-1).getName();//起点-终点
			rowData[i+2+reachFlights.size()][2]=locationString;
			switch (fpe.getStateName()) {//状态
			case "Running":
				rowData[i+2+reachFlights.size()][3]="已起飞";
				break;
			case "Cancelled":
				rowData[i+2+reachFlights.size()][3]="已取消";
				break;
			case "Allocated":
				rowData[i+2+reachFlights.size()][3]="即将起飞";
				break;
			default:
				break;
			}
        }
        JTable table=new JTable(rowData,col);
        table.setGridColor(Color.GRAY);
        table.setForeground(Color.BLACK);
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(100);
        TableColumn tableColumn = table.getColumnModel().getColumn(1);
        tableColumn.setWidth(300);
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
		Location startlocation=new Location("10E","45N" , "哈尔滨机场", true);
		Location endlocation=new Location("3E","30N" , "浦东机场", true);
		List<Location> locations=new ArrayList<Location>();
		locations.add(startlocation);
		locations.add(endlocation);
		List<FlightEntry<Plane>> cpes=new ArrayList<>();
		FlightEntry<Plane> cpe1=FlightPlanningEntry.CreateFlight("NR5821");
		Timeslot timeslot=new Timeslot("2020-05-11 15:40", "2020-05-11 19:20");//根据现实时间调整测试时间,日期应设置为与测试日期相同
		List<Timeslot> oneTimeslot=new ArrayList<Timeslot>();
		oneTimeslot.add(timeslot);
		cpe1.setLocations(locations);
		cpe1.setTime(oneTimeslot);
		Plane plane=new Plane("N4112", "R", 100, 4.0);
		List<Plane> onePlane=new ArrayList<>();
		onePlane.add(plane);
		cpe1.allocateResource(onePlane);	
		cpe1.start();
		Timeslot timeslot2=new Timeslot("2020-05-11 10:11", "2020-05-11 15:50");//根据现实时间调整测试时间,日期应设置为与测试日期相同
		List<Timeslot> seTimeslot=new ArrayList<Timeslot>();
		seTimeslot.add(timeslot2);
		FlightEntry<Plane> cpe2=FlightPlanningEntry.CreateFlight("NT0121");
		List<Location> locations2=new ArrayList<Location>();
		locations2.add(endlocation);
		locations2.add(startlocation);
		cpe2.setLocations(locations2);
		cpe2.setTime(seTimeslot);
		cpe2.allocateResource(onePlane);
		cpe2.start();
		cpes.add(cpe2);
		cpes.add(cpe1);
		Calendar calendar=Calendar.getInstance();
		FlightBoard fb=new FlightBoard(startlocation, cpes,calendar);
		fb.visualize();
	}*/
	
}
