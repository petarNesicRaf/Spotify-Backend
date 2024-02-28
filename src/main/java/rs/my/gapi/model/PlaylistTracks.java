package rs.my.gapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistTracks {
    private int total;
    private List<Track> tracks;
}
