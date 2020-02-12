package com.syngenta.portal.data;

import com.syngenta.portal.data.service.DataLoadingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.syngenta.portal.data")
@PropertySources({
        @PropertySource("classpath:/configuration/common.properties")
})
public class Application {

    private Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    public static void main(String... args) throws IOException {

        Date date = new Date();
        String time = new SimpleDateFormat("yyyy-MM-dd").format(date) + "T" +
                new SimpleDateFormat("HH-mm-ss").format(date);

        System.setProperty("logFileName", "Gateway_Data_Load" + time + ".log");
        SpringApplication.run(Application.class, args);
//        ApplicationContext ctx = SpringApplication.run(Application.class, args);
//        ctx.getBean(DataLoadingService.class).run();
    }
}
