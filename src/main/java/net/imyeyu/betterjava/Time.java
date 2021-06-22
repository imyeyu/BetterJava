package net.imyeyu.betterjava;

import net.imyeyu.betterjava.bean.DateTimeDifference;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.util.Date;

/**
 * 时间戳转换相关
 *
 * 夜雨 创建于 2021-06-10 20:07
 */
public class Time {

	/** yy */
	public static final SimpleDateFormat yearFormat      = new SimpleDateFormat("yy");
	/** yyyy */
	public static final SimpleDateFormat yearFormatFull  = new SimpleDateFormat("yyyy");
	/** M */
	public static final SimpleDateFormat monthFormat     = new SimpleDateFormat("M");
	/** MM */
	public static final SimpleDateFormat monthFormatFull = new SimpleDateFormat("MM");
	/** d */
	public static final SimpleDateFormat dayFormat       = new SimpleDateFormat("d");
	/** dd */
	public static final SimpleDateFormat dayFormatFull   = new SimpleDateFormat("dd");
	/** yyyyMMdd */
	public static final SimpleDateFormat ymdFormat       = new SimpleDateFormat("yyyyMMdd");
	/** HH:mm:ss */
	public static final SimpleDateFormat timeFormat      = new SimpleDateFormat("HH:mm:ss");
	/** yyyy-MM-dd */
	public static final SimpleDateFormat dateFormat      = new SimpleDateFormat("yyyy-MM-dd");
	/** yyyy-MM-dd HH:mm:ss */
	public static final SimpleDateFormat dateTimeFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static int f(double v) {
		return (int) Math.floor(v);
	}

	/**
	 * 计算当前时间和指定时间差（年-日-时-分-秒-毫秒）
	 *
	 * @param beginUnixTime 开始时间戳
	 * @return 计算结果
	 */
	public static DateTimeDifference calcDifference(Long beginUnixTime) {
		return calcDifference(beginUnixTime, new Date().getTime());
	}

	/**
	 * 计算时间差（年-日-时-分-秒-毫秒）
	 *
	 * @param beginUnixTime 开始时间戳
	 * @param endUnixTime   结束时间戳（如果没有就是当前时间）
	 * @return 计算结果
	 */
	public static DateTimeDifference calcDifference(Long beginUnixTime, Long endUnixTime) {
		if (endUnixTime < beginUnixTime) {
			throw new IllegalArgumentException("结束时间不应早于开始时间：" + endUnixTime + " < " + beginUnixTime);
		}
		final double cs = 1E3, cm = 6E4, ch = 36E5, cd = 864E5, cy = 31536E6;

		final long l = endUnixTime - beginUnixTime;
		final int y  = f(l / cy),
				  d  = f((l / cd) - y * 365),
				  h  = f((l - (y * 365 + d) * cd) / ch),
				  m  = f((l - (y * 365 + d) * cd - h * ch) / cm),
				  s  = f((l - (y * 365 + d) * cd - h * ch - m * cm) / cs),
				  ms = f(((l - (y * 365 + d) * cd - h * ch - m * cm) / cs - s) * cs);
		return new DateTimeDifference(y, d, h, m, s, ms);
	}

	/**
	 * 转义为日期 yyyy-MM-dd
	 *
	 * @param unixTime 时间戳
	 * @return 日期字符串
	 */
	public static String toDate(long unixTime) {
		return dateFormat.format(new Date(unixTime));
	}

	/**
	 * 转义为时间 HH:mm:ss
	 *
	 * @param unixTime 时间戳
	 * @return 时间字符串
	 */
	public static String toTime(long unixTime) {
		return timeFormat.format(new Date(unixTime));
	}

	/**
	 * 转义为日期时间 yyyy-MM-dd HH:mm:ss
	 *
	 * @param unixTime 时间戳
	 * @return 日期时间字符串
	 */
	public static String toDateTime(long unixTime) {
		return dateTimeFormat.format(new Date(unixTime));
	}

	/**
	 * 获取某月的最后一日
	 *
	 * @param y 年
	 * @param m 月
	 * @return 最后一日
	 */
	public static int getLastDayOfMonth(int y, int m) {
		return LocalDate.of(y, m, 1).getMonth().length(Year.of(y).isLeap());
	}
}