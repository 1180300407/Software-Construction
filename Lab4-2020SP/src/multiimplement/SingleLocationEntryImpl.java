package multiimplement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import Location.Location;
import LogFile.MyFormatter;
import multidimension.SingleLocationEntry;

/**
 * ʵ��SingleLocationEntry�ӿ�
 * �ɸ���λ�ã��ǿɱ���
 * @author 123
 *
 */

public class SingleLocationEntryImpl implements SingleLocationEntry{
	private List<Location> location=new ArrayList<Location>();
	private static Logger myLogger=Logger.getLogger("SingleLocationEntryImplLog");
	//Abstraction function:
	//	AF(location)=�趨��λ��
	//Representation invariant:
	//	location�Ĵ�СΪ1
	//Safety from rep exposure:
	//	��Ա������private�ģ�Ϊ���ɱ����ͣ�ʹ�÷���ʽ�����������ڱ�ʾй¶
	
	private void checkRep() {
		assert location.size()==1;
		//��־��¼
		myLogger.setLevel(Level.INFO);
		myLogger.setUseParentHandlers(false);
		//д���ļ�
		FileHandler handler;
		try {
			handler = new FileHandler("src/LogFile/SingleLocationEntryImplLog.log");
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
	
	@Override
	public void setLocation(List<Location> location) {
		//����ʽ����
		if(location.size()!=1)
			return;
		this.location=new ArrayList<Location>();
		this.location.add(location.get(0));
		checkRep();
	}

	@Override
	public List<Location> getLocation() {
		List<Location> location=new ArrayList<Location>();
		location.addAll(this.location);
		return location;
	}
}
