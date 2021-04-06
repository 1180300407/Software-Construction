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
 * ������Ϣ�壬�ɱ���
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
	//	AF(location,reachFlights,departureFlights,allFlights)=һ��location������Ϣ��
	//	����reachFlights������countСʱǰ��ĵִﺽ��
	//	departureFlights������countСʱǰ�����ɺ���
	//	allFlights��ʾ����countСʱǰ�󾭹���λ�õĺ��༯��
	//Representation invariant:
	//	allFlights�����к����Ӧ�ѷ���ɻ�
	//Safety from rep exposure:
	//	��Ա����ȫ��private�ģ�����ʽ����
	private void checkRep() {
		for(FlightEntry<Plane> fe:allFlights) {
			assert fe.getResource()!=null;
		}
	}
	
	/**
	 * ���캯��
	 * @param location ��Ϣ�������ľ���λ��
	 * @param allflights ��λ�ô������ĺ��༯�ϣ����к����Ӧ�ѷ���ɻ�
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
	
	//ʵ�ֵ�����
	@Override
	public Iterator<FlightEntry<Plane>> iterator(){
		try {
			sortFlights(); //��������ʵ���������
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return allFlights.iterator();
	}
	
	/**
	 * �����к��༯���������õ���locationλ��������ָ��ʱ��һСʱǰ��ĺ���,���ֱ𱣴�
	 * @param calendar ָ��ʱ��
	 * @throws ParseException δ�ܽ�ʱ��ת��Ϊ��׼��ʽ
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
			boolean ismiddle=false;//��ʶ�ú����Ƿ����м�վ
			int index=fpe.getLocation().indexOf(location);
			timeslot=fpe.getTime().get(0);
			Date date1=null;//����ʱ��
			if(fpe.getLocation().size()==2) {//û�о�ͣվ
				date1 = sdf.parse(timeslot.getEndtime());
			}
			else if(fpe.getLocation().size()==3){//�о�ͣվ
				Timeslot timeslot2=fpe.getTime().get(1);
				date1=sdf.parse(timeslot2.getEndtime());
				ismiddle=true;
			}
			else {//λ�ø�������׼
				continue;
			}
			Date date2 = sdf.parse(timeslot.getStarttime());//���ʱ��
			
			Calendar cal1=Calendar.getInstance();
			Calendar cal2=Calendar.getInstance();
			cal1.setTime(date1);
			cal2.setTime(date2);
			if(index==fpe.getLocation().size()-1&&cal1.get(0)==calendar.get(0)&&cal1.get(1)==calendar.get(1)&&cal1.get(6)==calendar.get(6)) {//ָ��ʱ����ִ�ʱ����ͬһ��
				if(cal1.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY))//��ͬһСʱ֮�ڣ��ض�����Ҫ��
					reachFlights.add(fpe);
				else if(cal1.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)-count){//��ǰһСʱ�����Ӵ��ڵ���ָ��ʱ�̣�����һСʱ֮��
					if(cal1.get(Calendar.MINUTE)>=calendar.get(Calendar.MINUTE))
						reachFlights.add(fpe);
				}
				else if(cal1.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)+count) {//�ں�һСʱ������С�ڵ����ƶ�ʱ�̣�����һСʱ֮��
					if(cal1.get(Calendar.MINUTE)<=calendar.get(Calendar.MINUTE))
						reachFlights.add(fpe);
				}
			}
			else  if(index==0&&cal2.get(0)==calendar.get(0)&&cal2.get(1)==calendar.get(1)&&cal2.get(6)==calendar.get(6)) {//ָ��ʱ�������ʱ����ͬһ��
				if(cal2.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY))//��ͬһСʱ֮�ڣ��ض�����Ҫ��
					departureFlights.add(fpe);
				else if(cal2.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)-count){//��ǰһСʱ�����Ӵ��ڵ���ָ��ʱ�̣�����һСʱ֮��
					if(cal2.get(Calendar.MINUTE)>=calendar.get(Calendar.MINUTE))
						departureFlights.add(fpe);
				}
				else if(cal2.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)+count) {//�ں�һСʱ������С�ڵ����ƶ�ʱ�̣�����һСʱ֮��
					if(cal2.get(Calendar.MINUTE)<=calendar.get(Calendar.MINUTE))
						departureFlights.add(fpe);
				}
			}
			else if(index==1&&ismiddle) {//���м�վ��
				Date date3 = sdf.parse(fpe.getTime().get(0).getEndtime());//�ִ��м�վ��ʱ��
				Date date4 = sdf.parse(fpe.getTime().get(1).getStarttime());//�뿪�м�վ��ʱ��
				Calendar cal3=Calendar.getInstance();
				Calendar cal4=Calendar.getInstance();
				cal3.setTime(date3);
				cal4.setTime(date4);
				if(cal3.get(0)==calendar.get(0)&&cal3.get(1)==calendar.get(1)&&cal3.get(6)==calendar.get(6)) {//�ִ��м��ʱ����ͬһ��
					if(cal3.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY))//��ͬһСʱ֮�ڣ��ض�����Ҫ��
						reachFlights.add(fpe);
					else if(cal3.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)-count){//��ǰһСʱ�����Ӵ��ڵ���ָ��ʱ�̣�����һСʱ֮��
						if(cal3.get(Calendar.MINUTE)>=calendar.get(Calendar.MINUTE))
							reachFlights.add(fpe);
					}
					else if(cal3.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)+count) {//�ں�һСʱ������С�ڵ����ƶ�ʱ�̣�����һСʱ֮��
						if(cal3.get(Calendar.MINUTE)<=calendar.get(Calendar.MINUTE))
							reachFlights.add(fpe);
					}
				}
				else if(cal4.get(0)==calendar.get(0)&&cal3.get(1)==calendar.get(1)&&cal3.get(6)==calendar.get(6)) {//�뿪�м��ʱ����ͬһ��
					if(cal4.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY))//��ͬһСʱ֮�ڣ��ض�����Ҫ��
						departureFlights.add(fpe);
					else if(cal4.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)-count){//��ǰһСʱ�����Ӵ��ڵ���ָ��ʱ�̣�����һСʱ֮��
						if(cal4.get(Calendar.MINUTE)>=calendar.get(Calendar.MINUTE))
							departureFlights.add(fpe);
					}
					else if(cal4.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)+count) {//�ں�һСʱ������С�ڵ����ƶ�ʱ�̣�����һСʱ֮��
						if(cal4.get(Calendar.MINUTE)<=calendar.get(Calendar.MINUTE))
							departureFlights.add(fpe);
					}
				}
			}
		}
		sortFlights();
	}
	
	/**
	 * ΪreachFlights��departureFlights�е�Ԫ������
	 * @throws ParseException δ�ܽ�ʱ��ת��Ϊ��׼��ʽ
	 */
	private void sortFlights() throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		for(int i=0;i<reachFlights.size()-1;i++) {//Ϊ�ִﺽ������
			Timeslot timei=reachFlights.get(i).getTime().get(0);
			Date minDate=sdf.parse(timei.getEndtime());
			int min=i;
			for(int j=i+1;j<reachFlights.size();j++) {//ѡ������
				Timeslot timej=reachFlights.get(j).getTime().get(0);
				Date date=sdf.parse(timej.getEndtime());//�Ƚ�ÿ���������ֹʱ��
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
		
		for(int i=0;i<departureFlights.size()-1;i++) {//Ϊ������������
			Timeslot timei=departureFlights.get(i).getTime().get(0);
			Date minDate=sdf.parse(timei.getStarttime());
			int min=i;
			for(int j=i+1;j<departureFlights.size();j++) {//ѡ������
				Timeslot timej=departureFlights.get(j).getTime().get(0);
				Date date=sdf.parse(timej.getStarttime());//�ȽϺ������ʼʱ��
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
		while(i<reachFlights.size()||j<departureFlights.size()) {//��allFlights����Ϊ�������к��ࣨ�Ȱ����ִ��ְ���������
			if(i==reachFlights.size()) {						//��ʱ����絽����������򺽰༯��
				allFlights.add(departureFlights.get(j));//�ִﺽ��ȫ�����룬�������ĳ�������ֱ�Ӽ���
				j++;
			}
			else if(j==departureFlights.size()) {//��������ȫ�����룬�������ĵִﺽ��ֱ�Ӽ���
				allFlights.add(reachFlights.get(i));
				i++;
			}
			else {//���߸�С���ȼ���
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
	 * Board���ӻ�չ��
	 * @throws ParseException ʱ��δ��ת��Ϊ��׼��ʽ
	 */
	public void visualize() throws ParseException {
		Calendar calendar=Calendar.getInstance();
		setRequestFlights(calendar);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		String time=sdf.format(calendar.getTime());
		JFrame jf=new JFrame("FlightBoard");
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// ����������壬ʹ�ñ߽粼��
        JPanel panel = new JPanel(new BorderLayout());
        String[] col= {" ",time,location.getName()," "};
        Object[][] rowData=new Object[reachFlights.size()+1+departureFlights.size()+1][4];
        rowData[0][0]=" ";
        rowData[0][1]=" ";
        rowData[0][2]="�ִﺽ��";
        rowData[0][3]=" ";
        for(int i=0;i<reachFlights.size();i++) {//���ִﺽ����ӻ�
        	FlightEntry<Plane> fpe=reachFlights.get(i);
        	Timeslot timeslot=fpe.getTime().get(0);
			Date date=sdf.parse(timeslot.getEndtime());
			Calendar calendar2=Calendar.getInstance();
			calendar2.setTime(date);
			int hour1=calendar2.get(Calendar.HOUR_OF_DAY);
			int minute1=calendar2.get(Calendar.MINUTE);
			String time1;//ʱ��
			if(minute1<10)
				time1=hour1+":"+"0"+minute1;
			else 
				time1=hour1+":"+minute1;
			rowData[i+1][0]= time1;
			rowData[i+1][1]=fpe.getName();//��������
			int size=fpe.getLocation().size();
			String locationString=fpe.getLocation().get(0).getName()+"-"+fpe.getLocation().get(size-1).getName();//���-�յ�
			rowData[i+1][2]=locationString;
			switch (fpe.getStateName()) {//״̬
			case "Running":
				rowData[i+1][3]="��������";
				break;
			case "Cancelled":
				rowData[i+1][3]="��ȡ��";
				break;
			case "Ended":
				rowData[i+1][3]="�ѽ���";
				break;
			default:
				break;
			}
        }
        rowData[reachFlights.size()+1][0]=" ";
        rowData[reachFlights.size()+1][1]=" ";
        rowData[reachFlights.size()+1][2]="��������";
        rowData[reachFlights.size()+1][3]=" ";
        for(int i=0;i<departureFlights.size();i++) {//������������ӻ�
        	FlightEntry<Plane> fpe=departureFlights.get(i);
        	Timeslot timeslot=fpe.getTime().get(0);
			Date date=sdf.parse(timeslot.getStarttime());
			Calendar calendar2=Calendar.getInstance();
			calendar2.setTime(date);
			int hour1=calendar2.get(Calendar.HOUR_OF_DAY);
			int minute1=calendar2.get(Calendar.MINUTE);
			String time1;//ʱ��
			if(minute1<10)
				time1=hour1+":"+"0"+minute1;
			else 
				time1=hour1+":"+minute1;
			rowData[i+2+reachFlights.size()][0]= time1;//ʱ��
			rowData[i+2+reachFlights.size()][1]=fpe.getName();//��������
			int size=fpe.getLocation().size();
			String locationString=fpe.getLocation().get(0).getName()+"-"+fpe.getLocation().get(size-1).getName();//���-�յ�
			rowData[i+2+reachFlights.size()][2]=locationString;
			switch (fpe.getStateName()) {//״̬
			case "Running":
				rowData[i+2+reachFlights.size()][3]="�����";
				break;
			case "Cancelled":
				rowData[i+2+reachFlights.size()][3]="��ȡ��";
				break;
			case "Allocated":
				rowData[i+2+reachFlights.size()][3]="�������";
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
	/*   //�����õ�main����
	public static void main(String[] args) throws ParseException {
		Location startlocation=new Location("10E","45N" , "����������", true);
		Location endlocation=new Location("3E","30N" , "�ֶ�����", true);
		List<Location> locations=new ArrayList<Location>();
		locations.add(startlocation);
		locations.add(endlocation);
		List<FlightEntry<Plane>> cpes=new ArrayList<>();
		FlightEntry<Plane> cpe1=FlightPlanningEntry.CreateFlight("NR5821");
		Timeslot timeslot=new Timeslot("2020-05-11 15:40", "2020-05-11 19:20");//������ʵʱ���������ʱ��,����Ӧ����Ϊ�����������ͬ
		List<Timeslot> oneTimeslot=new ArrayList<Timeslot>();
		oneTimeslot.add(timeslot);
		cpe1.setLocations(locations);
		cpe1.setTime(oneTimeslot);
		Plane plane=new Plane("N4112", "R", 100, 4.0);
		List<Plane> onePlane=new ArrayList<>();
		onePlane.add(plane);
		cpe1.allocateResource(onePlane);	
		cpe1.start();
		Timeslot timeslot2=new Timeslot("2020-05-11 10:11", "2020-05-11 15:50");//������ʵʱ���������ʱ��,����Ӧ����Ϊ�����������ͬ
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
