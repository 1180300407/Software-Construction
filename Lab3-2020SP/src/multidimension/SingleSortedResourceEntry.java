package multidimension;

import java.util.List;

/**
 * 表示占用单个可区分资源的计划项，可对计划项进行资源分配
 * @author 123
 *
 * @param <R> 泛型R表示处理的资源的类型
 */

public interface SingleSortedResourceEntry<R> {
	
	/**
	 * @return 已分配的单个资源,资源数为1
	 */
	public List<R> getResource(); 
	
	/**
	 * 对资源进行分配，只可分配一次
	 * @param resource 待分配资源
	 */
	public void allocateResource(List<R> resource);
}
