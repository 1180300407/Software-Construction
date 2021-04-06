package Location;

/**
 * "λ��"��Ϣ�����ɱ���
 * @author 123
 *
 */

public class Location {
	private final String longitude;
	private final String latitude;
	private final String name;
	private final boolean isshareable;
	
	//Abstraction function:
	//	AF(longitude,latitude,name,isshareable)=һ��λ��
	//	��λ�õľ�γ����longitude,latitude
	//	λ�õ�������name�����isshareableΪtrue˵����λ�ÿ��Թ�����֮���ɹ���
	//Representation invariant:
	//	true
	//Safety from rep exposure:
	//	��Ա����ȫ��private��final�ģ���Ϊ���ɱ����ͣ������ڱ�ʾй¶
	
	/**
	 * ���캯��
	 * @param longitude ָ��λ�þ���
	 * @param latitude ָ��λ��γ��
	 * @param name ָ��λ������
	 * @param isshareable ָ��λ���Ƿ�ɹ���
	 */
	public Location(String longitude, String latitude, String name, boolean isshareable) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.name = name;
		this.isshareable = isshareable;
	}

	//��дhashCode��equals,name��ΪΨһ��ʶ
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
	 * @return ��λ�õľ���
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @return ��λ�õ�γ��
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @return ��λ�õ�����
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return true��ʾ��λ�ÿɹ�����֮���ɹ���
	 */
	public boolean isshareable() {
		return isshareable;
	}
	
}
