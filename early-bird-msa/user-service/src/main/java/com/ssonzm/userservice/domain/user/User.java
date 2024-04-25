package com.ssonzm.userservice.domain.user;

import com.ssonzm.coremodule.domain.BaseEntity;
import com.ssonzm.userservice.common.converter.StringEncryptUniqueConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.ssonzm.userservice.dto.user.UserRequestDto.UserUpdateReqDto;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Convert(converter = StringEncryptUniqueConverter.class)
    @Column(name = "user_name", nullable = false)
    private String name;

    @Convert(converter = StringEncryptUniqueConverter.class)
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Convert(converter = StringEncryptUniqueConverter.class)
    @Column(nullable = false)
    private String phoneNumber;

    @Convert(converter = StringEncryptUniqueConverter.class)
    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Builder
    public User(Long id, String username, String email, String password,
                String phoneNumber, String address, UserRole role) {
        this.id = id;
        this.name = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateUserInfo(UserUpdateReqDto userUpdateReqDto) {
        this.phoneNumber = userUpdateReqDto.getPhoneNumber();
        this.address = userUpdateReqDto.getAddress();
    }
}
