package debug;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class LowestPriceTest {
	private List<Integer> price;
	private List<List<Integer>> offers;
	private List<Integer> needs;
	private LowestPrice lp;
	//测试策略:1.单个商品；6个商品；n个商品(1<n<6)
	//		  2.没有special offer；单个special offer;n个special offer(1<n<=100)
	//		  3.special offer中商品数超过需求的数目；special offer中商品数不超过需求的数目
	//		  4.special offer中只选择一次；special offer选择多次；
	@Before
	public void prepare() {
		price=new ArrayList<Integer>();
		offers=new ArrayList<>();
		needs=new ArrayList<Integer>();
		lp=new LowestPrice();
	}
	
	//单个商品，一个special offer，special offer中商品数不超过需求的数目，special offer中只选择一次
	@Test
	public void SingleItemWithSingleOfferLessthanWanttest() {
		price.add(5);
		needs.add(4);
		List<Integer> offer1=new ArrayList<Integer>();
		offer1.add(3);
		offer1.add(13);
		offers.add(offer1);
		assertEquals(18, lp.shoppingOffers(price, offers, needs));
	}

	//单个商品，多个special offer，special offer中商品数不超过需求的数目，special offer中只选择一次
	@Test
	public void SingleItemWithMultiOfferLessthanWanttest() {
		price.add(5);
		needs.add(6);
		List<Integer> offer1=new ArrayList<Integer>();
		offer1.add(4);
		offer1.add(11);
		List<Integer> offer2=new ArrayList<Integer>();
		offer2.add(2);
		offer2.add(7);
		List<Integer> offer3=new ArrayList<Integer>();
		offer3.add(2);
		offer3.add(6);
		offers.add(offer1);
		offers.add(offer2);
		offers.add(offer3);
		assertEquals(17, lp.shoppingOffers(price, offers, needs));
	}
	
	//没有special offer
	@Test
	public void WithoutOffertest() {
		price.add(5);
		price.add(3);
		needs.add(6);
		needs.add(1);
		assertEquals(33, lp.shoppingOffers(price, offers, needs));
	}
	
	//存在special offer中商品数超过需求的数目
	@Test
	public void OfferProvideMoreItemstest() {
		price.add(5);
		needs.add(5);
		List<Integer> offer1=new ArrayList<Integer>();
		offer1.add(3);
		offer1.add(13);
		List<Integer> offer3=new ArrayList<Integer>();
		offer3.add(6);
		offer3.add(21);
		offers.add(offer1);
		offers.add(offer3);
		assertEquals(23, lp.shoppingOffers(price, offers, needs));
	}
	
	//单个商品，多个special offer，special offer中商品数不超过需求的数目，special offer要选择多次
	@Test
	public void SingleItemWithMultiOfferMorethanOnceLessthanWanttest() {
		price.add(5);
		needs.add(6);
		List<Integer> offer1=new ArrayList<Integer>();
		offer1.add(3);
		offer1.add(13);
		List<Integer> offer2=new ArrayList<Integer>();
		offer2.add(2);
		offer2.add(6);
		offers.add(offer1);
		offers.add(offer2);
		assertEquals(18, lp.shoppingOffers(price, offers, needs));//3个offer2
	}
	
	//6个商品，一个special offer，special offer中商品数不超过需求的数目，special offer中只选择一次
	@Test
	public void SixItemWithSingleOfferLessthanWanttest() {
		price.add(5);
		price.add(7);
		price.add(9);
		price.add(3);
		price.add(4);
		price.add(6);
		needs.add(1);
		needs.add(2);
		needs.add(1);
		needs.add(3);
		needs.add(2);
		needs.add(4);
		List<Integer> offer1=new ArrayList<Integer>();
		offer1.add(1);
		offer1.add(1);
		offer1.add(1);
		offer1.add(1);
		offer1.add(2);
		offer1.add(2);
		offer1.add(26);
		offers.add(offer1);
		assertEquals(26+7+6+12, lp.shoppingOffers(price, offers, needs));
	}
	
	//n个商品(此处n=3)，一个special offer，special offer中商品数不超过需求的数目，special offer中只选择一次
	@Test
	public void MultiItemWithSingleOfferLessthanWanttest() {
		price.add(5);
		price.add(7);
		price.add(9);
		needs.add(4);
		needs.add(2);
		needs.add(1);
		List<Integer> offer1=new ArrayList<Integer>();
		offer1.add(2);
		offer1.add(2);
		offer1.add(1);
		offer1.add(26);
		offers.add(offer1);
		assertEquals(26+10, lp.shoppingOffers(price, offers, needs));
	}
	
	//6个商品，多个special offer，special offer中商品数不超过需求的数目，special offer中只选择一次
	@Test
	public void SixItemWithMultiOfferLessthanWanttest() {
		price.add(5);
		price.add(7);
		price.add(9);
		price.add(3);
		price.add(4);
		price.add(6);
		needs.add(1);
		needs.add(2);
		needs.add(1);
		needs.add(5);
		needs.add(2);
		needs.add(4);
		List<Integer> offer1=new ArrayList<Integer>();
		offer1.add(1);
		offer1.add(1);
		offer1.add(1);
		offer1.add(1);
		offer1.add(2);
		offer1.add(2);
		offer1.add(26);
		offers.add(offer1);
		List<Integer> offer2=new ArrayList<Integer>();
		offer2.add(0);
		offer2.add(0);
		offer2.add(0);
		offer2.add(0);
		offer2.add(0);
		offer2.add(2);
		offer2.add(10);
		offers.add(offer2);
		List<Integer> offer3=new ArrayList<Integer>();
		offer3.add(0);
		offer3.add(0);
		offer3.add(0);
		offer3.add(3);
		offer3.add(0);
		offer3.add(0);
		offer3.add(6);
		offers.add(offer3);
		assertEquals(26+10+6+7+3, lp.shoppingOffers(price, offers, needs));
	}
	
	//n个商品(此处n=3)，多个special offer，special offer中商品数不超过需求的数目，special offer中只选择一次
	@Test
	public void MultiItemWithMultiOfferLessthanWanttest() {
		price.add(5);
		price.add(7);
		price.add(9);
		needs.add(5);
		needs.add(2);
		needs.add(4);
		List<Integer> offer1=new ArrayList<Integer>();
		offer1.add(1);
		offer1.add(1);
		offer1.add(1);
		offer1.add(16);
		offers.add(offer1);
		List<Integer> offer2=new ArrayList<Integer>();
		offer2.add(3);
		offer2.add(0);
		offer2.add(0);
		offer2.add(10);
		offers.add(offer2);
		List<Integer> offer3=new ArrayList<Integer>();
		offer3.add(0);
		offer3.add(1);
		offer3.add(3);
		offer3.add(26);
		offers.add(offer3);
		assertEquals(26+10+16+5, lp.shoppingOffers(price, offers, needs));
	}
	
	//6个商品，多个special offer，special offer中商品数不超过需求的数目，special offer中选择多次
	@Test
	public void SixItemWithMultiOfferLessthanWantMoreThanOncetest() {
		price.add(5);
		price.add(7);
		price.add(9);
		price.add(3);
		price.add(4);
		price.add(6);
		needs.add(3);
		needs.add(2);
		needs.add(2);
		needs.add(3);
		needs.add(2);
		needs.add(4);
		List<Integer> offer1=new ArrayList<Integer>();
		offer1.add(1);
		offer1.add(1);
		offer1.add(1);
		offer1.add(1);
		offer1.add(1);
		offer1.add(0);
		offer1.add(20);
		offers.add(offer1);
		List<Integer> offer2=new ArrayList<Integer>();
		offer2.add(0);
		offer2.add(0);
		offer2.add(0);
		offer2.add(0);
		offer2.add(0);
		offer2.add(2);
		offer2.add(8);
		offers.add(offer2);
		assertEquals(20*2+8*2+5+3, lp.shoppingOffers(price, offers, needs));
	}
	
	//n个商品(n=3)，多个special offer，special offer中商品数不超过需求的数目，special offer中选择多次
	@Test
	public void MultiItemWithMultiOfferLessthanWantMoreThanOncetest() {
		price.add(5);
		price.add(7);
		price.add(9);
		needs.add(3);
		needs.add(2);
		needs.add(4);
		List<Integer> offer1=new ArrayList<Integer>();
		offer1.add(1);
		offer1.add(1);
		offer1.add(0);
		offer1.add(7);
		offers.add(offer1);
		List<Integer> offer2=new ArrayList<Integer>();
		offer2.add(0);
		offer2.add(0);
		offer2.add(2);
		offer2.add(10);
		offers.add(offer2);
		assertEquals(10*2+7*2+5, lp.shoppingOffers(price, offers, needs));
	}
}
