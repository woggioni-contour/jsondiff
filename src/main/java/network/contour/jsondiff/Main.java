package network.contour.jsondiff;

import lombok.SneakyThrows;
import network.contour.jsondiff.commands.Diff;
import network.contour.jsondiff.commands.Patch;
import picocli.CommandLine;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

class VersionProvider implements CommandLine.IVersionProvider {
    private final String version;

    @SneakyThrows
    VersionProvider() {
        Enumeration<URL> it = getClass().getClassLoader().getResources(JarFile.MANIFEST_NAME);

        while(it.hasMoreElements()) {
            URL manifestURL = it.nextElement();

            Manifest mf = new Manifest();
            try(InputStream is = manifestURL.openStream()) {
                mf.read(is);
            }
            Attributes mainAttributes = mf.getMainAttributes();
            if(Objects.equals("jsondiff", mainAttributes.getValue(Name.SPECIFICATION_TITLE))) {
                version = mainAttributes.getValue(Name.SPECIFICATION_VERSION);
                return;
            }
        }
        throw new RuntimeException("Version information not found in manifest");
    }

    @Override
    public String[] getVersion() {
        return new String[] { version };
    }
}

@CommandLine.Command(name = "jsondiff", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
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
