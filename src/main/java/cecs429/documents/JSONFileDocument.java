package cecs429.documents;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
public class JSONFileDocument implements FileDocument{

    private int mDocumentId;
    private Path mFilePath;
    ObjectMapper objectMapper = new ObjectMapper();
    public JSONFileDocument(int id, Path absoluteFilePath) {
        mDocumentId = id;
        mFilePath = absoluteFilePath;
    }

    @Override
    public int getId() {
        return mDocumentId;
    }

    @Override
    public Reader getContent() {
        try {
            return Files.newBufferedReader(mFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getTitle() {
        return mFilePath.getFileName().toString();
    }

    @Override
    public Path getFilePath() {
        return mFilePath;
    }
}
