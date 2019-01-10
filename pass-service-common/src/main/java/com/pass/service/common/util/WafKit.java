package com.pass.service.common.util;

import java.util.regex.Pattern;

/**
 * Web防火墙工具类
 * 
 * @Date	 2014-5-8 	 
 */
public class WafKit {

	/**
	 * 过滤XSS脚本内容
	 * @param value
	 * 				待处理内容
	 * @return
	 */
	public static String stripXSS(String value) {
		String rlt = null;

		if (null != value) {
			rlt = value.replaceAll("", "");

			Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
			rlt = scriptPattern.matcher(rlt).replaceAll("");

			scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
			rlt = scriptPattern.matcher(rlt).replaceAll("");

			scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL);
			rlt = scriptPattern.matcher(rlt).replaceAll("");

			scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL);
			rlt = scriptPattern.matcher(rlt).replaceAll("");

			scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL);
			rlt = scriptPattern.matcher(rlt).replaceAll("");

			scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
			rlt = scriptPattern.matcher(rlt).replaceAll("");

			scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
			rlt = scriptPattern.matcher(rlt).replaceAll("");

			scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL);
			rlt = scriptPattern.matcher(rlt).replaceAll("");
		}
		
		return rlt;
	}

	/**
	 * 过滤SQL注入内容
	 * @param value
	 * 				待处理内容
	 * @return
	 */
	public static String stripSqlInjection(String value) {
		return (null == value) ? null : value.replaceAll("('.+--)|(--)|(%7C)", ""); //value.replaceAll("('.+--)|(--)|(\\|)|(%7C)", "");
	}

	/**
	 * 过滤SQL/XSS注入内容
	 * @param value
	 * 				待处理内容
	 * @return
	 */
	public static String stripSqlXSS(String value) {
		return stripXSS(stripSqlInjection(value));
	}

}
