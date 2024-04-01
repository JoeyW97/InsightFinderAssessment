package com.insightfinder.service;


import com.insightfinder.errorMessage.ErrorMessageLoader;
import com.insightfinder.models.FindMessageResult;
import com.insightfinder.repo.ErrorMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ErrorMessageMatchingServiceTest {
    private ErrorMessageRepository repository;
    private ErrorMessageMatchingService service;
    private ErrorMessageLoader loader;

    @BeforeEach
    void setup() {
        repository = mock(ErrorMessageRepository.class);
        loader = mock(ErrorMessageLoader.class);
        service = new ErrorMessageMatchingService(repository, loader);
    }

    @Test
    void convertMatchedWordsToUpperCase() {
        when(repository.findMatchingMessages(anyString())).thenReturn(List.of("error1 in error2", "NullPointerError", "ErrOr"));
        var matches = service.findMatchingMessages("error");
        assertThat(matches).containsExactly(
                new FindMessageResult(2, "ERROR1 in ERROR2"),
                new FindMessageResult(1, "NullPointerERROR"),
                new FindMessageResult(1, "ERROR")
        );
    }
}
