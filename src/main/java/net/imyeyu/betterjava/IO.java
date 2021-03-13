package net.imyeyu.betterjava;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
	 * @return 为 true 时写入成功
	 */
	public static boolean toFile(File file, String data) {
		boolean isSuccessed = false;
		try {
			if (!file.exists()) {
				if (file.createNewFile()) {
					FileOutputStream fos = new FileOutputStream(file);
					OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
					BufferedWriter bw = new BufferedWriter(osw);
					bw.write(data);
					bw.flush();
					bw.close();
					osw.close();
					fos.close();
					isSuccessed = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isSuccessed;
	}

	/**
	 * 读取数据流为字符串
	 *
	 * @param isr 输入流
	 * @return 字符串内容
	 */
	public static String toString(InputStreamReader isr) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(isr);
			String input;
			while ((input = br.readLine()) != null) {
				sb.append(input).append("\r\n");
			}
			br.close();
			isr.close();
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
			result = toString(new InputStreamReader(fis, charset));
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
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
	 * 读取 Jar 内文件为字符串内容（UTF-8）
	 *
	 * @param path Jar 内文件路径
	 * @return 字符串内容
	 */
	public static String jarFileToString(String path) {
		String result = "";
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
			if (is != null) {
				result = toString(new InputStreamReader(is, StandardCharsets.UTF_8));
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 复制 Jar 内的文件到磁盘（字节流）
	 *
	 * @param jarPath  Jar 内文件
	 * @param filePath 磁盘路径
	 * @return 为 true 时复制成功
	 */
	public static boolean jarFileToDisk(String jarPath, String filePath) {
		boolean isSuccessed = false;
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(jarPath);
			if (is != null) {
				FileOutputStream fos = new FileOutputStream(filePath);
				byte[] input = new byte[128];
				int l;
				while ((l = is.read(input)) != -1) {
					fos.write(input, 0, l);
				}
				fos.close();
				is.close();
				isSuccessed = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isSuccessed;
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
}