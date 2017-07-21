package java.wechat.service.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.wechat.service.exception.JsonConvertFailedException;
import java.wechat.service.exception.UnsupportedURLTypeException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.alibaba.fastjson.JSON;

@SuppressWarnings("all")
public class HttpUtils {
	private static Logger logger = LogManager.getLogger(HttpUtils.class);
	
    public static class Parameter {
        public Parameter(String key, String value) {
            this.key = key;
            this.value = value;
        }

        private final String key;
        private final String value;

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    private static void writeParameterOutputStream(OutputStream os, List<Parameter> parameters) throws IOException {
        if (parameters != null && parameters.size() > 0) {
            boolean first = true;
            for (Parameter parameter : parameters) {
                try {
                    if (first) {
                        first = false;
                    } else {
                        os.write('&');
                    }
                    os.write(URLEncoder.encode(parameter.key, "UTF-8").getBytes());
                    os.write('=');
                    os.write(URLEncoder.encode(parameter.value, "UTF-8").getBytes());
                } catch (java.io.UnsupportedEncodingException e) {
                    logger.error("Unsupported encoding:UTF-8");
                }
            }
        }
    }

    public static String getRawData(String method, String url, List<Parameter> parameters, int timeout) throws JsonConvertFailedException, UnsupportedURLTypeException {
        URL urlUrl = null;
		String cacheKey = null;
        method = method.toUpperCase();
        if ("GET".equals(method)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                writeParameterOutputStream(baos, parameters);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (baos.size() > 0) {
                String extParamStr = new String(baos.toByteArray());
                if (url.contains("?")) {
                    url += "&";
                } else {
                    url += "?";
                }
                url += extParamStr;
            }
            //当方法为GET且满足url前缀条件时查询缓存
//			if(url.startsWith(WebUtil.getGameInfoUrl())){
//				cacheKey = md5(url);
//				MemcachedClient mc = WebUtil.getMemcachedClient();
//				if(mc!=null){
//					String cacheData = (String) mc.get(cacheKey);
//					if(cacheData!=null){
//						return cacheData;
//					}
//				}
//			}
        }
        try {
            urlUrl = new URL(url);
        } catch (MalformedURLException e) {
            throw new UnsupportedURLTypeException("URL 转换出错");
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) urlUrl.openConnection();
            try {
                conn.setRequestMethod(method);
            } catch (ProtocolException e) {
                logger.error("UNSUPPORT POST METHOD:" + method);
                return null;
            }
            conn.setUseCaches(false);
            conn.setReadTimeout(timeout);
            conn.setDoInput(true);
            if ("POST".equals(method)) {
                if (parameters != null && parameters.size() > 0) {
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                }
                conn.connect();
                if (parameters != null && parameters.size() > 0) {
                    OutputStream os = conn.getOutputStream();
                    writeParameterOutputStream(os, parameters);
                    os.flush();
                }
            }
            int status = conn.getResponseCode();
            if (status != 200) {
                String respMsg = conn.getResponseMessage();
                logger.error("status:" + status + ",respMsg:" + respMsg);
                return null;
            }
            InputStream is = conn.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int available = 0;
            while ((available = is.read(buffer)) != -1) {
                baos.write(buffer, 0, available);
            }
            String retStr;
            try {
                retStr = new String(baos.toByteArray(), "UTF-8");
            } catch (java.io.UnsupportedEncodingException e) {
                logger.error("Unsupported encoding:UTF-8");
                return null;
            }
            logger.debug(retStr);
//			if(cacheKey!=null){
//				MemcachedClient mc = WebUtil.getMemcachedClient();
//				if(mc!=null){
//					mc.set(cacheKey, 1800, retStr);//缓存1800秒
//				}
//			}
            return retStr;
        } catch (IOException e) {
            logger.error("IOExcpeiton!", e);
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static Map<String, Object> getData(String method, String url, List<Parameter> parameters, int timeout) throws JsonConvertFailedException, UnsupportedURLTypeException {
        String paramStr = JSON.toJSONString(parameters);
        logger.info("-----------------------url:" + url + ",param:" + paramStr + "-----------------------重新向服务器请求数据！");
        String ret = getRawData(method, url, parameters, timeout);
        return JSON.parseObject(ret, Map.class);
    }

    public static Map<String, Object> getDataPOST(String url, List<Parameter> parameters, int timeout) throws JsonConvertFailedException, UnsupportedURLTypeException {
        return getData("POST", url, parameters, timeout);
    }

    public static Map<String, Object> getDataGET(String url, List<Parameter> parameters, int timeout) throws JsonConvertFailedException, UnsupportedURLTypeException {
        return getData("GET", url, parameters, timeout);
    }
	
}
