package Api;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import tworunpos.DebugScreen;

import java.io.IOException;
import java.net.URI;

public class RestServer extends Thread{

    private DebugScreen debugScreen = DebugScreen.getInstance();

    public void run() {
        String baseUrl = "http://localhost:8080";
        ResourceConfig config = new ResourceConfig();
        config.registerClasses(HalloWeltService.class);
        config.registerClasses(ArticleResource.class);
        final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create(baseUrl),config, false);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                server.shutdownNow();
            }
        }));

        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("REST-API started successfully!");
        debugScreen.print("REST-API started successfully!");

        System.out.println(String.format("\nGrizzly-HTTP-Server gestartet mit der URL: %s\n"
                        + "Stoppen des Grizzly-HTTP-Servers mit:      Strg+C\n",
                baseUrl + HalloWeltService.webContextPath));

        //Thread.currentThread().join();
    }


}