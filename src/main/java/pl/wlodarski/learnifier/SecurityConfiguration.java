//package pl.wlodarski.learnifier;
//
//
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import pl.wlodarski.learnifier.upload.application.UploadProperties;
//import pl.wlodarski.learnifier.upload.web.filter.UploadFilter;
//
//@Configuration
//public class SecurityConfiguration {
//
//    final UploadProperties uploadProperties;
//
//    public SecurityConfiguration(UploadProperties uploadProperties) {
//        this.uploadProperties = uploadProperties;
//    }
//
//    @Bean
//    public FilterRegistrationBean<UploadFilter> filterRegistrationBean() {
//        FilterRegistrationBean<UploadFilter> registrationBean = new FilterRegistrationBean();
//        UploadFilter customURLFilter = new UploadFilter(uploadProperties);
//
//        registrationBean.setFilter(customURLFilter);
//        registrationBean.addUrlPatterns("/upload/*");
//        registrationBean.setOrder(2);
//        return registrationBean;
//    }
//}
