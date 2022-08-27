package book.realworldsoftwaredevelopment.chapter3.v7;

public interface Exporter {
    // void 를 반환하는 것은 좋지 않다.
    String export(SummaryStatistics summaryStatistics);
}
