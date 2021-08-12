package net.imyeyu.betterjava;

import net.imyeyu.betterjava.bean.DateTimeDifference;

import java.text.SimpleDateFormat;
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

	/** 1 秒时间戳 */
	public static final long S = 1000;
	/** 1 分钟时间戳 */
	public static final long M = S * 60;
	/** 1 小时时间戳 */
	public static final long H = M * 60;
	/** 1 天时间戳 */
	public static final long D = H * 24;


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
		final double cy = 31536E6;

		final double l  = endUnixTime - beginUnixTime;
		final int    y  = BetterJava.floor(l / cy),
				     d  = BetterJava.floor((l / D) - y * 365),
				     h  = BetterJava.floor((l - (y * 365 + d) * D) / H),
				     m  = BetterJava.floor((l - (y * 365 + d) * D - h * H) / M),
				     s  = BetterJava.floor((l - (y * 365 + d) * D - h * H - m * M) / S),
				     ms = BetterJava.floor(((l - (y * 365 + d) * D - h * H - m * M) / S - s) * S);
		return new DateTimeDifference(y, d, h, m, s, ms);
	}

	/** @return 今天零时时间戳 */
	public static long today() {
		long now = System.currentTimeMillis();
		final long eightHour = H * 8;
		return ((now + eightHour - (now + now) % (now * 24)) - eightHour);
	}

	/** @return 明天零时时间戳 */
	public static long tomorrow() {
		return today() + D;
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
}