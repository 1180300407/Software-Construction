package Timeslot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * һ����ʼʱ���,���ɱ���
 * @author 123
 *
 */

public class Timeslot {
	private final String starttime;
	private final String endtime;
	
	//Abstraction function:
	//	AF(starttime,endtime)=��starttimeΪ��ʼʱ�䣬endtimeΪ��ֹʱ���ʱ���
	//Representation invariant:
	//	starttime��endtime���� yyyy-MM-dd HH:mm ��ʽ
	//	endtime����starttime
	//Safety from rep exposure:
	//	��Ա����ȫ��private��final�ģ���Ϊ���ɱ����ͣ������ڱ�ʾй¶
	private void checkRep() {
		String pattern="yyyy-MM-dd HH:mm";
		SimpleDateFormat dateFormat=new SimpleDateFormat(pattern);
		try {
			Date date1=dateFormat.parse(starttime);
			Date date2=dateFormat.parse(endtime);
			assert date2.after(date1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ���캯��
	 * @param starttime ��ʼʱ��
	 * @param endtime ��ֹʱ��
	 */
	public Timeslot(String starttime,String endtime) {
		String pathString="\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}";
		Pattern pattern=Pattern.compile(pathString);
		Matcher matcher1=pattern.matcher(starttime);
		Matcher matcher2=pattern.matcher(endtime);
		if(matcher1.matches()&&matcher2.matches()) {
			this.starttime=starttime;
			this.endtime=endtime;
			checkRep();
		}
		else {
			throw new RuntimeException("ʱ���ʽ���Ϸ�!");
		}
	}

	/**
	 * @return ����ʱ��Ե���ʼʱ��
	 */
	public String getStarttime() {
		return starttime;
	}

	/**
	 * @return ����ʱ��Ե���ֹʱ��
	 */
	public String getEndtime() {
		return endtime;
	}

	//���ɱ�����дequals��hashcode
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endtime == null) ? 0 : endtime.hashCode());
		result = prime * result + ((starttime == null) ? 0 : starttime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Timeslot other = (Timeslot) obj;
		if (endtime == null) {
			if (other.endtime != null)
				return false;
		} else if (!endtime.equals(other.endtime))
			return false;
		if (starttime == null) {
			if (other.starttime != null)
				return false;
		} else if (!starttime.equals(other.starttime))
			return false;
		return true;
	}
	
	
}