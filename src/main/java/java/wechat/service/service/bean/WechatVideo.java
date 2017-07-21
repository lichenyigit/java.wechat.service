package java.wechat.service.service.bean;


import java.wechat.service.service.bean.base.WechatBase;

/**
 * 视频、小视频
 * @author lichenyi
 *
 * 2017年1月24日 上午10:07:26
 */
public class WechatVideo extends WechatBase {

	private String MediaId;
	private String ThumbMediaId;
	public String getMediaId() {
		return MediaId;
	}
	public String getThumbMediaId() {
		return ThumbMediaId;
	}
	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}
	public void setThumbMediaId(String thumbMediaId) {
		ThumbMediaId = thumbMediaId;
	}
	
}
