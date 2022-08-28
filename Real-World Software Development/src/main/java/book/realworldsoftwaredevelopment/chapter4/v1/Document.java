package book.realworldsoftwaredevelopment.chapter4.v1;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public class Document {
    private final Map<String, String> attributes;
}
