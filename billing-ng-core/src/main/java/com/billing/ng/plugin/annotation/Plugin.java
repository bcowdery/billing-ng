package com.billing.ng.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks available plugin implementations. Plugins must also implement an appropriate
 * interface defining the behaviour and use of the plugin.
 *
 * @author Brian Cowdery
 * @since 15/02/11
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Plugin {

    String name() default "";

}
