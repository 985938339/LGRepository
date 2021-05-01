package com.lg.exception;

import com.baomidou.mybatisplus.extension.api.R;
import com.lg.constant.Result;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author liug132055
 */
@ControllerAdvice
public class ControllerExceptionHandler {
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<String> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return R.restResult(e.getBindingResult().getFieldError().getDefaultMessage(), Result.ARG_VIOLATION);
    }

    @ResponseBody
    @ExceptionHandler(StockException.class)
    public R<String> handlerException(StockException e) {
        return R.failed(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public R<String> handlerMemberException(Exception e) {
        e.printStackTrace();
        return R.restResult(e.getMessage(), Result.WEB_500);
    }
}
