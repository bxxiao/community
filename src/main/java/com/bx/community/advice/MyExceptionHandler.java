package com.bx.community.advice;

import com.bx.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler
    public ModelAndView handle(Throwable e, Model model, HttpServletRequest request, HttpServletResponse response){
        if(e instanceof CustomizeException){
            model.addAttribute("message", e.getMessage());
        }else {
            model.addAttribute("message", "服务器异常");
        }
        return new ModelAndView("error");
    }
}
