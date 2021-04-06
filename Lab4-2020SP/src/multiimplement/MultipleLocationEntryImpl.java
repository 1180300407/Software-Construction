package multiimplement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import Location.Location;
import LogFile.MyFormatter;
import multidimension.MultipleLocationEntry;

/**
 * MultipleLocationEntry�ӿڵ�ʵ�֣��Զ��λ�ý��д����ɱ���
 * @author 123
 *
 */

public class MultipleLocationEntryImpl implements MultipleLocationEntry{
	private List<Location> locations=new ArrayList<Location>();
	private boolean setbefore=false;
	private static Logger myLogger=Logger.getLogger("MultipleLocationEntryImpl");
	//Abstraction function:
	//	AF(locations,sebefore)=�趨��λ�ã���setbeforeΪfalse��֤��δ���趨λ��
	//	��setbeforeΪtrue����λ���Ѿ��趨���������޸�
	//Representation invariant:
	//	��setbeforeΪtrueʱ�����ٸı���һλ��
	//	locations������Ӧ���ڵ���2�����ٰ��������յ�,ÿ��λ�û�����ͬ
	//Safety from rep exposure:
	//	��Ա������private�ģ�Ϊ���ɱ����ͣ�����ֵ�ͳ�Ա������ֵʱ��ת��Ϊ���ɱ����ͣ������ڱ�ʾй¶
	
	private void checkRep() {
		assert locations.size()>=2;
		Set<Location> locationset=new HashSet<Location>(locations);
		assert locationset.size()==locations.size();//������ͬ
		//��־��¼
		myLogger.setLevel(Level.INFO);
		myLogger.setUseParentHandlers(false);
		//д���ļ�
		FileHandler handler;
		try {
			handler = new FileHandler("src/LogFile/MultipleLocationEntryImplLog.log");
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
	public List<Location> getLocation(){
		return Collections.unmodifiableList(locations);
	}
	
	public boolean getsetbefore() {
		return setbefore;
	}
	
	@Override
	public void setLocations(List<Location> locs) {
		if(setbefore) {
			System.out.println("λ���Ѿ��趨���������ٽ����޸ģ�");
			return;
		}
		
		locations=new ArrayList<Location>();
		for(Location location:locs) {
			locations.add(location);
		}
		checkRep();
		setbefore=true;
	}
}
