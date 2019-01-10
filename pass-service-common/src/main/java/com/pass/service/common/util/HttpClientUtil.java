package com.pass.service.common.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class HttpClientUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final int TIMEOUT = 60000;
    private static final int SO_TIMEOUT = 60000;
    /**
	 * 默认编码
	 */
	private static final String CHARSET = "UTF-8";
	private static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    public static String postJsonMethod(String url, String params) throws Exception {
		 BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(
	                            RegistryBuilder.<ConnectionSocketFactory>create()
	                                    .register("http",
	                                            PlainConnectionSocketFactory.getSocketFactory())
	                                    .register("https",
	                                            SSLConnectionSocketFactory.getSocketFactory())
	                                    .build(),
	                            null, null, null);
	        

	        HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connManager).build();

	        HttpPost httpPost = new HttpPost(url);

	    	RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(TIMEOUT).setSocketTimeout(SO_TIMEOUT).build();
	        httpPost.setConfig(requestConfig);

	        StringEntity postEntity = new StringEntity(params, CHARSET);
	        httpPost.addHeader("Content-Type", "application/json");
	      
	        httpPost.setEntity(postEntity);

	        HttpResponse httpResponse = httpClient.execute(httpPost);
	        HttpEntity httpEntity = httpResponse.getEntity();
	        return EntityUtils.toString(httpEntity, "UTF-8");
	}    
    
    public static String postMethod(String url, Map<String, String> params, String charset)
            throws Exception {
        if (StringUtils.isEmpty(url)) {
            LOGGER.warn("the request url is empty!");
            return "";
        }
        RequestConfig config = RequestConfig.custom().setConnectTimeout(TIMEOUT)
                .setSocketTimeout(SO_TIMEOUT).build();
        CloseableHttpClient httpClient =
                HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        HttpPost httpPost = null;
        String result = null;
        CloseableHttpResponse response = null;
        try {
            httpPost = new HttpPost(url);

            List<NameValuePair> pairs = null;
            if (params != null && !params.isEmpty()) {
                pairs = new ArrayList<NameValuePair>(params.size());
                for (String key : params.keySet()) {
                    pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
                }
            }

            if (pairs != null && pairs.size() > 0) {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, charset));
            }

            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                    LOGGER.debug("post request to {} and params is {}\n",
                        url, params);
//                    LOGGER.debug("post request to {} and params is {} ,server's response \r\n{}",
//                            url, params, result);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Get request error", e);
            throw e;
        } finally {
            response.close();
            httpClient.close();
        }
        return result;
    }
    
    public static int postMethodForQrcode(String url, Map<String, Object> params, String filePath, String fileName, String charset)
            throws Exception {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(TIMEOUT)
                .setSocketTimeout(SO_TIMEOUT).build();
        CloseableHttpClient httpClient =
                HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        HttpPost httpPost = null;
        int state = 1;
        CloseableHttpResponse response = null;
        try {
            httpPost = new HttpPost(url);

            httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
            String body = JSON.toJSONString(params);           //必须是json模式的 post      
            StringEntity entity = new StringEntity(body);
            entity.setContentType("image/png");

            httpPost.setEntity(entity);

            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    state = FileUtil.saveToImgByInputStream(resEntity.getContent(), filePath, fileName);
                    LOGGER.debug("post request to {} and params is {}\n",
                        url, params);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Get request error", e);
            throw e;
        } finally {
            response.close();
            httpClient.close();
        }
        return state;
    }

    public static String postMethod(String url, Map<String, String> params) throws Exception {
        return postMethod(url, params, "UTF-8");
    }
    
    public static String postMethodForQrcode(String url, Map<String, Object> params, String filePath, String fileName) throws Exception {
        if (postMethodForQrcode(url, params, filePath, fileName, "UTF-8") > 0) {
            return fileName;
        } else {
            return null;
        }
    }

    public static String getMethod(String url, Map<String, String> params, String charset)
            throws Exception {
        if (StringUtils.isEmpty(url)) {
            LOGGER.warn("the request url is empty!");
            return "";
        }
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpClient httpClient = (CloseableHttpClient) wrapClient(client);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout((int) SO_TIMEOUT)
                .setConnectTimeout((int) TIMEOUT).build();// 设置请求和传输超时时
        CloseableHttpResponse response = null;
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for (String key : params.keySet()) {
                    pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
            LOGGER.info("getMethod url is {}", url);
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpGet.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, "utf-8");
                /*LOGGER.debug("get request to {} and params is {} ,server's response \r\n{}", url,
                        params, result);*/
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            LOGGER.error("Get request error", e);
            throw e;
        } finally {
            response.close();
            httpClient.close();
        }
    }

    public static String getMethod(String url, Map<String, String> params) throws Exception {

        return getMethod(url, params, "UTF-8");
    }

    /**
    *
    * @param strUrl 请求地址
    * @param params 请求参数
    * @param method 请求方法
    * @return  网络请求字符串
    * @throws Exception
    */
   public static String net(String strUrl, Map params,String method) throws Exception {
       HttpURLConnection conn = null;
       BufferedReader reader = null;
       String rs = null;
       try {
           StringBuffer sb = new StringBuffer();
           if(method==null || method.equals("GET")){
               strUrl = strUrl+"?"+urlencode(params);
           }
           URL url = new URL(strUrl);
           conn = (HttpURLConnection) url.openConnection();
           LOGGER.info("http url is:" + url);
           if(method==null || method.equals("GET")){
               conn.setRequestMethod("GET");
           }else{
               conn.setRequestMethod("POST");
               conn.setDoOutput(true);
           }
           conn.setRequestProperty("User-agent", userAgent);
           conn.setUseCaches(false);
           conn.setConnectTimeout(TIMEOUT);
           conn.setReadTimeout(SO_TIMEOUT);
           conn.setInstanceFollowRedirects(false);
           conn.connect();
           if (params!= null && method.equals("POST")) {
               try {
                   DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                       out.writeBytes(urlencode(params));
               } catch (Exception e) {
                   throw e;
               }
           }
           InputStream is = conn.getInputStream();
           reader = new BufferedReader(new InputStreamReader(is, CHARSET));
           String strRead = null;
           while ((strRead = reader.readLine()) != null) {
               sb.append(strRead);
           }
           rs = sb.toString();
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           if (reader != null) {
               reader.close();
           }
           if (conn != null) {
               conn.disconnect();
           }
       }
       return rs;
   }

   //将map型转为请求参数型
   public static String urlencode(Map<String,Object>data) {
       StringBuilder sb = new StringBuilder();
       for (Map.Entry i : data.entrySet()) {
           try {
               sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
           } catch (UnsupportedEncodingException e) {
               e.printStackTrace();
           }
       }
       return sb.toString();
       //return sb.substring(0, sb.length() -2);
   }
   
    /**
     * 避免HttpClient的”SSLPeerUnverifiedException: peer not authenticated”异常 不用导入SSL证书
     * 
     * @param base
     * @return
     */
    public static HttpClient wrapClient(HttpClient base) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {}

                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {}
            };
            ctx.init(null, new TrustManager[] {tm}, null);
            SSLConnectionSocketFactory ssf =
                    new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(ssf).build();
            return httpclient;
        } catch (Exception ex) {
            ex.printStackTrace();
            return HttpClients.createDefault();
        }
    }
    
    
    public static String postFormDataMethod(String url, Map<String, String> params,String cookie) throws Exception {
		 BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(
	                            RegistryBuilder.<ConnectionSocketFactory>create()
	                                    .register("http",
	                                            PlainConnectionSocketFactory.getSocketFactory())
	                                    .register("https",
	                                            SSLConnectionSocketFactory.getSocketFactory())
	                                    .build(),
	                            null, null, null);
	        

	        HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connManager).build();

	        HttpPost httpPost = new HttpPost(url);

	    	RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(TIMEOUT).setSocketTimeout(SO_TIMEOUT).build();
	        httpPost.setConfig(requestConfig);

	        
	        List<NameValuePair> paramList = new ArrayList <NameValuePair>();  
	        if(params != null && params.size() > 0){
	            Set<String> keySet = params.keySet();  
	            for(String key : keySet) {  
	                paramList.add(new BasicNameValuePair(key, params.get(key)));  
	            }  
	        }
	        try {  
	        	httpPost.setEntity(new UrlEncodedFormEntity(paramList, CHARSET));  
	        } catch (UnsupportedEncodingException e1) {  
	            e1.printStackTrace();  
	        }  
	      
	        if(StringUtils.isNotBlank(cookie)){
	        	httpPost.setHeader("Cookie", cookie);
	        }

	        HttpResponse httpResponse = httpClient.execute(httpPost);
	        HttpEntity httpEntity = httpResponse.getEntity();
	        return EntityUtils.toString(httpEntity, "UTF-8");
	}    
}
