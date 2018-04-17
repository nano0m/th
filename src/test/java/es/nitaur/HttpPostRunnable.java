package es.nitaur;

import org.apache.http.client.methods.*;
import org.apache.http.client.utils.*;
import org.apache.http.entity.*;
import org.apache.http.impl.client.*;
import org.springframework.http.*;
import java.io.*;
import java.net.*;

public class HttpPostRunnable implements Runnable {

    public static final String ANSWER_QUESTION_API_FIRST_QUESTION = "/api/quiz/answerQuestion/1";
    public static final String LOCALHOST = "localhost";
    public static final String HTTP = "http";
    public static final Integer MAX_RETRIES = 10;

    private final int port;
    private final int idx;

    public HttpPostRunnable(int port, int idx) {
        this.port = port;
        this.idx = idx;
    }

    @Override
    public void run() {
        try {
            URI uri = new URIBuilder()
                    .setScheme(HTTP)
                    .setHost(LOCALHOST)
                    .setPort(this.port)
                    .setPath(ANSWER_QUESTION_API_FIRST_QUESTION)
                    .build();

            HttpPost post = new HttpPost(uri);
            post.setHeader("Content-type", "application/json");
            post.setEntity(new StringEntity("[{\"answer\":\"Test " + this.idx + "\"}, {\"answer\": \"TEST " + this.idx + "\"}]"));

            System.out.println("Executing request # " + this.idx + post.getRequestLine());

            this.executeRequest(post, 1);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void executeRequest(HttpPost post, int retryIdx) throws IOException, InterruptedException {
        if (retryIdx < MAX_RETRIES) {
            CloseableHttpClient http_client = HttpClients.createDefault();
            CloseableHttpResponse response = http_client.execute(post);

            if (HttpStatus.INTERNAL_SERVER_ERROR.value() == response.getStatusLine().getStatusCode()) {
                System.out.println("Call failed, re-executing request # " + this.idx + post.getRequestLine());
                Thread.sleep(100);
                executeRequest(post, retryIdx++);
            }
        }
    }
}