package Resources;

/**
 * 表示“教师”资源，不可变类
 * @author 123
 *
 */

public class Teacher {
	private final String id;
	private final String name;
	private final boolean sex;
	private final String professtionalTitle;
	
	//Abstraction function:
	//	AF(id,type,maxnum,manufactureyear)=一位老师
	//  老师的身份证号是id，老师的姓名是name，sex为true则为男老师，反之为女老师
	//	老师的职称是professtionalTitle
	//Representation invariant:
	//	true
	//Safety from rep exposure:
	//	成员变量全是private且final的，均为不可变类型，不存在表示泄露
	
	/**
	 * 构造函数
	 * @param id 指定教师身份证号
	 * @param name 指定教师姓名
	 * @param sex 指定教师性别
	 * @param professtionalTitle 指定教师职称
	 */
	public Teacher(String id, String name, boolean sex, String professtionalTitle) {
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.professtionalTitle = professtionalTitle;
	}

	/**
	 * @return 教师的身份证号
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return 教师的姓名
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return 教师的性别，true代表男性，反之女性
	 */
	public boolean getSex() {
		return sex;
	}

	/**
	 * @return 教师的职称
	 */
	public String getProfesstionalTitle() {
		return professtionalTitle;
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
		Teacher other = (Teacher) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
