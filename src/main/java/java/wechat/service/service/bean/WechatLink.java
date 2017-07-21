package java.wechat.service.service.bean;


import java.wechat.service.service.bean.base.WechatBase;

/**
 * 链接消息
 * @author lichenyi
 *
 * 2017年1月24日 上午10:11:48
 */
public class WechatLink extends WechatBase {

	private String Title;
	private String Description;
	private String Url;
	public String getTitle() {
		return Title;
	}
	public String getDescription() {
		return Description;
	}
	public String getUrl() {
		return Url;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public void setUrl(String url) {
		Url = url;
	}
	
}
