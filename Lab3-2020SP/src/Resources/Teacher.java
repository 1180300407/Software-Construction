package Resources;

/**
 * ��ʾ����ʦ����Դ�����ɱ���
 * @author 123
 *
 */

public class Teacher {
	private final String id;
	private final String name;
	private final boolean sex;
	private final String professtionalTitle;
	
	//Abstraction function:
	//	AF(id,type,maxnum,manufactureyear)=һλ��ʦ
	//  ��ʦ�����֤����id����ʦ��������name��sexΪtrue��Ϊ����ʦ����֮ΪŮ��ʦ
	//	��ʦ��ְ����professtionalTitle
	//Representation invariant:
	//	true
	//Safety from rep exposure:
	//	��Ա����ȫ��private��final�ģ���Ϊ���ɱ����ͣ������ڱ�ʾй¶
	
	/**
	 * ���캯��
	 * @param id ָ����ʦ���֤��
	 * @param name ָ����ʦ����
	 * @param sex ָ����ʦ�Ա�
	 * @param professtionalTitle ָ����ʦְ��
	 */
	public Teacher(String id, String name, boolean sex, String professtionalTitle) {
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.professtionalTitle = professtionalTitle;
	}

	/**
	 * @return ��ʦ�����֤��
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return ��ʦ������
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return ��ʦ���Ա�true�������ԣ���֮Ů��
	 */
	public boolean getSex() {
		return sex;
	}

	/**
	 * @return ��ʦ��ְ��
	 */
	public String getProfesstionalTitle() {
		return professtionalTitle;
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
		Teacher other = (Teacher) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
