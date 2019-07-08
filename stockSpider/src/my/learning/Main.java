package my.learning;


import java.nio.charset.Charset;
import java.util.ArrayList;
import java.sql.*;

public class Main {


    public static void main(String[] args) {
        DBConfiguration.SetDBType("aliyun");
        String STOCKFIELDS = "name,today_open,lastday_close,today_close,highest,lowest,buy1,sold1,trade_volume,trade_amount,change,date,time";
        UrlHelper urlHelper = new UrlHelper("GBK");
        String s = null;
        MySqlHelper mysqlHelper;
        RealStock realStock;
        try {
            mysqlHelper = new MySqlHelper(DBConfiguration.url, DBConfiguration.userName, DBConfiguration.password);
            String root = mysqlHelper.getClass().getResource("/").getPath();

            ArrayList stockCodes = StockListReader.ReadFile(root+"stockList.txt");
            realStock = new RealStock();
            for(int i=265;i<300;i++){
                String stockCode = stockCodes.get(i).toString();
                //String stockHistory = realStock.getStockHistory(stockCode);
                //mysqlHelper.updateHistory(stockCode,stockHistory);
                Thread.sleep((long)(Math.random() * 20000));
                System.out.println("number:..."+i);
                mysqlHelper.updateDivident(stockCode,realStock.getHistoryDividend(stockCode));
            }

        } catch (Exception e) {
            System.out.println("error occurs...");
            e.printStackTrace();
        }
        System.out.println(Charset.defaultCharset());
        // write your code here
    }
}
