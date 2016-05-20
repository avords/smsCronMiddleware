package cn.com.flaginfo.ware.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import cn.com.flaginfo.ware.config.ApplicationConfig;

public class FrameworkFilter implements Filter{

    public void destroy() {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if ((servletRequest instanceof HttpServletRequest) && (servletResponse instanceof HttpServletResponse)) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            HttpSession session = request.getSession();
            String dynamicDomain = ApplicationConfig.getDynamicDomain();
            if(StringUtils.isNotBlank(dynamicDomain)){
                session.setAttribute("dynamicDomain", ApplicationConfig.getDynamicDomain());
            }else{
                session.setAttribute("dynamicDomain", request.getServletContext());
            }
            filterChain.doFilter(request, response);
        }
    }

    public void init(FilterConfig arg0) throws ServletException {

    }

}
