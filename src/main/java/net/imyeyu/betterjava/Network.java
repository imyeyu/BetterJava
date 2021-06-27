package net.imyeyu.betterjava;

import javax.naming.NoPermissionException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 网络操作
 *
 * 夜雨 创建于 2021/2/13 09:55
 */
public final class Network implements BetterJava {

	/**
	 * 发送 GET 请求
	 *
	 * @param url   请求链接
	 * @param isSSL 为 true 时使用 HTTPS 请求
	 * @return 返回结果
	 * @throws Exception 请求异常
	 */
	public static String doGet(String url, boolean isSSL) throws Exception {
		URL uri = new URL(url);

		HttpURLConnection connect = (HttpURLConnection) uri.openConnection();
		if (isSSL) {
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[] {X509}, null);
			if (connect instanceof HttpsURLConnection) {
				((HttpsURLConnection) connect).setSSLSocketFactory(sslcontext.getSocketFactory());
			}
		}
		connect.setRequestMethod("GET");
		setRequestHeader(connect);

		String line;
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(connect.getInputStream(), StandardCharsets.UTF_8));
		while ((line = br.readLine()) != null) {
			sb.append(line).append("\r\n");
		}
		br.close();
		connect.disconnect();

		return sb.toString();
	}
	public static String doGet(String url) throws Exception {
		return doGet(url, false);
	}
	public static String doGet(String url, Map<String, String> params, boolean isSSL) throws Exception {
		return doGet(Encode.enURL(url, params), isSSL);
	}
	public static String doGet(String url, Map<String, String> params) throws Exception {
		return doGet(Encode.enURL(url, params), false);
	}

	private static final TrustManager X509 = new X509TrustManager() {
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}
	};

	/**
	 * 发送 POST 请求
	 *
	 * @param url    请求地址
	 * @param params 请求参数
	 * @return 返回结果
	 */
	public static String doPost(String url, Map<String, String> params) {
//		return doPost(Encode.enURL(url, params));
		return "";
	}

	/**
	 * 使用默认浏览器打开 URL 地址
	 *
	 * @param url 地址
	 */
	public static void openURIInBrowser(String url) {
		try {
			Desktop dp = Desktop.getDesktop();
			if (dp.isSupported(Desktop.Action.BROWSE)) {
				dp.browse(URI.create(url));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * PING 一个地址的延时（Windows 方法）
	 *
	 * @param ip IP 地址
	 * @return 延时毫秒，为 -1 时表示请求超时
	 */
	public static int pingHostByCMD(String ip) {
		Runtime rt = Runtime.getRuntime();
		int ping = -1;
		try {
			Process process = rt.exec("ping " + ip + " -n 1");
			process.isAlive();
			StringBuilder sb = new StringBuilder();
			InputStreamReader isr = new InputStreamReader(process.getInputStream(), "GB2312");
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			line = sb.toString();
			if (!(line.contains("请求超时") || line.contains("timed out"))) {
				int start, end;
				if (line.contains("平均")) {
					start = line.indexOf("平均") + 5;
				} else {
					start = line.indexOf("Average") + 10;
				}
				end = line.indexOf("ms", start);
				ping = Integer.parseInt(line.substring(start, end));
			}
			br.close();
			isr.close();
			return ping;
		} catch (Exception e) {
			return ping;
		}
	}

	/**
	 * 下载网络文件
	 *
	 * @param url      网络文件地址
	 * @param path     下载到..文件夹
	 * @param fileName 下载后的文件命名
	 * @throws Exception 下载异常
	 */
	public static void downloadFile(String url, String path, String fileName) throws Exception {
		File dir = new File(path);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new NoPermissionException("无法创建文件夹：" + dir.getAbsolutePath());
			}
		}
		// 下载
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setConnectTimeout(8000);
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		InputStream is = conn.getInputStream();

		File file = new File(dir + SEP + fileName);
		FileOutputStream fos = new FileOutputStream(file);

		byte[] buffer = new byte[1024];
		int l;
		while ((l = is.read(buffer)) != -1) {
			fos.write(buffer, 0, l);
		}
		fos.close();
		is.close();
	}

	/**
	 * 获取本机 IP 地址（ip.chinaz.com 接口）
	 *
	 * @return 本机地址
	 */
	public static String getNetworkIp() {
		String response = doPost("http://ip.chinaz.com", null);
		Matcher m = Pattern.compile("<dd class=\"fz24\">(.*?)</dd>").matcher(response);
		return m.find() ? m.group(1) : "";
	}

	/**
	 * 检测一个端口是否被占用
	 *
	 * @param port 端口
	 * @return 为 true 时表示已被占用
	 */
	public static boolean isBusyPort(int port) {
		if (port < 1 || 65535 < port) {
			throw new IllegalArgumentException("端口值范围 [1, 65535]");
		}
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress("localhost", port), 500);
			socket.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 获取远程文件大小
	 *
	 * @param url   地址
	 * @return 字节大小
	 */
	public static Long getLength(String url) throws Exception {
		return getLength(url, false);
	}

	/**
	 * 获取远程文件大小
	 *
	 * @param url   地址
	 * @param isSSL 是否 SSL 请求
	 * @return 字节大小
	 */
	public static Long getLength(String url, boolean isSSL) throws Exception {
		URL uri = new URL(url);
		HttpURLConnection connect = (HttpURLConnection) uri.openConnection();
		if (isSSL) {
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[] {X509}, null);
			if (connect instanceof HttpsURLConnection) {
				((HttpsURLConnection) connect).setSSLSocketFactory(sslcontext.getSocketFactory());
			}
		}
		connect.setRequestMethod("GET");
		setRequestHeader(connect);
		return connect.getContentLengthLong();
	}

	/**
	 * 设置请求头
	 *
	 */
	private static void setRequestHeader(URLConnection connect) {
		connect.setRequestProperty("accept", "*/*");
		connect.setRequestProperty("connection", "Keep-Alive");
		connect.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		connect.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		connect.setRequestProperty("Accept-Charset", "UTF-8");
		connect.setConnectTimeout(8000);
	}
}