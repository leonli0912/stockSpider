package my.learning;


import java.nio.charset.Charset;

public class Main {


    public static void main(String[] args) {
        UrlHelper urlHelper = new UrlHelper();
        String s = null;
        try {
            s = urlHelper.doGet("http://hq.sinajs.cn/list=sh600519");
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.print(s);
        System.out.println(Charset.defaultCharset());
        // write your code here
    }
}
