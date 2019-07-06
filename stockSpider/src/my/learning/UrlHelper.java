package my.learning;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;

public class UrlHelper {
    private String charset;
    private String proxyHost;
    private Integer proxyPort;

    {
        charset = "GBK";
        proxyPort = 8080;
        proxyHost = null;//"proxy.sin.sap.corp";
    }
    public UrlHelper(String scharset){
        charset = scharset;
    }
    public void setCharset(String scharset){
        charset = scharset;
    }
    public String doGet(String url) throws Exception {

        URL localURL = new URL(url);
        URLConnection connection = this.openConnection(localURL);
        //connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
        System.out.println("Proxy? " + httpURLConnection.usingProxy());
        httpURLConnection.setRequestProperty("Accept-Charset", charset);
        httpURLConnection.setRequestProperty("Content-Type", "application/javascript");

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
        if (proxyHost != null && proxyPort != null) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            connection = localURL.openConnection(proxy);
        } else {
            connection = localURL.openConnection();
        }
        return connection;
    }
}
