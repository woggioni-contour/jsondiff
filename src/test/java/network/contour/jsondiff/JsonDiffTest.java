package network.contour.jsondiff;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.diff.JsonDiff;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class JsonDiffTest {

    @RequiredArgsConstructor
    enum TestCase {
        A("[1, 2, 3, 4]", "[1, 3, 4]", ""),
        B("{\"a\" : [1,2,3]}", "{\"a\": [1,3], \"b\" : [false, null, \"hello\"]}", ""),
        C("{\"a\" : {\"3/4\": [1,2,3]}}", "{\"a\": {\"3/4\": [1,3]}}", ""),
        D("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", "[1, 3, 4, 5, 6, 7, 8, 9, 10, 2]", "");

        private final String json1;
        private final String json2;
        private final String expectedDiff;
    }


    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @ParameterizedTest
    @EnumSource(TestCase.class)
    void test(TestCase testCase) {
        JsonNode node1 = objectMapper.readTree(testCase.json1);
        JsonNode node2 = objectMapper.readTree(testCase.json2);
        JsonPatch patch = JsonDiff.asJsonPatch(node1, node2);
        JsonNode patchNode = objectMapper.valueToTree(patch);
        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
        writer.writeValue(System.out, patchNode);
        System.out.println();
    }
}
