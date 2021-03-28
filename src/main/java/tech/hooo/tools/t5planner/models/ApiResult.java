package tech.hooo.tools.t5planner.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResult<T> {

    /**
     * 成功返回码
     */
    public static final int RETURNCODE_SUCCESS = 200;
    /**
     * 失败返回码
     */
    public static final int RETURNCODE_FAILED = 290;


    /**
     * 访问成功后，生成返回给客户端的对象
     *
     * @param obj 访问成功返回的数据对象
     */
    public static <T> ApiResult<T> success(T obj) {
        return create(RETURNCODE_SUCCESS, obj, null);
    }

    /**
     * 访问成功后，生成返回给客户端的对象
     *
     * @param obj     访问成功返回的数据对象
     * @param message 访问成功返回的消息
     */
    public static <T> ApiResult<T> success(T obj, String message) {
        return create(RETURNCODE_SUCCESS, obj, message);
    }

    /**
     * 访问失败后，生成返回给客户端的对象
     *
     * @param message 访问成功返回的消息
     */
    public static <T> ApiResult<T> failure(String message) {
        return create(RETURNCODE_FAILED, null, message);
    }

    /**
     * 访问失败后，生成返回给客户端的对象
     *
     * @param returnCode 访问失败代码
     * @param message    访问失败的消息
     */
    public static <T> ApiResult<T> failure(int returnCode, String message) {
        return create(returnCode, null, message);
    }

    /**
     * Session已过期，需要重新登录，生成返回给客户端的对象
     */
    public static <T> ApiResult<T> sessionExpired() {
        return sessionExpired(null);
    }

    /**
     * Session已过期，需要重新登录，生成返回给客户端的对象
     */
    public static <T> ApiResult<T> sessionExpired(String message) {
        message = StringUtils.isEmpty(message) ? "Session已过期，请重新登录" : message;
        return create(401, null, message);
    }

    /**
     * 生成返回给客户端的对象
     *
     * @param returnCode 访问结果代码
     * @param data       返回的数据
     * @param message    访问结果的消息内容
     */
    public static <T> ApiResult<T> create(int returnCode, T data, String message) {
        return ApiResult.<T>builder()
                .data(data)
                .message(message)
                .returnCode(returnCode)
                .build();
    }

    /**
     * ReturnCode
     */
    @JsonProperty("code")
    private int returnCode;
    /**
     * 服务反馈消息
     */
    @JsonProperty("message")
    private String message;
    /**
     * 具体数据
     */
    @JsonProperty("data")
    private T data;
}
