package com.dbapi.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回结果集
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("统一结果集处理器")
public class Result<T> {

    /**
     * 状态码
     */
    @ApiModelProperty(value = "状态码")
    private Integer code;

    /**
     * 状态信息
     */
    @ApiModelProperty(value = "状态信息")
    private Boolean status;

    /**
     * 返回信息
     */
    @ApiModelProperty(value = "返回信息")
    private String message;

    /**
     * 数据
     */
    @ApiModelProperty(value = "数据")
    private T data;

    /**
     * 全参数方法
     *
     * @param code    状态码
     * @param status  状态
     * @param message 返回信息
     * @param data    返回数据
     * @param <T>     泛型
     * @return {@link Result <T>}
     */
    private static <T> Result<T> response(Integer code, Boolean status, String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setStatus(status);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 全参数方法
     *
     * @param code    状态码
     * @param status  状态
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link Result <T>}
     */
    private static <T> Result<T> response(Integer code, Boolean status, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setStatus(status);
        result.setMessage(message);
        return result;
    }

    /**
     * 成功返回（无参）
     *
     * @param <T> 泛型
     * @return {@link Result <T>}
     */
    public static <T> Result<T> success() {
        return response(HttpStatusEnum.SUCCESS.getCode(), true, HttpStatusEnum.SUCCESS.getMessage(), null);
    }

    /**
     * 成功返回（枚举参数）
     *
     * @param httpResponseEnum 枚举参数
     * @param <T>              泛型
     * @return {@link Result <T>}
     */
    public static <T> Result<T> success(HttpStatusEnum httpResponseEnum) {
        return response(httpResponseEnum.getCode(), true, httpResponseEnum.getMessage());
    }

    /**
     * 成功返回（状态码+返回信息）
     *
     * @param code    状态码
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link Result <T>}
     */
    public static <T> Result<T> success(Integer code, String message) {
        return response(code, true, message);
    }

    /**
     * 成功返回（返回信息 + 数据）
     *
     * @param message 返回信息
     * @param data    数据
     * @param <T>     泛型
     * @return {@link Result <T>}
     */
    public static <T> Result<T> success(String message, T data) {
        return response(HttpStatusEnum.SUCCESS.getCode(), true, message, data);
    }

    /**
     * 成功返回（状态码+返回信息+数据）
     *
     * @param code    状态码
     * @param message 返回信息
     * @param data    数据
     * @param <T>     泛型
     * @return {@link Result <T>}
     */
    public static <T> Result<T> success(Integer code, String message, T data) {
        return response(code, true, message, data);
    }

    /**
     * 成功返回（数据）
     *
     * @param data 数据
     * @param <T>  泛型
     * @return {@link Result <T>}
     */
    public static <T> Result<T> success(T data) {
        return response(HttpStatusEnum.SUCCESS.getCode(), true, HttpStatusEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回（返回信息）
     *
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link Result <T>}
     */
    public static <T> Result<T> success(String message) {
        return response(HttpStatusEnum.SUCCESS.getCode(), true, message, null);
    }

    /**
     * 失败返回（无参）
     *
     * @param <T> 泛型
     * @return {@link Result <T>}
     */
    public static <T> Result<T> fail() {
        return response(HttpStatusEnum.ERROR.getCode(), false, HttpStatusEnum.ERROR.getMessage(), null);
    }

    /**
     * 失败返回（枚举）
     *
     * @param httpResponseEnum 枚举
     * @param <T>              泛型
     * @return {@link Result <T>}
     */
    public static <T> Result<T> fail(HttpStatusEnum httpResponseEnum) {
        return response(httpResponseEnum.getCode(), false, httpResponseEnum.getMessage());
    }

    /**
     * 失败返回（状态码+返回信息）
     *
     * @param code    状态码
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link Result <T>}
     */
    public static <T> Result<T> fail(Integer code, String message) {
        return response(code, false, message);
    }

    /**
     * 失败返回（返回信息+数据）
     *
     * @param message 返回信息
     * @param data    数据
     * @param <T>     泛型
     * @return {@link Result <T>}
     */
    public static <T> Result<T> fail(String message, T data) {
        return response(HttpStatusEnum.ERROR.getCode(), false, message, data);
    }

    /**
     * 失败返回（状态码+返回信息+数据）
     *
     * @param code    状态码
     * @param message 返回消息
     * @param data    数据
     * @param <T>     泛型
     * @return {@link Result <T>}
     */
    public static <T> Result<T> fail(Integer code, String message, T data) {
        return response(code, false, message, data);
    }

    /**
     * 失败返回（数据）
     *
     * @param data 数据
     * @param <T>  泛型
     * @return {@link Result <T>}
     */
    public static <T> Result<T> fail(T data) {
        return response(HttpStatusEnum.ERROR.getCode(), false, HttpStatusEnum.ERROR.getMessage(), data);
    }

    /**
     * 失败返回（返回信息）
     *
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link Result <T>}
     */
    public static <T> Result<T> fail(String message) {

        return response(HttpStatusEnum.ERROR.getCode(), false, message, null);
    }
}


