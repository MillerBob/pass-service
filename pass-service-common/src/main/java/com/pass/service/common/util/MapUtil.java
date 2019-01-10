package com.pass.service.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

public class MapUtil {
	
	public static String getStringValue(Map map, String key) {
		if(map == null) return null;
		
		Object value = map.get(key);
		if (value == null) {
			return null;
		}
		if (value instanceof String) {
			return StringUtils.trimToEmpty((String) value);
		/*} else if (value instanceof CLOB) {
			return oracleClob2Str((CLOB) value);*/
		} else if (value instanceof JSONObject) {
			return ((JSONObject)value).getString("value");
		} else {
			return value.toString();
		}
	}
	
	public static Double getDoubleValue(Map map, String key) {
        if (map == null) {
            return null;
        }

        Object value = map.get(key);
        if (value == null || value == "") {
            return null;
        } else {
            return Double.parseDouble(value + "");
        }

    }
	
	public static int getIntValue(Map map, String key) {
		if(map == null) return 0;
		
		Object value = map.get(key);
		if (value == null) {
			return 0;
		}
		if (value instanceof BigDecimal) {
			return ((BigDecimal) value).intValue();
		}else if(value instanceof Long){
			return ((Long)value).intValue();
		}else if(value instanceof Short){
			return ((Short)value).intValue(); 
		}else if(value instanceof Integer){
			return ((Integer)value).intValue();
		}else if(value instanceof Double){
			return ((Double)value).intValue();
		}else if(value instanceof String){
			if(StringUtils.isBlank(value+"")) {
				return 0;
			}else {
				try {
					return Integer.parseInt(value+"");
				}catch(Exception e) {
					throw new RuntimeException("无法将key【"+key+"】,value【"+value+"】转换为Integer类型！");
				}
			}
		} else {
			throw new RuntimeException("无法将key【"+key+"】,value【"+value+"】转换为Integer类型！");
		}
	}
	public static Long getLongValue(Map map, String key) {
		if(map == null) return 0l;
		
		Object value = map.get(key);
		if (value == null) {
			return 0l;
		}
		if (value instanceof BigDecimal) {
			return ((BigDecimal) value).longValue();
		}else if(value instanceof Long){
			return (Long)value;
		}else if(value instanceof Short){
			return ((Short)value).longValue(); 
		}else if(value instanceof Integer){
			return ((Integer)value).longValue();
		}else if(value instanceof String){
			if(StringUtils.isBlank(value+"")) {
				return 0l;
			}else {
				try {
					return Long.parseLong(value+"");
				}catch(Exception e) {
					throw new RuntimeException("无法将key【"+key+"】,value【"+value+"】转换为Long类型！");
				}
			}
		} else {
			throw new RuntimeException("无法将key【"+key+"】,value【"+value+"】转换为Long类型！");
		}
	}
	
	public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {    
        if (map == null)   
            return null;    
  
        Object obj = beanClass.newInstance();  
  
        Field[] fields = obj.getClass().getDeclaredFields();   
        for (Field field : fields) {    
            int mod = field.getModifiers();    
            if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){    
                continue;    
            }    
  
            field.setAccessible(true);    
            field.set(obj, map.get(field.getName()));   
        }   
  
        return obj;  
    }  

}
