package my.learning;


import java.nio.charset.Charset;
import java.util.ArrayList;

public class Main {


    public static void main(String[] args) {
        UrlHelper urlHelper = new UrlHelper();
        String s = null;
        try {
            ArrayList stockCodes = StockListReader.ReadFile("src/stockList.txt");
            System.out.println(stockCodes.size());
            for(int i=0;i<10;i++){
                String stockCode = stockCodes.get(i).toString();
                System.out.println("get data"+stockCode);
                s = urlHelper.doGet("http://hq.sinajs.cn/list="+stockCode);
                System.out.print(s);
            }

        } catch (Exception e) {
            System.out.println("error occurs...");
            e.printStackTrace();
        }
        System.out.println(Charset.defaultCharset());
        // write your code here
    }
}
