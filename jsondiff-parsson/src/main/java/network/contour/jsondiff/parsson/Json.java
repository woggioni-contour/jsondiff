package network.contour.jsondiff.parsson;

import jakarta.json.JsonReader;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;
import jakarta.json.JsonWriter;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonGenerator;
import lombok.SneakyThrows;

import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public class Json {
    public static final JsonProvider provider;

    static {
        ServiceLoader<JsonProvider> sl = ServiceLoader.load(JsonProvider.class, Json.class.getClassLoader());
        provider = StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(sl.iterator(), 0), false)
                .findFirst()
                .orElseThrow(
                    () -> new ServiceConfigurationError(
                        String.format("No provider found for %s", JsonProvider.class.getName()))
                );
    }

    @SneakyThrows
    public static JsonStructure parse(Path path) {
        return parse(Files.newBufferedReader(path));
    }

    @SneakyThrows
    public static JsonStructure parse(Reader reader) {
        JsonStructure result;
        try (JsonReader jsonReader = provider.createReader(reader)) {
            result = jsonReader.read();
        }
        return result;
    }

    @SneakyThrows
    public static JsonStructure parse(InputStream inputStream) {
        return parse(new InputStreamReader(inputStream));
    }

    public interface Output extends Closeable {
        void write(JsonValue jsonValue);

        static Output fromWriter(Writer writer, boolean pretty) {
            if (pretty) {
                return new Json.Output() {
                    private JsonWriter jsonWriter = Json.provider.createWriter(writer);
                    @Override
                    public void write(JsonValue jsonValue) {
                        jsonWriter.write(jsonValue);
                    }

                    @Override
                    public void close() {
                        jsonWriter.close();
                    }
                };
            } else {
                return new Json.Output() {
                    private JsonGenerator jsonGenerator = Json.provider.createGenerator(writer);
                    @Override
                    public void write(JsonValue jsonValue) {
                        jsonGenerator.write(jsonValue);
                    }

                    @Override
                    public void close() {
                        jsonGenerator.close();
                    }
                };
            }
        }
    }
}
