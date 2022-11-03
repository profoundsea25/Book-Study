package study.modern_java_in_action.part1.chapter3;

import java.io.BufferedReader;
import java.io.IOException;

public interface BufferedReaderProcessor {
    String process(BufferedReader b) throws IOException;
}
