//package pl.wlodarski.learnifier.upload.web.filter;
//
//import lombok.SneakyThrows;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
//import pl.wlodarski.learnifier.upload.application.UploadProperties;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.Part;
//import java.io.IOException;
//import java.util.List;
//import java.util.Objects;
//
//@Component
//public class UploadFilter implements Filter {
//    private static final String FILE_PART = "file";
//
//    private final List<String> supportedContentTypes;
//
//    public UploadFilter(UploadProperties uploadProperties) {
//        supportedContentTypes = uploadProperties.getSupportedTypesAsList();
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
//
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//
//    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
//        if (request.getContentType() != null && request.getContentType().toLowerCase().contains("multipart/form-data") && request.getMethod().equals("POST")) {
//            String filePartContentType = getFilePartContentType(new StandardMultipartHttpServletRequest(request));
//            if (supportedContentTypes.contains(filePartContentType)) {
//            }
//
//        }
//    }
//
//    @SneakyThrows
//    private String getFilePartContentType(StandardMultipartHttpServletRequest httpServletRequest) {
//        Part file = httpServletRequest.getPart(FILE_PART);
//        if (Objects.isNull(file)) {
//            return StringUtils.EMPTY;
//        } else {
//            return file.getContentType();
//        }
//    }
//
//}
