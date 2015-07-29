package org.giwi.vertx.injection;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;


/**
 * The type Mongo client provider.
 */
public class MongoClientProvider implements Provider<MongoClient> {
    /**
     * The Config.
     */
    @Inject
    @Named("mongo.persistor")
    private JsonObject config;
    /**
     * The Vertx.
     */
    @Inject
    private Vertx vertx;

    /**
     * Provides a fully-constructed and injected instance of {@code T}.
     *
     * @return the mongo client
     *
     * @throws RuntimeException if the injector encounters an error while
     *                          providing an instance. For example, if an injectable member on
     *                          throws an exception, the injector may wrap the exception
     *                          and throw it to the caller of
     *                          . Callers should not try
     *                          to handle such exceptions as the behavior may vary across injector
     *                          implementations and even different configurations of the same injector.
     */
    @Override
    public MongoClient get() {
        return MongoClient.createShared(vertx, config.getJsonObject("mongo.db"));
    }
}
