package com.insightfinder.service;


import com.insightfinder.errorMessage.ErrorMessageLoader;
import com.insightfinder.models.FindMessageResult;
import com.insightfinder.repo.ErrorMessageRepository;
import com.insightfinder.config.Configuration;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ErrorMessageMatchingService {
    private final ErrorMessageRepository repository;
    private final ErrorMessageLoader errorMessageLoader;

    public ErrorMessageMatchingService() {
        repository = ErrorMessageRepository.getRepository();
        errorMessageLoader = new ErrorMessageLoader(repository);
        errorMessageLoader.loadErrorMessageFiles(Configuration.getErrorMessageFiles());
    }

    public ErrorMessageMatchingService(ErrorMessageRepository repository, ErrorMessageLoader errorMessageLoader) {
        this.repository = repository;
        this.errorMessageLoader = errorMessageLoader;
    }

    /**
     * Find the matching message for the given keyword (case ignored)
     *
     * @param keyword keyword to search for
     * @return ist of messages that contains the keyword with the keyword in uppercase
     */
    public List<FindMessageResult> findMatchingMessages(String keyword) {
        String pattern = "(?i)(" + Pattern.quote(keyword) + ")";
        return repository.findMatchingMessages(keyword).stream()
                .map(msg -> {
                    String mappedMessage = msg.replaceAll(pattern, keyword.toUpperCase());
                    int count = 0;
                    Pattern p = Pattern.compile(keyword.toUpperCase());
                    Matcher matcher = p.matcher(mappedMessage);
                    while (matcher.find()) {
                        count++;
                    }
                    return new FindMessageResult(count, mappedMessage);
                }) // we can map the result to a Result object in the future if needed
                .collect(Collectors.toList());
    }
}
