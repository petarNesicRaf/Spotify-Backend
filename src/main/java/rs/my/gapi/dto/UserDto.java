package rs.my.gapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;
    private String displayName;
    private String email;
    private int totalFollowers;
    private String type;
    private String country;
}
