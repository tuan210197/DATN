//package shupship.config;
//
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//
//public class CORSFilter implements Filter {
//    public CORSFilter() {
//    }
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//        HttpServletResponse response = (HttpServletResponse) res;
//
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers",
//                "Origin,Accept,X-Requested-With," +
//                        "Content-Type," +
//                        "Access-Control-Request-Method," +
//                        "Access-Control-Request-Headers," +
//                        "Authorization," +
//                        "x-ijt");
//        response.setHeader("Access-Control-Expose-Headers", "X-Total-Results");
//
//        chain.doFilter(req, res);
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) {
//    }
//
//    @Override
//    public void destroy() {
//    }
//
//}
