package Location;

/**
 * "位置"信息，不可变类
 * @author 123
 *
 */

public class Location {
	private final String longitude;
	private final String latitude;
	private final String name;
	private final boolean isshareable;
	
	//Abstraction function:
	//	AF(longitude,latitude,name,isshareable)=一个位置
	//	该位置的经纬度是longitude,latitude
	//	位置的名称是name，如果isshareable为true说明该位置可以共享，反之不可共享
	//Representation invariant:
	//	true
	//Safety from rep exposure:
	//	成员变量全是private且final的，均为不可变类型，不存在表示泄露
	
	/**
	 * 构造函数
	 * @param longitude 指定位置经度
	 * @param latitude 指定位置纬度
	 * @param name 指定位置名称
	 * @param isshareable 指定位置是否可共享
	 */
	public Location(String longitude, String latitude, String name, boolean isshareable) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.name = name;
		this.isshareable = isshareable;
	}

	//重写hashCode和equals,name作为唯一标识
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Location other = (Location) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}



	/**
	 * @return 该位置的经度
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @return 该位置的纬度
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @return 该位置的名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return true表示该位置可共享，反之不可共享
	 */
	public boolean isshareable() {
		return isshareable;
	}
	
}
