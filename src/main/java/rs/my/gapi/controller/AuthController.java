package rs.my.gapi.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.http.*;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import rs.my.gapi.dto.AuthRequest;
import rs.my.gapi.dto.AuthResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    //todo secure ids and keys
    private static final String STATE_KEY = "spotify_auth_state";
    private static final String CLIENT_ID = "77e6a100dda94ff18033fd1ab16a4d44";
    private static final String CLIENT_SECRET = "b4ea0c728b26461e8c6a3dd8640fcb6f";
    private static final String REDIRECT_URI = "http://localhost:8080/api/v1/auth/callback";
    private static final String SCOPE = "user-read-private user-read-email";

    @GetMapping
    public ResponseEntity<String> hello()
    {
        return ResponseEntity.ok("helloo admin");
    }



    @GetMapping("/login")
    public void login(HttpServletResponse response) {
        String state = generateRandomString(16);
        String encodedScope = URLEncoder.encode(SCOPE, StandardCharsets.UTF_8);
        // Set cookie
        ResponseCookie stateCookie = ResponseCookie.from(STATE_KEY, state).build();
        String authorizationUrl = UriComponentsBuilder
                .fromUriString("https://accounts.spotify.com/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", CLIENT_ID)
                .queryParam("scope", encodedScope)
                .queryParam("redirect_uri", REDIRECT_URI)
                .queryParam("state", state)
                .queryParam("show_dialog", true)
                .build().toUriString();

        //return ResponseEntity.status(302).location(URI.create(authorizationUrl)).header(HttpHeaders.SET_COOKIE, stateCookie.toString()).build();
        try {
            response.setHeader(HttpHeaders.SET_COOKIE, stateCookie.toString());
            response.sendRedirect(authorizationUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/callback")
    public ResponseEntity callback(
            @CookieValue(value = STATE_KEY,required = false) String stateCookie,
            @RequestParam("code") String code,
            @RequestParam("state") String state){

        if (stateCookie == null || !state.equals(stateCookie)) {
            URI errorUri = URI.create("/#error=state_mismatch");
            return ResponseEntity.status(302).location(errorUri).build();
        }
        List<NameValuePair> authParams = new ArrayList<>();
        authParams.add(new BasicNameValuePair("code", code));
        authParams.add(new BasicNameValuePair("redirect_uri", REDIRECT_URI));
        authParams.add(new BasicNameValuePair("grant_type", "authorization_code"));

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost tokenRequest = new HttpPost("https://accounts.spotify.com/api/token");
        tokenRequest.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + base64Encode(CLIENT_ID + ":" + CLIENT_SECRET));
        tokenRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");

        try {
            tokenRequest.setEntity(new UrlEncodedFormEntity(authParams));
            org.apache.http.HttpResponse response = httpClient.execute(tokenRequest);
            if (response.getStatusLine().getStatusCode() == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());


                return ResponseEntity.ok(responseBody);
            } else {
                URI errorUri = UriComponentsBuilder.fromUriString("/#error=invalid_token").build().toUri();
                return ResponseEntity.status(302).location(errorUri).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    private String base64Encode(String input) {
        return java.util.Base64.getEncoder().encodeToString(input.getBytes());
    }
    private String generateRandomString(int length) {
        String possibleCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder(length);
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = r.nextInt(possibleCharacters.length());
            randomString.append(possibleCharacters.charAt(randomIndex));
        }
        return randomString.toString();
    }
}
