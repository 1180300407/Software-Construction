package MainApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import API.*;
import Board.CourseBoard;
import Location.Location;
import Resources.Teacher;
import Schedule.CourseSchedule;
import Timeslot.Timeslot;
import common.PlanningEntry;
import compositeinterface.CourseEntry;

public class CourseCalendarApp {
	public void menu() {//����Ŀ¼
		System.out.println("-------�γ̹���ϵͳ-------");
		System.out.println("**** 1.���ӹ���Ľ�ʦ  ****");
		System.out.println("**** 2.ɾ������Ľ�ʦ  ****");
		System.out.println("**** 3.���ӹ���Ľ���  ****");
		System.out.println("**** 4.ɾ������Ľ���  ****");
		System.out.println("**** 5.����һ���¿γ�  ****");
		System.out.println("**** 6.Ϊ�γ̷����ʦ  ****");
		System.out.println("**** 7.���Ŀγ̽���      ****");
		System.out.println("**** 8.��ʦָ���γ��Ͽ�****");
		System.out.println("**** 9.��ʦָ���γ��¿�****");
		System.out.println("**** 10.�鿴�γ�״̬    ****");
		System.out.println("**** 11.��ǰȡ���γ�    ****");
		System.out.println("**** 12.չʾ���ҿγ̱� ****");
		System.out.println("**** 13.������ռ�ó�ͻ***");
		System.out.println("**** 14.����ʦ�����ͻ***");
		System.out.println("**** 15.չʾ��ʦ�γ̰���***");
		System.out.println("**** 16.�˳�ϵͳ           ****");
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		CourseCalendarApp ccApp=new CourseCalendarApp();
		ccApp.menu();
		String input=bf.readLine();
		CourseSchedule cs=new CourseSchedule();
		while(!input.equals("16")) {
			switch (input) {
			case "1":{//���ӽ�ʦ
				System.out.println("�����������ʦ�����֤�š��������Ա�(M/F)��ְ��");
				input=bf.readLine();
				String[] information=input.split(" ");
				while(information.length!=4||(!information[2].equals("F")&&!information[2].equals("M"))) {
					if(information.length!=4)
						System.out.println("���벻����!����������!");//�쳣����
					else
						System.out.println("�Ա���������!����������!");//�쳣����
					input=bf.readLine();
					information=input.split(" ");
				}
				if(information[2].equals("M")) {//������ʦ
					Teacher teacher=new Teacher(information[0],information[1] , true, information[3]);
					cs.addTeacher(teacher);
				}
				else {
					Teacher teacher=new Teacher(information[0],information[1] , false, information[3]);
					cs.addTeacher(teacher);
				}		
				break;
			}
			case "2":{//ɾ����ʦ
				System.out.println("�������ɾ����ʦ�����֤��:");
				input=bf.readLine();
				cs.deleteTeacher(input);
				break;
			}
			case "3":{//���ӽ���
				System.out.println("������������ҵľ��ȡ�γ�ȡ�����");
				input=bf.readLine();
				String[] information=input.split(" ");
				while(information.length!=3) {
					System.out.println("���벻����!����������!");//�쳣����
					input=bf.readLine();
					information=input.split(" ");
					
				}
				Location location=new Location(information[0], information[1], information[2], false);
				cs.addLocation(location);//���λ��
				break;
			}
			case "4":{//ɾ������
				System.out.println("�������ɾ�����ҵ�����:");
				input=bf.readLine();
				cs.deleteLocation(input);
				break;
			}
			case "5":{//�����γ�
				System.out.println("������Ҫ�����Ŀγ�����:");
				String coursename=bf.readLine();
				System.out.println("������γ�Ҫռ�õĽ�������:");
				String location=bf.readLine();
				System.out.println("������γ�Ԥ���Ͽ�ʱ��(yyyy-MM-dd HH:mm):");
				String start=bf.readLine();
				System.out.println("������γ�Ԥ���¿�ʱ��(yyyy-MM-dd HH:mm):");
				String end=bf.readLine();
				Timeslot timeslot=new Timeslot(start, end);
				cs.createCourse(coursename, location, timeslot);
				break;
			}
			case "6":{//Ϊ�γ̷����ʦ
				System.out.println("������Ҫ����γ�����:");
				String coursename=bf.readLine();
				System.out.println("��������������Ҫ�����ʦ�����֤��(�ÿո�ָ�):");
				input=bf.readLine();
				String[] teacher=input.split(" ");
				List<String> teacherIDs=new ArrayList<String>();
				for(int i=0;i<teacher.length;i++) {
					teacherIDs.add(teacher[i]);
				}
				cs.allocateTeacher(coursename, teacherIDs);
				break;
			}
			case "7":{//�γ̸���λ��
				System.out.println("������Ҫ���Ľ��ҵĿγ�����:");
				String coursename=bf.readLine();
				System.out.println("������Ҫ���ĵ���λ��:");
				String location=bf.readLine();
				cs.changeLocation(coursename, location);
				break;
			}
			case "8":{//�Ͽ�
				System.out.println("������Ҫ�ϿεĿγ�����:");
				String coursename=bf.readLine();
				cs.startCourse(coursename);
				break;
			}
			case "9":{//�¿�
				System.out.println("������Ҫ�¿εĿγ�����:");
				String coursename=bf.readLine();
				cs.endCourse(coursename);
				break;
			}
			case "10":{//�鿴�γ�״̬
				System.out.println("������Ҫ�鿴״̬�Ŀγ�����:");
				String coursename=bf.readLine();
				System.out.println("Ŀǰ�γ�״̬Ϊ:"+cs.getCourseState(coursename));
				break;
			}
			case "11":{//ȡ���γ�
				System.out.println("��������ǰȡ���Ŀγ�����:");
				String coursename=bf.readLine();
				cs.cancelCourse(coursename);
				break;
			}
			case "12":{//Board���ӻ�
				System.out.println("������Ҫչʾ�γ̵Ľ�������:");
				String locationname=bf.readLine();
				Location location=cs.getLocationbyName(locationname);
				Calendar calendar=Calendar.getInstance();
				CourseBoard cb=new CourseBoard(location, cs.getCourses(),calendar);
				try {
					cb.visualize();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;
			}
			case "13":{//���λ�ó�ͻ
				PlanningEntryAPIs peAPI=new PlanningEntryAPIsFirstImpl();
				//PlanningEntryAPIs peAPI=new PlanningEntryAPIsSecondImpl();//����ģʽ��client������ѡ��һ��ʵ�ַ�ʽ
				boolean flag=peAPI.checkLocationConflict(cs.getCourses());
				if(flag) {
					System.out.println("��ǰ�ƻ������λ�ó�ͻ!");
				}
				else {
					System.out.println("��ǰ�ƻ������λ�ó�ͻ!");
				}
				break;
			}
			case "14":{//����ʦ�����ͻ
				PlanningEntryAPIs peAPI=new PlanningEntryAPIsFirstImpl();
				//PlanningEntryAPIs peAPI=new PlanningEntryAPIsSecondImpl();//����ģʽ��client������ѡ��һ��ʵ�ַ�ʽ
				boolean flag=peAPI.checkResourceExclusiveConflict(cs.getCourses());
				if(flag) {
					System.out.println("��ǰ�γ̼����д��ڽ�ʦ�����ͻ!");
				}
				else {
					System.out.println("��ǰ�γ̼����в����ڽ�ʦ�����ͻ!");
				}
				break;
			}
			case "15":{//�鿴��ʦ�γ̰���
				System.out.println("������Ҫ�鿴�γ̰��ŵĽ�ʦ���֤��:");
				String teacherId=bf.readLine();
				PlanningEntryAPIs peAPI=new PlanningEntryAPIsFirstImpl();
				List<CourseEntry<Teacher>> ces=cs.getCoursesofassignTeacher(teacherId);
				if(ces==null) {
					System.out.println("�ý�ʦδ����γ�!");
					break;
				}
				else {
					System.out.println("�ý�ʦ����Ŀγ���:");
					for(CourseEntry<Teacher> ce:ces) {
						System.out.println(ce.getName()+" Ŀǰ�γ�״̬Ϊ:"+ce.getStateName());
					}
					System.out.println("�Ƿ���Ҫ�鿴ָ���γ̵�ǰ��γ�?(Y/N)");
					input=bf.readLine();
					if(input.equals("Y")) {
						System.out.println("������Ҫ�鿴��ָ���γ�:");
						input=bf.readLine();
						CourseEntry<Teacher> courseEntry=cs.getCoursebyName(input);
						while(courseEntry==null) {
							System.out.println("��ѯʧ��!�������������ѡȡ!");
							input=bf.readLine();
							courseEntry=cs.getCoursebyName(input);
						}
						Teacher teacher=cs.getTeacherbyID(teacherId);
						try {
							PlanningEntry<Teacher> cEntry=peAPI.findPreEntryPerResource(teacher, courseEntry, cs.getCourses());
							if(cEntry==null)
								System.out.println("�ÿγ���ǰ��γ�!");
							else
								System.out.println("��ǰ��γ�Ϊ:"+cEntry.getName());
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
				break;
			}
			default:{
				System.out.println("�������,������ѡ����!");
				break;
			}
			
			}
			
			if(!input.equals("16")) {//ÿ���һ�ι���,���´�ӡ�˵����û�ѡȡ
				ccApp.menu();
				input=bf.readLine();
			}
		}
	}
}
	
