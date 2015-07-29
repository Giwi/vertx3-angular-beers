package org.giwi.vertx.routes;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.giwi.vertx.annotation.VertxRoute;

/**
 * The type Main route.
 */
@VertxRoute(rootPath = "/")
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

        mainRouter.route("/*").handler(StaticHandler.create());
        return mainRouter;
    }
}
