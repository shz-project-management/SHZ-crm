package CRM.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Setter
@Getter
@ComponentScan
@Component
public class PermissionPathsConfig {
    private List<String> authPermissionPathsForAll;
    private List<String> permissionPathsForAll;
    private List<String> permissionPathsForLeaders;
    private List<String> permissionPathsForUsers;

    @PostConstruct
    public void init() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("permission-paths.json");
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(inputStream);
            authPermissionPathsForAll = objectMapper.convertValue(rootNode.get("authPermissionPathsForAll"), new TypeReference<List<String>>(){});
            permissionPathsForAll = objectMapper.convertValue(rootNode.get("permissionPathsForAll"), new TypeReference<List<String>>(){});
            permissionPathsForLeaders = objectMapper.convertValue(rootNode.get("permissionPathsForLeaders"), new TypeReference<List<String>>(){});
            permissionPathsForUsers = objectMapper.convertValue(rootNode.get("permissionPathsForUsers"), new TypeReference<List<String>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doSomething() {
        // Use the authPermissionPathsForAll field to access the configuration values
    }
}