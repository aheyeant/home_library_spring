package cz.cvut.kbss.ear.homeLibrary;


import cz.cvut.kbss.ear.homeLibrary.config.RestConfig;
import cz.cvut.kbss.ear.homeLibrary.config.ServiceConfig;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories
@EnableAspectJAutoProxy(proxyTargetClass = true)    // Necessary for beans without interface
@Import({ServiceConfig.class, RestConfig.class})


public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.  class, args);
    }
}
