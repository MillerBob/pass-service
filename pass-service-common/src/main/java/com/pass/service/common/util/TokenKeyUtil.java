package com.pass.service.common.util;

/**
 * 获取token缓存key工具
 *
 * @author liangcm
 */
public class TokenKeyUtil {
	public static final String CACHE_PREFIX ="login.token";
    public static String cacheTokenKey(String token) {
        return CACHE_PREFIX + token;
    }
}
