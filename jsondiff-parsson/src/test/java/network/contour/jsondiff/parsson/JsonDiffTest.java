package network.contour.jsondiff.parsson;

import jakarta.json.JsonMergePatch;
import jakarta.json.JsonPatch;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jdk.jshell.spi.SPIResolutionException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Objects;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JsonDiffTest {
    private static final JsonProvider jsonProvider = JsonProvider.provider();

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {"A", "B", "C", "D"})
    void test(String testCase) {
        URL resource = getClass().getResource("test/" + testCase + ".json");
        Objects.requireNonNull(resource);
        JsonStructure testCaseDocument = Json.parse(resource.openStream());
        JsonStructure node1 = (JsonStructure) testCaseDocument.asJsonObject().get("doc1");
        JsonStructure node2 = (JsonStructure) testCaseDocument.asJsonObject().get("doc2");
        JsonPatch patch = Json.provider.createDiff(node1, node2);
        try(Json.Output jsonOutput = Json.Output.fromWriter(new OutputStreamWriter(System.out), true)) {
            jsonOutput.write(patch.toJsonArray());
        }
        System.out.println();
        JsonStructure expectedDiff = (JsonStructure) testCaseDocument.asJsonObject().get("diff");
        JsonPatch expectedPatch = Json.provider.createPatch(expectedDiff.asJsonArray());
        Assertions.assertEquals(expectedPatch, patch);
    }
}
