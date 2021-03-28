package tech.hooo.tools.t5planner.configs;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import tech.hooo.tools.t5planner.models.ApiResult;
import tech.hooo.tools.t5planner.models.BizException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/*
 * 此类：拦截controller返回的数据，并做修改
 * */
@Slf4j
@ResponseBody
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Resource
    private ObjectMapper jsonMapper;

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        //返回true 表示拦截 Controller 所有 API 接口的返回结果。
//        String methodName = methodParameter.getMethod().getName();
        String className = methodParameter.getMethod().getDeclaringClass().getName();

        if (className.startsWith("springfox.documentation")) {
            return false;
        }

        return true;
    }

    @Override
    public Object beforeBodyWrite(Object object, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        //对返回的结果进行处理
        if (object instanceof ApiResult) {
            return object;
        } else if (object instanceof String) {
            try {
                return jsonMapper.writeValueAsString(ApiResult.success(object));
            } catch (JsonProcessingException e) {
                log.error("", e);
                return "{'code':290,'message':'输出数据出现异常','data':null}";//ApiResult.failure("输出数据出现异常");
            }
        } else {
            return ApiResult.success(object);
        }
    }

    @ExceptionHandler(value = Exception.class)
    public ApiResult<String> exceptionHandler(HttpServletRequest request, Exception e) {
        log.error("111", e);
        //绑定异常是需要明确提示给用户的
        if (e instanceof BizException) {
            BizException be = (BizException) e;
            return ApiResult.failure(be.getCode(), be.getMessage());
        } else {
            return ApiResult.failure(e.getMessage());
//            return ApiResult.failure("系统繁忙, 请稍后再试...");
        }
    }
}
