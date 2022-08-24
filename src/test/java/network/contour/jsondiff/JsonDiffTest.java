package network.contour.jsondiff;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.diff.JsonDiff;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URL;
import java.util.Objects;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JsonDiffTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {"A", "B", "C", "D"})
    void test(String testCase) {
        URL resource = getClass().getResource("test/" + testCase + ".json");
        Objects.requireNonNull(resource);
        JsonNode testCaseDocument = objectMapper.readTree(resource.openStream());
        JsonNode node1 = testCaseDocument.get("doc1");
        JsonNode node2 = testCaseDocument.get("doc2");
        JsonPatch patch = JsonDiff.asJsonPatch(node1, node2);
        JsonNode patchNode = objectMapper.valueToTree(patch);
        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
        writer.writeValue(System.out, patchNode);
        System.out.println();
        JsonNode expectedDiff = testCaseDocument.get("diff");
        Assertions.assertEquals(expectedDiff, patchNode);
    }
}
