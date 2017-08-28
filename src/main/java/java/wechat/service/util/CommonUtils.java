package java.wechat.service.util;

import com.sun.tracing.dtrace.ArgsAttributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.wechat.service.exception.ReceiveWXMsgFailedException;
import java.wechat.service.exception.RequestToMapException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CommonUtils {

	// 创建Map
	public static Map<String, Object> createMap(String key, Object value) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if (value == null) {
			value = "";
		}
		map.put(key, CommonUtils.setString(value.toString()));
		return map;
	}

	public static Map<String, Object> generateMap(Map<String, Object> map,
			String key, Object value) {
		if (value == null) {
			value = "";
		}
		map.put(key, value);
		return map;
	}

	public static String setString(String str) {
		return setString(str, "");
	}

	public static String setString(String str, String defaultStr) {
		if (isNotBlank(str)) {
			return str;
		} else {
			return defaultStr;
		}
	}

	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}
	
	public static boolean isNull(String str){
		if(str == null)
			return true;
		return false;
	}
	
	public static boolean isNotNull(String str){
		return !isNull(str);
	}

	public static Map<String, Object> resquestParameter2Map(HttpServletRequest request) throws RequestToMapException {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String[]> url = request.getParameterMap();
		for (Entry<String, String[]> entry : url.entrySet()) {
			String key = entry.getKey();
			String[] valueArray = entry.getValue();
			String value;
			value = new String(valueArray[0]);
			map.put(key, value);
		}
		return map;
	}

	public static Long getLongFromString(String str){
		if(CommonUtils.isBlank(str)){
			return null;
		}
		try {
			return Long.valueOf(Long.parseLong(str));
		} catch (Exception e) {
		}
		return null;
	}
	
	public static void responseCORS(HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		response.setCharacterEncoding("UTF-8");
		//response.setContentType("application/json");
	}
	
	public static String getWechatMessage(HttpServletRequest request) throws ReceiveWXMsgFailedException {
		try {
			InputStream iStream = request.getInputStream();
			BufferedReader bufferedReader = null;
			StringBuffer stringBuilder = new StringBuffer();
			
			/*byte[] b = new byte[128];
			while(iStream.read(b) > -1){
				str.append(new String(b));
			}
			return str.toString().trim();*/
			
			 if (iStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(iStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
			return stringBuilder.toString();
		} catch (IOException e) {
			throw new ReceiveWXMsgFailedException();
		}
	}

}
