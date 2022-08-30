package network.contour.jsondiff.parsson.commands;

import jakarta.json.JsonPatch;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;
import lombok.SneakyThrows;
import network.contour.jsondiff.lib.commands.AbstractPatch;
import network.contour.jsondiff.parsson.Json;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

public class Patch extends AbstractPatch {
    @Override
    @SneakyThrows
    public void run() {
        JsonStructure node = Json.parse(inputFile);
        JsonPatch patch = Json.provider.createPatch(Json.parse(patchFile).asJsonArray());
        JsonValue result = patch.apply(node);
        try(Json.Output jsonOutput = Json.Output.fromWriter(new OutputStreamWriter(Optional.ofNullable(outputFile)
                .map(new Function<Path, OutputStream>() {
                    @Override
                    @SneakyThrows
                    public OutputStream apply(Path path) {
                        return Files.newOutputStream(path);
                    }
                })
                .orElse(System.out)), pretty)) {
            jsonOutput.write(result);
        }
    }
}
