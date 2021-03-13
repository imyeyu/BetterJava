package net.imyeyu.betterjava;

import com.sun.management.OperatingSystemMXBean;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 其他工具集
 *
 * 夜雨 创建于 2021/2/12 21:08
 */
public final class Tools {

	/**
	 * 字符串剪切，对等宽字体有效
	 *
	 * @param data   字符串
	 * @param length 保留长度
	 * @return 剪切结果
	 */
	public static String cutString(String data, int length) {
		if (data.length() < length / 2) {
			return data;
		}
		int count = 0;
		StringBuilder sb = new StringBuilder();
		String[] array = data.split("");
		for (int i = 0; i < array.length; i++) {
			count += array[i].getBytes().length > 1 ? 2 : 1;
			sb.append(array[i]);
			if (count >= length) {
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * 随机哈希表
	 *
	 * @param map   哈希表
	 * @param limit 数量限制
	 * @return 随机结果
	 */
	public static <T> Map<T, T> randomMap(Map<T, T> map, int limit) {
		Map<T, T> resultTemporary = new LinkedHashMap<>();
		List<Map.Entry<T, T>> list = new ArrayList<>(map.entrySet());
		list.sort((lhs, rhs) -> {
			int randomOne = (int) (Math.random() * 10 + lhs.hashCode());
			int randomTwo = (int) (Math.random() * 10 + rhs.hashCode());
			return randomOne - randomTwo;
		});
		for (int i = 0, l = list.size(); i < l; i++) {
			Map.Entry<T, T> mapEntry = list.get(i);
			if (resultTemporary.size() < limit) {
				resultTemporary.put(mapEntry.getKey(), mapEntry.getValue());
			}
		}
		return resultTemporary;
	}

	/**
	 * 根据键排序哈希表
	 *
	 * @param map        哈希表
	 * @param comparator 比较器
	 * @return 排序结果
	 */
	public static <T, K> Map<T, K> sortMap(Map<T, K> map, Comparator<T> comparator) {
		if (map == null)   return null;
		if (map.isEmpty()) return map;

		Map<T, K> r = new TreeMap<>(comparator);
		r.putAll(map);
		return r;
	}

	/**
	 * 根据字符串键排序哈希表
	 *
	 * @param map 哈希表
	 * @return 排序结果
	 */
	public static <T> Map<String, T> sortMapByStringKey(Map<String, T> map) {
		return sortMap(map, String::compareTo);
	}

	/**
	 * 根据数字键排序哈希表
	 *
	 * @param map 哈希表
	 * @return 排序结果
	 */
	public static <T> Map<Number, T> sortMapByNumberKey(Map<Number, T> map) {
		return sortMap(map, Comparator.comparingDouble(Number::doubleValue));
	}

	/**
	 * 安全地根据键移除哈希表的对象
	 *
	 * @param map 哈希表
	 * @param key 键
	 * @return 移除后的表
	 */
	public static <T, K> Map<T, K> removeByKey(Map<T, K> map, T key) {
		Iterator<T> iterator = map.keySet().iterator();
		T t;
		while (iterator.hasNext()) {
			t = iterator.next();
			if (key.equals(t)) {
				iterator.remove();
			}
		}
		return map;
	}

	/**
	 * 获取系统内存大小（MB）
	 *
	 * @return 系统内存大小
	 */
	public static int getSystemMemorySize() {
		OperatingSystemMXBean osmium = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		long size = osmium.getTotalMemorySize();
		size = size / 1024 / 1024;
		return (int) size;
	}

	/**
	 * 检查某程序的某进程是否在运行（Windows 方法）
	 *
	 * @param appName     程序名
	 * @param processName 进程名
	 * @return true 为正在运行，否则为 false
	 */
	public static boolean findProcess(String appName, String processName) {
		BufferedReader bufferedReader;
		try {
			Process proc = Runtime.getRuntime().exec("tasklist -v -fi " + '"' + "imagename eq " + appName + '"');
			bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.startsWith(appName) && line.contains(processName)) {
					return true;
				}
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 设置字符串到剪切板（复制）
	 *
	 * @param s 字符串
	 */
	public static void setIntoClipboard(String s) {
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(s), null);
	}

	/**
	 * 获取剪切版的字符串（粘贴）
	 *
	 * @return 剪切板字符串，如果不是剪切板没有字符串将返回空的字符串
	 */
	public static String getIntoClipboard() {
		try {
			return Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null).getTransferData(DataFlavor.stringFlavor).toString();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * <br>格式化一个储存容量
	 * <br>支持 B, KB, MB, GB, TB
	 * <br>示例：
	 * <pre>
	 *     Tools.storageFormat(102411, 2); // 返回 100.01 KB
	 * </pre>
	 *
	 * @param size    字节大小
	 * @param decimal 保留小数
	 * @return 格式化结果
	 */
	public static String byteFormat(double size, int decimal) {
		final String[] unit = {" B ", " KB", " MB", " GB", " TB"};
		if (0 < size) {
			String format;
			for (int i = 0; i < unit.length; i++, size /= 1024) {
				format = String.format("%." + decimal + "f" + unit[i], size);
				if (size <= 1000) {
					return format;
				} else {
					if (i == unit.length - 1) {
						return format;
					}
				}
			}
		}
		return "N/A";
	}

	/**
	 * 向上反射查找方法
	 *
	 * @param object         对象
	 * @param methodName     方法名
	 * @param parameterTypes 可选参
	 * @return 方法对象
	 */
	public static Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes) {
		Method method;
		for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				method = clazz.getDeclaredMethod(methodName, parameterTypes);
				return method;
			} catch (Exception e) {
				// 向上转型
			}
		}
		return null;
	}

	/**
	 * 计算字符串相似度（编辑距离算法）
	 *
	 * @param source   需比较的字符串
	 * @param target   被比较的字符串
	 * @param isIgnore 为 true 时忽略大小写
	 * @return 相似度 [0, 1]
	 */
	private static float levenshteinDistance(String source, String target, boolean isIgnore) {
		int[][] d;
		int n = source.length(), m = target.length(), i, j, temp;
		char charS, charT;

		if (n == 0) return m;
		if (m == 0) return n;

		d = new int[n + 1][m + 1];
		for (i = 0; i <= n; i++) {
			d[i][0] = i;
		}
		for (j = 0; j <= m; j++) {
			d[0][j] = j;
		}
		for (i = 1; i <= n; i++) {
			charS = source.charAt(i - 1);
			for (j = 1; j <= m; j++) {
				charT = target.charAt(j - 1);
				if (isIgnore) {
					if (charS == charT || charS == charT + 32 || charS + 32 == charT) {
						temp = 0;
					} else {
						temp = 1;
					}
				} else {
					if (charS == charT) {
						temp = 0;
					} else {
						temp = 1;
					}
				}
				d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
			}
		}
		return d[n][m];
	}

	/**
	 * 三数求最小
	 *
	 * @param one   值一
	 * @param two   值二
	 * @param three 值三
	 * @return 最小值
	 */
	private static int min(int one, int two, int three) {
		return (one = Math.min(one, two)) < three ? one : three;
	}

	/**
	 * 求字符串相似度，忽略大小写
	 *
	 * @param source 需比较的字符串
	 * @param target 被比较的字符串
	 * @return 相似度 [0, 1]
	 */
	public static float getSimilarityRatio(String source, String target) {
		return getSimilarityRatio(source, target, true);
	}

	/**
	 * 求字符串相似度
	 *
	 * @param source   需比较的字符串
	 * @param target   被比较的字符串
	 * @param isIgnore 为 true 时忽略大小写
	 * @return 相似度 [0, 1]
	 */
	public static float getSimilarityRatio(String source, String target, boolean isIgnore) {
		float ret;
		final int max = Math.max(source.length(), target.length());
		if (max == 0) {
			ret = 1;
		} else {
			ret = 1 - levenshteinDistance(source, target, isIgnore) / max;
		}
		return ret;
	}

	/**
	 * 取出哈希表的键作为列表
	 *
	 * @param map 哈希表
	 * @return 以哈希表键为类型的列表
	 */
	public static <T, K> List<T> mapKeys(Map<T, K> map) {
		if ((map != null) && (!map.isEmpty())) {
			return new ArrayList<>(map.keySet());
		}
		return null;
	}
}