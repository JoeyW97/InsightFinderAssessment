# InsightFinder Coding Assessment

## Project Overview
This project provides a CLI to takes a keyword from user, searches and returns error messages contain the keyword. Matched keyword is shown in uppercase.

The sample error message files the program reads are in `src/main/resources`

## Running Step
### Environment Setup
Download and install Java 17
You can check your java version by
```shell
➜  ~ java -version                                                                                                               
openjdk version "17.0.2" 2022-01-18
```
[README.md](..%2F..%2FDownloads%2FInsightFinderAssessment%2FREADME.md)
### Run in Intellij
1. Open Intellij, open the project by choosing File -> Open.../.../InsightFinderCodingAssessment
2. Find and run `src/main/java/Main.java`
3. Run unit tests using `mvn test`

### Run the jar file
Run the jar file in your terminal with command
```shell
java -cp <path to the jar file>/InsightFinderAssessment/InsightFinderCodingAssessment-1.0-SNAPSHOT.jar  Main
```

### Test Plan
Start the program in Intellij or terminal
```shell
➜  ~ java -cp /Users/joeyw/IdeaProjects/InsightFinderCodingAssessment/target/InsightFinderCodingAssessment-1.0-SNAPSHOT.jar  Main
WARNING: sun.reflect.Reflection.getCallerClass is not supported. This will impact performance.
Welcome to use Error Message Search System!
Please enter a keyword to search for or -1 to exit: error
Syntax ERROR on token ";", { expected after this token
IOException: ERROR reading input
NoSuchMethodERROR: method not found
NoSuchFieldERROR: field not found
AssertionERROR: assertion failed
Please enter a keyword to search for or -1 to exit: warn
No matched error message found for 'warn'
Please enter a keyword to search for or -1 to exit: -1

*****************************************************
Thanks for using our Error Message Search System!
Here is your search summary: 
Total searches conducted: 2
Total matched messages found: 5
Total keyword occurrence: 5
keyword='error'          : {matchedMessageCount: 5, keywordOccurrence, 5, elapseTime: 11.0ms, searchTime: 2024-04-01 10:57:12}
keyword='warn'           : {matchedMessageCount: 0, keywordOccurrence, 0, elapseTime: 0.0ms, searchTime: 2024-04-01 10:57:21}
```

# Further Considerations
1. This program reads error message files concurrently. The intent is to speed up the file reading. However, this optimization may not work for the example files. Since the example files are small, the overhead of managing multiple threads may outweigh the benefits of parallelism. Multithreading can be more beneficial if we want to ingest multiple large files.
2. The error messages are stored in memory, we can store the messages in to database in the future for durability and scalability.
3. Due to the limit of time, no complex model and user input validation are used, which should be considered in real-world development.
