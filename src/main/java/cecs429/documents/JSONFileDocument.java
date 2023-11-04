package cecs429.documents;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
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


    // Uses jackson databind to read json file
    @Override
    public Reader getContent() {
        try {
            JsonNode node = objectMapper.readTree(Files.newBufferedReader(mFilePath));
            JsonNode key = node.get("body");
            String jsonBody = key.toString();

            return new StringReader(jsonBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getTitle() {
        try {
            JsonNode node = objectMapper.readTree(Files.newBufferedReader(mFilePath));
            JsonNode key = node.get("title");
            String title = key.toString();

            return title;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        return mFilePath.getFileName().toString();
    }

    @Override
    public Path getFilePath() {
        return mFilePath;
    }

    public static FileDocument loadJSONFileDocument(Path absolutePath, int documentId) {
        return new JSONFileDocument(documentId, absolutePath);
    }
}
