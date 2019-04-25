package com.example.mohamed.newswithpreferences;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils() {
    }

    public static List<Article> fetchArticleData(String requestUrl) {

        URL url = createUrl(requestUrl);


        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {

            Log.e(LOG_TAG, "It was not possible to connect to the server", e);
        }


        List<Article> articles = extractFeatureFromJson(jsonResponse);

        return articles;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {

            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {

                inputStream.close();
            }
        }
        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Article> extractFeatureFromJson(String articlesJSON) {


        List<Article> articles = new ArrayList<>();

        try {

            if (TextUtils.isEmpty(articlesJSON)) {
                return null;
            }

            JSONObject baseJsonResponse = new JSONObject(articlesJSON);

            JSONObject responseObject = baseJsonResponse.getJSONObject("response");
            JSONArray articlesArray = responseObject.getJSONArray("results");

            if (articlesArray.length() > 0) {

                for (int i = 0; i < articlesArray.length(); i++) {

                    JSONObject articleObject = articlesArray.getJSONObject(i);


                    JSONArray categories = null;
                    categories = new JSONArray(articleObject.getString("tags"));
                    JSONObject itemJsonObject2 = null;


                    String section = "";
                    if (articleObject.has("sectionName")) {
                        section = articleObject.getString("sectionName");
                    }

                    String date = "";
                    if (articleObject.has("webPublicationDate")) {
                        date = articleObject.getString("webPublicationDate");
                    }

                    String title = "";
                    if (articleObject.has("webTitle")) {
                        title = articleObject.getString("webTitle");
                    }

                    String url = "";
                    if (articleObject.has("webUrl")) {
                        url = articleObject.getString("webUrl");
                    }


                    String authorName = "";
                    for (int k = 0; k < categories.length(); k++) {

                        itemJsonObject2 = categories.getJSONObject(k);

                        if (itemJsonObject2.has("webTitle")) {
                            authorName = itemJsonObject2.getString("webTitle");
                        }
                    }


                    Article article = new Article(section, title, date, url, authorName);

                    articles.add(article);
                }
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the article JSON results", e);
        }

        return articles;
    }

}
