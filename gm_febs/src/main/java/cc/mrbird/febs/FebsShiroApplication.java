package cc.mrbird.febs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@MapperScan({"cc.mrbird.febs.system.mapper", "cc.mrbird.febs.monitor.mapper", "cc.mrbird.febs.**.mapper"})
public class FebsShiroApplication {
    public static void main(String[] args) {
        SpringApplication.run(FebsShiroApplication.class, args);
    }
}