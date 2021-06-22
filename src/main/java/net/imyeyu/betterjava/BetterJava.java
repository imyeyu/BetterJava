package net.imyeyu.betterjava;

/**
 * BetterJava，由 YeyuUtils -> iUtils -> iTools -> BetterJava 迭代
 *
 * 夜雨 创建于 2021/2/13 11:39
 */
public interface BetterJava {

	String BETTER_JAVA_VERSION = "1.1.6";

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