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
public class BaseServlet<T> extends HttpServlet {

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
		try {
			parseRequest(request);
		} catch (RequestToMapException e1) {
			e1.printStackTrace();
		}
		Result<Object> result = new Result<Object>();
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
		returnJson(result, response);
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
	
    public void returnJson(Object val, HttpServletResponse response) {
    	response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        String json = null;
        
        try {
    		json = JsonUtil.writeValue(val);
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
    
    private void doServlet(Result<Object> result, HttpServletRequest request, HttpServletResponse response, String method) throws BaseException{
    	if("post".equals(method)){
    		result.setResult(processPost(request, response));
    	}else if("get".equals(method)){
    		result.setResult(processGet(request, response));
    	}else{
    		result.setResult(processGet(request, response));
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
    
    protected T processPost(HttpServletRequest request, HttpServletResponse response) throws BaseException {
		return null;
	}
    
}
