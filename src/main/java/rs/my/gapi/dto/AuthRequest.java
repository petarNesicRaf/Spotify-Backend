package rs.my.gapi.dto;

public class AuthRequest {
    private String code;
    private String redirectUri;
    private String grantType;

    public AuthRequest() {
    }

    public AuthRequest(String code, String redirectUri, String grantType) {
        this.code = code;
        this.redirectUri = redirectUri;
        this.grantType = grantType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }
}
