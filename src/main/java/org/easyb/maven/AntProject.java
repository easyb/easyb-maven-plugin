package org.easyb.maven;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Main;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.BuildLogger;
import org.apache.tools.ant.NoBannerLogger;
import org.apache.tools.ant.launch.Locator;

/**
 * AntProject which is safe to use in Windows.  Using the AntBuilder when Ant is loaded from the local maven
 * repository frequently throws an exception because of http://jira.codehaus.org/browse/MGROOVY-64.
 */
public class AntProject extends Project {
    public AntProject() {
        super();
        BuildLogger logger = new NoBannerLogger();

        logger.setMessageOutputLevel(org.apache.tools.ant.Project.MSG_INFO);
        logger.setOutputPrintStream(System.out);
        logger.setErrorPrintStream(System.err);

        addBuildListener(logger);

        init();
        getBaseDir();
    }

    /**
     * Avoid call to setAntLib since it is the method causing the unescaped file URIs to be created.
     */
    @Override
    public void initProperties() throws BuildException {
        setJavaVersionProperty();
        setSystemProperties();
        setPropertyInternal(MagicNames.ANT_VERSION, Main.getAntVersion());
        setAntLibCorrectly();
    }

    private void setPropertyInternal(String name, String value) {
        PropertyHelper ph = PropertyHelper.getPropertyHelper(this);
        ph.setProperty(null, name, value, false);
    }

    private void setAntLibCorrectly() {
        File antlib = getClassSource(
            Project.class);
        if (antlib != null) {
            setPropertyInternal(MagicNames.ANT_LIB, antlib.getAbsolutePath());
        }
    }

    public static File getClassSource(Class c) {
        String classResource = c.getName().replace('.', '/') + ".class";
        try {
            classResource = URLEncoder.encode(classResource, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return Locator.getResourceSource(c.getClassLoader(), classResource);
    }
}
