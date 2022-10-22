package com.zhou.servicebase.exceptionhandler;

import com.zhou.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@ControllerAdvice
@Slf4j
public class GobalExceptionHandler {
    /**
     *
     * @ExceptionHandler    指定出现什么异常执行这个方法
     * @ResponseBody    为了返回json数据
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("程序出现错误,开始执行全局异常处理!");
    }

    /**
     * 处理特定异常(ArithmeticException)
     * @param e
     * @return
     */
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("程序出现错误,开始执行ArithmeticException异常处理!");
    }

    /**
     * 处理自定义异常(ZhouException)
     * @param e
     * @return
     */
    @ExceptionHandler(ZhouException.class)
    @ResponseBody
    public R error(ZhouException e){
        log.error(e.getMsg());
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }



}
