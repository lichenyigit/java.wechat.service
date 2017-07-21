package java.wechat.service.service.bean;

/**
 * 语音消息
 * @author lichenyi
 *
 * 2017年1月24日 上午10:12:12
 */
public class WechatVoice {

	private String MediaId;
	private String Format;
	private String Recongnition;
	public String getRecongnition() {
		return Recongnition;
	}
	public void setRecongnition(String recongnition) {
		Recongnition = recongnition;
	}
	public String getMediaId() {
		return MediaId;
	}
	public String getFormat() {
		return Format;
	}
	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}
	public void setFormat(String format) {
		Format = format;
	}
	
}
