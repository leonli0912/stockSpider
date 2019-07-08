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
    public UrlHelper(String scharset){
        charset = scharset;
    }

    public UrlHelper(String scharset,Boolean bRollingIp){
        charset = scharset;
        isRollingIp = bRollingIp;
        if (bRollingIp){
            try {
                initProxyPool();
            }catch (Exception e) {
                e.printStackTrace();
            }
            int index = (int)Math.random()*((proxies.size() - 0) + 1);
            MyProxy newProxy = proxies.get(index);
            proxyHost = newProxy.proxyHost;
            proxyPort = newProxy.proxyPort;
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
        String inputCharset="";
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        //响应失败
        if (httpURLConnection.getResponseCode() >= 300) {
            throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
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
        //return new String(resultBuffer.toString().getBytes(),"GBK");
        return resultBuffer.toString();
    }

    private URLConnection openConnection(URL localURL) throws IOException {
        URLConnection connection;

        if (isRollingIp&&requestCounter>=10){
            switchProxy();
            requestCounter=0;
        }
        if (proxyHost != null && proxyPort != null) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            connection = localURL.openConnection(proxy);
            requestCounter++;
        } else {
            connection = localURL.openConnection();
        }
        return connection;
    }

    private void initProxyPool() throws Exception{
        ProxyPool pp = new ProxyPool();
        proxies = pp.getProxies();
    }

    private void switchProxy(){
        int index = (int)Math.random()*((proxies.size() - 0) + 1);
        MyProxy newProxy = proxies.get(index);
        proxyHost = newProxy.proxyHost;
        proxyPort = newProxy.proxyPort;
        System.out.print("switch proxy to :"+proxyHost+","+proxyPort);
    }
}
