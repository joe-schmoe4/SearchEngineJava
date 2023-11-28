package cecs429.indexing;

import java.nio.file.Path;
import java.util.List;

public class DiskPositionalIndex implements Index {
    private Path mFilePath;

    public DiskPositionalIndex(Path absoluteFilePath) {
        mFilePath = absoluteFilePath;
    }
    @Override
    public List<Posting> getPostings(String term) {
        return null;
    }

    @Override
    public List<String> getVocabulary() {
        return null;
    }
}
