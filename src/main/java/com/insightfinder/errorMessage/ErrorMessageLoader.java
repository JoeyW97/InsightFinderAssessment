package com.insightfinder.errorMessage;


import com.insightfinder.repo.ErrorMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ErrorMessageLoader {
    private static final Logger logger = LoggerFactory.getLogger(ErrorMessageLoader.class);
    private final ErrorMessageRepository repository;

    public ErrorMessageLoader(ErrorMessageRepository repository) {
        this.repository = repository;
    }

    /**
     * Load error message in the given files concurrently and store into the error message database
     *
     * @param fileNames file name of the files that contains error messages
     */
    public void loadErrorMessageFiles(List<String> fileNames) {
        ExecutorService executorService = Executors.newFixedThreadPool(fileNames.size());
        List<CompletableFuture<List<String>>> futures = new ArrayList<>();
        for (String fileName : fileNames) {
            CompletableFuture<List<String>> future = CompletableFuture.supplyAsync(() -> {
                try (BufferedReader reader =
                             new BufferedReader(
                                     new InputStreamReader(Objects.requireNonNull(
                                             getClass().getClassLoader().getResourceAsStream(fileName))))) {
                    List<String> messages = new ArrayList<>();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        messages.add(line);
                    }
                    // Can use a parser util class to parse the file if the file content get more complex in the future
                    logger.debug("loaded " + fileName);
                    return messages;
                } catch (Exception e) {
                    logger.error("Error loading error message file: {}, {}", fileName, e.getMessage());
                    return List.of();
                }
            }, executorService);
            futures.add(future);
        }

        logger.debug("Loaded {} error message files", futures.size());

        CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0]));
        CompletableFuture<List<String>> allResults = allOfFuture.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .collect(Collectors.toList()));
        allResults.thenAccept(messages -> {
            repository.addErrorMessages(messages);
            executorService.shutdown();
        });
        allResults.join();
    }
}
