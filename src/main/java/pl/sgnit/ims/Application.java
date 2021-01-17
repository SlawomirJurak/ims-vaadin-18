package pl.sgnit.ims;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.event.EventListener;
import org.vaadin.artur.helpers.LaunchUtil;
import pl.sgnit.ims.backend.user.User;

import java.lang.reflect.Field;
import java.util.Arrays;

@SpringBootApplication
@PWA(name = "IMS Vaadin 18", shortName = "IMS Vaadin 18")
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
