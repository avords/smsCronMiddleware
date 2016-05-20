package cn.com.flaginfo.ware.util;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 事务工具
 * 
 * @author dongquan.yan
 *
 */
public class ConnectionUtils {

    private static ThreadLocal<Connection> conns = new ThreadLocal<Connection>();

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = conns.get();
            if (conn == null) {
                conn = DBUtils.getConnection();
                conns.set(conn);
            }
        } catch (Exception e) {
            throw new RuntimeException("从数据源获取链接失败");
        }
        return conn;
    }

    public static void startTransaction() {
        Connection conn = getConnection();
        try {
            if (conn != null) {
                conn.setAutoCommit(false);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } // 开始事务
    }

    public static void commit() {
        Connection conn = getConnection();
        try {
            if (conn != null && !conn.getAutoCommit()) {
                conn.commit();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void rollback() {
        Connection conn = getConnection();
        try {
            if (conn != null && !conn.getAutoCommit()) {
                conn.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void colseAndRelease() {
        Connection conn = getConnection();
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
            conns.remove();// 从当前线程上解绑。（服务器用到了线程池的技术）
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void colse() {
        Connection conn = getConnection();
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
