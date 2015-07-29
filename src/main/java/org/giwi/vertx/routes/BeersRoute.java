package org.giwi.vertx.routes;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.giwi.vertx.annotation.VertxRoute;

import javax.inject.Inject;

/**
 * The type Sub route.
 */
@VertxRoute(rootPath = "/api")
public class BeersRoute implements VertxRoute.Route {
    Logger LOG = LoggerFactory.getLogger(BeersRoute.class.getName());

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
        router.route().handler(BodyHandler.create());
        router.route().path("/*").produces("application/json").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "application/json");
            routingContext.next();
        });

        router.get("/BeerList").handler(routingContext -> {
            JsonObject query = new JsonObject();
            mongoClient.find("beers", query, res -> {
                if (res.succeeded()) {
                    JsonArray jar = new JsonArray();
                    res.result().forEach(jar::add);
                    routingContext.response().end(Json.encodePrettily(jar));
                } else {
                    LOG.error(res.cause());
                }
            });
        });

        router.get("/Beer/:beerId").handler(routingContext -> {
            JsonObject query = new JsonObject().put("_id", routingContext.request().getParam("beerId"));
            mongoClient.findOne("beers", query, new JsonObject(), res -> {
                if (res.succeeded()) {
                    routingContext.response().end(res.result().encodePrettily());
                } else {
                    LOG.error(res.cause());
                }
            });
        });

        router.put("/Beer").consumes("application/json").handler(routingContext -> {
            JsonObject beer = routingContext.getBodyAsJson();
            mongoClient.insert("beers", beer, res -> {
                if (res.succeeded()) {
                    beer.put("_id", res.result());
                    routingContext.response().end(beer.encodePrettily());
                } else {
                    LOG.error(res.cause());
                }
            });
        });

        return router;
    }
}
