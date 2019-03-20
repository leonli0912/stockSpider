package my.learning;


import java.nio.charset.Charset;
import java.util.ArrayList;

public class Main {


    public static void main(String[] args) {
        UrlHelper urlHelper = new UrlHelper();
        String s = null;
        try {
            ArrayList stockCodes = StockListReader.Read("src/stockList.txt");
            System.out.println(stockCodes.size());
            s = urlHelper.doGet("http://hq.sinajs.cn/list=sh600519");
        } catch (Exception e) {
            System.out.println("error occurs...");
            e.printStackTrace();
        }


        System.out.print(s);
        System.out.println(Charset.defaultCharset());
        // write your code here
    }
}
