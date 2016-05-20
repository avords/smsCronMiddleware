package cn.com.flaginfo.ware.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Properties;

import cn.com.flaginfo.ware.vo.InstallObject;
/**
 * 类的静态字段写入文件
 * 和读取文件到静态字段的工具类
 * @author dongquan.yan
 *
 */
public class PropFileUtils {
    private static final String path = PropFileUtils.class.getClassLoader().getResource("").getPath()+"middleinfo.properties";
    public static void writeStaticFieldToFile(Class c) throws Exception{
        Properties properties = new Properties();
        PrintWriter out = new PrintWriter(
                new BufferedWriter(
                        new FileWriter(path)));
        Field[] fields = c.getDeclaredFields();
        for(Field f:fields){
            int modifier = f.getModifiers();
            if(!Modifier.isStatic(modifier)){
                continue;
            }
            String name = f.getName();
            Object value = null;
            try{
                value = f.get(c);
            }catch(Exception e){
                //调用get方法
                Method method = c.getMethod("get"+captureName(name));
                value = method.invoke(c);
            }
            properties.setProperty(name, (value==null?"":value.toString()));
        }
        properties.store(out, null);
        out.flush();
        out.close();
    }

    public static void readFileToStaticField(Class c) throws Exception{
        Properties properties = new Properties();
        BufferedReader in = new BufferedReader(new FileReader(path));
        properties.load(in);
        Field[] fields = c.getDeclaredFields();
        for(Field f:fields){
            int modifier = f.getModifiers();
            if(!Modifier.isStatic(modifier)){
                continue;
            }
            String name = f.getName();
            String value = properties.getProperty(name);
            if(value==null||value.equals("")){
                continue;
            }
            try{
                f.set(c, value);
            }catch(Exception e){
                //调用get方法
                Method method = c.getMethod("set"+captureName(name),String.class);
                method.invoke(c, value);
            }
        }
        in.close();
    }
    private static String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }
    public static void main(String[] args) {
        try {
//            InstallObject.setCName("章程");
//            InstallObject.setId("111中");
            //writeStaticFieldToFile(InstallObject.class);
            readFileToStaticField(InstallObject.class);
            System.out.println(InstallObject.getCName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
