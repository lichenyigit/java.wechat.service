package java.wechat.service.util;

public class Dictionary {

	/**
	 * 文字
	 */
	public static final String WECHATTEXT = "text";
	/**
	 * 图片
	 */
	public static final String WECHATIMAGE = "image";
	/**
	 * 链接
	 */
	public static final String WECHATLINK = "link";
	/**
	 * 地理位置
	 */
	public static final String WECHATLOCATION = "location";
	/**
	 * 小视频
	 */
	public static final String WECHATSHORTVIDEO = "shortvideo";
	/**
	 * 视频
	 */
	public static final String WECHATVIDEO = "video";
	/**
	 * 语音
	 */
	public static final String WECHATVOICE = "voice";
	
	
	public static final String appid = "wxf69c8f41c5588098";	//1097704842@qq.com
	public static final String secret = "8da8210528b3fc6e96004111441c88c0";
	public static final String token = "xiongdaojia";
	public static final String encodingAesKey = "z6uPrr97yiWXzEJ1yzAzbtAXib7G6Qfq6a2ScyalkDv";
	public static final String nonce = "lichenyi";
//	public static final String openId = "oGo_1vk5XJpJ8gC97NT9W4aPTtwA";
	public static final String GET_WX_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";    // 微信公众号获取token地址
    public static final String GET_WX_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";    // 微信公众号获取ticket地址
    public static final String SEND_WX_MSG = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry";

	//获取设备及用户信息
	public static final String SHAKE_INFO_URL = "https://api.weixin.qq.com/shakearound/user/getshakeinfo?access_token=%s&ticket=%s&need_poi";

    //获取用户基本信息(UnionID机制)
	public static final String USER_INFO_URL = "GET https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN ";



}
