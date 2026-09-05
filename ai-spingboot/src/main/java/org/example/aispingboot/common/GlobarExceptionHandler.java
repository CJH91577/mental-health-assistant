package org.example.aispingboot.common;

import org.example.aispingboot.exception.BusinessException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobarExceptionHandler {
    // 处理参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handlerException(MethodArgumentNotValidException e) {
        // 处理异常数据的处理
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return Result.error(ResultCode.PARAM_ERROR.getCode(), ResultCode.PARAM_ERROR.getMsg(), message);
    }

    // 处理业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        // 如果异样携带额外的数据
        if (e.getData() != null) {
            return Result.error(e.getCode(), e.getMessage(), e.getData());
        }
        return Result.error(e.getCode(), e.getMessage(), null);
    }
}
