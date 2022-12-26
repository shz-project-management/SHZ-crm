package CRM.utils;

import CRM.entity.User;
import CRM.entity.requests.RegisterUserRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@NoArgsConstructor
@PropertySource("classpath:application.properties")
public class GithubCodeDecoder {

    private String clientId;
    private String clientSecret;
    private String accessTokenUrl;
    private String redirectUrl;

    public GithubCodeDecoder(String clientId, String clientSecret, String accessTokenUrl, String redirectUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.accessTokenUrl = accessTokenUrl;
        this.redirectUrl = redirectUrl;
    }

    public RegisterUserRequest getUserDataFromCode(String code) throws IOException {
        String userToken = decodeGithubToken(code);
        return getUserData(userToken);
    }

    private String decodeGithubToken(String code) throws IOException {

        URL url = new URL(accessTokenUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        StringBuilder params = new StringBuilder();
        params.append("client_id=").append(URLEncoder.encode(clientId, "UTF-8"));
        params.append("&client_secret=").append(URLEncoder.encode(clientSecret, "UTF-8"));
        params.append("&code=").append(URLEncoder.encode(code, "UTF-8"));
        params.append("&redirect_uri=").append(URLEncoder.encode(redirectUrl, "UTF-8"));

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(params.toString());
        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + responseCode);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        JsonObject JSONresponse = new JsonParser().parse(reader).getAsJsonObject();
        reader.close();
        connection.disconnect();

        return JSONresponse.get("access_token").getAsString();
    }

    private RegisterUserRequest getUserData(String token) throws IOException {
        URL url = new URL("https://api.github.com/user");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + responseCode);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        JsonObject JSONresponse = new JsonParser().parse(reader).getAsJsonObject();
        reader.close();
        connection.disconnect();

        return new RegisterUserRequest(JSONresponse.get("name").getAsString(), token, getUserEmail(token));
    }

    public String getUserEmail(String token) throws IOException {
        URL url = new URL("https://api.github.com/user/emails");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + responseCode);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        JsonArray JSONresponse = new JsonParser().parse(reader).getAsJsonArray();
        reader.close();
        connection.disconnect();

        String email = null;
        for (JsonElement element : JSONresponse) {
            JsonObject object = element.getAsJsonObject();
            if (object.get("primary").getAsBoolean()) {
                email = object.get("email").getAsString();
                break;
            }
        }

        return email;
    }
}
