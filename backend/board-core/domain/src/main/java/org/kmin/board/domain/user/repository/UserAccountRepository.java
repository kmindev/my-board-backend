package org.kmin.board.domain.user.repository;

import org.kmin.board.domain.user.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
}
