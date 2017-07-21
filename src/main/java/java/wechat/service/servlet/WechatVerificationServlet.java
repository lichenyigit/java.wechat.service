package java.wechat.service.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.wechat.service.exception.BaseException;
import java.wechat.service.service.WechatService;
import java.wechat.service.servlet.base.BasServlet;
import java.wechat.service.util.CommonUtils;
import java.wechat.service.util.ConvertUtil;
import java.wechat.service.util.StringUtil;


@SuppressWarnings("all")
@WebServlet(urlPatterns={"/wx"})
public class WechatVerificationServlet extends BasServlet<String> {
	private static final Logger logger = LogManager.getLogger(WechatVerificationServlet.class);
	
	private static final long serialVersionUID = 1L;

	protected String processGet(HttpServletRequest request, HttpServletResponse response) throws BaseException {
		String signature = ConvertUtil.getNonEmptyStringFromRequestParam(request, "signature");
		String timestamp = ConvertUtil.getNonEmptyStringFromRequestParam(request, "timestamp");
		String nonce = ConvertUtil.getNonEmptyStringFromRequestParam(request, "nonce");
		String echostr = ConvertUtil.getNonEmptyStringFromRequestParam(request, "echostr");
		String signString = WechatService.getSignature(timestamp, nonce, "");
		if(WechatService.checkSignature(signature, signString)){
			return echostr;
		}
		return "签名校验失败";
	}
	
	protected String processPost(HttpServletRequest request, HttpServletResponse response) throws BaseException {
		String signature = ConvertUtil.getNonEmptyStringFromRequestParam(request, "signature");
		String timestamp = ConvertUtil.getNonEmptyStringFromRequestParam(request, "timestamp");
		String nonce = ConvertUtil.getNonEmptyStringFromRequestParam(request, "nonce");
		String openid = ConvertUtil.getNonEmptyStringFromRequestParam(request, "openid");
		
		String wxMessage = CommonUtils.getWechatMessage(request);
		String result = WechatService.getResult(wxMessage, openid);
		if(StringUtil.isNotBlank(result)){
			return result;
		}
		return "success";
	}
		
}
