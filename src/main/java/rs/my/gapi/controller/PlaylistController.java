package rs.my.gapi.controller;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.my.gapi.client.PlaylistClient;
import rs.my.gapi.model.Playlist;
import rs.my.gapi.model.Track;
import rs.my.gapi.service.impl.PlaylistServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/v1/playlist")
public class PlaylistController {
    //todo interface
    private final PlaylistServiceImpl playlistService;

    @Autowired
    public PlaylistController(PlaylistServiceImpl playlistService)
    {
        this.playlistService = playlistService;
    }

    @GetMapping("/user-playlists")
    public ResponseEntity<List<Playlist>> getUserPlaylists(@RequestHeader("Authorization") String authorizationHeader)
    {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String accessToken = authorizationHeader.substring(7);

        List<Playlist> playlists = playlistService.getUserPlaylists(accessToken);
        if(playlists == null)
            return ResponseEntity.status(404).build();

        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/{playlist_id}/playlist-tracks")
    public ResponseEntity<List<Track>> getTracksFromPlaylist(@RequestHeader("Authorization") String authorizationHeader,
                                                             @PathVariable String playlist_id)
    {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String accessToken = authorizationHeader.substring(7);
        List<Track> tracks = playlistService.getTracksFromPlaylist(accessToken, playlist_id);
        if(tracks == null || tracks.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok(tracks);
    }
}
