package org.giwi.vertx;

import org.giwi.vertx.annotation.VertxRoute;
import org.giwi.vertx.injection.GuiceModule;
import org.giwi.vertx.routes.SubRoute;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

/**
 * The type Main.
 */
public class Main {


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));
        HttpServer server = vertx.createHttpServer();
        Router mainRouter = Router.router(vertx);
        FileSystem fs = vertx.fileSystem();
        JsonObject config = new JsonObject(new String(fs.readFileBlocking("conf.json").getBytes()));
        Injector injector = Guice.createInjector(new GuiceModule(config, vertx));

        VertxRoute.Loader.getRoutesInPackage("org.giwi.vertx.routes").forEach((key, route) -> {
                    injector.injectMembers(route);
                    mainRouter.mountSubRouter(key, route.init(vertx));
                }
        );

        mainRouter.mountSubRouter("/sub", new SubRoute().init(vertx));
        server.requestHandler(mainRouter::accept).listen(8080);
    }
}
