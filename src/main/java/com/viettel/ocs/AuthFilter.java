package com.viettel.ocs;


import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
    public static final String ACCESS_TOKEN = "access_token";
    public static final String URL_SSO_UI = "http://172.20.20.45:82";
    public static final String DEFAULT_URL_API_INFO = "http://172.20.20.45:2022/services/sso/api/auth/info";
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // logic handle authentication cushis
        // 1. get cookie, nếu null thì redirect sang trang login của sso
        // 2. nếu get cookie thành công thì gọi api http post để lấy user info,
        // nếu get thành công thì trả về trang home hoặc thực thi các api tiếp theo,
        // còn không thì redirect sang trang login của sso

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies == null || cookies.length == 0) {
            // redirect sso ui to login
            httpServletResponse.sendRedirect(URL_SSO_UI);
        } else {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (ACCESS_TOKEN.equalsIgnoreCase(cookie.getName())) {
                    // call api get api get user info
                    String accessToken = cookie.getValue();

                    HttpGet httpGet = new HttpGet(DEFAULT_URL_API_INFO);
                    // add request headers
                    httpGet.setHeader("Authorization", "Bearer " + accessToken);

                    try (CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpGet)) {
                        // Get HttpResponse Status
                        int httpStatusCode = closeableHttpResponse.getStatusLine().getStatusCode();
                        if (httpStatusCode == 401) {
                            httpServletResponse.sendRedirect(URL_SSO_UI);
                        }
                        HttpEntity entity = closeableHttpResponse.getEntity();
                        Header headers = entity.getContentType();

                        if (entity != null) {
                            // return it as a String
                            String result = EntityUtils.toString(entity);
                            System.out.println(result);
                        }
                    } catch (Exception exception) {
                        // call api get user info exception
                    }
                }
            }
        }

        /*httpServletRequest.getRequestDispatcher("home.xhtml").forward(httpServletRequest, response);*/
        filterChain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    public void getUserInfo(String url, String accessToken) {
        HttpGet request = new HttpGet(url);
        // add request headers
        request.addHeader("Authorization", "Bearer " + accessToken);

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                System.out.println(result);
            }
        } catch (Exception exception) {
            // call api get user info exception
        }
    }

}
