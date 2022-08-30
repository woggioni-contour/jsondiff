package network.contour.jsondiff.parsson.commands;

import jakarta.json.JsonStructure;
import lombok.SneakyThrows;
import network.contour.jsondiff.lib.commands.AbstractDiff;
import network.contour.jsondiff.parsson.Json;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

public class Diff extends AbstractDiff {

    @Override
    @SneakyThrows
    public void run() {
        JsonStructure node1 = Json.parse(inputFile1);
        JsonStructure node2 = Json.parse(inputFile2);
        JsonStructure patch = Json.provider.createDiff(node1, node2).toJsonArray();

        try (Json.Output jsonDumper = Json.Output.fromWriter(new OutputStreamWriter(Optional.ofNullable(outputFile)
                .map(new Function<Path, OutputStream>() {
                    @Override
                    @SneakyThrows
                    public OutputStream apply(Path path) {
                        return Files.newOutputStream(path);
                    }
                })
                .orElse(System.out)), pretty)) {
            jsonDumper.write(patch);
        }
    }
}
