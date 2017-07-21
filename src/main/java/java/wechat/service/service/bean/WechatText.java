package java.wechat.service.service.bean;


import java.wechat.service.service.bean.base.WechatBase;

/**
 * 文本消息
 * @author lichenyi
 *
 * 2017年1月24日 上午10:12:02
 */
public class WechatText extends WechatBase {

	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
