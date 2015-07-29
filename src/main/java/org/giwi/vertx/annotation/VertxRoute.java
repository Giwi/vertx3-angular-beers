package org.giwi.vertx.annotation;

import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import org.reflections.Reflections;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The interface Vertx route.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface VertxRoute {
    /**
     * Root path.
     *
     * @return the string
     */
    String rootPath() default "/";

    /**
     * The LOG.
     */
    Logger LOG = LoggerFactory.getLogger(VertxRoute.class.getName());

    /**
     * The interface Route.
     */
    interface Route {
        /**
         * Init void.
         *
         * @param vertx the vertx
         *
         * @return the router
         */
        Router init(Vertx vertx);
    }

    /**
     * The type Loader.
     */
    class Loader {
        /**
         * Add package.
         *
         * @param packageName the package name
         *
         * @return the routes in package
         */
        public static Map<String, Route> getRoutesInPackage(String packageName) {
            Reflections reflections = new Reflections(packageName);
            Map<String, Route> routers = new HashMap<>();
            Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(VertxRoute.class);
            for (Class<?> rit : annotated) {
                try {
                    if (Route.class.isAssignableFrom(rit)) {
                        Route r = (Route) rit.getConstructor().newInstance();
                        LOG.info("Startig route : " + rit.getCanonicalName());
                        routers.put(rit.getAnnotation(VertxRoute.class).rootPath(), r);
                    }
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
                        | InvocationTargetException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
            return routers;
        }
    }
}
