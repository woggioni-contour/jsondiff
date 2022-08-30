package network.contour.jsondiff.lib.commands;

import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(name = "patch", description = "Applies a Patch to a JSON document")
public abstract class AbstractPatch implements Runnable {
    @CommandLine.Option(names = {"-i", "--input-file"}, description = "Path to the file to patch", required = true)
    protected Path inputFile;

    @CommandLine.Option(names = {"-p", "--patch"}, description = "Path to the JSON patch file", required = true)
    protected Path patchFile;

    @CommandLine.Option(names = {"-o", "--output"},
            description = "Write output to the provided file path (if not provided defaults to stdout)")
    protected Path outputFile;

    @CommandLine.Option(names = {"-P", "--pretty"}, description = "Pretty prints the generated diff")
    protected boolean pretty = false;
}
