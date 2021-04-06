package debug;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * In a Store, there are some kinds of items to sell. Each item has a price.
 * 
 * However, there are some special offers, and a special offer consists of one
 * or more different kinds of items with a sale price.
 * 
 * You are given the each item's price, a set of special offers, and the number
 * we need to buy for each item. The job is to output the lowest price you have
 * to pay for exactly certain items as given, where you could make optimal use
 * of the special offers.
 * 
 * Each special offer is represented in the form of an array, the last number
 * represents the price you need to pay for this special offer, other numbers
 * represents how many specific items you could get if you buy this offer.
 * 
 * You could use any of special offers as many times as you want.
 * 
 * Example 1:
 * 
 * Input: [2,5], [[3,0,5],[1,2,10]], [3,2] Output: 14
 * 
 * Explanation:
 * 
 * There are two kinds of items, A and B. Their prices are $2 and $5
 * respectively.
 * 
 * In special offer 1, you can pay $5 for 3A and 0B
 * 
 * In special offer 2, you can pay $10 for 1A and 2B.
 * 
 * You need to buy 3A and 2B, so you may pay $10 for 1A and 2B (special offer
 * #2), and $4 for 2A.
 * 
 * Example 2:
 * 
 * Input: [2,3,4], [[1,1,0,4],[2,2,1,9]], [1,2,1] Output: 11
 * 
 * Explanation:
 * 
 * The price of A is $2, and $3 for B, $4 for C.
 * 
 * You may pay $4 for 1A and 1B, and $9 for 2A ,2B and 1C.
 * 
 * You need to buy 1A ,2B and 1C, so you may pay $4 for 1A and 1B (special offer
 * #1), and $3 for 1B, $4 for 1C.
 * 
 * You cannot add more items, though only $9 for 2A ,2B and 1C.
 * 
 * 
 * Note:
 * 
 * 1. There are at most 6 kinds of items, 100 special offers.
 * 
 * 2. For each item, you need to buy at most 6 of them.
 * 
 * 3. You are not allowed to buy more items than you want, even if that would
 * lower the overall price.
 * 
 * ---------------------------------------------------------------------------------------
 * Please debug and fix potential bugs in the following code and make it execute correctly,
 * robust, and completely in accordance with above requirements.
 * 
 * DON'T change the initial logic of the code.
 * ---------------------------------------------------------------------------------------
 * 
 */
public class LowestPrice {

	/**
	 * 计算一组单价、需求数已知的商品在给定的优惠条件下花费的最低价格，单价数与数量应该匹配，且不应为空。即price的大小与needs的大小相等
	 * @param price 该组商品的单价(price.size()应该与needs.size()相等且不为空)
	 * @param special 对于其中部分商品的优惠
	 * @param needs 该组商品的需求数
	 * @return 该组商品花费的最低价格
	 */
	public int shoppingOffers(List<Integer> price, List<List<Integer>> special, List<Integer> needs) {
		return shopping(price, special, needs);
	}

	/**
	 * 计算一组单价、需求数已知的商品在给定的优惠条件下花费的最低价格，单价数与数量应该匹配，且不应为空。即price的大小与needs的大小相等
	 * @param price 该组商品的单价(price.size()应该与needs.size()相等且不为空)
	 * @param special 对于其中部分商品的优惠
	 * @param needs 该组商品的需求数
	 * @return 该组商品花费的最低价格
	 */
	public int shopping(List<Integer> price, List<List<Integer>> special, List<Integer> needs) {
		int j = 0, res = dot(needs, price);
		if(special==null||special.isEmpty())//没有优惠，增加判断
			return res;
		for (List<Integer> s : special) {//逐个offer进行试探，每次将一个special offer加入“购物车”，更新需求，递归求解
										 //选择一个offer并且全部试探以后，res变为这次试探后结果最小值，然后将下一个offer作为首先选取，进行重复试探
										 //再次进行验证，res维护最小特性就保证了是这些试探结果中的最小的组合
			List<Integer> clone = new ArrayList<>(needs);

			for (j = 0; j < needs.size(); j++) {
				int diff = clone.get(j) - s.get(j);
				if (diff < 0)//<=0改为<0，因为diff=0说明该offer恰将这个item全部购买，符合要求
					break;
				clone.set(j, diff);
			}
			if (j == needs.size()) {//s中offer总价格在s.get(needs.size())中
				res = Math.min(res, s.get(j) + shopping(price, special, clone));
			}
		}
		return res;
	}

	/**
	 * 通过一组商品的单价与数量计算出总价，单价数与数量应该匹配，即a的大小与b的大小相等，且不应为空
	 * @param a 每个商品需要的数量(a.size()应该与b.size()相等且不为空)
	 * @param b	每个商品需要的单价()
	 * @return 这一组商品的总价
	 */
	public int dot(List<Integer> a, List<Integer> b) {//计算不用offer时的总价
		int sum = 0;
		for (int i = 0; i < a.size(); i++) {
			sum += a.get(i) * b.get(i);
		}
		return sum;
	}

}
