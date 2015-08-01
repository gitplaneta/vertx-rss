package eu.busz.rss;

import eu.busz.rss.util.HttpService;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class MainVerticleTest {

    private Vertx vertx;
    private HttpService http;

    @Before
    public void setUp(TestContext context) {
        vertx = Vertx.vertx();
        http = new HttpService(vertx);
        vertx.deployVerticle(MainVerticle.class.getName(), context.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void test(TestContext context) {
        final Async async = context.async();
        http.get(8080, "localhost", "/").thenAccept(body -> {
            context.assertEquals("Hello world", body);
            async.complete();
        });
    }
}
