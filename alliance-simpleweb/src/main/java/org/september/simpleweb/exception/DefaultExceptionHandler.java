package org.september.simpleweb.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.september.core.component.log.LogHelper;
import org.september.core.exception.BusinessException;
import org.september.core.exception.NotAuthorizedException;
import org.september.simpleweb.model.ArgError;
import org.september.simpleweb.model.ResponseVo;
import org.september.simpleweb.model.ResponseVo.Error_Type;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

@ControllerAdvice
public class DefaultExceptionHandler {
	
	protected final static LogHelper logger = LogHelper.getLogger(DefaultExceptionHandler.class);
	
    /**
     * 400 - Bad Request
     */
	@ResponseBody
    @ExceptionHandler(BindException.class)
    public ResponseVo<String> handleBindException(BindException e) {
    	logger.getBuilder().debug("参数格式错误",e);
    	ResponseVo<String> resp = ResponseVo.<String>BUILDER().setCode(ResponseVo.BUSINESS_CODE.FAILED).setErrorType(ResponseVo.Error_Type.Args_Not_Valid);
        BindingResult result = e.getBindingResult();
        for(FieldError error : result.getFieldErrors()) {
        	ArgError err = new ArgError();
        	err.setField(error.getField());
        	err.setMessage(error.getDefaultMessage());
        	resp.getArgErrors().add(err);
        }
        return  resp;
    }
 
    /**
     * 400 - Bad Request
     */
	@ResponseBody
    @ExceptionHandler(ValidationException.class)
    public ResponseVo<String> handleValidationException(ValidationException e) {
    	logger.getBuilder().warn("参数验证错误",e);
    	return ResponseVo.<String>BUILDER().setCode(ResponseVo.BUSINESS_CODE.FAILED).setErrorType(Error_Type.Business_Exception).setDesc("参数验证异常");
    }
 
    /**
     * 业务层需要自己声明异常的情况
     */
	@ResponseBody
    @ExceptionHandler(BusinessException.class)
    public ResponseVo<String> handleServiceException(BusinessException e) {
    	logger.getBuilder().warn("业务系统异常",e);
    	return ResponseVo.<String>BUILDER().setCode(ResponseVo.BUSINESS_CODE.FAILED).setErrorType(Error_Type.Business_Exception).setDesc(e.getMessage());
    }
 
    /**
     * 获取其它异常。包括500
     * @param e
     * @return
     * @throws Exception
     */
   @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(Exception e , HttpServletRequest request, HttpServletResponse response , HandlerMethod handler){
	   logger.getBuilder().warn("系统内部异常",e);
	   ResponseBody anno = handler.getMethodAnnotation(ResponseBody.class);
       boolean isXhr=false;
       if(anno!=null){
           isXhr = true;
       }
	   
       if(isXhr) {
    	   ResponseVo<String> respVo = ResponseVo.<String>BUILDER().setCode(ResponseVo.BUSINESS_CODE.FAILED).setErrorType(Error_Type.UnExpect_Exception).setDesc(e.getMessage());
    	   responseAjax(response , respVo);
    	   return null;
       }else {
    	   ModelAndView modelAndView = new ModelAndView();
    	   if(e instanceof NotAuthorizedException) {
    		   modelAndView.setViewName("503");
    	   }else {
    		   modelAndView.setViewName("500");
    	   }
           modelAndView.addObject("desc", e.getMessage());
           return modelAndView;
       }
    }
   
   public static void responseAjax(HttpServletResponse response, Object responseVo) {
       try {
           String result = JSON.toJSONString(responseVo);
           response.setHeader("Content-Type", "application/json;charset=UTF-8");
           response.getOutputStream().write(result.getBytes("UTF-8"));
           response.getOutputStream().flush();
       } catch (IOException e) {
       	logger.getBuilder().error("Ajax返回值异常", e);
       } finally {
           try {
               response.getOutputStream().close();
           } catch (IOException e) {
           	logger.getBuilder().error("写流关闭失败", e);
           }
       }
   }
}
