package network.contour.jsondiff.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.SneakyThrows;
import network.contour.jsondiff.Json;
import picocli.CommandLine;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

@CommandLine.Command(name = "patch", description = "Applies a Patch to a JSON document")
public class Patch implements Runnable {
    @CommandLine.Option(names = {"-i", "--input-file"}, description = "Path to the file to patch", required = true)
    private Path inputFile;

    @CommandLine.Option(names = {"-p", "--patch"}, description = "Path to the JSON patch file", required = true)
    private Path patchFile;

    @CommandLine.Option(names = {"-o", "--output"},
            description = "Write output to the provided file path (if not provided defaults to stdout)")
    private Path outputFile;

    @CommandLine.Option(names = {"-P", "--pretty"}, description = "Pretty prints the generated diff")
    private boolean pretty = false;

    @Override
    @SneakyThrows
    public void run() {
        ObjectMapper objectMapper = new ObjectMapper();
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
