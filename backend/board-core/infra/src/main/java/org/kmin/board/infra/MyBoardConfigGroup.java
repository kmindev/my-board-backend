package org.kmin.board.infra;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.kmin.board.infra.config.JpaConfig;

@Getter
@RequiredArgsConstructor
public enum MyBoardConfigGroup {
    JPA(JpaConfig.class);

    private final Class<? extends MyBoardConfig> configClass;

}
