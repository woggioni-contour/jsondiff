package network.contour.jsondiff.lib.commands;

import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(name = "diff", description = "generate diff between 2 JSON documents")
public abstract class AbstractDiff implements Runnable {
    @CommandLine.Option(names = {"-i1", "--input-file1"}, description = "Path to the first file to diff", required = true)
    protected Path inputFile1;

    @CommandLine.Option(names = {"-i2", "--input-file2"}, description = "Path to the second file to diff", required = true)
    protected Path inputFile2;

    @CommandLine.Option(names = {"-o", "--output"},
            description = "Write output to the provided file path (if not provided defaults to stdout)")
    protected Path outputFile;

    @CommandLine.Option(names = {"-P", "--pretty"}, description = "Pretty prints the generated diff")
    protected boolean pretty = false;
}
