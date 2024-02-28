package rs.my.gapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Track {
    private String id;
    private String name;
    private List<String> artistNames = new ArrayList<>();
    private String previewUrl;
    private String addedAt;
    private String addedByHref;
    private boolean explicit;
    private int popularity;
    private boolean isLocal;
    //todo artists

    private TrackFeatures trackFeatures;

}
