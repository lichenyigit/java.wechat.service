package java.wechat.service.service.bean;


import java.wechat.service.service.bean.base.WechatBase;

/**
 * 图文消息
 * @author lichenyi
 *
 * 2017年1月24日 上午10:11:34
 */
public class WechatImage extends WechatBase {

	private String PicUrl;
	private String MediaId;
	public String getPicUrl() {
		return PicUrl;
	}
	public String getMediaId() {
		return MediaId;
	}
	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}
	
}
