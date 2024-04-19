package com.ssonzm.userservcie.domain.user;

import com.ssonzm.userservcie.common.converter.StringEncryptUniqueConverter;
import com.ssonzm.userservcie.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "Users")
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
}
