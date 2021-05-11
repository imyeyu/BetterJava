package net.imyeyu.betterjava.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>精确的多任务服务，指定某一时刻执行，并每隔某一时段再执行
 * <p>通常用于: 每日任务，每时任务等
 * <p>执行间隔需要大于等于 10 分钟，否则更推荐其他计时器，默认间隔 1 天
 * 
 * 夜雨 创建于 2021/2/13 11:39
 */
public abstract class ExactService<T> {

	public static final long MINUTE = 60000;
	public static final long HORSE = MINUTE * 60;
	public static final long DAY = HORSE * 24;
	public static final long WEEK = DAY * 7;
	
	private long i = 0;
	private Timer timer;

	/** 每天零时执行 */
	public ExactService() {
		this(0, 0, 0, DAY);
	}

	/**
	 * 指定某一时刻执行，并每隔一天再执行
	 *
	 * @param h 时
	 * @param m 分
	 * @param s 秒
	 */
	public ExactService(int h, int m, int s) {
		this(h, m, s, DAY);
	}

	/**
	 * 指定某一时刻执行，并每隔某一时段再执行
	 *
	 * @param h 时
	 * @param m 钟
	 * @param s 秒
	 * @param interval 间隔
	 */
	public ExactService(int h, int m, int s, long interval) {
		if (interval < 10 * MINUTE) {
			throw new IllegalArgumentException("最小执行间隔不应小于 10 分钟，如果需要频率更高的计时器，请使用其他 Service");
		}

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, h);
		calendar.set(Calendar.MINUTE, m);
		calendar.set(Calendar.SECOND, s);
		// 第一次执行的时间
		Date date = calendar.getTime();
		// 如果小于当前的时间
		if (date.before(new Date())) {
			Calendar startDT = Calendar.getInstance();
			startDT.setTime(date);
			startDT.add(Calendar.DATE, 1);
			date = startDT.getTime();
		}
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				onUpdate(++i);
			}
		}, date, interval);
	}

	/**
	 * 执行事件
	 *
	 * @param i 第 i 次执行
	 */
	protected void onUpdate(long i) {

	}
	
 	public void shutdown() {
		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}
	}
}