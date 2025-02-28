package com.domhub.api.repository;

import com.domhub.api.model.TypeRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TypeRoomRepository extends JpaRepository<TypeRoom, Long> {
    Optional<TypeRoom> findById(Integer idTypeRoom);

}
