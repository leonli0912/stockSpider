package my.learning;


import java.nio.charset.Charset;
import java.util.ArrayList;
import java.sql.*;

public class Main {


    public static void main(String[] args) {
        String STOCKFIELDS = "name,today_open,lastday_close,today_close,highest,lowest,buy1,sold1,trade_volume,trade_amount,change,date,time";
        UrlHelper urlHelper = new UrlHelper();
        String s = null;
        MySqlHelper mysqlHelper;
        RealStock realStock;
        try {
            mysqlHelper = new MySqlHelper("","","");
            ArrayList stockCodes = StockListReader.ReadFile("src/stockList.txt");
            realStock = new RealStock();
            for(int i=0;i<1;i++){
                String stockCode = stockCodes.get(i).toString();
                String stockHistory = realStock.getStockHistory(stockCode);
                //System.out.print(stockHistory);
                mysqlHelper.updateHistory(stockCode,stockHistory);

                //realStock.getLatestPrice(stockCode);
                //mysqlHelper.insertSingleHQ(stockCode,result);
            }

        } catch (Exception e) {
            System.out.println("error occurs...");
            e.printStackTrace();
        }
        System.out.println(Charset.defaultCharset());
        // write your code here
    }
}
