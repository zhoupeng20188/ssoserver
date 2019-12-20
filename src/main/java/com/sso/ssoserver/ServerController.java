package com.sso.ssoserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
public class ServerController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("/user/me")
    public Principal user(Principal principal) {
        System.out.println(principal);
        return principal;
    }


    @RequestMapping("/check/token")
    @ResponseBody
    public String test(HttpServletRequest request) {
        String token = request.getParameter("token");
        //读取字符串
        String key = "access:" + token;
        String access_token = stringRedisTemplate.opsForValue().get(key);
//        System.out.println(access_token);
        if(StringUtils.isEmpty(access_token)){
            return "1";
        }
        return "0";
    }

    @RequestMapping("/login234")
    @ResponseBody
    public String test22(HttpServletRequest request) {

        return "0";
    }

    @RequestMapping("/admin/test")
    @ResponseBody
    public String admin() {
       return "only admin role can see this";
    }

    @RequestMapping("/user/test")
    @ResponseBody
    public String user() {
        return "only user role can see this";
    }
}
