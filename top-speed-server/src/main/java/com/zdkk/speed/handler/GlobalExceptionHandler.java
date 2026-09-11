package com.zdkk.speed.handler;

import com.zdkk.speed.constant.MessageConstant;
import com.zdkk.speed.exception.BaseException;
import com.zdkk.speed.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理类
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public Result<String> handleException(BaseException e) {
        log.error("【全局异常】：{}", e.getMessage());
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(value = {SQLIntegrityConstraintViolationException.class})
    public Result<String> handleException(SQLIntegrityConstraintViolationException e) {
        log.error("【数据库唯一约束冲突】{}", e.getMessage());
        // 兜底统一提示，不解析具体字段
        return Result.error(MessageConstant.DATA_ALREADY_EXISTS);
    }
}
