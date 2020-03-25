package com.weixin.utils;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

public class HttpUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    protected static final String POST_METHOD = "POST";
    private static final String GET_METHOD = "GET";

    static {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                logger.debug("ClientTrusted");
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                logger.debug("ServerTrusted");
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        }};

        HostnameVerifier doNotVerify = (s, sslSession) -> true;

        try {
            SSLContext sc = SSLContext.getInstance("SSL", "SunJSSE");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(doNotVerify);
        } catch (Exception e) {
            logger.error("Initialization https impl occur exception : {}", e);
        }
    }


    /**
     * 默认的http请求执行方法
     *
     * @param url  url 路径
     * @param method 请求的方法 POST/GET
     * @param map  请求参数集合
     * @param data  输入的数据 允许为空
     * @return result
     */
    private static String HttpDefaultExecute(String url, String method, Map<String, String> map, String data) {
        String result = "";
        try {
            url = setParmas(url, map, null);
            result = defaultConnection(url, method, data);
        } catch (Exception e) {
            logger.error("出错参数 {}", map);
        }
        return result;
    }

    public static String httpGet(String url, Map<String, String> map) {
        return HttpDefaultExecute(url, GET_METHOD, map, null);
    }

    public static String httpPost(String url, Map<String, String> map, String data) {
        return HttpDefaultExecute(url, POST_METHOD, map, data);
    }

    /**
     * 默认的https执行方法,返回
     *
     * @param url  url 路径
     * @param method 请求的方法 POST/GET
     * @param map  请求参数集合
     * @param data  输入的数据 允许为空
     * @return result
     */
    private static String HttpsDefaultExecute(String url, String method, Map<String, String> map, String data) {
        try {
            url = setParmas(url, map, null);
            logger.info(data);
            return defaultConnection(url, method, data);
        } catch (Exception e) {
            logger.error("出错参数 {}", map);
        }
        return "";
    }

    public static String doGet(String url, Map<String, String> map) {
        return HttpsDefaultExecute(url, GET_METHOD, map, null);
    }

    public static String doPost(String url, Map<String, String> map, String data) {
        return HttpsDefaultExecute(url, POST_METHOD, map, data);
    }

    /**
     * @param path  请求路径
     * @param method 方法
     * @param data  输入的数据 允许为空
     * @return
     * @throws Exception
     */
    private static String defaultConnection(String path, String method, String data) throws Exception {
        if (StringUtils.isBlank(path)) {
            throw new IOException("url can not be null");
        }
        String result = null;
        URL url = new URL(path);
        HttpURLConnection conn = getConnection(url, method);
        if (StringUtils.isNotEmpty(data)) {
            OutputStream output = conn.getOutputStream();
            output.write(data.getBytes(StandardCharsets.UTF_8));
            output.flush();
            output.close();
        }
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream input = conn.getInputStream();
            result = IOUtils.toString(input, StandardCharsets.UTF_8);
            input.close();
            conn.disconnect();
        }
//    log.info(result);
        return result;
    }

    /**
     * 根据url的协议选择对应的请求方式
     *
     * @param url  请求路径
     * @param method 方法
     * @return conn
     * @throws IOException 异常
     */
    //待改进
    protected static HttpURLConnection getConnection(URL url, String method) throws IOException {
        HttpURLConnection conn;
        if (StringUtils.equals("https", url.getProtocol())) {
            conn = (HttpsURLConnection) url.openConnection();
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }
        if (conn == null) {
            throw new IOException("connection can not be null");
        }
        conn.setRequestProperty("Pragma", "no-cache");// 设置不适用缓存
        conn.setRequestProperty("Cache-Control", "no-cache");
        conn.setRequestProperty("Connection", "Close");// 不支持Keep-Alive
        conn.setUseCaches(false);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setInstanceFollowRedirects(true);
        conn.setRequestMethod(method);
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);

        return conn;
    }


    /**
     * 根据url
     *
     * @param url 请求路径
     * @return isFile
     * @throws IOException 异常
     */
    //待改进
    protected static HttpURLConnection getConnection(URL url, boolean isFile) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (conn == null) {
            throw new IOException("connection can not be null");
        }
        //设置从httpUrlConnection读入
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        //如果是上传文件，则设为POST
        if (isFile) {
            conn.setRequestMethod(POST_METHOD); //GET和 POST都可以 文件略大改成POST
        }
        // 设置请求头信息
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Charset", String.valueOf(StandardCharsets.UTF_8));
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);
        return conn;
    }


    /**
     * 拼接参数
     *
     * @param url   需要拼接参数的url
     * @param map   参数
     * @param charset 编码格式
     * @return 拼接完成后的url
     */
    public static String setParmas(String url, Map<String, String> map, String charset) throws Exception {
        String result = StringUtils.EMPTY;
        boolean hasParams = false;
        if (StringUtils.isNotEmpty(url) && MapUtils.isNotEmpty(map)) {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey().trim();
                String value = entry.getValue().trim();
                if (hasParams) {
                    builder.append("&");
                } else {
                    hasParams = true;
                }
                if (StringUtils.isNotEmpty(charset)) {
                    builder.append(key).append("=").append(URLEncoder.encode(value, charset));
                } else {
                    builder.append(key).append("=").append(value);
                }
            }
            result = builder.toString();
        }

        URL u = new URL(url);
        if (StringUtils.isEmpty(u.getQuery())) {
            if (url.endsWith("?")) {
                url += result;
            } else {
                url = url + "?" + result;
            }
        } else {
            if (url.endsWith("&")) {
                url += result;
            } else {
                url = url + "&" + result;
            }
        }
        logger.debug("request url is {}", url);
        return url;
    }
}