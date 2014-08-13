package util;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpUtils {

    public static String httpGetJson(String url) throws ClientProtocolException, IOException {
        return httpGetJson(new HttpGet(url));
    }

    public static String httpGetJson(URI uri) throws ClientProtocolException, IOException {
        return httpGetJson(new HttpGet(uri));
    }

    public static String httpGetJson(HttpGet request) throws ClientProtocolException, IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(request);

        String json = "";
        if (response.getStatusLine().getStatusCode() == 200) {
            json = IOUtils.toString(response.getEntity().getContent());
        }

        return json;
    }
}
