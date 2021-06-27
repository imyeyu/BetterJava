package net.imyeyu.betterjava;

import java.io.File;

/**
 * BetterJava，由 YeyuUtils -> iUtils -> iTools -> BetterJava 迭代
 *
 * 夜雨 创建于 2021/2/13 11:39
 */
public interface BetterJava {

	/** BetterJava 版本 */
	String VERSION = "1.1.6";

	/** 文件系统路径分隔符 File.separator */
	String SEP = File.separator;

	/**
	 * 前补零（最终长度 2 字符）
	 *
	 * @param number 数值
	 * @return 补零字符串
	 */
	default String zero(Number number) {
		return zero(2, number);
	}

	/**
	 * 前补零
	 *
	 * @param l      最终长度
	 * @param number 数值
	 * @return 补零字符串
	 */
	default String zero(int l, Number number) {
		return String.format("%0" + l + "d", number);
	}

	/**
	 * 向下取整返回整型
	 *
	 * @param v 数值
	 * @return 取整结果
	 */
	static int floor(double v) {
		return (int) Math.floor(v);
	}
}