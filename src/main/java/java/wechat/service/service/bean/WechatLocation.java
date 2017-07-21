package java.wechat.service.service.bean;


import java.wechat.service.service.bean.base.WechatBase;

/**
 * 地理位置
 * @author lichenyi
 *
 * 2017年1月24日 上午10:11:55
 */
public class WechatLocation extends WechatBase {

	private String Location_X;
	private String Location_Y;
	private String Scale;
	private String Label;
	public String getLocation_X() {
		return Location_X;
	}
	public String getLocation_Y() {
		return Location_Y;
	}
	public String getScale() {
		return Scale;
	}
	public String getLabel() {
		return Label;
	}
	public void setLocation_X(String location_X) {
		Location_X = location_X;
	}
	public void setLocation_Y(String location_Y) {
		Location_Y = location_Y;
	}
	public void setScale(String scale) {
		Scale = scale;
	}
	public void setLabel(String label) {
		Label = label;
	}
	
}
