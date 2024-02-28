package rs.my.gapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String access_token;
    private String token_type;
    private String scope;
    private int expires_in;
    private String refresh_token;
}
