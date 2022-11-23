package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class Main {
    private static final String URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static void main(String[] args) {
        String json = getJSONStringByURL(URL);
        List<Cat> cats = jsonToList(json);
        cats.stream().filter(p -> p.getUpvotes() != null && p.getUpvotes() > 0).forEach(p -> System.out.println(p.getText()));
    }

    private static String getJSONStringByURL(String url) {
        StringBuilder jsonStr = new StringBuilder();
        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build()) {
            HttpGet request = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                String tmp;
                while ((tmp = reader.readLine()) != null) {
                    jsonStr.append(tmp).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonStr.toString();
    }

    private static List<Cat> jsonToList(String json) {
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<List<Cat>>() {
        }.getType();
        return gson.fromJson(json, listType);
    }
}