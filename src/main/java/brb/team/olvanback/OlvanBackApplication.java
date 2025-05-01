package brb.team.olvanback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OlvanBackApplication {

    public static void main(String[] args) {

        SpringApplication.run(OlvanBackApplication.class, args);
    }

}
