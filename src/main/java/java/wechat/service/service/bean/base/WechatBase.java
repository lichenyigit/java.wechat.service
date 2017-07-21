package java.wechat.service.service.bean.base;

/**
 * 微信xml基础类
 * @author lichenyi
 *
 * 2017年1月24日 上午10:00:03
 */
public class WechatBase {

	private String ToUserName;
	private String FromUserName;
	private long CreateTime;
	private String MsgType;
	private String MsgId;
	
	public String getToUserName() {
		return ToUserName;
	}
	public String getFromUserName() {
		return FromUserName;
	}
	public String getMsgType() {
		return MsgType;
	}
	public String getMsgId() {
		return MsgId;
	}
	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}
	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
	public long getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}
	
}
