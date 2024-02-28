package rs.my.gapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackFeatures {
    //from get tracks audio features
    private double tempo;
    private int key;
    private int mode;
    private double acousticness;
    private double danceability;
    private int durationMs;
    private double energy;
    private double instrumentalness;
    private double liveness;
    private double loudness;
    private double speechiness;
    private double valence;
}
