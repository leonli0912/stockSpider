package my.learning;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;


public class UrlHelper {
    private String charset;
    private String proxyHost;
    private Integer proxyPort;
    private MyProxy currentProxy;
    private boolean isRollingIp;
    private static Integer requestCounter;
    private ArrayList<MyProxy> proxies;

    {
        charset = "GBK";
        proxyPort = 8080;
        proxyHost = null;//"proxy.sin.sap.corp";
        isRollingIp = false;
        requestCounter=0;
    }

    /**
     * constructor
     * @param scharset
     */
    public UrlHelper(String scharset){
        charset = scharset;
    }

    /**
     * overload constructor with rolling ip option
     * @param scharset
     * @param bRollingIp
     */
    public UrlHelper(String scharset,Boolean bRollingIp){
        charset = scharset;
        isRollingIp = bRollingIp;
        if (bRollingIp){
            try {
                initProxyPool();
            }catch (Exception e) {
                e.printStackTrace();
            }
            int index = (int)(Math.random()*proxies.size());
            currentProxy = proxies.get(index);
            proxyHost = currentProxy.proxyHost;
            proxyPort = currentProxy.proxyPort;
            System.out.print("set proxy to :"+proxyHost+","+proxyPort);
        }
    }

    public void setCharset(String scharset){
        charset = scharset;
    }

    public String doGet(String url) throws Exception {

        URL localURL = new URL(null, url, new sun.net.www.protocol.https.Handler());
        URLConnection connection = this.openConnection(localURL);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;

        //httpURLConnection.setRequestProperty("Accept-Charset", charset);
        //httpURLConnection.setRequestProperty("Content-Type", "application/javascript");

        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        String inputCharset;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine;
        //响应失败
        try {
            if (httpURLConnection.getResponseCode() >= 300) {
                System.out.println("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
                proxies.remove(currentProxy);
                switchProxy();
                doGet(url);
                //throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }
        }catch (java.net.ConnectException e){
            System.out.println("connection time out");
            proxies.remove(currentProxy);
            switchProxy();
            doGet(url);
        }

        try {
            inputStream = httpURLConnection.getInputStream();
            if (charset == "GBK"){
                inputCharset = "GB2312";
            }else{
                inputCharset = charset;
            }
            inputStreamReader = new InputStreamReader(inputStream,inputCharset);//GB2312 for get historyPrice
            reader = new BufferedReader(inputStreamReader);

            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }

        } finally {

            if (reader != null) {
                reader.close();
            }

            if (inputStreamReader != null) {
                inputStreamReader.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }

        }
        return resultBuffer.toString();
    }

    private URLConnection openConnection(URL localURL) throws IOException {
        URLConnection connection;

        if (isRollingIp&&requestCounter>=10){
            switchProxy();

        }
        if (proxyHost != null && proxyPort != null) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            connection = localURL.openConnection(proxy);
            requestCounter++;
        } else {
            connection = localURL.openConnection();
        }
        System.out.println("do get...,count of request is "+requestCounter);
        return connection;
    }

    private void initProxyPool() {
        ProxyPool pp = new ProxyPool();
        proxies = pp.getProxies();
    }

    private void switchProxy(){
        int index = (int)(Math.random()*proxies.size());
        currentProxy = proxies.get(index);
        proxyHost = currentProxy.proxyHost;
        proxyPort = currentProxy.proxyPort;
        requestCounter=0;
        System.out.println("switch proxy to :"+proxyHost+","+proxyPort+";");
    }
}
