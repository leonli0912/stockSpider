package my.learning;


import java.nio.charset.Charset;
import java.util.ArrayList;

public class Main {


    public static void main(String[] args) {
        String STOCKFIELDS = "name,today_open,lastday_close,today_close,highest,lowest,buy1,sold1,trade_volume,trade_amount,change,date,time";
        UrlHelper urlHelper = new UrlHelper();
        String s = null;
        try {
            ArrayList stockCodes = StockListReader.ReadFile("src/stockList.txt");

            for(int i=0;i<10;i++){
                String stockCode = stockCodes.get(i).toString();
                System.out.println("get data"+stockCode);
                s = urlHelper.doGet("http://hq.sinajs.cn/list="+stockCode);
                String result = s.substring(s.indexOf("=")+1).replace("\"", "");

                System.out.print(result);
            }

        } catch (Exception e) {
            System.out.println("error occurs...");
            e.printStackTrace();
        }
        System.out.println(Charset.defaultCharset());
        // write your code here
    }
}
