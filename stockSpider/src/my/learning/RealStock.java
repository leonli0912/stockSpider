package my.learning;

public class RealStock {
    UrlHelper urlHelper = new UrlHelper();

    public RealStock() {

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

    public String getHistoryDividend(String stockCode) throws java.lang.Exception{
        final String BASEURL = "https://androidinvest.com/Stock/HistoryDividend/";
        String requestUrl = BASEURL + stockCode;
        String response = null;
        response = urlHelper.doGet(requestUrl);
        System.out.print("getStockHistory:" + response + "\n");
        return response;
    }
}
