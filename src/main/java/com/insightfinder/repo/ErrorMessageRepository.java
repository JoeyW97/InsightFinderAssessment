package com.insightfinder.repo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A singleton ErrorMessageRepository in the project
 */
public class ErrorMessageRepository {
    private static ErrorMessageRepository repository;

    // errorMessageDb can be extended to store message objects if message structure is more complex in the future
    private final List<String> errorMessageDb;

    private ErrorMessageRepository() {
        errorMessageDb = Collections.synchronizedList(new ArrayList<>());
    }

    public static ErrorMessageRepository getRepository() {
        if (repository == null) {
            repository = new ErrorMessageRepository();
        }
        return repository;
    }

    public void addErrorMessages(List<String> errorMessages) {
        errorMessageDb.addAll(errorMessages);
    }

    /**
     * Find the matching message for the given keyword (case ignored)
     * @param keyword keyword to search for
     * @return list of messages that contains the keyword
     */
    public List<String> findMatchingMessages(String keyword) {
        List<String> matchedMessages;
        synchronized (errorMessageDb) {
            matchedMessages = errorMessageDb.stream()
                    .filter(message -> message.toLowerCase().contains(keyword.toLowerCase()))
                    .toList();
        }
        return matchedMessages;
    }

    public List<String> findAllErrorMessages() {
        return new ArrayList<>(errorMessageDb);
    }
}
