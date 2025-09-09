package org.kmin.board.api.config;

import org.kmin.board.infra.EnableMyBoardConfig;
import org.kmin.board.infra.MyBoardConfigGroup;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableMyBoardConfig(
    MyBoardConfigGroup.JPA
)
class InfraConfig {
}
