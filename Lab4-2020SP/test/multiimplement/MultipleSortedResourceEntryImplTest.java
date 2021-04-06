package multiimplement;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Resources.Carriage;

public class MultipleSortedResourceEntryImplTest {
	
	//���Բ���:����һ�Σ����ö��
	//Ϊ�˽⿪allocateResource������get����֮������,�����е�resources��������Ϊpublic���в���
	/*
	@Test
	public void allocateReourcetest() {
		Carriage carriage1=new Carriage("1", "t", 10, "manufactureyear");
		Carriage carriage2=new Carriage("5", "t", 10, "manufactureyear");
		Carriage carriage3=new Carriage("9", "t", 10, "manufactureyear");
		List<Carriage> carriages=new ArrayList<Carriage>();
		carriages.add(carriage3);
		carriages.add(carriage2);
		carriages.add(carriage1);
		MultipleSortedResourceEntryImpl<Carriage> msr=new MultipleSortedResourceEntryImpl<Carriage>();
		msr.allocateResource(carriages);
		assertEquals(carriages, msr.resources);
		carriages.remove(carriage2);
		msr.allocateResource(carriages);
		assertNotEquals(carriages, msr.resources);
	}*/
	
	
	@Test
	public void getResource() {
		Carriage carriage1=new Carriage("1", "t", 10, "manufactureyear");
		Carriage carriage2=new Carriage("5", "t", 10, "manufactureyear");
		Carriage carriage3=new Carriage("9", "t", 10, "manufactureyear");
		List<Carriage> carriages=new ArrayList<Carriage>();
		carriages.add(carriage3);
		carriages.add(carriage2);
		carriages.add(carriage1);
		MultipleSortedResourceEntryImpl<Carriage> msr=new MultipleSortedResourceEntryImpl<Carriage>();
		msr.allocateResource(carriages);
		assertEquals(carriages, msr.getResource());
	}
}
