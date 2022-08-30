package network.contour.jsondiff.jsonpatch;

import network.contour.jsondiff.jsonpatch.commands.Diff;
import network.contour.jsondiff.jsonpatch.commands.Patch;
import network.contour.jsondiff.lib.AbstractVersionProvider;
import picocli.CommandLine;

class VersionProvider extends AbstractVersionProvider {
    VersionProvider() {
        super("jsondiff-jsonpatch");
    }
}

@CommandLine.Command(name = "jsondiff-jsonpatch", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
public class Main implements Runnable {

    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new Main());
        commandLine.addSubcommand(new Diff());
        commandLine.addSubcommand(new Patch());
        System.exit(commandLine.execute(args));
    }

    @Override
    public void run() {
    }
}
