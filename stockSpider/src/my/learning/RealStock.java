package my.learning;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RealStock {
    private UrlHelper urlHelper;
    private MySqlHelper mysql ;
    public RealStock() {
        urlHelper = new UrlHelper("GBK",true);

    }
    private void initSqlHelper(){
            mysql = new MySqlHelper(DBConfiguration.url, DBConfiguration.userName, DBConfiguration.password);
    }
    public String getStockHistory(String stockCode) throws java.lang.Exception {
        return getStockHistory(stockCode, 30);
    }

    public String getStockHistory(String stockCode, int interval) throws java.lang.Exception {

        String s = null;
        s = urlHelper.doGet("https://finance.sina.com.cn/realstock/newcompany/" + stockCode + "/pqfq.js?_=14");
        String result = s.substring(0, s.indexOf("/*"));
        result = result.substring(s.indexOf("data:") + 6, result.length() - 3);
        //System.out.print("getStockHistory:" + result + "\n");
        return result;

    }

    public String getLatestPrice(String stockCode) throws java.lang.Exception {
        String s = null;
        System.out.println("getLatestPrice:" + stockCode + "\n");
        s = urlHelper.doGet("http://hq.sinajs.cn/list=" + stockCode);
        String result = s.substring(s.indexOf("=") + 1).replace("\"", "");
        System.out.print(result + "\n");
        return result;
    }

    public String[] getHistoryDividend(String stockCode) {
        String[] result;
        String rawHtml = "";
        try {
            rawHtml = getHistoryDividendRaw(stockCode);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
            System.out.print("Get History Dividend Failed");
            return null;
        }
        result = ParseHtml(rawHtml);
        return result;
    }

    public void updateHistoryDividend(String stockCode,String[] dividends)throws java.sql.SQLException{
        final String sqlSharedStateMement = "insert into myschema.stockdividend(id,dividendId,dividendDetail)" +
                "value('" + stockCode + "',";
        final String querySql = "select * from myschema.stockdividend where id ='"+stockCode +
                "' and dividendId='";
        initSqlHelper();
        int divId = 0;
        if (dividends == null){
            return;
        }
        for (int i=0;i<dividends.length;i++){
            if (dividends[i]!=null){
                if (mysql.executeQuery(querySql+divId+"'")==0){
                String sqlStatemement = sqlSharedStateMement + divId + "," + "'"+dividends[i].trim() +"'"+ ")";
                mysql.execute(sqlStatemement);
                }
                divId++;
            }
        }
        destroySqlHelper();
    }

    private void destroySqlHelper(){
        mysql.destory();
    }
    private String[] ParseHtml(String htmlText) {
        String[] result = new String[100];
        Document document = Jsoup.parse(htmlText);
        Elements allElements = document.getElementsByClass("mt-3");
        for (Element element :allElements){
            if (element.tag().toString().equals("ul")){
                result =ParseText(element.text());
            }
        }
        return result;
}

    private String[] ParseText(String longText) {
        if (longText.equals("")) {
            return null;
        }
        String[] dividends = longText.split("。");
        return dividends;
    }

    private String getHistoryDividendRaw(String stockCode) throws java.lang.Exception {
        final String BASEURL = "https://androidinvest.com/Stock/HistoryDividend/";
        String requestUrl = BASEURL + stockCode;
        String response = null;
        urlHelper.setCharset("UTF-8");
        response = urlHelper.doGet(requestUrl);
        System.out.print("getStockHistory:" + stockCode + "\n");
        return response;
    }


}
