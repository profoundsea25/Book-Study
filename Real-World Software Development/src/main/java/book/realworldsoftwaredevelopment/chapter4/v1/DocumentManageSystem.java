package book.realworldsoftwaredevelopment.chapter4.v1;

import java.util.HashMap;
import java.util.Map;

public class DocumentManageSystem {

    private final Map<String, Importer> extensionToImporter = new HashMap<>();

    public DocumentManageSystem() {
//        extensionToImporter.put("letter", new LetterImporter());
//        extensionToImporter.put("report", new ReportImporter());
        extensionToImporter.put("jpg", new ImageImporter());
    }
}
