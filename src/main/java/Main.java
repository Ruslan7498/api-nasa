import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;

public class Main {
    public static final String REMOTE_SERVICE_URL = "https://api.nasa.gov/planetary/apod?api_key=y7qkCG7gGGklpTggVqAfcdPnMe6JOfos2f2fjfBP";
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setUserAgent("My Test Service")
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectTimeout(5000)
                            .setSocketTimeout(30000)
                            .setRedirectsEnabled(false)
                            .build())
                    .build();
            response = getRespose(REMOTE_SERVICE_URL, httpClient);
            Header headers = mapper.readValue(response.getEntity().getContent(),
                    new TypeReference<Header>() {
                    });
            System.out.println(headers.toString());
            String url = headers.getUrl();
            response = getRespose(url, httpClient);
            File dir = makeDir(headers.getDate());
            String nameFile = createNameFile(url);
            saveFile(response, dir, nameFile);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static CloseableHttpResponse getRespose(String url, CloseableHttpClient httpClient) {
        CloseableHttpResponse response = null;
        HttpGet request = new HttpGet(url);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        try {
            response = httpClient.execute(request);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return response;
    }

    public static File makeDir(String name) {
        File dir = new File(name);
        dir.mkdir();
        return dir;
    }

    public static String createNameFile(String url) {
        int number = url.lastIndexOf("/");
        return url.substring(number + 1);
    }

    public static void saveFile(CloseableHttpResponse response, File dir, String nameFile) {
        try (FileOutputStream output = new FileOutputStream(new File(dir, nameFile));
             InputStream input = response.getEntity().getContent()) {
            byte[] bytes = new byte[100];
            int bytesRead = 0;
            while ((bytesRead = input.read(bytes, 0, bytes.length)) != -1) {
                output.write(bytes, 0, bytesRead);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
