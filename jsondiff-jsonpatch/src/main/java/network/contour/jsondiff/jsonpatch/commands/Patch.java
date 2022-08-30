package network.contour.jsondiff.jsonpatch.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.SneakyThrows;
import network.contour.jsondiff.jsonpatch.Json;
import network.contour.jsondiff.lib.commands.AbstractPatch;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

public class Patch extends AbstractPatch {
    @Override
    @SneakyThrows
    public void run() {
        JsonNode node = Json.parse(inputFile);
        JsonPatch patch = JsonPatch.fromJson(Json.parse(patchFile));
        JsonNode result = patch.apply(node);
        try(Writer writer = new OutputStreamWriter(Optional.ofNullable(outputFile)
                .map(new Function<Path, OutputStream>() {
                    @Override
                    @SneakyThrows
                    public OutputStream apply(Path path) {
                        return Files.newOutputStream(path);
                    }
                })
                .orElse(System.out))) {
            ObjectWriter objectWriter = Json.objectWriter(pretty);
            objectWriter.writeValue(writer, result);
        }
    }
}
