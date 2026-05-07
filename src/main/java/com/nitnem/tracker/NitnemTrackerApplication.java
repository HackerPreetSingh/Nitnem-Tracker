package com.nitnem.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.nitnem.*")
public class NitnemTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NitnemTrackerApplication.class, args);
    }

}
