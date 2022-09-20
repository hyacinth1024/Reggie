package cn.itcast.reggie;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
@MapperScan("cn.itcast.reggie.mapper")   //扫描指定包路径下所有Mapper接口
public class ReggieApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class,args);
    }
}
