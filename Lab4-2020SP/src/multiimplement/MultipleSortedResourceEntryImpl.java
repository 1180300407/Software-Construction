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

import LogFile.MyFormatter;
import multidimension.MultipleSortedResourceEntry;

/**
 * ʵ��MultipleSortedResourceEntry<R>�ӿڣ�ʵ�ֶԶ����������Դ�Ĵ���
 * �������ȫ�����ԣ��ǿɱ���
 * ����ʵ���ò�������setbefore�Ĳ�ֵͬ��ά��resources�Ĳ��ɱ�
 * @author 123
 *
 * @param <R>
 */

public class MultipleSortedResourceEntryImpl<R> implements MultipleSortedResourceEntry<R>{
	private List<R> resources=new ArrayList<>();
	private boolean setbefore=false;
	private static Logger myLogger=Logger.getLogger("MultipleSortedResourceLog");
	//	AF(resources,sebefore)=������ԴΪresources�ļƻ����setbeforeΪfalse��֤��δ��������Դ
	//	��setbeforeΪtrue������Դ�Ѿ����䣬�����ٸı�
	//Representation invariant:
	//	��setbeforeΪtrueʱ�����ٸı���Դ
	//	�������Դ�в������ظ�Ԫ��
	//Safety from rep exposure:
	//	��Ա������private�ģ�����ֵ�ͳ�Ա������ֵʱ��ת��Ϊ���ɱ�����
	private void checkRep() {
		Set<R> resourceSet=new HashSet<>(resources);
		assert resourceSet.size()==resources.size();
		//��־��¼
		myLogger.setLevel(Level.INFO);
		myLogger.setUseParentHandlers(false);
		//д���ļ�
		FileHandler handler;
		try {
			handler = new FileHandler("src/LogFile/MultipleSortedResourceLog.log");
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
	public List<R> getResource(){
		return Collections.unmodifiableList(resources);
	}
	
	@Override
	public void allocateResource(List<R> rs) {
		if(!setbefore) {
			resources=new ArrayList<>();//����ʽ����
			for(R resource:rs) {
				resources.add(resource);
			}
			resources=Collections.unmodifiableList(resources);
			checkRep();
			setbefore=true;
		}
	}
}
