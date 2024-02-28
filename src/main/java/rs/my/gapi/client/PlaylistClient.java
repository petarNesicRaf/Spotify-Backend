package rs.my.gapi.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.tags.HtmlEscapeTag;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlaylistClient {
    public String fetchUserPlaylists(String accessToken)
    {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.spotify.com/v1/me/playlists",
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                String.class
        );

        System.out.println("Response status code: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            //todo
            return null;
        }
    }

    public String fetchTracksFromPlaylist(String accessToken, String playlistId)
    {
        //https://api.spotify.com/v1/playlists/3cEYpjA9oz9GiPac4AsH4n/tracks
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.spotify.com/v1/playlists/" + playlistId+"/tracks",
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

    public String fetchTrackAnalysis(String accessToken, String query)
    {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        System.out.println("QUERY");
        System.out.println("https://api.spotify.com/v1/audio-features?" +  query);
        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.spotify.com/v1/audio-features?" + query,
                    HttpMethod.GET,
                    new HttpEntity<>(httpHeaders),
                    String.class
        );
        if(response.getStatusCode() != HttpStatus.OK  || response.getBody().isEmpty() || response.getBody()==null)
            return null;//todo
        System.out.println("RESPONSE");
        System.out.println(response.getBody());

        return response.getBody();
    }
}
