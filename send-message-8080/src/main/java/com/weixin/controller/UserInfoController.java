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
            params.put("first", WechatTemplate.item("商场营业时间恢复通知", "#173177"));
            params.put("keyword1", WechatTemplate.item("金桥太茂", "#173177"));
            params.put("keyword2", WechatTemplate.item("尊敬的顾客，金桥太茂即日起恢复正常营业时间10：00-22：00。请大家自觉佩戴口罩，安全出行。", "#173177"));
            params.put("remark", WechatTemplate.item("金桥太茂已全面做好每日清洁卫生及消毒防范措施，为您和您的家人提供更为安心的购物环境。期待您的再次光临！", "#173177"));
            WechatTemplate wechatTemplate = new WechatTemplate();
            wechatTemplate.setTemplate_id("zvTn1G9Oo5gR_CdMMLI0gwtnn_rOXQ45fG4_roQM2tE");
            wechatTemplate.setTouser(list.get(i).getOpenId().toString());

//        wechatTemplate.setUrl(url);
            wechatTemplate.setData(params);
            String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=31__ZcahjvsSYom0Gp5UXM0I6dMPTnVsiYxBpRetuQfELQAGTG-VnkwyqjHlLbvtcklaWW1HtOsoFGevyXx834S15zYMjVYzDYE_k33H7pTk2Bz0qermFTqAmsQTO6x2GRIxKmFEheu2TmG_M0kQHBdAJAULF";
            JSONObject json = JSONObject.fromObject(wechatTemplate);//将java对象转换为json对象
            logger.info("id: {}||openId: {} ", list.get(i).getId(), list.get(i).getOpenId());
            org.springframework.http.HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            HttpEntity<String> entity = new HttpEntity<String>(json.toString(), headers);
//                restTemplate.postForEntity(url, entity, String.class).getBody();

        }
        return "success";
    }
}
