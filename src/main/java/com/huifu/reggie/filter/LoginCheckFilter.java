package com.huifu.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.huifu.reggie.common.BaseContext;
import com.huifu.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        //定义不需要过滤的请求路径
        String[] urls = new String[]{"/employee/login", "/employee/logout", "/backend/**", "/front/**"};
        boolean check = check(urls, requestURI);
        if (check) {
            filterChain.doFilter(request, response);
            return;
        }
        log.info("拦截到请求: {}", requestURI);
        //这次请求需要判断是否登录
        Object employee = request.getSession().getAttribute("employee");
        if (null != employee) {
            Long id = (Long) employee;
            BaseContext.setCurrentId(id);
            filterChain.doFilter(request, response);
            return;
        }
        //如果未登录，则返回未登录状态
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /**
     * 路径匹配
     *
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
