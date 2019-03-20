package com.imooc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * 微信授权接口
 */
@RestController
@RequestMapping("/wechat")
@Slf4j
public class WeixinController {
  /*  @GetMapping("/auth")
    public void auth(@RequestParam("code") String code) {
        log.info("进入 auth 方法。。。");
        log.info("code={} ", code);
        String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx485677689e4e600d&secret=484d6e0ce8a2a81f9a147226de04d1b6&code=" + code + "&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(URL, String.class);
        log.info(response.toString());
    }*/

    //用于测试微信的验证
    /*@RequestMapping(value = "/userInfo")
    public void get(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.error("WechatController   ----   WechatController");
        System.out.println("========WechatController========= ");
        log.info("请求进来了...");
        Enumeration pNames = request.getParameterNames();
        while (pNames.hasMoreElements()) {
            String name = (String) pNames.nextElement();
            String value = request.getParameter(name);
            // out.print(name + "=" + value);
            String logs = "name =" + name + "     value =" + value;
            log.error(logs);
        }
        String signature = request.getParameter("signature");/// 微信加密签名
        String timestamp = request.getParameter("timestamp");/// 时间戳
        String nonce = request.getParameter("nonce"); /// 随机数
        String echostr = request.getParameter("echostr"); // 随机字符串
        PrintWriter out = response.getWriter();

        //if (SignUtil.checkSignature(signature, timestamp, nonce)) {
        out.print(echostr);
//		}s
        out.close();
    }
*/
}
