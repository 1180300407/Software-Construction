package Board;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.table.TableColumn;
import Resources.Teacher;

import Location.Location;
import LogFile.MyFormatter;
import Timeslot.Timeslot;
import compositeinterface.*;
/**
 * һ�����ҿγ̱�
 * @author 123
 *
 */

public class CourseBoard implements Iterable<CourseEntry<Teacher>>{
	private final Location location;
	private List<CourseEntry<Teacher>> courses=new ArrayList<>();
	private static Logger myLogger=Logger.getLogger("CourseBoardLog");
	//Abstraction function:
	//	AF(location,courses)=һ��location������Ϣ��
	//	����courses�������е�����location�����еĿγ�
	//Representation invariant:
	//	courses�����пγ̾�Ӧ�ѷ����ʦ
	//Safety from rep exposure:
	//	��Ա����ȫ��private��,����ʽ����
	private void checkRep() {
		for(CourseEntry<Teacher> ce:courses) {
			assert ce.getResource()!=null;
			assert ce.getResource().size()!=0;
			//��־��¼
			myLogger.setLevel(Level.INFO);
			myLogger.setUseParentHandlers(false);
			//д���ļ�
			FileHandler handler=null;
			try {
				handler = new FileHandler("src/LogFile/CourseBoardLog.log");
				handler.setFormatter(new MyFormatter());//���ù̶���ʽ
				handler.setLevel(Level.INFO);
				myLogger.addHandler(handler);
				myLogger.info("���в��������");
				handler.close();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
	}
	/**
	 * ���캯��
	 * @param location ��Ϣ�������ľ���λ��
	 * @param cpes ��λ�ô������Ŀγ̼��ϣ����пγ̾�Ӧ�ѷ����ʦ
	 */
	public CourseBoard(Location location,List<CourseEntry<Teacher>> cpes,Calendar calendar) {
		this.location=location;
		this.courses=cpes;
		checkRep();
		try {
			setRequestCourses(calendar);
		} catch (ParseException e) {
			e.printStackTrace();
		}	
	}
	//ʵ�ֵ�����
	@Override
	public Iterator<CourseEntry<Teacher>> iterator(){
		try {
			sortCourses();//��������ʵ���������
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return courses.iterator();
	}
	
	/**
	 * �����пγ̼����������õ�������locationλ�õĿγ�,������
	 * @param calendar ָ��ʱ��
	 * @throws ParseException δ�ܽ�ʱ��ת��Ϊ��׼��ʽ
	 */
	public void setRequestCourses(Calendar calendar) throws ParseException {
		List<CourseEntry<Teacher>> newcpes=new ArrayList<CourseEntry<Teacher>>();
		newcpes.addAll(courses);
		this.courses=new ArrayList<CourseEntry<Teacher>>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		Timeslot timeslot;
		for(CourseEntry<Teacher> cpe:newcpes) {
			if(!cpe.getLocation().get(0).equals(location)) {//���ұ�����ͬ
				continue;
			}
			if(cpe.getResource().isEmpty())
				continue;
			timeslot=cpe.getTime().get(0);
			Date date1=sdf.parse(timeslot.getStarttime());
			Calendar cal1=Calendar.getInstance();
			cal1.setTime(date1);
			if(cal1.get(0)==calendar.get(0)&&cal1.get(1)==calendar.get(1)&&cal1.get(6)==calendar.get(6)) {//ָ��ʱ�����Ͽ�ʱ����ͬһ��
				courses.add(cpe);
			}
		}
		sortCourses();
	}
	
	/**
	 * Ϊcourses�е�Ԫ������
	 * @throws ParseException δ�ܽ�ʱ��ת��Ϊ��׼��ʽ
	 */
	private void sortCourses() throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		for(int i=0;i<courses.size()-1;i++) {
			Timeslot timei=courses.get(i).getTime().get(0);
			Date minDate=sdf.parse(timei.getEndtime());
			int min=i;
			for(int j=i+1;j<courses.size();j++) {
				Timeslot timej=courses.get(j).getTime().get(0);
				Date date=sdf.parse(timej.getEndtime());
				if(date.getTime()<minDate.getTime()) {
					minDate=date;
					min=j;
				}
			}
			if(min!=i) {
				CourseEntry<Teacher> cpei=courses.get(i);
				CourseEntry<Teacher> cpemin=courses.get(min);
				courses.set(i, cpemin);
				courses.set(min, cpei);
			}
		}
	}
	
	/**
	 * ��Ϣ����ӻ���չʾ��ʵʱ�䵱�յ����пγ�
	 * @throws ParseException ʱ��δ��ת��Ϊ��׼��ʽ
	 */
	public void visualize() throws ParseException {
		Calendar calendar=Calendar.getInstance();
		setRequestCourses(calendar);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		String time=sdf.format(calendar.getTime());
		JFrame jf=new JFrame("CourseBoard");
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// ����������壬ʹ�ñ߽粼��
        JPanel panel = new JPanel(new BorderLayout());
        String[] col= {" ",time,location.getName()," "};
        Object[][] rowData=new Object[courses.size()][4];
        for(int i=0;i<courses.size();i++) {
        	CourseEntry<Teacher> cpe=courses.get(i);
        	Timeslot timeslot=cpe.getTime().get(0);
			Date date=sdf.parse(timeslot.getStarttime());
			Calendar calendar2=Calendar.getInstance();
			calendar2.setTime(date);
			int hour1=calendar2.get(Calendar.HOUR_OF_DAY);
			int minute1=calendar2.get(Calendar.MINUTE);
			Date date2=sdf.parse(timeslot.getEndtime());
			sdf.format(date2);
			calendar2.setTime(date2);
			int hour2=calendar2.get(Calendar.HOUR_OF_DAY);
			int minute2=calendar2.get(Calendar.MINUTE);
			String time1;
			String time2;
			if(minute1<10)
				time1=hour1+":"+"0"+minute1;
			else 
				time1=hour1+":"+minute1;
			if(minute2<10)
				time2=hour2+":"+"0"+minute2;
			else 
				time2=hour2+":"+minute2;
			rowData[i][0]= time1+"-"+time2;
			rowData[i][1]=cpe.getName();
			rowData[i][2]=cpe.getResource().get(0).getName();
			switch (cpe.getStateName()) {
			case "Running":
				rowData[i][3]="�����Ͽ�";
				break;
			case "Cancelled":
				rowData[i][3]="��ȡ��";
				break;
			case "Allocated":
				rowData[i][3]="���Ͽ�";
				break;
			case "Ended":
				rowData[i][3]="���¿�";
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
		Location location=new Location("10E","30S" , "����A", false);
		List<Location> oneLocation=new ArrayList<Location>();
		oneLocation.add(location);
		List<CourseEntry<Teacher>> cpes=new ArrayList<CourseEntry<Teacher>>();
		CourseEntry<Teacher> cpe1=CoursePlanningEntry.CreateCourse("soft");
		Timeslot timeslot=new Timeslot("2020-05-11 16:20", "2020-05-11 19:20");//����������ʱ����в���,��ע��Ҫ��ʾ������Ӧ������Ϊ�����������ͬ
		List<Timeslot> oneTimeslot=new ArrayList<Timeslot>();
		oneTimeslot.add(timeslot);
		cpe1.setLocation(oneLocation);
		cpe1.setTime(oneTimeslot);
		Teacher teacher=new Teacher("130283xxx", "name", true, "professtionalTitle");
		List<Teacher> oneTeacher=new ArrayList<Teacher>();
		oneTeacher.add(teacher);
		cpe1.allocateResource(oneTeacher);	
		Timeslot timeslot2=new Timeslot("2020-05-11 18:30", "2020-05-11 19:50");//����������ʱ����в���,��ע��Ҫ��ʾ������Ӧ������Ϊ�����������ͬ
		List<Timeslot> seTimeslot=new ArrayList<Timeslot>();
		seTimeslot.add(timeslot2);
		CourseEntry<Teacher> cpe2=CoursePlanningEntry.CreateCourse("ware");
		cpe2.setLocation(oneLocation);
		cpe2.setTime(seTimeslot);
		cpe2.allocateResource(oneTeacher);
		cpes.add(cpe2);
		cpes.add(cpe1);
		Calendar calendar=Calendar.getInstance();
		CourseBoard cb=new CourseBoard(location, cpes,calendar);
		cb.visualize();
	}*/
}
