package rs.my.gapi.client;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserClient {
    public String fetchCurrentUser(String accessToken)
    {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.spotify.com/v1/me",
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                String.class
        );
        if(response.getStatusCode() != HttpStatus.OK ||response.getBody().isEmpty() || response.getBody()==null)
        {
            //todo
            return null;
        }
        return response.getBody();
    }
}
