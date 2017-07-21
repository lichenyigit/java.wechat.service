package java.wechat.service.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.wechat.service.exception.AesException;
import java.wechat.service.service.bean.*;
import java.wechat.service.util.wechat.WXBizMsgCrypt;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.w3c.dom.DOMException;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class XMLUtil {
	private static final Logger logger = LogManager.getLogger(XMLUtil.class);
	
	//*****************************************************************************************************************************************************
	//*********************************************** 解析xml
	//*****************************************************************************************************************************************************
	/**
	 * 将xml解析为相应的bean对象
	 * @author lichenyi
	 * @param xmlStr
	 * @return
	 * @throws AesException
	 * 2017年1月24日 下午2:18:56
	 */
	public static <T> T parseXMLToWechatBean(String xmlStr) throws AesException {
		String msgType = getMsgType(xmlStr);
		T t = getMsgTypeBean(msgType);
		
		return t;
	}

	/**
	 * 通过xml获取消息类型
	 * @author lichenyi
	 * @param xmlStr
	 * @return
	 * @throws AesException
	 * 2017年1月24日 下午12:49:28
	 */
	@SuppressWarnings("unchecked")
	public static String getMsgType(String xmlStr) throws AesException{
		try {
			xmlStr = URLDecoder.decode(xmlStr, "utf-8");
			Document document = DocumentHelper.parseText(xmlStr);
			Element root = document.getRootElement();
			List<Element> elements = root.elements();//获得根节点下的所有节点的集合
			Object[] MsgTypes = elements.stream().filter(e -> e.getName() == "MsgType").filter(e -> e.getText() != "").toArray();
			String msgType = ((Element)MsgTypes[0]).getText();
			return msgType;
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			logger.error(e1.getMessage());
			throw new AesException(-30002);
		} catch (DocumentException e1) {
			logger.error(e1.getMessage());
			throw new AesException(-30002);
		}
	}
	
	/**
	 * 通过msgType获取相应的bean
	 * @author lichenyi
	 * @param msgType
	 * @return
	 * 2017年1月24日 下午2:26:40
	 */
	@SuppressWarnings("unchecked")
	private static <T> T getMsgTypeBean(String msgType){
		T t = null;
		switch (msgType) {
			case Dictionary.WECHATIMAGE:
				t = (T) new WechatImage();
				break;
	
			case Dictionary.WECHATTEXT:
				t = (T) new WechatText();
				break;
				
			case Dictionary.WECHATLINK:
				t = (T) new WechatLink();
				break;
				
			case Dictionary.WECHATLOCATION:
				t = (T) new WechatLocation();
				break;
				
			case Dictionary.WECHATSHORTVIDEO:
				t = (T) new WechatVideo();
				break;
				
			case Dictionary.WECHATVIDEO:
				t = (T) new WechatVideo();
				break;
				
			case Dictionary.WECHATVOICE:
				t = (T) new WechatVoice();
				break;
				
			default:
				t = null;
				break;
		}
		return t;
	}
	
	//*****************************************************************************************************************************************************
	//*********************************************** 拼接xml
	//*****************************************************************************************************************************************************
	public static String sendTextXML(String toUser, String fromUser, String content){
		String xml = "<xml>"+
						"<ToUserName><![CDATA[%s]]></ToUserName>"+
						"<FromUserName><![CDATA[%s]]></FromUserName>"+
						"<CreateTime>"+System.currentTimeMillis()/1000+"</CreateTime>"+
						"<MsgType><![CDATA[%s]]></MsgType>"+
						"<Content><![CDATA[%s]]></Content>"+
						"<FuncFlag>0</FuncFlag>"+
					"</xml>";
		String result = String.format(xml, toUser, fromUser, Dictionary.WECHATTEXT, content);
		return result;
	}
	
	public static String setEncrypt(String replyMsg, boolean sign) throws AesException{
		try {
			String encrypt = replyMsg;
			if(sign){
				WXBizMsgCrypt pc = new WXBizMsgCrypt(Dictionary.token, Dictionary.encodingAesKey, Dictionary.appid);
				String mingwen = pc.encryptMsg(replyMsg, System.currentTimeMillis()/1000+"", Dictionary.nonce);
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				StringReader sr = new StringReader(mingwen);
				InputSource is = new InputSource(sr);
				org.w3c.dom.Document document = db.parse(is);
				org.w3c.dom.Element root = document.getDocumentElement();
				NodeList nodelist1 = root.getElementsByTagName("Encrypt");
				encrypt = nodelist1.item(0).getTextContent();
			}
			String format = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";
			String fromXML = String.format(format, encrypt);
			return fromXML;
		} catch (DOMException e) {
			e.printStackTrace();
			throw new AesException(-40011);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new AesException(-40011);
		} catch (SAXException e) {
			e.printStackTrace();
			throw new AesException(-40011);
		} catch (IOException e) {
			e.printStackTrace();
			throw new AesException(-40011);
		}
	}
	
	public static void main(String[] args) {
		String xmlString = "<xml><ToUserName><![CDATA[lichenyiwx]]></ToUserName><FromUserName><![CDATA[oGo_1vk5XJpJ8gC97NT9W4aPTtwA]]></FromUserName><CreateTime>1485252161</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[this is text]]></Content></xml>";
		try {
			setEncrypt(xmlString, false);
		} catch (AesException e) {
			e.printStackTrace();
		}
	}
	
}
