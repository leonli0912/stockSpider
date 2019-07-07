package my.learning;

public class MyProxy {
    public String proxyHost;
    public Integer proxyPort;
    public MyProxy(String host,String port){
        proxyHost=host;
        try {
            proxyPort = Integer.parseInt(port);
        }catch (NumberFormatException e){
            System.out.print(e);
        }

    }
}
