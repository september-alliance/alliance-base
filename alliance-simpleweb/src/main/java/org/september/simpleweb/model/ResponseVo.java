package org.september.simpleweb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResponseVo<T> implements Serializable{

	/** 业务处理结果 **/
    public interface BUSINESS_CODE {
        /** 成功 **/
        int SUCCESS = 0;

        /** 失败 **/
        int FAILED = -1;
    }
    
    /** 业务处理结果 **/
    public interface Error_Type {
        /** 参数格式错误 **/
        String Args_Not_Valid = "args_not_valid";

        /** 其他业务异常 **/
        String Business_Exception = "biz_exception";
        
        /** 其他业务异常 **/
        String UnExpect_Exception = "unexpect_exception";
    }
    
    /** 
     */
    private static final long serialVersionUID = -3819569459544701549L;

    /** 业务处理结果, 0成功 -1失败处理失败，默认成功 **/
    private Integer code;

    /** 提示信息 **/
    private String desc;

    /** 失败时具体的错误代码 **/
    private String errorType;
    
    /** 返回值 **/
    private T data;
    
    private List<ArgError> argErrors = new ArrayList<>();
    
    private ResponseVo() {
        // 默认业务处理成功
        this.code = BUSINESS_CODE.SUCCESS;
    }

    public Integer getCode() {
        return code;
    }

    public ResponseVo<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public ResponseVo<T> setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResponseVo<T> setData(T data) {
        this.data = data;
        return this;
    }

    public String getErrorType() {
		return errorType;
	}

	public ResponseVo<T> setErrorType(String errorType) {
		this.errorType = errorType;
		return this;
	}

	public List<ArgError> getArgErrors() {
		return argErrors;
	}

	public void setArgErrors(List<ArgError> argErrors) {
		this.argErrors = argErrors;
	}

	/**
     * 简化使用
     * @param <E>
     * @return
     */
    public static <E> ResponseVo<E> BUILDER() {
        return new ResponseVo<E>();
    }
}
