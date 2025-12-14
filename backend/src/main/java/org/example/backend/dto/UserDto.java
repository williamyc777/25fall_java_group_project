package org.example.backend.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.backend.domain.User;

@Setter
@Getter
public class UserDto {
    Long id;
    String username;
    String name;
    String password;
    String bio;
    String avatar;
    Boolean canCreate;
    Boolean canEnroll;
    Boolean canComment;
    public UserDto() {
    }
    public UserDto(Long id, String username, String name, String password, String bio, String avatar, Boolean canCreate, Boolean canEnroll, Boolean canComment) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.bio = bio;
        this.avatar = avatar;
        this.canCreate = canCreate;
        this.canEnroll = canEnroll;
        this.canComment = canComment;
    }

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.password = user.getPassword();
        this.bio = user.getBio();
        this.avatar = user.getAvatar();
        this.canCreate = user.getPermission().isCanCreate();
        this.canEnroll = user.getPermission().isCanEnroll();
        this.canComment = user.getPermission().isCanComment();
    }
}
