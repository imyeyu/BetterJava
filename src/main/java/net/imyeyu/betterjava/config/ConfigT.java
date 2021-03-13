package net.imyeyu.betterjava.config;

/**
 * 泛型配置
 *
 * 夜雨 创建于 2021/2/13 11:38
 */
public class ConfigT<T> {
	
	private final String key;
	
	public ConfigT(String key) {
		this.key = key;
	}
	
	public String get() {
		return key;
	}
}
