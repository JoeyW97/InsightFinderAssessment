package com.insightfinder.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

public class ErrorMessageRepositoryTest {
    private ErrorMessageRepository repository;

    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        Field instance = ErrorMessageRepository.class.getDeclaredField("repository");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    public void testAddMessage() {
        repository = ErrorMessageRepository.getRepository();
        repository.addErrorMessages(List.of("NullPointerException"));
        assertThat(repository.findAllErrorMessages())
                .containsExactly("NullPointerException");
    }

    @Test
    public void testAddMessageConcurrently() throws InterruptedException {
        repository = ErrorMessageRepository.getRepository();
        List<String> messages = List.of("error1, error2, error3");
        ExecutorService service = Executors.newFixedThreadPool(messages.size());
        CountDownLatch latch = new CountDownLatch(messages.size());
        for (String msg : messages) {
            service.execute(() -> {
                repository.addErrorMessages(List.of(msg));
                latch.countDown();
            });
        }
        latch.await();
        assertThat(repository.findAllErrorMessages())
                .containsExactlyElementsOf(messages);
    }

    @ParameterizedTest
    @ValueSource(strings = {"error", "ERROR", "eRroR"})
    public void testFindMatchingMessages(String keyword) {
        repository = ErrorMessageRepository.getRepository();
        List<String> messages = List.of("error1", "error2", "abcd");
        repository.addErrorMessages(messages);
        var matchedMessages = repository.findMatchingMessages(keyword);
        assertThat(matchedMessages)
                .containsExactly("error1", "error2");
    }
}
