package Resources;

/**
 * ��ʾ�����ᡱ��Դ�����ɱ���
 * @author 123
 *
 */

public class Carriage {
	private final String id;//������
	private final String type;//��������
	private final int maxnum;//���ᶨԱ��
	private final String manufactureyear;//�������
	
	//Abstraction function:
	//	AF(id,type,maxnum,manufactureyear)=һ�����Ϊid�ĳ��ᣬ��������Ϊtype����Ա��Ϊmaxnum�����������manufactureyear
	//Representation invariant:
	//	true
	//Safety from rep exposure:
	//	��Ա����ȫ��private��final�ģ���Ϊ���ɱ����ͣ������ڱ�ʾй¶
	
	/**
	 * ���캯��
	 * @param id ָ��������
	 * @param type ָ���������ͣ�Ϊ����һ��
	 * @param maxnum ָ�����ᶨԱ��
	 * @param manufactureyear ָ���������
	 */
	public Carriage(String id, String type, int maxnum, String manufactureyear) {
		this.id = id;
		this.type = type;
		this.maxnum = maxnum;
		this.manufactureyear = manufactureyear;
	}

	/**
	 * @return ������
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return ��������
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return ���ᶨԱ��
	 */
	public int getMaxnum() {
		return maxnum;
	}

	/**
	 * @return ����������
	 */
	public String getManufactureyear() {
		return manufactureyear;
	}

	//��дequals��hashCode,idΪΨһ��׼
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
