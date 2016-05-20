package cn.com.flaginfo.ware.aop;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

import cn.com.flaginfo.ware.util.ConnectionUtils;
import cn.com.flaginfo.ware.util.TransactionUtils;

public class TransactionInterceptor implements MethodInterceptor {
    private Logger logger = Logger.getLogger(getClass());
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long start = System.currentTimeMillis();
        ConnectionUtils.startTransaction();
        Method method = invocation.getMethod();
        String methodKey =method.getDeclaringClass().getName()+"@"+method.getName();
        Object result = null;
        try {
            TransactionUtils.push(methodKey);
            result = invocation.proceed();
            TransactionUtils.pull();
            if(TransactionUtils.isEmpty()){
                ConnectionUtils.commit();
                ConnectionUtils.colseAndRelease();
                logger.info("cost time:"+methodKey+"==>"+(System.currentTimeMillis()-start)+"ms");
            }
            if(System.currentTimeMillis()-start>500){
                logger.info("****"+methodKey+" cost time:"+(System.currentTimeMillis()-start)+"ms. The method is more slowly ****");
            }
        } catch (Exception e) {
            logger.info(e);
            ConnectionUtils.rollback();
            ConnectionUtils.colseAndRelease();
            TransactionUtils.remove();
            throw new RuntimeException(e);
        }
        return result;
    }

}
