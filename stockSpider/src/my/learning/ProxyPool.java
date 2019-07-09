package my.learning;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class ProxyPool {
    private ArrayList<MyProxy> proxies;
    public ProxyPool(){
        proxies = new ArrayList<MyProxy>();
        prepareProxy();
    }
    private void prepareProxy(){
        int page = 1;
        while(page<=1){
            try {
                String proxyHtml = new UrlHelper("utf-8").doGet("https://www.xicidaili.com/nn/"+page);
                parseHtml(proxyHtml);
            }catch (java.lang.Exception e){
                System.out.print(e);
            }
            page++;
        }
    }
    private void parseHtml(String htmlText) {
        Document document = Jsoup.parse(htmlText);
        Elements allElements = document.select("#ip_list > tbody>tr") ;
        for (Element element :allElements){
            if (element.children().first().tag().toString().equals("th")){
                continue;
            }
            proxies.add(new MyProxy(element.child(1).text(),element.child(2).text())) ;
            System.out.println("proxy pool add "+ element.child(1).text()+':'+element.child(2).text());
        }
    }
    public ArrayList<MyProxy> getProxies(){
        return proxies;
    }
}
