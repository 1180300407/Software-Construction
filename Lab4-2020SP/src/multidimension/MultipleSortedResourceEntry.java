package multidimension;

import java.util.List;

/**
 * 表示占用多个有序资源的计划项，可对计划项进行资源分配
 * @author 123
 *
 * @param <R> 占用资源的类型
 */

public interface MultipleSortedResourceEntry<R> {
	/**
	 * @return 已分配的资源
	 */
	public List<R> getResource(); 
	
	/**
	 * 对资源进行分配，只可分配一次,资源的相对顺序应该已经确定
	 * @param rs 待分配资源
	 */
	public void allocateResource(List<R> rs);
}
