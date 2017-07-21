package java.wechat.service.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.wechat.service.exception.BaseException;
import java.wechat.service.service.WechatService;
import java.wechat.service.servlet.base.BaseServlet;


@WebServlet(urlPatterns={"/GetAccessTokenServlet"})
public class GetAccessTokenServlet extends BaseServlet<String> {

	private static final long serialVersionUID = 1L;

	protected String processGet(HttpServletRequest request, HttpServletResponse response) throws BaseException {
		return WechatService.getAccessToken();
	}
		
}
