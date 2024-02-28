package rs.my.gapi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.my.gapi.client.PlaylistClient;
import rs.my.gapi.model.Playlist;
import rs.my.gapi.model.PlaylistTracks;
import rs.my.gapi.model.Track;
import rs.my.gapi.model.TrackFeatures;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlaylistServiceImpl {
    private final PlaylistClient playlistClient;

    @Autowired
    public PlaylistServiceImpl(PlaylistClient playlistClient) {
        this.playlistClient = playlistClient;
    }
    public List<Playlist> getUserPlaylists(String accessToken)
    {
        String playlistString = playlistClient.fetchUserPlaylists(accessToken);
        if(playlistString==null || playlistString =="")
            throw new RuntimeException("0 playlists were returned");

        ObjectMapper objectMapper = new ObjectMapper();
        List<Playlist> playlists = new ArrayList<>();
        try {
            JsonNode jsonResponse = objectMapper.readTree(playlistString);
            JsonNode items = jsonResponse.get("items");
            for(JsonNode item:items)
            {
                Playlist playlist = new Playlist(item.get("id").asText(), item.get("name").asText(),item.get("description").asText());
                playlists.add(playlist);
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return playlists;
    }

    public List<Track> getTracksFromPlaylist(String accessToken, String playlistId)
    {
        String playlistTracks = playlistClient.fetchTracksFromPlaylist(accessToken,playlistId);

        if(playlistTracks.isEmpty() || playlistTracks == null || playlistTracks.equals("[]"))
            return null; //todo

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            PlaylistTracks playlistWrapper = new PlaylistTracks();

            JsonNode jsonResponse = objectMapper.readTree(playlistTracks);
            playlistWrapper.setTotal(jsonResponse.get("total").asInt());
            JsonNode items = jsonResponse.get("items");
            List<Track> tracks = new ArrayList<>();
            //List<String> trackIds = new ArrayList<>();
            String queryId = "";
            System.out.println("ITEMS");
            System.out.println(jsonResponse.get("items"));
            for(JsonNode item:items)
            {
                Track track = new Track();
                track.setAddedAt(item.get("added_at").asText());
                track.setAddedByHref(item.get("added_by").get("href").asText());
                track.setLocal(item.get("is_local").asBoolean());
                JsonNode artists = item.get("track").get("artists");
                for(JsonNode artist : artists)
                {
                    track.getArtistNames().add(artist.get("name").asText());
                }

//                if(item.get("track").get("explicit") != null)
//                    track.setExplicit(item.get("explicit").asBoolean());

                track.setId(item.get("track").get("id").asText());
                track.setName(item.get("track").get("name").asText());
                track.setPopularity(item.get("track").get("popularity").asInt());
                track.setPreviewUrl(item.get("track").get("preview_url").asText());


                tracks.add(track);

                queryId = queryId.concat(track.getId()).concat(",");
                //trackIds.add(track.getId());
            }
            //todo 100 max ids
            String queryParam = "ids=" +  queryId.substring(0, queryId.length() - 1);

            String featureResponse = playlistClient.fetchTrackAnalysis(accessToken, queryParam);

            JsonNode featureJson = objectMapper.readTree(featureResponse);
            JsonNode audioFeatures = featureJson.get("audio_features");
            //List<TrackFeatures> trackFeatures = new ArrayList<>();
            int i = 0;
            for(JsonNode audio:audioFeatures)
            {
                TrackFeatures tf = new TrackFeatures();
                tf.setAcousticness(audio.get("acousticness").asDouble());
                tf.setDanceability(audio.get("danceability").asDouble());
                tf.setDurationMs(audio.get("duration_ms").asInt());
                tf.setEnergy(audio.get("energy").asDouble());
                tf.setInstrumentalness(audio.get("instrumentalness").asDouble());
                tf.setKey(audio.get("key").asInt());
                tf.setLiveness(audio.get("liveness").asDouble());
                tf.setLoudness(audio.get("loudness").asDouble());
                tf.setMode(audio.get("mode").asInt());
                tf.setSpeechiness(audio.get("speechiness").asDouble());
                tf.setTempo(audio.get("tempo").asDouble());
                tf.setValence(audio.get("valence").asDouble());

                //trackFeatures.add(tf);
                tracks.get(i).setTrackFeatures(tf);
                i++;
            }

            return tracks;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
