package cn.com.flaginfo.ware.util;

import java.util.Stack;

/**
 * 事务线程变量
 * @author Rain
 *
 */
public class TransactionUtils {
	private static ThreadLocal threadLocal = new ThreadLocal();
	
	public static void push(String object){
		Stack stack = (Stack)threadLocal.get();
		if(stack==null){
			stack = new Stack();
			threadLocal.set(stack);
		}
		stack.add(object);
	}
	
	public static String pull(){
		Stack stack = (Stack)threadLocal.get();
		if(stack!=null && !stack.isEmpty()){
			return (String)stack.pop();
		}
		return null;
	}
	
	public static boolean isEmpty(){
		Stack stack = (Stack)threadLocal.get();
		if(stack == null|| stack.isEmpty()){
			return true;
		}
		return false;
	}
	
	public static void remove(){
		threadLocal.remove();
	}
	
	
}
