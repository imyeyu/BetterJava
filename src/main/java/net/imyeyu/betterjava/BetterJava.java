package net.imyeyu.betterjava;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * BetterJava，由 YeyuUtils -> iUtils -> iTools -> BetterJava 迭代
 *
 * 夜雨 创建于 2021/2/13 11:39
 */
public final class BetterJava {

	public static final String VERSION = "1.1.4";
	
	/**
	 * 队列对象，输入若干对象，逐一遍历，返回第一个不为空的对象
	 * 
	 * @param <T> 对象类型
	 * @param ts  对象数组
	 * @return 第一个不为空的对象
	 */
	@SafeVarargs
	public static <T>T queue(T... ts) {
		for (int i = 0; i < ts.length; i++) {
			if (ts[i] != null) {
				return ts[i];
			}
		}
		return null;
	}

	/** @return 当前日期（yyyy-MM-dd） */
	public static String date() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	/** @return 当前时间（HH:mm:ss） */
	public static String time() {
		return new SimpleDateFormat("HH:mm:ss").format(new Date());
	}

	/** @return 当前日期时间（yyyy-MM-dd HH:mm:ss） */
	public static String datetime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
}