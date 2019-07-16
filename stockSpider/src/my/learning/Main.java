package my.learning;


import java.nio.charset.Charset;
import java.util.ArrayList;
import java.sql.*;

public class Main {


    public static void main(String[] args) {
        /*test*/


        //String STOCKFIELDS = "name,today_open,lastday_close,today_close,highest,lowest,buy1,sold1,trade_volume,trade_amount,change,date,time";
        DBConfiguration.SetDBType("aliyun");

        RealStock realStock;
        try {

            realStock = new RealStock();
            String root = realStock.getClass().getResource("/").getPath();
            ArrayList stockCodes = StockListReader.ReadFile(root+"stockList.txt");
            for(int i=3000;i<3000;i++){

                String stockCode = stockCodes.get(i).toString();
                //String stockHistory = realStock.getStockHistory(stockCode);
                //mysqlHelper.updateHistory(stockCode,stockHistory);
                //Thread.sleep((long)(Math.random() * 2000));

                System.out.println("number:..."+i+" stock code: "+stockCode);
                realStock.updateHistoryDividend(stockCode,realStock.getHistoryDividend(stockCode));
            }

        } catch (Exception e) {
            System.out.println("error occurs...");
            e.printStackTrace();
        }
    }
}
