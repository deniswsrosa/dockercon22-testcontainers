package com.denis.dockercon.repository;

import com.denis.dockercon.entity.GameUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameUserRepository extends JpaRepository<GameUser, Long> {
}
