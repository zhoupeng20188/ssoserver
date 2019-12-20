package com.sso.ssoserver;

import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author zp
 * @create 2019/12/17 14:21
 */
@Controller
@SessionAttributes("authorizationRequest")
public class GrantController {

//    @RequestMapping("/oauth/confirm_access")
////    @RequestMapping("/custom/confirm_access")
//    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
//
//        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
//
//
//        ModelAndView view = new ModelAndView();
//        view.setViewName("base-grant");
//
//        view.addObject("clientId", authorizationRequest.getClientId());
//
//        view.addObject("scopes", authorizationRequest.getScope());
//
//        return view;
//    }

    @RequestMapping("/custom/confirm_access")
    public String getAccessConfirmation(Map<String, Object> param, HttpServletRequest request, Model model) throws Exception {
        System.out.println("/custom/confirm_access in ...");
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) param.get("authorizationRequest");
        String clientId = authorizationRequest.getClientId();
        model.addAttribute("scopes",authorizationRequest.getScope());
        model.addAttribute("clientId",clientId);

        return "base-grant";
    }

}
