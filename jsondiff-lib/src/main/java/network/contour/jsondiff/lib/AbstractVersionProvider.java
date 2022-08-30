package network.contour.jsondiff.lib;

import lombok.SneakyThrows;
import picocli.CommandLine;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public abstract class AbstractVersionProvider implements CommandLine.IVersionProvider {
    private final String version;

    @SneakyThrows
    protected AbstractVersionProvider(String specificationTitle) {
        Enumeration<URL> it = getClass().getClassLoader().getResources(JarFile.MANIFEST_NAME);

        while(it.hasMoreElements()) {
            URL manifestURL = it.nextElement();

            Manifest mf = new Manifest();
            try(InputStream is = manifestURL.openStream()) {
                mf.read(is);
            }
            Attributes mainAttributes = mf.getMainAttributes();
            if(Objects.equals(specificationTitle, mainAttributes.getValue(Attributes.Name.SPECIFICATION_TITLE))) {
                version = mainAttributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
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
