package com.sso.ssoserver;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author zp
 * @create 2019/12/17 11:03
 */
@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(){
//        return "authorize";
        return "login-vue";
    }
}
