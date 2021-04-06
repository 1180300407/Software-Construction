package debug;

import java.util.TreeMap;

/**

 * 

 * 实现一个EventManager类来管理个人日程，通过该类的一个方法

 * 

 * book(int day, int start, int end)

 * 

 * 来添加新事件

 * 

 * 待添加的新事件发生在day，这是一个整数，表示一年里的第day天

 * 

 * start表示事件的起始时间，为该day天的第start小时

 * 

 * end表示该事件的结束时间，为该day天的第end小时。

 * 

 * 例如：

 * book(1,8,10)表示添加一个在1月1日（第1天）的8点开始，10点结束的事件。

 * book(1, 0, 1)表示在第1天的0:00-1:00的事件  

 * book(1, 22,24)表示在第1天的22:00-24:00的事件

 * 

 * 事件的长度单位是小时，不需要考虑分钟。

 * 

 * 约束条件：1<=day<=365（无需考虑闰年之类的问题），0<=start<end<=24。



 * “k-重叠”是指：有k个事件的时间范围在某个时间段内存在交集，即这k个事件在某个小时内都已经启动且尚未结束。

 * book(…)方法的返回值是：当本次调用结束后的最大k值。

 * 

 * 例如：

 * 

 * EventManager.book(1, 10, 20); 	// returns 1

 * EventManager.book(1, 1, 7); 		// returns 1

 * EventManager.book(1, 10, 22); 	// returns 2

 * EventManager.book(1, 5, 15); 	// returns 3

 * EventManager.book(1, 5, 12); 	// returns 4

 * EventManager.book(1, 7, 10); 	// returns 4

 * 

 * 请对以下代码进行调试和修改，使其完整、正确、健壮的完成上述需求，但不要改变该代码的内在逻辑。

 *

 * 

 */
public class EventManager {
	private final static int weight=29;//day的权重,大于24，用start+day*weight(end+day*weight)来作为map的key
									   //由于权重大于24，这样日期靠前的key一定更小，因此一定存储在map的前面
									   //这样保证了交集只会出现在同一天的事件上，并且遍历时是将同一天的全部遍历完以后才会遍历下一天
									   //否则可能会出现前一天与后一天的key值相同，甚至大小关系调换
	
	static TreeMap<Integer, Integer> temp = new TreeMap<>();//key:start+day*weight(end+day*weight)
															//value:start+day*weight出现的次数(end+day*weight出现次数的相反数)
	//算法思想：我们保证通过为day加权保证可以定位到每一天，从中挑选一年中的某一天中相交区间的最大值
	//			然后通过将区间起点的value设置为正值，终点value设置为其相反数，这样按照原程序思想在累加过程中寻找最大值
	//			最大正值就是在累加区间内所有每个开始部分时，只要遇到结束部分，就是负值，不是最大，也恰意味交集减少
	/**
	 * 
	 * @param start start time of the event to be added, should be in [0, 24)
	 * @param end   end time of the event to be added, should be in (0, 24]
	 * @return 		the max number of concurrent events in the same hour
	 */
	public static int book(int day, int start, int end) {
		//区间起点
		if(!temp.containsKey(start+day*weight)) {//未出现过，设置出现次数为1
			temp.put(start+day*weight, 1);
		}
		else {//出现过，进行累加
			temp.put(start+day*weight, temp.get(start+day*weight)+1);
		}
		
		//区间终点
		if (!temp.containsKey(end+day*weight)) {//未出现过，设置为相反数-1
			temp.put(end+day*weight, -1);
		}
		else {//出现过，累加-1
			temp.put(end+day*weight, temp.get(end+day*weight)-1);
		}

		int active = 0, ans = 0;
		for (int d : temp.values()) {//寻找最大value即最大交集数
			active += d;
			if (active >= ans)
				ans = active;
		}
		
		return ans;
	}
	
	public static void reSetEvent() {//对保存的event进行清空
		temp=new TreeMap<Integer, Integer>();
	}
}
