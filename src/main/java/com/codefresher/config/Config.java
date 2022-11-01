package com.codefresher.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Properties;
import java.util.function.BiFunction;

@Configuration
public class Config {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public BiFunction<String, String, String> repOrAddParam(){
        return (paramName, newValue) -> ServletUriComponentsBuilder.fromCurrentRequest()
                .replaceQueryParam(paramName, newValue).build(false)
                .toUriString();
    }
    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dxa26qe2x",
                "api_key", "178348695386325",
                "api_secret", "3ry7k5zru97Ka_-aOkEcBFU0fUM",
                "secure", true
        ));
    }
    @Bean
    public JavaMailSender getJavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("vantuyen24032001@gmail.com");
        mailSender.setPassword("TuyenPTIT14");

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");

        return mailSender;
    }
}
