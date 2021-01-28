package ru.dininslam.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

//    @Bean
//    public ApplicationRunner runner(Producer producer) {
//        return args -> producer.send("Привет от сервера");
//    }

}
