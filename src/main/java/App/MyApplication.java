package App;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class MyApplication {

    public static void main(String[] args) {
        // SpringApplication app = new SpringApplication(MyApplication.class);
        // app.run();
        SpringApplication.run(MyApplication.class, args);
    }

    /*
    public void run(String... args) throws Exception {
        System.out.println("enabled:" + myConfig.isEnabled());
        System.out.println("servers: " + myConfig.getServers());
    }
     */
}
