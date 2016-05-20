package cn.com.flaginfo.ware.filter;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 验证用户是否登录的拦截器
 * @author hc
 *
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		super.afterConcurrentHandlingStarted(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		request.setCharacterEncoding("UTF-8");  
        response.setCharacterEncoding("UTF-8");  
        response.setContentType("text/html;charset=UTF-8");  
        
		String uri = request.getRequestURI();
		if((uri.indexOf("/login/")!=-1)||(uri.indexOf("/static/")!=-1)){
			//不检查
			
		}else{
			 Object obj = request.getSession().getAttribute("loginInfo");
			 if(null==obj){
				 String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
				 PrintWriter out = response.getWriter();  
                 StringBuilder builder = new StringBuilder();  
                 builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");  
                 builder.append("window.top.location.href=\"");  
                 builder.append(basePath);  //这里是http://ip:port/项目名
                 builder.append("login/login\";</script>");  //这里是重新登录的页面url
                 out.print(builder.toString());  
                 out.close(); 
			 }
		}
		
		
		return super.preHandle(request, response, handler);
	}
     
}
