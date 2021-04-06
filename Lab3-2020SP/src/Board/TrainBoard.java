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
	//	AF(location,reachTrains,leaveTrains,trains)=һ��location������Ϣ��
	//	����reachTrains������countСʱǰ��ĵִﳵ��
	//	reachTrains������countСʱǰ��ķ�������
	//	trains��ʾ����countСʱǰ�󾭹���λ�õĳ��μ���
	//Representation invariant:
	//	trains�������г���Ӧ�ѷ��䳵��
	//Safety from rep exposure:
	//	��Ա����ȫ��private�ģ�����ʽ����
	private void checkRep() {
		for(TrainEntry<Carriage> te:trains) {
			assert te.getResource()!=null;
		}
	}
	
	/**
	 * ���캯��
	 * @param location ��Ϣ������λ��
	 * @param tpes ��λ�ô��������г����ϣ������г���Ӧ�ѷ��䳵��
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
	
	//ʵ�ֵ�����
	@Override
	public Iterator<TrainEntry<Carriage>> iterator(){
		sortTrains();//��������ʵ���������
		return trains.iterator();
	}
	
	/**
	 * ��Board�д洢�������г������������õ���locationλ��������ָ��ʱ��һСʱǰ��ĳ���,���ֱ𱣴�
	 * @param calendar ָ��ʱ��
	 * @throws ParseException δ�ܽ�ʱ��ת��Ϊ��׼��ʽ
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
			if(index==0) {//��λ��Ϊ��㣬ֻ�п������뿪������
				timeslot=tpe.getTime().get(0);
				Date date=sdf.parse(timeslot.getStarttime());
				Calendar cal=Calendar.getInstance();
				cal.setTime(date);
				if(cal.get(0)==calendar.get(0)&&cal.get(1)==calendar.get(1)&&cal.get(6)==calendar.get(6)) {
					if(cal.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY))//��ͬһСʱ֮�ڣ��ض�����Ҫ��
						leaveTrains.put(tpe, cal.getTime().getTime());
					else if(cal.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)-count){//��ǰһСʱ�����Ӵ��ڵ���ָ��ʱ�̣�����һСʱ֮��
						if(cal.get(Calendar.MINUTE)>=calendar.get(Calendar.MINUTE))
							leaveTrains.put(tpe, cal.getTime().getTime());
					}
					else if(cal.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)+count) {//�ں�һСʱ������С�ڵ����ƶ�ʱ�̣�����һСʱ֮��
						if(cal.get(Calendar.MINUTE)<=calendar.get(Calendar.MINUTE))
							leaveTrains.put(tpe, cal.getTime().getTime());
					}
				}
			}
			else if(index==tpe.getLocation().size()-1) {//��λ��Ϊ�յ㣬ֻ�п����ڵִﳵ����
				timeslot=tpe.getTime().get(index-1);
				Date date=sdf.parse(timeslot.getEndtime());
				Calendar cal=Calendar.getInstance();
				cal.setTime(date);
				if(cal.get(0)==calendar.get(0)&&cal.get(1)==calendar.get(1)&&cal.get(6)==calendar.get(6)) {
					if(cal.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY))//��ͬһСʱ֮�ڣ��ض�����Ҫ��
						reachTrains.put(tpe, cal.getTime().getTime());
					else if(cal.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)-count){//��ǰһСʱ�����Ӵ��ڵ���ָ��ʱ�̣�����һСʱ֮��
						if(cal.get(Calendar.MINUTE)>=calendar.get(Calendar.MINUTE))
							reachTrains.put(tpe, cal.getTime().getTime());
					}
					else if(cal.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)+count) {//�ں�һСʱ������С�ڵ����ƶ�ʱ�̣�����һСʱ֮��
						if(cal.get(Calendar.MINUTE)<=calendar.get(Calendar.MINUTE))
							reachTrains.put(tpe, cal.getTime().getTime());
					}
				}
			}
			else {//�м�վ��
				Timeslot timeslot1=tpe.getTime().get(index-1);//�����index��λ�õ�ʱ��
				Date date1=sdf.parse(timeslot1.getEndtime());
				Calendar cal1=Calendar.getInstance();
				cal1.setTime(date1);
				Timeslot timeslot2=tpe.getTime().get(index);
				Date date2=sdf.parse(timeslot2.getStarttime());//�ӵ�index��λ���뿪��ʱ��
				Calendar cal2=Calendar.getInstance();
				cal2.setTime(date2);
				if(cal1.get(0)==calendar.get(0)&&cal1.get(1)==calendar.get(1)&&cal1.get(6)==calendar.get(6)) {//��ͬһ��
					if(cal1.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY))//��ͬһСʱ֮�ڣ��ض�����Ҫ��
						reachTrains.put(tpe, cal1.getTime().getTime());
					else if(cal1.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY-count)){//��ǰһСʱ�����Ӵ��ڵ���ָ��ʱ�̣�����һСʱ֮��
						if(cal1.get(Calendar.MINUTE)>=calendar.get(Calendar.MINUTE))
							reachTrains.put(tpe, cal1.getTime().getTime());
					}
					else if(cal1.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)+count) {//�ں�һСʱ������С�ڵ����ƶ�ʱ�̣�����һСʱ֮��
						if(cal1.get(Calendar.MINUTE)<=calendar.get(Calendar.MINUTE))
							reachTrains.put(tpe, cal1.getTime().getTime());
					}
				}
				if(cal2.get(0)==calendar.get(0)&&cal2.get(1)==calendar.get(1)&&cal2.get(6)==calendar.get(6)) {//��ͬһ��
					if(cal2.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY))//��ͬһСʱ֮�ڣ��ض�����Ҫ��
						leaveTrains.put(tpe, cal2.getTime().getTime());
					else if(cal1.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)-count){//��ǰһСʱ�����Ӵ��ڵ���ָ��ʱ�̣�����һСʱ֮��
						if(cal1.get(Calendar.MINUTE)>=calendar.get(Calendar.MINUTE))
							leaveTrains.put(tpe, cal2.getTime().getTime());
					}
					else if(cal1.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)+count) {//�ں�һСʱ������С�ڵ����ƶ�ʱ�̣�����һСʱ֮��
						if(cal1.get(Calendar.MINUTE)<=calendar.get(Calendar.MINUTE))
							leaveTrains.put(tpe, cal2.getTime().getTime());
					}
				}
			}
		}
		sortTrains();
	}
	
	/**
	 * ΪreachTrains��departureTrains�е�Ԫ�������Լ�trains����
	 */
	private void sortTrains() {
		Map<TrainEntry<Carriage>, Long> sortreach=new LinkedHashMap<TrainEntry<Carriage>, Long>();
		Map<TrainEntry<Carriage>, Long> sortleave=new LinkedHashMap<TrainEntry<Carriage>, Long>();
		List<Map.Entry<TrainEntry<Carriage>, Long>> list1=new ArrayList<>();
		list1.addAll(reachTrains.entrySet());
		List<Map.Entry<TrainEntry<Carriage>, Long>> list2=new ArrayList<>();
		list2.addAll(leaveTrains.entrySet());//��map��Ԫ�ظ��Ƶ�list���Խ�������
		Collections.sort(list1, new MapvalueComparator());
		Collections.sort(list2,new MapvalueComparator());
		for(Map.Entry<TrainEntry<Carriage>, Long> entry:list1) {//�������ļ��뵽����map��
			sortreach.put(entry.getKey(), entry.getValue());
		}
		
		for(Map.Entry<TrainEntry<Carriage>, Long> entry:list2) {//�������ļ��뵽����map��
			sortleave.put(entry.getKey(), entry.getValue());
		}
		this.reachTrains=sortreach;
		this.leaveTrains=sortleave;
		trains=new ArrayList<TrainEntry<Carriage>>();
		int i=0,j=0;
		while(i<list1.size()||j<list2.size()) {//��leaveTrains��reachTrains�������г������絽��˳�����trains
			if(i==list1.size()) {		//ʹ���Ϊ���絽������������г�����
				trains.add(list2.get(j).getKey());//�ִ��г�ȫ�����룬�������ĳ����г�ֱ�Ӽ���
				j++;
			}
			else if(j==list2.size()) {//�����г�ȫ�����룬�������ĵִ��г�ֱ�Ӽ���
				trains.add(list1.get(i).getKey());
				i++;
			}
			else {//���߸�С���ȼ���
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
	 * Board�Ŀ��ӻ�չ��
	 * @throws ParseException ʱ��δ��ת��Ϊ��׼��ʽ
	 */
	public void visualize() throws ParseException {
		Calendar calendar=Calendar.getInstance();
		setRequestTrains(calendar);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		String time=sdf.format(calendar.getTime());
		JFrame jf=new JFrame("TrainBoard");
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// ����������壬ʹ�ñ߽粼��
        JPanel panel = new JPanel(new BorderLayout());
        String[] col= {" ",time,location.getName()," "};
        Object[][] rowData=new Object[reachTrains.size()+1+leaveTrains.size()+1][4];
        int i=1;
        rowData[0][0]=" ";
        rowData[0][1]=" ";
        rowData[0][2]="�ִﳵ��";
        rowData[0][3]=" ";
        for(Map.Entry<TrainEntry<Carriage>, Long> entry:reachTrains.entrySet()) {//���ִﳵ�ο��ӻ�
        	TrainEntry<Carriage> tpe=entry.getKey();
        	Date date=new Date(entry.getValue());
        	sdf.format(date);
			Calendar calendar2=Calendar.getInstance();
			calendar2.setTime(date);
			int hour1=calendar2.get(Calendar.HOUR_OF_DAY);
			int minute1=calendar2.get(Calendar.MINUTE);
			String time1;//ʱ��
			if(minute1<10)
				time1=hour1+":"+"0"+minute1;
			else 
				time1=hour1+":"+minute1;
			rowData[i][0]= time1;
			rowData[i][1]=tpe.getName();//�г�����
			int maxindex=tpe.getLocation().size()-1;
			String locationString=tpe.getLocation().get(0).getName()+"-"+tpe.getLocation().get(maxindex).getName();//���-�յ�
			rowData[i][2]=locationString;
			switch (tpe.getStateName()) {//״̬
			case "Running":
				rowData[i][3]="�����ִ�";
				break;
			case "Cancelled":
				rowData[i][3]="��ȡ��";
				break;
			case "Ended":
				rowData[i][3]="�ѵ���";
				break;
			case "Blocked":
				rowData[i][3]="�ѵ���";
				break;
			default:
				break;
			}
			i++;
        }
        rowData[i][0]=" ";
        rowData[i][1]=" ";
        rowData[i][2]="��������";
        rowData[i][3]=" ";
        i++;
        for(Map.Entry<TrainEntry<Carriage>, Long> entry:leaveTrains.entrySet()) {//������������ӻ�
        	TrainEntry<Carriage> tpe=entry.getKey();
        	Date date=new Date(entry.getValue());
        	sdf.format(date);
			Calendar calendar2=Calendar.getInstance();
			calendar2.setTime(date);
			int hour1=calendar2.get(Calendar.HOUR_OF_DAY);
			int minute1=calendar2.get(Calendar.MINUTE);
			String time1;//ʱ��
			if(minute1<10)
				time1=hour1+":"+"0"+minute1;
			else 
				time1=hour1+":"+minute1;
			rowData[i][0]= time1;
			rowData[i][1]=tpe.getName();//�г�����
			int maxindex=tpe.getLocation().size()-1;
			String locationString=tpe.getLocation().get(0).getName()+"-"+tpe.getLocation().get(maxindex).getName();//���-�յ�
			rowData[i][2]=locationString;
			switch (tpe.getStateName()) {//״̬
			case "Running":
				rowData[i][3]="�ѷ���";
				break;
			case "Cancelled":
				rowData[i][3]="��ȡ��";
				break;
			case "Allocated":
				rowData[i][3]="��������";
				break;
			case "Blocked":
				rowData[i][3]="��������";
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
	
	/*   //�����õ�main����
	public static void main(String[] args) throws ParseException {
		Location startlocation=new Location("10E","45N" , "��������", true);
		Location endlocation=new Location("3E","30N" , "������", true);
		List<TrainEntry<Carriage>> cpes=new ArrayList<>();
		TrainEntry<Carriage> cpe1=TrainPlanningEntry.CreateTrain("T147");
		List<Location> locations=new ArrayList<Location>();
		locations.add(startlocation);
		locations.add(endlocation);
		Timeslot timeslot=new Timeslot("2020-05-14 22:00", "2020-05-15 03:20");//������ʵʱ���������ʱ��,����Ӧ����Ϊ�����������ͬ
		List<Timeslot> oneTimeslot=new ArrayList<Timeslot>();
		oneTimeslot.add(timeslot);
		cpe1.setLocations(locations);
		cpe1.setTime(oneTimeslot);
		Carriage carriage=new Carriage("4", "rt", 200, "century");
		List<Carriage> carriages=new ArrayList<>();
		carriages.add(carriage);
		cpe1.allocateResource(carriages);	
		cpe1.start();
		Timeslot timeslot2=new Timeslot("2020-05-13 10:11", "2020-05-14 22:50");//������ʵʱ���������ʱ��,����Ӧ����Ϊ�����������ͬ
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
		Timeslot timeslot3=new Timeslot("2020-05-13 10:11", "2020-05-14 22:30");//������ʵʱ���������ʱ��,����Ӧ����Ϊ�����������ͬ
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
	
	
