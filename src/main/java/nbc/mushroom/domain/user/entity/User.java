package nbc.mushroom.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbc.mushroom.domain.common.dto.AuthUser;
import nbc.mushroom.domain.common.entity.Timestamped;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "`user`")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;

    @Builder
    public User(Long id, String email, String password, String nickname, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.userRole = userRole;
    }


    public static User fromAuthUser(AuthUser authUser) {
        return User.builder()
            .id(authUser.id())
            .email(authUser.email())
            .userRole(authUser.userRole())
            .build();
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void updateRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
