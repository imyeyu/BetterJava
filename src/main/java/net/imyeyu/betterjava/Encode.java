package net.imyeyu.betterjava;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 编码操作
 *
 * 夜雨 创建于 2021/2/13 10:59
 */
public final class Encode {

	/**
	 * 修改字符串编码
	 *
	 * @param data       字符串
	 * @param oldCharset 旧的编码
	 * @param newCharset 新的编码
	 * @return 编码结果
	 */
	public static String changeCharset(String data, String oldCharset, String newCharset) {
		try {
			if (data != null) {
				byte[] bs = data.getBytes(oldCharset);
				return new String(bs, newCharset);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Unicode 编码所有字符
	 *
	 * @param data 字符串
	 * @return 编码结果
	 */
	public static String enUnicodeAll(String data) {
		StringBuilder sb = new StringBuilder();
		char[] c = data.toCharArray();
		for (int i = 0, l = c.length; i < l; i++) {
			sb.append("\\u").append(Integer.toHexString(c[i]));
		}
		return sb.toString();
	}

	/**
	 * Unicode 编码全角字符
	 *
	 * @param data 字符串
	 * @return 编码结果
	 */
	public static String enUnicode(String data) {
		StringBuilder sb = new StringBuilder();
		char[] c = data.toCharArray();
		for (int i = 0, l = c.length; i < l; i++) {
			if (!isHalfChar(c[i])) {
				sb.append("\\u").append(Integer.toHexString(c[i]));
			} else {
				sb.append(c[i]);
			}
		}
		return sb.toString();
	}

	/**
	 * Unicode 解码
	 *
	 * @param data Unicode 编码的字符串
	 * @return 解码结果
	 */
	public static String deUnicode(String data) {
		StringBuilder sb = new StringBuilder();
		String[] hex = data.split("\\\\u");
		int index;
		for (int i = 1, l = hex.length; i < l; i++) {
			if (4 < hex[i].length()) {
				index = Integer.parseInt(hex[i].substring(0, 4), 16);
				sb.append((char) index);
				sb.append(hex[i].substring(4));
			} else {
				index = Integer.parseInt(hex[i], 16);
				sb.append((char) index);
			}
		}
		return sb.toString();
	}

	/**
	 * Base64 编码字符串
	 *
	 * @param data 字符串
	 * @return 编码结果
	 */
	public static String enBase64(String data) {
		return Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Base64 解码字符串
	 *
	 * @param data Base64 编码的字符串
	 * @return 解码结果
	 */
	public static String deBase64(String data) {
		return new String(Base64.getDecoder().decode(data), StandardCharsets.UTF_8);
	}

	/**
	 * 计算字符串 MD5
	 *
	 * @param data 字符串
	 * @return 计算结果
	 */
	public static String md5(String data) {
		StringBuilder sb = new StringBuilder();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			sb.append(new BigInteger(1, md.digest(data.getBytes())).toString(16));
			for (int i = 0; i < 32 - sb.length(); i++) {
				sb.insert(0, "0");
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 是否存在中文
	 *
	 * @param data 字符串
	 * @return 为 true 时表示存在中文
	 */
	public static boolean hasChinese(String data) {
		return Pattern.compile("[\u4e00-\u9fa5]").matcher(data).find();
	}

	/**
	 * 是否存在日文
	 *
	 * @param data 字符串
	 * @return 为 true 时表示存在日文
	 */
	public static boolean hasJapanese(String data) {
        try {
			return data.getBytes("shift-jis").length >= (2 * data.length());
		} catch (UnsupportedEncodingException e) {
			return false;
		}
	}

	/**
	 * 是否是数字。搜刮自 commons-lang
	 *
	 * @param data 字符串
	 * @return 为 true 是表示是数字
	 */
	public static boolean isNumber(String data) { 
        if (data == null || data.length() == 0) {
            return false;
        }
        final char[] chars = data.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        
        final int start = chars[0] == '-' || chars[0] == '+' ? 1 : 0;
        if (sz > start + 1 && chars[start] == '0' && data.indexOf('.') == -1) {
            if (chars[start + 1] == 'x' || chars[start + 1] == 'X') {
                int i = start + 2;
                if (i == sz) {
                    return false;
                }
                for (; i < chars.length; i++) {
                    if ((chars[i] < '0' || chars[i] > '9')
                        && (chars[i] < 'a' || chars[i] > 'f')
                        && (chars[i] < 'A' || chars[i] > 'F')) {
                        return false;
                    }
                }
                return true;
           } else if (Character.isDigit(chars[start + 1])) {
               int i = start + 1;
               for (; i < chars.length; i++) {
                   if (chars[i] < '0' || chars[i] > '7') {
                       return false;
                   }
               }
               return true;
           }
        }
        sz--;
        int i = start;
        while (i < sz || i < sz + 1 && allowSigns && !foundDigit) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;
                allowSigns = false;

            } else if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    return false;
                }
                hasDecPoint = true;
            } else if (chars[i] == 'e' || chars[i] == 'E') {
                if (hasExp) {
                    return false;
                }
                if (!foundDigit) {
                    return false;
                }
                hasExp = true;
                allowSigns = true;
            } else if (chars[i] == '+' || chars[i] == '-') {
                if (!allowSigns) {
                    return false;
                }
                allowSigns = false;
                foundDigit = false;
            } else {
                return false;
            }
            i++;
        }
        if (i < chars.length) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                return true;
            }
            if (chars[i] == 'e' || chars[i] == 'E') {
                return false;
            }
            if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    return false;
                }
                return foundDigit;
            }
            if (!allowSigns  && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
                return foundDigit;
            }
            if (chars[i] == 'l' || chars[i] == 'L') {
                return foundDigit && !hasExp && !hasDecPoint;
            }
            return false;
        }
        return !allowSigns && foundDigit;
	}

	/**
	 * 是否为半角字符
	 *
	 * @param c  字符
	 * @return 为 true 是表示是半角字符
	 */
	public static boolean isHalfChar(char c) {
		return (int) c < 129;
	}

	/**
	 * 编码 URL 链接，只会编码参数
	 *
	 * @param url URL 链接
	 * @return 编码结果
	 */
	public static String enURL(String url) {
		if (url == null || url.equals("")) throw new NullPointerException("空的 URL 地址");
		if (!url.contains("?")) {
			return url;
		}
		String[] urlSP = url.split("\\?");
		String[] kvs = urlSP[1].split("&");
		Map<String, String> parameters = new HashMap<>();
		String[] kv;
		for (int i = 0; i < kvs.length; i++) {
			kv = kvs[i].split("=");
			parameters.put(kv[0], kv[1]);
		}
		return enURL(urlSP[0], parameters);
	}

	/**
	 * 编码 URL 链接的参数
	 *
	 * @param url        URL 地址
	 * @param parameters 参数
	 * @return 编码结果
	 */
	public static String enURL(String url, Map<String, String> parameters) {
        if (url == null || url.equals("")) throw new NullPointerException("空的 URL 地址");
        StringBuilder r = new StringBuilder(url);
        StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> item : parameters.entrySet()) {
			sb.append("&").append(item.getKey()).append("=").append(URLEncoder.encode(item.getValue(), StandardCharsets.UTF_8));
		}
		return r.append("?").append(sb.substring(1)).toString();
	}

	/**
	 * 解码 URL 链接
	 *
	 * @param url 已编码的 URL 链接
	 * @return 解码结果
	 */
	public static String deURL(String url) {
		if (url == null) return "";
		return URLDecoder.decode(url, StandardCharsets.UTF_8);
	}
}