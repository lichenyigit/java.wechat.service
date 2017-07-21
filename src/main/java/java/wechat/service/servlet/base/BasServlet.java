package java.wechat.service.servlet.base;

import java.io.IOException;
import java.wechat.service.exception.*;
import java.wechat.service.util.CommonUtils;
import java.wechat.service.util.JsonUtil;
import java.wechat.service.util.Result;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



@SuppressWarnings("all")
public class BasServlet<T> extends HttpServlet {

	private static final long serialVersionUID = -2766701061704928482L;

	private Logger logger;
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger = LogManager.getLogger(this.getClass());
		super.service(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		try {
			parseRequest(request);
		} catch (RequestToMapException e1) {
			e1.printStackTrace();
		}
		/*Result<Object> result = new Result<Object>();
		try{
			result.setResult(processPost(request, response));
		}catch(DatabaseException e){
			result = new Result<Object>(e);
			logger.error(e.getMessage(), e);
		}catch(BaseException e){
			result = new Result<Object>(e);
		}catch(Throwable e){
			result = new Result<Object>(new UndefinedCommonException(e));
			logger.error(e.getMessage(), e);
		}
		returnJson(result, response);*/
		try {
			String responseStr = processPost(request, response);
			logger.info(String.format(" POST result --> 【%s】", responseStr));
			response.getWriter().print(responseStr);
		} catch (BaseException e) {
			e.printStackTrace();
			try {
				throw new ReponsePrintException();
			} catch (ReponsePrintException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		try {
			parseRequest(request);
		} catch (RequestToMapException e1) {
			e1.printStackTrace();
		}
		Result<Object> result = new Result<Object>();
		try{
			result.setResult(processGet(request, response));
		}catch(DatabaseException e){
			result = new Result<Object>(e);
			logger.error(e.getMessage(), e);
		}catch(BaseException e){
			result = new Result<Object>(e);
		}catch(Throwable e){
			result = new Result<Object>(new UndefinedCommonException(e));
			logger.error(e.getMessage(), e);
		}
		returnJson(result, response);
	}
	
    /**
     * 
     * @author lichenyi
     * @param val
     * @param response
     * @param sign=true 对返回结果进行封装，sign=false返回结果不再对结果进行封装
     * 2017年1月23日 下午12:33:36
     */
    public void returnJson(Object val, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        String json = null;
        
        try {
    		Result<Object> obj = (Result<Object>) val;
    		json = JsonUtil.writeValue(obj.getResult()).replaceAll("\"", "");
            logger.trace("responseMessage:" + json);
        } catch (JsonConvertFailedException e) {
            logger.error("JSON转换失败(toJson error)", e);
            try {
                json = JsonUtil.writeValue(new Result<T>());
            } catch (JsonConvertFailedException e1) {
                logger.fatal("Convert empty Result 2 json failed!", e1);
                e1.printStackTrace();
            }
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        logger.info(String.format(" result --> 【%s】", json));
        try {
            response.getWriter().write(json);
        } catch (IOException e) {
            logger.error("Get response writer failed!", e);
        }
    }
    
    private void parseRequest(HttpServletRequest request) throws RequestToMapException{
    	logger.info("\n------------------------------------------------------------------------------------------" +
                "\n\t      client IP : " + request.getRemoteAddr() +
                "\n\t   content type : " + request.getMethod() +
                "\n\t    request url : " + request.getRequestURL() +
                "\n\t     parameters : " + String.format("【%s】", CommonUtils.resquestParameter2Map(request)) +
                "\n------------------------------------------------------------------------------------------");
    }
    
    protected T processGet(HttpServletRequest request, HttpServletResponse response) throws BaseException {
		return null;
	}
    
    protected String processPost(HttpServletRequest request, HttpServletResponse response) throws BaseException {
		return null;
	}
    
}
