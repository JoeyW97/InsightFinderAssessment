package com.insightfinder.errorMessage;

import com.insightfinder.repo.ErrorMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ErrorMessageLoaderTest {
    private ErrorMessageLoader loader;
    private ErrorMessageRepository repository;

    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        Field instance = ErrorMessageRepository.class.getDeclaredField("repository");
        instance.setAccessible(true);
        instance.set(null, null);
        repository = ErrorMessageRepository.getRepository();
        loader = new ErrorMessageLoader(repository);
    }

    @Test
    public void loadErrorMessageFilesTest() {
        loader.loadErrorMessageFiles(List.of("testError1.txt", "testError2.txt"));
        assertThat(repository.findAllErrorMessages())
                .containsExactly("error1", "error2", "error3", "error4", "error5", "error6");
    }

    @Test
    @DisplayName("skip load files if files not exist")
    public void loadFileErrorTest() {
        loader.loadErrorMessageFiles(List.of("testError1.txt", "testError3.txt"));
        assertThat(repository.findAllErrorMessages())
                .containsExactly("error1", "error2", "error3");
    }
}

