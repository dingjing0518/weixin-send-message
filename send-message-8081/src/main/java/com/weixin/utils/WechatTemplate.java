package com.weixin.utils;

import java.util.TreeMap;

public class WechatTemplate {

        private String touser;//用戶openid

        private String template_id;//模板ID

        private String url;//URL置空，则在发送后，点击模板消息会进入一个空白页面（ios），或无法点击（android）

        private TreeMap<String, TreeMap<String, String>> data; //data数据

        public static TreeMap<String, String> item(String value, String color) {
            TreeMap<String, String> params = new TreeMap<String, String>();
            params.put("value", value);
            params.put("color", color);
            return params;
        }

        public TreeMap<String, TreeMap<String, String>> getData() {
            return data;
        }

        public void setData(TreeMap<String, TreeMap<String, String>> data) {
            this.data = data;
        }

        public String getTouser() {
            return touser;
        }

        public void setTouser(String touser) {
            this.touser = touser;
        }

        public String getTemplate_id() {
            return template_id;
        }

        public void setTemplate_id(String template_id) {
            this.template_id = template_id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "WechatTemplate{" +
                    "touser='" + touser + '\'' +
                    ", template_id='" + template_id + '\'' +
                    ", url='" + url + '\'' +
                    ", data=" + data +
                    '}';
        }
}
