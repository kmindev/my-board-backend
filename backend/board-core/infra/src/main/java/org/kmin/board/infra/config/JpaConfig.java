package org.kmin.board.infra.config;

import org.kmin.board.infra.MyBoardConfig;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EntityScan("org.kmin.board")
@EnableJpaRepositories("org.kmin.board")
public class JpaConfig implements MyBoardConfig {
}
