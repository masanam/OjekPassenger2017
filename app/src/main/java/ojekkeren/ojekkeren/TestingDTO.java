package ojekkeren.ojekkeren;

import java.util.HashMap;
import java.util.List;

/**
 * Created by andi on 3/26/2016.
 */
public class TestingDTO {
    private String username;
    private String password;
    private List<TestingContentsDTO> contents;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<TestingContentsDTO> getContents() {
        return contents;
    }

    public void setContents(List<TestingContentsDTO> contents) {
        this.contents = contents;
    }


}
