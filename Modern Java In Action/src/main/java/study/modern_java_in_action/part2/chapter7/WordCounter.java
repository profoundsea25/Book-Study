package study.modern_java_in_action.part2.chapter7;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WordCounter {
    private final int counter;
    private final boolean lastSpace;

    // `WordCounter` 상태를 어떻게 바꿀 것인지 (= 새로운 `WordCounter` 클래스를 어떤 상태로 생성할 것인지) 정의
    public WordCounter accumulate(Character c) {
        if (Character.isWhitespace(c)) {
            return lastSpace ? this : new WordCounter(counter, true);
        } else {
            return lastSpace ? new WordCounter(counter + 1, false) : this;
        }
    }

    // 문자열 서브 스트림을 처리한 `WordCounter`의 결과를 합친다.
    public WordCounter combine(WordCounter wordCounter) {
        return new WordCounter(counter + wordCounter.counter, wordCounter.lastSpace);
    }

    public int getCounter() {
        return counter;
    }
}
