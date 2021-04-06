package debug;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Note that this class may use the other two class: Flight and Plane.
 * 
 * Debug and fix errors. DON'T change the initial logic of the code.
 *
 */
public class FlightClient {
	
	/**
	 * Given a list of flights and a list of planes, suppose each flight has not yet been
	 * allocated a plane to, this method tries to allocate a plane to each flight and ensures that
	 * there're no any time conflicts between all the allocations. 
	 * For example:
	 *  Flight 1 (2020-01-01 8:00-10:00) and Flight 2 (2020-01-01 9:50-10:40) are all allocated 
	 *  the same plane B0001, then there's conflict because from 9:50 to 10:00 the plane B0001
	 *  cannot serve for the two flights simultaneously.
	 *  
	 * @param planes a list of planes
	 * @param flights a list of flights each of which has no plane allocated
	 * @return false if there's no allocation solution that could avoid any conflicts
	 */
	
	public boolean planeAllocation(List<Plane> planes, List<Flight> flights) {
		boolean bFeasible = true;
		Random r = new Random();
		
		Collections.sort(flights,new FlightComparator());
		
		for (Flight f : flights) {
			boolean bAllocated = false;
			Set<Integer> randomSet=new HashSet<Integer>();
			
			while (!bAllocated) {
				if(randomSet.size()>=planes.size())//������һ����ֹѭ���������������ȡ��ɻ���������ζ��ȫ���ɻ�����̽��������ʧ�ܣ�
					break;			    			//�����ԭ����һ�����������ѭ��
				int randomNum=r.nextInt(planes.size());//ȡһ�������
				randomSet.add(randomNum);
				Plane p = planes.get(randomNum);//���ѡ��һ���ɻ�������
				Calendar fStart = f.getDepartTime();
				Calendar fEnd = f.getArrivalTime();
				boolean bConflict = false;
				
				for (Flight t : flights) {
					Plane q = t.getPlane();
					if(q==null)//ԭ����δ����δ����ɻ��ĺ��෵�ؿ���Ϊnull����������ж�
						continue;
					if (! q.equals(p))//���÷ɻ��Ƿ��Ѿ��������
						continue;//δ����
					
					Calendar tStart = t.getDepartTime();//�ɻ��ѷ�������鿴�Ƿ��ͻ
					Calendar tEnd = t.getArrivalTime();
					//������Ҫ�Ƚ�ռ��ͬһ�ɻ��ĺ����Ƿ��г�ͻ��ʱ��������before,after�Ƚ϶�����>,<
					if ((fStart.after(tStart) && fStart.before(tEnd)) || (tStart.after(fStart) && tStart.before(fEnd))) {
						bConflict = true;
						break;
					}
				}
				
				if (!bConflict) {//ȫ���ȽϹ���û�г�ͻ������Է���
					f.setPlane(p);
					bAllocated=true;//�������б�ʶ����ֹѭ��,ԭ����δ��ʶ�����������ѭ��
					break;
				}
			}
			
			if(!bAllocated) {//ֻҪ��һ������δ����ɹ����������ٱ���������ʧ��
				bFeasible=false;
				break;
			}
		}
		return bFeasible;
	}

}

	class FlightComparator implements Comparator<Flight>{//ԭ������ͼΪ��������Ȼ�����������з��䣬��û�����Flight��ıȽ���
														 //�������ʵ��һ�����պ������ڵ�����Ƚϣ���������ͬʱ�������ʱ��Ƚ�
		@Override
		public int compare(Flight f1,Flight f2) {
			if(f1.getFlightDate().before(f2.getFlightDate()))
				return -1;
			else if(f1.getFlightDate().after(f2.getFlightDate()))
				return 1;
			else {
				if(f1.getDepartTime().before(f2.getDepartTime()))
					return -1;
				else if(f1.getDepartTime().after(f2.getDepartTime()))
					return 1;
				else
					return 0;
			}
		}
	}
