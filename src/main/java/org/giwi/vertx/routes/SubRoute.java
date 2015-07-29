package org.giwi.vertx.routes;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import org.giwi.vertx.annotation.VertxRoute;

import javax.inject.Inject;

/**
 * The type Sub route.
 */
@VertxRoute(rootPath = "/api")
public class SubRoute implements VertxRoute.Route {
    /**
     * The Mongo client.
     */
    @Inject
    MongoClient mongoClient;

    /**
     * Init router.
     *
     * @param vertx the vertx
     *
     * @return the router
     */
    public Router init(Vertx vertx) {
        Router router = Router.router(vertx);

        router.route().path("/*").consumes("application/json").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "application/json");
            routingContext.next();
        });

        router.get("/beers/").handler(routingContext -> {
            JsonObject query = new JsonObject();
            mongoClient.find("beers", query, res -> {
                if (res.succeeded()) {
                    JsonArray jar = new JsonArray();
                    res.result().forEach(jar::add);
                    routingContext.response().end();
                } else {
                    res.cause().printStackTrace();
                }
            });

        });

        router.put("/beers/").handler(routingContext -> {
            routingContext.response().end();
        });
        return router;
    }
}
