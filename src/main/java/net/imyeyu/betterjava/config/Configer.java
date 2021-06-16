package net.imyeyu.betterjava.config;

import net.imyeyu.betterjava.IO;

import javax.naming.ConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>配置文件管理器，请关注 {@link net.imyeyu.betterjava.config.Config} 对象
 * 因为加载器需要默认配置来检验当前配置是否存在异常（缺失或其他异常）
 *
 * 夜雨 创建于 2021/2/13 11:41
 */
public class Configer {
	
	private final File iniFile;
	private final String jarFile;
	private Config config;
	
	/**
	 * 参数为 Jar 内默认配置文件位置，如果没有配置文件将重新生成默认的
	 * 
	 * @param file 默认配置文件路径
	 */
	public Configer(String file) {
		this.jarFile = file;
		String appName = file.substring(file.lastIndexOf(Character.toChars(47)[0]) + 1);
		iniFile = new File(appName.endsWith(".ini") ? appName : appName + ".ini");
		if (!iniFile.exists()) {
			try {
				IO.jarFileToDisk(file, appName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 保存配置
	 * 
	 * @param config 配置对象
	 */
	public void set(Config config) {
		StringBuilder sb = new StringBuilder();
		String d = IO.toString(iniFile, "UTF-8");
		String[] c = d.split("\r\n|[\r\n]");
		String k;
		for (int i = 0, l = c.length; i < l; i++) {
			if (c[i].equals("")) {
				sb.append("\r\n");
				continue;
			}
			if (!c[i].startsWith("#")) {
				k = c[i].substring(0, c[i].indexOf("="));
				if (config.getString(k).equals("")) {
					sb.append(k).append("=\r\n");
				} else {
					sb.append(k).append("=").append(config.getString(k)).append("\r\n");
				}
			} else {
				sb.append(c[i]).append("\r\n");
			}
		}
		IO.toFile(iniFile, sb.toString());
	}
	
	/**
	 * <p>配置文件字符串生成配置对象
	 * <p>可能是读取现成的文件，也可能是默认的配置文件，所有要独立方法
	 * 
	 * @param d                       配置文件字符串
	 * @return                        配置对象
	 * @throws ConfigurationException 无法读取配置值
	 */
	private Config generateConfig(String d) throws ConfigurationException {
		Map<String, Object> map = new HashMap<>();
		String[] c = d.split("\r\n|[\r\n]");
		String k, v;
		for (int i = 0, l = c.length; i < l; i++) {
			c[i] = c[i].trim();
			if (c[i].length() != 0 && !c[i].startsWith("#")) {
				final int split = c[i].indexOf("=");
				if (split == -1) throw new ConfigurationException("配置缺失等号: " + c[i]);
				k = c[i].substring(0, split);
				if (c[i].length() - 1 != split) {
					v = c[i].substring(split + 1);
				} else {
					v = "";
				}
				map.put(k, v);
			}
		}
		return new Config(map);
	}

	/**
	 * <p>从现成的配置文件获取配置对象
	 * 
	 * @return 配置对象
	 * @throws ConfigurationException 配置异常
	 */
	public Config get() throws ConfigurationException, FileNotFoundException {
		Config configDEF = getDefault();
		config = generateConfig(IO.toString(iniFile, "UTF-8"));
		
		Map<String, Object> def = configDEF.getMap();
		for (Map.Entry<String, Object> config : def.entrySet()) {
			if (!this.config.has(config.getKey())) {
				throw new ConfigurationException("配置文件异常：缺失默认配置: " + config.getKey());
			}
		}
		return config;
	}
	
	/**
	 * 从默认的配置文件获取配置对象
	 * 
	 * @return                        配置对象
	 * @throws ConfigurationException 配置异常
	 */
	public Config getDefault() throws ConfigurationException, FileNotFoundException {
		return generateConfig(IO.jarFileToString(jarFile));
	}
	
	/**
	 * 导出 Jar 内部默认配置以重置配置
	 * 
	 * @return 默认配置对象
	 */
	public Config reset() {
		try {
			IO.jarFileToDisk(jarFile, iniFile.getAbsolutePath());
			config = get();
		} catch (FileNotFoundException e) {
			throw new NullPointerException("找不到默认配置文件: " + jarFile);
		} catch (ConfigurationException | IOException e) {
			e.printStackTrace();
		}
		return config;
	}
}