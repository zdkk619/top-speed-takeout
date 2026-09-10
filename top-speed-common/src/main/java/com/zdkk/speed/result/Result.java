package com.zdkk.speed.result;

import com.zdkk.speed.constant.ResultStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
@Schema(name = "Result", description = "通用返回结果")
public class Result<T> implements Serializable {
    @Schema(description = "状态码", example = "0")
    private Integer code;
    @Schema(description = "提示信息", example = "密码不正确")
    private String msg;
    @Schema(description = "返回数据")
    private T data;

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = ResultStatus.SUCCESS;
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<>();
        result.code = ResultStatus.SUCCESS;
        result.data = object;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.msg = msg;
        result.code = ResultStatus.FAILED;
        return result;
    }
}
