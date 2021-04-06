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
				if(randomSet.size()>=planes.size())//设置另一个终止循环的条件，随机数取遍飞机数量，意味着全部飞机都试探过，分配失败；
					break;			    			//如果像原程序一样，则会无限循环
				int randomNum=r.nextInt(planes.size());//取一个随机数
				randomSet.add(randomNum);
				Plane p = planes.get(randomNum);//随机选择一个飞机来分配
				Calendar fStart = f.getDepartTime();
				Calendar fEnd = f.getArrivalTime();
				boolean bConflict = false;
				
				for (Flight t : flights) {
					Plane q = t.getPlane();
					if(q==null)//原程序未考虑未分配飞机的航班返回可能为null，这里添加判断
						continue;
					if (! q.equals(p))//检查该飞机是否已经被分配过
						continue;//未分配
					
					Calendar tStart = t.getDepartTime();//飞机已分配过，查看是否冲突
					Calendar tEnd = t.getArrivalTime();
					//这里是要比较占用同一飞机的航班是否有冲突，时间早晚用before,after比较而不是>,<
					if ((fStart.after(tStart) && fStart.before(tEnd)) || (tStart.after(fStart) && tStart.before(fEnd))) {
						bConflict = true;
						break;
					}
				}
				
				if (!bConflict) {//全部比较过，没有冲突，则可以分配
					f.setPlane(p);
					bAllocated=true;//分配后进行标识，终止循环,原程序未标识，会造成无限循环
					break;
				}
			}
			
			if(!bAllocated) {//只要有一个航班未分配成功，则无需再遍历，分配失败
				bFeasible=false;
				break;
			}
		}
		return bFeasible;
	}

}

	class FlightComparator implements Comparator<Flight>{//原程序试图为航班排序，然后遍历航班进行分配，但没有针对Flight类的比较器
														 //因此这里实现一个按照航班日期的升序比较，当日期相同时按照起飞时间比较
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
