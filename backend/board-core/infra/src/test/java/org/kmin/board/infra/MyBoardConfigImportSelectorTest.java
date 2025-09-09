package org.kmin.board.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.lang.annotation.Annotation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;

@DisplayName("[Infra 설정] MyBoardConfigImportSelector 테스트")
class MyBoardConfigImportSelectorTest {

    private MyBoardConfigImportSelector sut = new MyBoardConfigImportSelector();

    @Test
    void entryEqualsSameInstance() {
        // Given
        MyBoardConfigGroup[] group = new MyBoardConfigGroup[]{
            MyBoardConfigGroup.JPA
        };
        AnnotationMetadata metadata = mock(AnnotationMetadata.class);
        EnableMyBoardConfig annotation = enableMyBoardConfig(group);

        given(metadata.getAnnotationAttributes(EnableMyBoardConfig.class.getName()))
            .willReturn(AnnotationUtils.getAnnotationAttributes(annotation));

        // When
        String[] result = sut.selectImports(metadata);

        // Then
        assertThat(result).hasSize(group.length);
        for (MyBoardConfigGroup myBoardConfigGroup : group) {
            Class<? extends MyBoardConfig> expectedConfigClass = myBoardConfigGroup.getConfigClass();
            assertThat(result).contains(expectedConfigClass.getName());
        }
    }

    private EnableMyBoardConfig enableMyBoardConfig(MyBoardConfigGroup... group) {
        return new EnableMyBoardConfig() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return EnableMyBoardConfig.class;
            }

            @Override
            public MyBoardConfigGroup[] value() {
                return group;
            }
        };
    }

}