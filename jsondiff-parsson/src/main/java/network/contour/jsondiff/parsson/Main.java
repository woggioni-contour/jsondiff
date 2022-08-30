package network.contour.jsondiff.parsson;

import network.contour.jsondiff.lib.AbstractVersionProvider;
import network.contour.jsondiff.parsson.commands.Diff;
import network.contour.jsondiff.parsson.commands.Patch;
import picocli.CommandLine;

class VersionProvider extends AbstractVersionProvider {
    VersionProvider() {
        super("jsondiff-parsson");
    }
}

@CommandLine.Command(name = "jsondiff-parsson", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
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

