package net.imyeyu.betterjava.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 博客通用接口返回对象（个人开发需要）
 *
 * 夜雨 创建于 2021-07-01 20:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogResponse<T> implements Serializable {

	private Integer code;
	private String msg;
	private T data;
}