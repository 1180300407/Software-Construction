package multiimplement;

import multidimension.UnBlockableEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import LogFile.MyFormatter;
import Timeslot.Timeslot;;

/**
 * UnBlockableEntry�ӿڵ�ʵ�֣��ɱ���
 * @author 123
 *
 */
public class UnBlockableEntryImpl implements UnBlockableEntry{
	private List<Timeslot> timeslot=new ArrayList<Timeslot>();
	private boolean setbefore=false;
	private static Logger myLogger=Logger.getLogger("UnBlockableEntryImplLog");
	//	AF(timeslot,sebefore)=�趨ʱ��Ϊtimeslot�ļƻ����setbeforeΪfalse��֤��δ���趨ʱ��
	//	��setbeforeΪtrue����ʱ���Ѿ��趨���������޸�
	//Representation invariant:
	//	��setbeforeΪtrueʱ�����ٸı�ʱ��,timeslot����Ϊ1
	//Safety from rep exposure:
	//	��Ա������private�ģ�Ϊ���ɱ����ͣ������ڱ�ʾй¶
	private void checkRep() {
		assert timeslot.size()==1;
		//��־��¼
		myLogger.setLevel(Level.INFO);
		myLogger.setUseParentHandlers(false);
		//д���ļ�
		FileHandler handler;
		try {
			handler = new FileHandler("src/LogFile/UnBlockableEntryImplLog.log");
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
	public List<Timeslot> getTime() {
		return Collections.unmodifiableList(timeslot);
	}
	
	@Override
	public void setTime(List<Timeslot> timeslot) {
		if(setbefore)
			return;
		if(timeslot.size()!=1)
			return;
		this.timeslot.add(timeslot.get(0));
		checkRep();
		setbefore=true;
	}
}