package com.df.common;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@Slf4j
public class MyController {

    @GetMapping("/myPath")
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String paramValue = request.getParameter("paramName");
        System.out.println("Parameter Name: " + request.getParameterNames().nextElement() + ", Value: " + paramValue);
        return "处理结果";
    }

    @PostMapping("/req")
    public String request(@RequestBody  UserReq req){


        return JSONObject.toJSONString(req);
    }

    @PostMapping("/req1")
    public String request1(String key1,String key2){
        return key1+key2;
    }

    @PostMapping("/req2")
    public String request2(@RequestParam String key1,@RequestParam String key2){
        return key1 + key2;
    }

    @PostMapping("/req3")
    public String request3(@RequestParam String key1){
        return key1 ;
    }
}
