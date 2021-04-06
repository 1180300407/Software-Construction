package multiimplement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import LogFile.MyFormatter;
import multidimension.SingleSortedResourceEntry;

/**
 * ʵ��SingleSortedResourceEntry<R>�ӿڣ��Ե�һ��������Դ�Ĵ����ɱ���
 * @author 123
 *
 * @param <R> ��Դ����
 */

public class SingleSortedResourceEntryImpl<R> implements SingleSortedResourceEntry<R>{
	private List<R> resource=new ArrayList<>();
	private boolean setbefore=false;
	private static Logger myLogger=Logger.getLogger("SingleSortedResourceEntryImplLog");
	//Abstraction function:
	//	AF(resource,sebefore)=�������Դ����setbeforeΪfalse��֤��δ�������
	//	��setbeforeΪtrue������Դ�Ѿ����䣬�������޸�
	//Representation invariant:
	//	��setbeforeΪtrueʱ�����ٽ��з���
	//	resource����Ϊ1
	//Safety from rep exposure:
	//	��Ա������private�ģ�����ֵ�ͳ�Ա������ֵʱ��ת��Ϊ���ɱ����ͣ������ڱ�ʾй¶
	private void checkRep() {
		assert resource.size()==1;
		//��־��¼
		myLogger.setLevel(Level.INFO);
		myLogger.setUseParentHandlers(false);
		//д���ļ�
		FileHandler handler;
		try {
			handler = new FileHandler("src/LogFile/SingleSortedResourceEntryImplLog.log");
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
	public List<R> getResource() {
		return resource;
	}
	
	@Override
	public void allocateResource(List<R> resource) {
		if(!setbefore) {
			if(resource.size()==1) {
				this.resource=new ArrayList<>();
				this.resource.add(resource.get(0));
				setbefore=true;
				checkRep();
			}
		}
	}
}
