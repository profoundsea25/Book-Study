package book.realworldsoftwaredevelopment.chapter3.v7;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SummaryStatistics {

    private final double sum;
    private final double max;
    private final double min;
    private final double average;
}
