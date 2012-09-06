package com.byttersoft.patchbuild.cache;

/**
 * 可缓存对象接口
 * 实现本接口说明对象可以被缓存管理
 * @author pangl
 *
 */
public interface Cacheable {
	
	/**
	 * 返回最后一次使用时间
	 * @return
	 */
	public long getLastUseTime();
	
	/**
	 * 更新最后一次使用时间为系统当前时间
	 */
	public void updateUseTime();

}
