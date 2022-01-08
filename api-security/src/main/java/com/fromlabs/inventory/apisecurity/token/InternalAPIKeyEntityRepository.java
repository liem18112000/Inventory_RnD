package com.fromlabs.inventory.apisecurity.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternalAPIKeyEntityRepository
        extends JpaRepository<InternalAPIKeyEntity, Long> {

}