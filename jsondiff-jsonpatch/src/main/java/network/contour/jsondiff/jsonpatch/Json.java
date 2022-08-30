package network.contour.jsondiff.jsonpatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Json {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static JsonNode parse(Path path)  {
        try(Reader reader = Files.newBufferedReader(path)) {
            return objectMapper.readTree(reader);
        }
    }

    public static ObjectWriter objectWriter(boolean prettyPrint) {
        return prettyPrint ? objectMapper.writerWithDefaultPrettyPrinter() : objectMapper.writer();
    }
}
