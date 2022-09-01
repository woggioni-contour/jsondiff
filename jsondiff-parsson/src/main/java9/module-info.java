open module network.contour.jsondiff.parsson {
    requires static lombok;
    requires jakarta.json;
    uses jakarta.json.spi.JsonProvider;
    requires network.contour.jsondiff.lib;
}