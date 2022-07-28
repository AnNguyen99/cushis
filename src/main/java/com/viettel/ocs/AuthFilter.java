package com.viettel.ocs;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // logic handle authentication cushis
        // 1. get cookie, nếu null thì redirect sang trang login của sso
        // 2. nếu get cookie thành công thì gọi api http post để lấy user info,
        // nếu get thành công thì trả về trang home hoặc thực thi các api tiếp theo,
        // còn không thì redirect sang trang login của sso

        request.getRequestDispatcher("home.xhtml").forward(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}
