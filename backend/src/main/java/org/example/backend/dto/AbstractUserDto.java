package org.example.backend.dto;
import lombok.Getter;
import lombok.Setter;
import org.example.backend.domain.AbstractUser;
import org.example.backend.domain.Admin;
import org.example.backend.domain.User;
import org.example.backend.domain.enums.UserType;

@Setter
@Getter
public class AbstractUserDto {
    Long id;
    String username;
    String name;
    String password;
    UserType userType;
    Boolean hasUnread;
    String bio;
    String avatar;

    public AbstractUserDto() {
    }
    public AbstractUserDto(AbstractUser abstractUser) {
        this.id  = abstractUser.getId();
        this.username = abstractUser.getUsername();
        this.name = abstractUser.getName();
        this.password = abstractUser.getPassword();
        if (abstractUser instanceof Admin) {
            this.userType = UserType.Admin;
        }
        if (abstractUser instanceof User){
            this.userType = UserType.User;
        }
        this.avatar = abstractUser.getAvatar();
    }
}
