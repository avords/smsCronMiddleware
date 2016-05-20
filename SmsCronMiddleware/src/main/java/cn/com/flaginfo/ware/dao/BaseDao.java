package cn.com.flaginfo.ware.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.springframework.stereotype.Repository;

import cn.com.flaginfo.ware.util.ConnectionUtils;
@Repository
public class BaseDao {

    public Map<String, Object> queryMap(String sql,Object... params) throws Exception{
        QueryRunner queryRunner = new QueryRunner();
        ResultSetHandler rsh = new MapHandler();
        Map<String, Object> map =  (Map<String, Object>) queryRunner.query(ConnectionUtils.getConnection(),sql,rsh,params);
        return map;
    }

    public List<Map<String, Object>> queryListMap(String sql,Object... params) throws Exception{
        QueryRunner queryRunner = new QueryRunner();
        MapListHandler rsh = new MapListHandler();
        List<Map<String, Object>> list =  queryRunner.query(ConnectionUtils.getConnection(),sql,rsh,params);
        return list;
    }

    public  Object queryBean(Class<?> c,String sql,Object... params) throws Exception{
        QueryRunner queryRunner = new QueryRunner();
        ResultSetHandler rsh = new BeanHandler(c);
        return  queryRunner.query(ConnectionUtils.getConnection(),sql,rsh,params);
    }

    public List<Object> queryListBean(Class<?> c,String sql,Object... params) throws Exception{
        QueryRunner queryRunner = new QueryRunner();
        ResultSetHandler rsh = new BeanListHandler(c);
        return  (List<Object>) queryRunner.query(ConnectionUtils.getConnection(),sql,rsh,params);
    }

    public boolean update(String sql,Object... params) throws Exception{
        QueryRunner queryRunner = new QueryRunner();
        int count = queryRunner.update(ConnectionUtils.getConnection(),sql, params);
        return count>0;
    }
}
