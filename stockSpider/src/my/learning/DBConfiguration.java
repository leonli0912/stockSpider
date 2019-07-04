package my.learning;

public class DBConfiguration {
    static String url = null;
    static String userName = null;
    static String password = null;

    public static void SetDBType(String type){
        if (type == "local"){
            url = "jdbc:mysql://localhost:3306/myschema";
            userName = "root";
            password = "ly880912";
        }
        if (type == "aliyun"){
            url = "jdbc:mysql://rm-uf6p609z3j3i7g7c2io.mysql.rds.aliyuncs.com:3306/myschema";
            userName = "admin1";
            password = "Welcome1";
        }

    }
}
