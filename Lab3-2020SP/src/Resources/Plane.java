package Resources;

/**
 * 表示"飞机"资源，不可变类
 * @author 123
 *
 */

public class Plane {
	private final String id;//飞机编号
	private final String type;//机型号
	private final int seats;//座位数
	private final double planeage;//机龄
	
	//Abstraction function:
	//	AF(id,type,seats,planeage)=一架编号为id的飞机，机型号是type，有seats个座位，机龄为planeage
	//Representation invariant:
	//	true
	//Safety from rep exposure:
	//	成员变量全是private且final的，均为不可变类型，不存在表示泄露
	
	/**
	 * 构造函数
	 * @param id 指定飞机编号
	 * @param type 指定飞机机型号
	 * @param seats 指定飞机座位数
	 * @param planeage 指定飞机机龄
	 */
	public Plane(String id, String type, int seats, double planeage) {
		this.id = id;
		this.type = type;
		this.seats = seats;
		this.planeage = planeage;
	}

	/**
	 * @return 该飞机的飞机编号
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return 该飞机的机型号
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return 该飞机的座位数
	 */
	public int getSeats() {
		return seats;
	}

	/**
	 * @return 该飞机的机龄
	 */
	public double getPlaneage() {
		return planeage;
	}

	
	//重写hashCode和equals，只根据ID进行区分
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
		Plane other = (Plane) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
