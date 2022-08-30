package network.contour.jsondiff.jsonpatch.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.diff.JsonDiff;
import lombok.SneakyThrows;
import network.contour.jsondiff.jsonpatch.Json;
import network.contour.jsondiff.lib.commands.AbstractDiff;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

public class Diff extends AbstractDiff {
    @Override
    @SneakyThrows
    public void run() {
        JsonNode node1 = Json.parse(inputFile1);
        JsonNode node2 = Json.parse(inputFile2);
        JsonPatch patch = JsonDiff.asJsonPatch(node1, node2);

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
            objectWriter.writeValue(writer,patch);
        }
    }
}
