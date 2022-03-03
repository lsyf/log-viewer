package com.lsyf.pay.logger.config;

import com.lsyf.pay.logger.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;


@ControllerAdvice
@Slf4j
public class ExceptionAdvice {
    public ExceptionAdvice() {
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ApiResponse handleException(Exception e) {
        log.warn("接口异常", e);
        return ApiResponse.fail(e.getMessage());
    }


    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    @ResponseBody
    public ApiResponse handleValidException(Exception e) {
        log.warn("接口异常", e);
        StringBuilder sb = new StringBuilder("参数错误:");
        BindingResult result = null;
        if (e instanceof BindException) {
            result = ((BindException) e).getBindingResult();
        } else if (e instanceof MethodArgumentNotValidException) {
            result = ((MethodArgumentNotValidException) e).getBindingResult();
        }
        if (result != null) {
            result.getAllErrors().forEach(error -> sb.append(error.getDefaultMessage()).append(","));
        }
        return ApiResponse.fail(sb.deleteCharAt(sb.length() - 1).toString());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseBody
    public ApiResponse handleException(ConstraintViolationException e) {
        log.warn("接口异常", e);
        StringBuilder sb = new StringBuilder("参数错误：");
        e.getConstraintViolations().forEach(v -> sb.append(v.getMessage()).append(","));
        return ApiResponse.fail(sb.deleteCharAt(sb.length() - 1).toString());
    }

}