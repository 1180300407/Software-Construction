package Timeslot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一组起始时间对,不可变类
 * @author 123
 *
 */

public class Timeslot {
	private final String starttime;
	private final String endtime;
	
	//Abstraction function:
	//	AF(starttime,endtime)=以starttime为起始时间，endtime为终止时间的时间对
	//Representation invariant:
	//	starttime和endtime都是 yyyy-MM-dd HH:mm 形式
	//	endtime晚于starttime
	//Safety from rep exposure:
	//	成员变量全是private且final的，均为不可变类型，不存在表示泄露
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
	 * 构造函数
	 * @param starttime 开始时间
	 * @param endtime 终止时间
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
			throw new RuntimeException("时间格式不合法!");
		}
	}

	/**
	 * @return 返回时间对的起始时间
	 */
	public String getStarttime() {
		return starttime;
	}

	/**
	 * @return 返回时间对的终止时间
	 */
	public String getEndtime() {
		return endtime;
	}

	//不可变类重写equals与hashcode
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