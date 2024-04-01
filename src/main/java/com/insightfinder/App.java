package com.insightfinder;

import com.insightfinder.exceptions.ErrorMessageLoaderException;
import com.insightfinder.models.FindMessageResult;
import com.insightfinder.models.SearchStats;
import com.insightfinder.service.ErrorMessageMatchingService;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final String EXIT_CODE = "-1";

    private String getCurrentTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
    }

    public void run() {
        ErrorMessageMatchingService errorMessageMatchingService;
        try {
            errorMessageMatchingService = new ErrorMessageMatchingService();
        } catch (ErrorMessageLoaderException e) {
            logger.error(e.getMessage());
            return;
        }
        Scanner scanner = new Scanner(System.in);
        int searchCount = 0;
        int totalMatchedMessageCount = 0;
        int totalKeywordOccurrence = 0;
        List<SearchStats> searchStats = new ArrayList<>();

        System.out.println("Welcome to use Error Message Search System!");

        while (true) {
            System.out.print("Please enter a keyword to search for or -1 to exit: ");
            String userInput = scanner.nextLine();
            userInput = userInput.trim();
            // this can be included in a validation method if more validations are needed in the future
            if (Strings.isEmpty(userInput)) {
                System.out.println("Please provide a non-empty keyword");
                continue;
            }

            // user exit the program
            if (userInput.equals(EXIT_CODE)) {
                break;
            }

            // start searching
            searchCount += 1;
            String searchTime = getCurrentTimestamp();
            long startTime = System.currentTimeMillis();
            List<FindMessageResult> findMessageResults = errorMessageMatchingService.findMatchingMessages(userInput);
            long endTime = System.currentTimeMillis();
            //display message found
            findMessageResults.forEach(findMessageResult -> System.out.println(findMessageResult.matchedMessage()));
            long elapsedTimeMillis = endTime - startTime;
            totalMatchedMessageCount += findMessageResults.size();
            int keywordOccurrence = findMessageResults.stream().mapToInt(FindMessageResult::keywordCount).sum();
            totalKeywordOccurrence += keywordOccurrence;
            searchStats.add(
                    new SearchStats(userInput, findMessageResults.size(), keywordOccurrence, elapsedTimeMillis, searchTime)
            );
            if (findMessageResults.isEmpty()) {
                System.out.println("No matched error message found for '" + userInput + "'");
            }
        }

        //print search summary
        System.out.println("\n*****************************************************");
        System.out.println("Thanks for using our Error Message Search System!");
        System.out.println("Here is your search summary: ");
        System.out.printf("Total searches conducted: %d\n", searchCount);
        System.out.printf("Total matched messages found: %d\n", totalMatchedMessageCount);
        System.out.printf("Total keyword occurrence: %d\n", totalKeywordOccurrence);
        searchStats.forEach(System.out::println);
    }
}
