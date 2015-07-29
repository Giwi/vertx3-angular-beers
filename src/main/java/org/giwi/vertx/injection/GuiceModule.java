package org.giwi.vertx.injection;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * The type Guice module.
 */
public class GuiceModule extends AbstractModule {
    /**
     * The Config.
     */
    private JsonObject config;
    /**
     * The Vertx.
     */
    private Vertx vertx;

    /**
     * Instantiates a new Guice module.
     *
     * @param config the config
     * @param vertx  the vertx
     */
    public GuiceModule(JsonObject config, Vertx vertx) {
        this.config = config;
        this.vertx = vertx;
    }

    /**
     * Guice module
     */
    @Override
    protected void configure() {
        //get the vertx configuration
        JsonObject mongoConfig = config.getJsonObject("mongo.db");

        bind(JsonObject.class)
                .annotatedWith(Names.named("mongo.db"))
                .toInstance(mongoConfig);
        bind(Vertx.class).toInstance(vertx);
        bind(MongoClient.class).toProvider(MongoClientProvider.class).asEagerSingleton();
    }
}
