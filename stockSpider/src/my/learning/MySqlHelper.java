package my.learning;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MySqlHelper {
    Connection connection;
    Statement statement;
    ResultSet resultSet;

    public MySqlHelper(String url, String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/myschema", "root", "ly880912");
            System.out.println("Database connected!");
            statement = connection.createStatement();
            if (statement.execute("select * from myschema.stockhq")) {
                resultSet = statement.getResultSet();
            }

            while (resultSet.next())
                System.out.println(resultSet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertSingleHQ(String stockID, String hq) {
        String[] hqDetail = hq.split(",");
        String sqlStateMement;
        DateFormat dateFormatf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        sqlStateMement = "insert into myschema.stockhq ( id,name,today_open,yesterday_close,newest,highest,lowest,buy1,sold1,volumn,amount,date)" +
                "values ('" + stockID + "','"
                + hqDetail[0] + "',"
                + hqDetail[1] + ","
                + hqDetail[2] + ","
                + hqDetail[3] + ","
                + hqDetail[4] + ","
                + hqDetail[5] + ","
                + hqDetail[6] + ","
                + hqDetail[7] + ","
                + hqDetail[8] + ","
                + hqDetail[9] + ","
                + dateFormatf.format(date) + ")";
        System.out.println("sql:" + sqlStateMement);
        try {
            statement.executeUpdate(sqlStateMement);
            connection.close();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateHistory(String stockCode, String source) throws Exception {
        String[] aClosePrice = source.split(",");
        Date date = new Date();
        String sqlSharedStateMement;
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        decimalFormat.setMaximumFractionDigits(3);

        sqlSharedStateMement = "insert into myschema.stockhistory(id,date,price)" +
                "values('" + stockCode + "',";

        for (String date_price : aClosePrice) {
            date = new SimpleDateFormat("yyyyMMdd").parse(date_price.split(":")[0].replace("_", ""));
            String sdate = new SimpleDateFormat("yyyyMMdd").format(date) ;
            Double price = Double.parseDouble(date_price.split(":")[1].replace("\"", ""));
            String sqlStatemement = sqlSharedStateMement + sdate + "," + decimalFormat.format(price) + ")";
            System.out.println("excute sqlStateMement:" + sqlStatemement);
            statement.execute(sqlStatemement);

        }

    }

}
