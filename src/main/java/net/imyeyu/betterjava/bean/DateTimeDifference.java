package net.imyeyu.betterjava.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 日期时间
 *
 * 夜雨 创建于 2021-06-10 20:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateTimeDifference {

	private int year;
	private int day;
	private int hour;
	private int minute;
	private int second;
	private int millisecond;
}