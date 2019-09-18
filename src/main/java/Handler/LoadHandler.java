package Handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import Request.LoadRequest;
import Response.LoadResponse;
import Service.LoadService;

public class LoadHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
            boolean success = false;

            try {
                if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                    Gson gson = new Gson();
                    Headers reqHeaders = exchange.getRequestHeaders();
                    InputStream reqBody = exchange.getRequestBody();
                    String reqData = readString(reqBody);

                    LoadRequest request = gson.fromJson(reqData, LoadRequest.class);
                    LoadService service = new LoadService();
                    LoadResponse response = service.load(request);
                    String respData = gson.toJson(response);

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream respBody = exchange.getResponseBody();
                    writeString(respData, respBody);

                    exchange.getResponseBody().close();
                    success = true;
                }

                if (!success) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    exchange.getResponseBody().close();
                }
            } catch (IOException e) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
                exchange.getResponseBody().close();
                e.printStackTrace();
            }
        }

        private String readString(InputStream is) throws IOException {
            StringBuilder sb = new StringBuilder();
            InputStreamReader sr = new InputStreamReader(is);
            char[] buf = new char[1024];
            int len;
            while ((len = sr.read(buf)) > 0) {
                sb.append(buf, 0, len);
            }
            return sb.toString();
        }

        private void writeString(String str, OutputStream os) throws IOException {
            OutputStreamWriter sw = new OutputStreamWriter(os);
            sw.write(str);
            sw.flush();
        }
    }

