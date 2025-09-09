package org.kmin.board.api.user.infrastructure.repository;

import org.kmin.board.api.user.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
}
