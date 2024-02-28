package rs.my.gapi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.my.gapi.client.UserClient;
import rs.my.gapi.dto.UserDto;

@Service
public class UserServiceImpl {
    private final UserClient userClient;

    @Autowired
    public UserServiceImpl(UserClient userClient) {
        this.userClient = userClient;
    }

    public UserDto getCurrentUserProfile(String accessToken)
    {
        String userProfile = userClient.fetchCurrentUser(accessToken);

        if(userProfile.isEmpty() || userProfile == null)
            return null; //todo

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode profileNode = objectMapper.readTree(userProfile);
            UserDto userDto = new UserDto();
            userDto.setId(profileNode.get("id").asText());
            userDto.setDisplayName(profileNode.get("display_name").asText());
            userDto.setEmail(profileNode.get("email").asText());
            userDto.setCountry(profileNode.get("country").asText());
            userDto.setType(profileNode.get("type").asText());
            userDto.setTotalFollowers(profileNode.get("followers").get("total").asInt());

            return userDto;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
