package java.wechat.service.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.wechat.service.exception.BaseException;
import java.wechat.service.service.WechatService;
import java.wechat.service.servlet.base.BaseServlet;

@WebServlet(urlPatterns={"/GetUserInfoServlet"})
public class GetUserInfoServlet extends BaseServlet<Map<String, Object>> {

	private static final long serialVersionUID = 1L;

	protected Map<String, Object> processGet(HttpServletRequest request, HttpServletResponse response) throws BaseException {
		return WechatService.getUserInfo();
	}

	protected Map<String, Object> processPost(HttpServletRequest request, HttpServletResponse response) throws BaseException {
		return WechatService.getUserInfo();
	}

}
