package pl.sgnit.ims.views.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ViewConfiguration {

    String id();

    String title();

    String[] path();

    String description();

    String url();
}
