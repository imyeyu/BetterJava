package net.imyeyu.betterjava;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 数据流操作
 *
 * 夜雨 创建于 2021/2/13 10:15
 */
public final class IO {

	/**
	 * 运行程序（.jar）所在磁盘的绝对路径
	 *
	 * @return 绝对路径
	 */
	public static String getJarAbsolutePath() {
		String path = IO.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		path = Encode.deURL(path);
		if (path.endsWith(".jar")) {
			path = path.substring(0, path.lastIndexOf(File.separator) + 1);
		}
		path = new File(path).getAbsolutePath();
		return path;
	}

	/**
	 * 写入字符串到文件
	 *
	 * @param file 文件
	 * @param data 字符串内容
	 */
	public static void toFile(File file, String data) throws IOException {
		toFile(file, new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
	}



	/**
	 * 写入数据流到文件
	 *
	 * @param absFile 文件绝对路径
	 * @param is      数据流
	 * @throws IOException 异常
	 */
	public static void toFile(String absFile, InputStream is) throws IOException {
		toFile(new File(absFile), is);
	}

	/**
	 * 写入数据流到文件
	 *
	 * @param file 文件
	 * @param is   数据流
	 * @throws IOException 异常
	 */
	public static void toFile(File file, InputStream is) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		byte[] input = new byte[4096];
		int l;
		while ((l = is.read(input)) != -1) {
			fos.write(input, 0, l);
		}
		fos.close();
		is.close();
	}


	/**
	 * 以 UTF-8 编码读取数据流为字符串
	 *
	 * @param is 输入流
	 * @return 字符串
	 */
	public static String toString(InputStream is) {
		return toString(is, StandardCharsets.UTF_8);
	}

	/**
	 * 读取文件为字符串（UTF-8）
	 *
	 * @param file 文件
	 * @return 文件内容
	 */
	public static String toString(File file) {
		return toString(file, "UTF-8");
	}

	/**
	 * 读取文件为字符串
	 *
	 * @param file    文件
	 * @param charset 编码类型
	 * @return 文件内容
	 */
	public static String toString(File file, String charset) {
		String result = "";
		try {
			FileInputStream fis = new FileInputStream(file);
			result = toString(fis);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 读取数据流为字符串
	 *
	 * @param is      输入流
	 * @param charset 编码格式
	 * @return 字符串
	 */
	public static String toString(InputStream is, Charset charset) {
		StringBuilder sb = new StringBuilder();
		try {
			InputStreamReader isr = new InputStreamReader(is, charset);
			BufferedReader br = new BufferedReader(isr);
			String input;
			while ((input = br.readLine()) != null) {
				sb.append(input).append("\r\n");
			}
			br.close();
			isr.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (0 < sb.length()) {
			return sb.substring(0, sb.length() - 1);
		} else {
			return "";
		}
	}

	/**
	 * 读取文件为数据流
	 *
	 * @param absFile 文件绝对路径
	 * @return 数据流
	 * @throws FileNotFoundException 找不到文件
	 */
	public static InputStream toInputStream(String absFile) throws FileNotFoundException {
		return new FileInputStream(absFile);
	}

	/**
	 * 读取 jar 内文件为数据流
	 *
	 * @param path 文件路径（文件路径，不需要 / 开始）
	 * @return 数据流
	 * @throws FileNotFoundException 找不到文件
	 */
	public static InputStream jarFileToInputStream(String path) throws FileNotFoundException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		if (is != null) {
			return is;
		}
		throw new FileNotFoundException("找不到该文件：" + path);
	}

	/**
	 * 读取 Jar 内文件为字符串内容（UTF-8）
	 *
	 * @param path Jar 内文件路径（无需 / 开始）
	 * @return 字符串内容
	 */
	public static String jarFileToString(String path) throws FileNotFoundException {
		return toString(jarFileToInputStream(path));
	}

	/**
	 * 复制 Jar 内的文件到磁盘（字节流）
	 *
	 * @param jarPath  Jar 内文件
	 * @param filePath 磁盘路径
	 * @throws IOException 执行异常
	 */
	public static void jarFileToDisk(String jarPath, String filePath) throws IOException {
		toFile(filePath, jarFileToInputStream(jarPath));
	}

	/**
	 * 隐藏文件（Windows 方法）
	 *
	 * @param files 文件列表
	 */
	public static void hidenFile(File... files) {
		try {
			for (int i = 0, l = files.length; i < l; i++) {
				if (!files[i].isHidden()) {
					if (files[i].exists()) {
						Runtime.getRuntime().exec("attrib " + "\"" + files[i].getAbsolutePath() + "\""+ " +H");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 计算文件或文件夹大小
	 *
	 * @param file 文件或文件夹
	 * @return 总大小（字节）
	 */
	public static synchronized long calcSize(final File file) {
		if (file.isFile()) {
			return file.length();
		}
		final File[] list = file.listFiles();
		long total = 0;
		if (list != null) {
			for (final File item : list) {
				total += calcSize(item);
			}
		}
		return total;
	}
}