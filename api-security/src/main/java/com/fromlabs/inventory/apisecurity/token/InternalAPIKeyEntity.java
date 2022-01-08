package com.fromlabs.inventory.apisecurity.token;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity(name = "InternalAPIKey")
@Table(name="api_keys")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class InternalAPIKeyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hashed_api_key", unique = true, nullable = false)
    @NotNull(message = "Hashed API key must not be null")
    @NotBlank(message = "Hashed API key must not be blank")
    private String hashedApiKey;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "authority_principal", nullable = false)
    private String authorityPrincipal;

    @Column(name = "api_key_role", nullable = false)
    private String apiKeyRole;

    @Column(name = "updated_at")
    private String updatedAt;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "is_active")
    private boolean active;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        InternalAPIKeyEntity that = (InternalAPIKeyEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
