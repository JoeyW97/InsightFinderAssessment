package com.insightfinder.models;

public class SearchStats {
    private final String keyword;
    private final int matchedMessageCount;
    private final double elapseTime;
    private final String searchTime;
    private final int keywordOccurrence;

    public SearchStats(String keyword, int matchedMessageCount, int keywordOccurrence, double elapseTime, String searchTime) {
        this.keyword = keyword;
        this.matchedMessageCount = matchedMessageCount;
        this.elapseTime = elapseTime;
        this.searchTime = searchTime;
        this.keywordOccurrence = keywordOccurrence;
    }

    @Override
    public String toString() {
        return String.format("%-25s: {matchedMessageCount: %d, keywordOccurrence, %d, elapseTime: %.1fms, searchTime: %s}",
                "keyword='" + keyword + "'", matchedMessageCount, keywordOccurrence, elapseTime, searchTime);
    }
}