package Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;

public class FileHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            String path = exchange.getRequestURI().getPath();
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                if (path.isEmpty() || path.equals("/")) {
                    path = "index.html";
                }
                String filePath = "/Users/roscoeevans/Desktop/code/240/truefms/familymapserver/src/main/web/" + path;
                File file = new File(filePath);
                if (file.exists() && file.canRead()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream response = exchange.getResponseBody();
                    Files.copy(file.toPath(), response);
                    response.close();
                    success = true;
                }
                if (!success) {
                    path = "HTML/404.html";
                    filePath = "/Users/roscoeevans/Desktop/code/240/truefms/familymapserver/src/main/web/" + path;
                    file = new File(filePath);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    OutputStream response = exchange.getResponseBody();
                    Files.copy(file.toPath(), response);
                    response.close();
                }
            }
        }
        catch(IOException ex) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getRequestBody().close();
        }

    }
}
