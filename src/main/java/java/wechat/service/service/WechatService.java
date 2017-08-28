package java.wechat.service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.wechat.service.cache.TimerCache;
import java.wechat.service.exception.AesException;
import java.wechat.service.util.Dictionary;
import java.wechat.service.util.HttpUtils;
import java.wechat.service.util.HttpUtils.Parameter;
import java.wechat.service.util.SecurityWechatUtil;
import java.wechat.service.util.XMLUtil;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@SuppressWarnings("all")
public class WechatService {
	private static final Logger logger = LogManager.getLogger(WechatService.class);
	
    private static TimerCache<String, String> accessTokenCache = new TimerCache<String, String>(1, 1000*60*60*2l);
    private static TimerCache<String, String> ticketCache = new TimerCache<String, String>(1, 1000*60*60*2l);
	
    public static String getToken(){
    	return Dictionary.token;
    }
    
	/**
	 * 返回accesstoken   缓存时间为2小时
	 * @author lichenyi
	 * @return
	 * 2017年1月20日 下午4:06:12
	 */
	public static String getAccessToken(){
		try {
			if(accessTokenCache != null && accessTokenCache.get("access_token") != null){
				return accessTokenCache.get("access_token");
			}
			List<Parameter> parameters = new ArrayList<Parameter>();
			parameters.add(new Parameter("grant_type", "client_credential"));//获取access_token必填
			parameters.add(new Parameter("appid", Dictionary.appid));
			parameters.add(new Parameter("secret", Dictionary.secret));
			Map<String, Object> resultMap = HttpUtils.getDataGET(Dictionary.GET_WX_TOKEN_URL, parameters, 30000);
			if(resultMap != null){
				String access_token = resultMap.get("access_token").toString();
				accessTokenCache.put("access_token", access_token);
				return access_token;
			}
		} catch (Exception e) {
			logger.error("getAccessToken failed, caused by:" + e);
		}
		return null;
	}
	
	public static String getTicket() {
		try {
			if(ticketCache != null && ticketCache.get("access_token") != null){
				return accessTokenCache.get("access_token");
			}else {
				List<Parameter> params = new ArrayList<HttpUtils.Parameter>();
				params.add(new Parameter("access_token", getAccessToken()));
				params.add(new Parameter("type", "jsapi"));
				Map<String, Object> resultMap = HttpUtils.getDataGET(Dictionary.GET_WX_TICKET_URL, params, 30000);
				if(resultMap != null){
					logger.info(String.format(" getTicket -->%s ", resultMap));
					if("0".equals(resultMap.get("errcode").toString()) && resultMap.get("ticket") != null){
						return resultMap.get("ticket").toString();
					}
				}else {
					return "ticket return null";
				}
			}
		} catch (Exception e) {
			logger.error("getTicket failed, caused by:" + e);
		}
		return null;
	}

	/**
	 * 获取签名
	 * @param
	 * @return
	 * @author lichenyi
	 * @date 2017-8-28 0028 13:02
	 */
	public static String getSignature(String timestamp, String nonce, String encrypt) throws AesException {
		return SecurityWechatUtil.getSHA1(Dictionary.token, timestamp, nonce, encrypt);
	}

	/**
	 * 检验签名
	 * @param
	 * @return
	 * @author lichenyi
	 * @date 2017-8-28 0028 13:02
	 */
	public static boolean checkSignature(String oldSingature, String newSignature){
		if(oldSingature.equals(oldSingature)){
			return true;
		}
		return false;
	}

	/**
	 * 获取设备及用户信息
	 * 获取设备信息，包括UUID、major、minor，以及距离、openID等信息。
	 * @param 
	 * @return 
	 * @author lichenyi
	 * @date 2017-8-28 0028 13:03
	 */
	public static Map<String, Object> getShakeInfo(){
		String url = String.format(Dictionary.SHAKE_INFO_URL, getToken(), getTicket());
		List<Parameter> parameters = new ArrayList<>();
		return HttpUtils.getDataPOST(url, parameters, 30000);
	}

	/**
	 * 获取用户信息
	 * @param
	 * @return
	 * @author lichenyi
	 * @date 2017-8-28 0028 13:09
	 */
	public static Map<String, Object> getUserInfo(){
		Map<String, Object> shakeInfo = getShakeInfo();
		Map<String, Object> shakeInfoData = (Map<String, Object>) shakeInfo.get("data");
		String openid = shakeInfoData.get("openid").toString();

		String url = String.format(Dictionary.USER_INFO_URL, getToken(), openid);
		List<Parameter> parameters = new ArrayList<>();
		return HttpUtils.getDataPOST(url, parameters, 30000);
	}

	public static String getResult(String xmlStr, String openid) throws AesException{
		String type = XMLUtil.getMsgType(xmlStr);
		String replyMsg = XMLUtil.sendTextXML(openid, "gh_bd4baec5e8a7", "this is "+type);
		return replyMsg;
	}
	
}
