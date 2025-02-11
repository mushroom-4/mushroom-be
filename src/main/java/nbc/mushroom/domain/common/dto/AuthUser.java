package nbc.mushroom.domain.common.dto;

import nbc.mushroom.domain.user.entity.UserRole;

public record AuthUser(Long id, String email, String nickname, UserRole userRole) {

}
