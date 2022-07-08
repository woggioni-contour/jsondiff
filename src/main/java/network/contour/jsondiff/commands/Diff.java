package network.contour.jsondiff.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.diff.JsonDiff;
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

@CommandLine.Command(name = "diff", description = "generate diff between 2 JSON documents")
public class Diff implements Runnable {
    @CommandLine.Option(names = {"-i1", "--input-file1"}, description = "Path to the first file to diff", required = true)
    private Path inputFile1;

    @CommandLine.Option(names = {"-i2", "--input-file2"}, description = "Path to the second file to diff", required = true)
    private Path inputFile2;

    @CommandLine.Option(names = {"-o", "--output"},
            description = "Write output to the provided file path (if not provided defaults to stdout)")
    private Path outputFile;

    @CommandLine.Option(names = {"-P", "--pretty"}, description = "Pretty prints the generated diff")
    private boolean pretty = false;

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
