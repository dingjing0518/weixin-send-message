package com.weixin.controller;

import com.weixin.entity.UserInfo;
import com.weixin.service.UserInfoService;
import com.weixin.utils.WechatTemplate;
import net.sf.json.JSONObject;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.TreeMap;

@RestController
public class UserInfoController {
    private static Logger logger = LoggerFactory.getLogger(UserInfoController.class);
    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public String list() {
        List<UserInfo> list = userInfoService.findAll();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getOpenId() == null || StringUtil.isBlank(list.get(i).getOpenId())) {
                continue;
            }
            RestTemplate restTemplate = new RestTemplate();
            TreeMap<String, TreeMap<String, String>> params = new TreeMap<String, TreeMap<String, String>>();
            //根据具体模板参数组装
            params.put("first", WechatTemplate.item("金桥太茂", "#173177"));
            params.put("keyword1", WechatTemplate.item("2020.03.28", "#173177"));
            params.put("keyword2", WechatTemplate.item("金桥太茂会员活动", "#173177"));
            params.put("keyword3", WechatTemplate.item("即日起至4/30，参与活动可享四大会员福利！1.每邀请1位好友加入太茂会员得400积分 2.现场购物消费享受2倍会员积分福利 3. 参与3/28 0点1积分秒杀，抢限量20张豪华土鸡蛋券！4.消费满额可换好礼！活动详情见微信或咨询商场服务台。", "#173177"));
            params.put("keyword4", WechatTemplate.item("-", "#173177"));
            WechatTemplate wechatTemplate = new WechatTemplate();
            wechatTemplate.setTemplate_id("W8nNQ8rmzVFSNzB5s0jN4n4vE2r6SOe4_ODXWZxlFGo");
            wechatTemplate.setTouser(list.get(i).getOpenId().toString());
//        wechatTemplate.setTouser("opg_Bty803w2A2o4SW7WlWAXnnQo");

//        wechatTemplate.setUrl(url);
            wechatTemplate.setData(params);
            String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=31_AxHk_ETHkQWyr4tLKmYuO2oOTo8DTxh5-nApTB0XxQYAw31CPOX0apumQC648FvZscyvPuOALFmPR_vTd7zjrdgyJrEM-CfpUk9DAXCStD-VRESo9iFKrka75_6DnoqlZbGuVxUCJGJl3KP6ITQhADAPAX";
            JSONObject json = JSONObject.fromObject(wechatTemplate);//将java对象转换为json对象
            logger.info("id: {}||openId: {} ", list.get(i).getId(), list.get(i).getOpenId());
            org.springframework.http.HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            HttpEntity<String> entity = new HttpEntity<String>(json.toString(), headers);
//            restTemplate.postForEntity(url, entity, String.class).getBody();

        }
        return "success";
    }
}
