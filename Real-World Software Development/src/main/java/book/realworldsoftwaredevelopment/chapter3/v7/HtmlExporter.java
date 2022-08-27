package book.realworldsoftwaredevelopment.chapter3.v7;

public class HtmlExporter implements Exporter {
    @Override
    public String export(SummaryStatistics summaryStatistics) {
        return "Something calculated";
    }
}
