package compositeinterface;

import java.util.List;

import Resources.Teacher;
import multidimension.MultipleSortedResourceEntry;
import multidimension.SingleLocationEntry;
import multidimension.UnBlockableEntry;
/**
 * 一个课程计划项
 * @author 123
 *
 * @param <R> 占用资源类型
 */
public interface CoursePlanningEntry<R extends Teacher> extends SingleLocationEntry,MultipleSortedResourceEntry<R>,UnBlockableEntry{
	/**
	 * 静态工厂,创造一个课程计划项的实例,初始状态为Waiting
	 * @param <R> 课程占用的资源,应是Teacher类或其子类
	 * @param name 课程名称
	 * @return 课程计划项的实例
	 */
	public static <R> CourseEntry<R> CreateCourse(String name) {
		CourseEntry<R> ce=new CourseEntry<R>(name);
		return ce;
	}
	
	/**
	 * 课程上课,教师分配后才可能正常上课
	 */
	public void start();
	
	/**
	 * 课程提前取消,课程未开始才可能取消成功
	 */
	public void cancel();
	
	/**
	 * 结束课程,即下课,课程已开始才可能下课
	 */
	public void end();

	/**
	 * @return 目前课程所处状态的名称
	 */
	public String getStateName();
	
	/**
	 * @return 课程名称
	 */
	public String getName();
	
	/**
	 * 为课程分配教师,教师的次序应该已经确定
	 */
	@Override
	public void allocateResource(List<R> resource);
	
}
