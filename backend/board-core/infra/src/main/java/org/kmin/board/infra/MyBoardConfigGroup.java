package org.kmin.board.infra;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.kmin.board.infra.jpa.JpaConfig;
import org.kmin.board.infra.rest_client.RestClientConfig;

@Getter
@RequiredArgsConstructor
public enum MyBoardConfigGroup {
    JPA(JpaConfig.class),
    REST_CLIENT(RestClientConfig.class);

    private final Class<? extends MyBoardConfig> configClass;

}
