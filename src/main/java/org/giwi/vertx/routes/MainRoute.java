package org.giwi.vertx.routes;

import org.giwi.vertx.annotation.VertxRoute;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * The type Main route.
 */
@VertxRoute()
public class MainRoute implements VertxRoute.Route {

    /**
     * Init void.
     *
     * @param vertx the vertx
     * @return the router
     */
    @Override
    public Router init(Vertx vertx) {
        Router mainRouter = Router.router(vertx);
        mainRouter.get("/hello").handler(routingContext -> {

            // This handler will be called for every request
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/plain");

            // Write to the response and end it
            response.end("Hello World from Vert.x-Web!");
        });
        mainRouter.route("/static/*").handler(StaticHandler.create());
        return mainRouter;
    }
}
