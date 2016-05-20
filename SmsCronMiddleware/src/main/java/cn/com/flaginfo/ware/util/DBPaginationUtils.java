package cn.com.flaginfo.ware.util;

public class DBPaginationUtils {
    public static String getOracleSQL(String sql) {
        StringBuilder result = new StringBuilder(sql);
        result.append("select * from (select  t.*,rownum rn from (").append(sql).append(") t)  rn between ? and ?");
        return result.toString();
    }
    public static String getMysqlSQL(String sql) {
        StringBuilder result = new StringBuilder(sql);
        result.append("select  t.* from (").append(sql).append(") t limit ?,?");
        return result.toString();
    }
}
