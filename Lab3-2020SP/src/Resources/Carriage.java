package Resources;

/**
 * 表示“车厢”资源，不可变类
 * @author 123
 *
 */

public class Carriage {
	private final String id;//车厢编号
	private final String type;//车厢类型
	private final int maxnum;//车厢定员数
	private final String manufactureyear;//出厂年份
	
	//Abstraction function:
	//	AF(id,type,maxnum,manufactureyear)=一个编号为id的车厢，车厢类型为type，定员数为maxnum，出厂年费是manufactureyear
	//Representation invariant:
	//	true
	//Safety from rep exposure:
	//	成员变量全是private且final的，均为不可变类型，不存在表示泄露
	
	/**
	 * 构造函数
	 * @param id 指定车厢编号
	 * @param type 指定车厢类型，为其中一种
	 * @param maxnum 指定车厢定员数
	 * @param manufactureyear 指定出厂年份
	 */
	public Carriage(String id, String type, int maxnum, String manufactureyear) {
		this.id = id;
		this.type = type;
		this.maxnum = maxnum;
		this.manufactureyear = manufactureyear;
	}

	/**
	 * @return 车厢编号
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return 车厢类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return 车厢定员数
	 */
	public int getMaxnum() {
		return maxnum;
	}

	/**
	 * @return 车厢出厂年份
	 */
	public String getManufactureyear() {
		return manufactureyear;
	}

	//重写equals和hashCode,id为唯一标准
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Carriage other = (Carriage) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
