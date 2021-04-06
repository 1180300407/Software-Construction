package Resources;

/**
 * ��ʾ"�ɻ�"��Դ�����ɱ���
 * @author 123
 *
 */

public class Plane {
	private final String id;//�ɻ����
	private final String type;//���ͺ�
	private final int seats;//��λ��
	private final double planeage;//����
	
	//Abstraction function:
	//	AF(id,type,seats,planeage)=һ�ܱ��Ϊid�ķɻ������ͺ���type����seats����λ������Ϊplaneage
	//Representation invariant:
	//	true
	//Safety from rep exposure:
	//	��Ա����ȫ��private��final�ģ���Ϊ���ɱ����ͣ������ڱ�ʾй¶
	
	/**
	 * ���캯��
	 * @param id ָ���ɻ����
	 * @param type ָ���ɻ����ͺ�
	 * @param seats ָ���ɻ���λ��
	 * @param planeage ָ���ɻ�����
	 */
	public Plane(String id, String type, int seats, double planeage) {
		this.id = id;
		this.type = type;
		this.seats = seats;
		this.planeage = planeage;
	}

	/**
	 * @return �÷ɻ��ķɻ����
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return �÷ɻ��Ļ��ͺ�
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return �÷ɻ�����λ��
	 */
	public int getSeats() {
		return seats;
	}

	/**
	 * @return �÷ɻ��Ļ���
	 */
	public double getPlaneage() {
		return planeage;
	}

	
	//��дhashCode��equals��ֻ����ID��������
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
