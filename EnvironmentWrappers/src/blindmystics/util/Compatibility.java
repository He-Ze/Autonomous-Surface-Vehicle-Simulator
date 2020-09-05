package blindmystics.util;

/**
 * Created by alec on 24/11/15.
 */
public class Compatibility {

    public enum GLVersion {
        GL30("3.0", 30),
        GL31("3.1", 31),
        GL32("3.2", 32),
        GL33("3.3", 33),
        GL40("4.0", 40),
        GL41("4.1", 41),
        GL42("4.2", 42),
        GL43("4.3", 43),
        GL44("4.4", 44),
        GL45("4.5", 45),
        GL46("4.6", 46);

        private String name;
        private int versionNumber;

        GLVersion(String name, int versionNumber) {
            this.name = name;
            this.versionNumber = versionNumber;
        }

        public String getName() {
            return name;
        }

        public int getVersionNumber() {
            return versionNumber;
        }

        public static GLVersion getGlVersion(String name) {
            for (GLVersion version : GLVersion.values()) {
                if (version.getName().equals(name)) {
                    return version;
                }
            }
            return GLVersion.GL46;
        }
    }


    public static final GLVersion GL_VERSION = getOpenGLVersion();
    public static final GLVersion MIN_FRAMEBUFFER_COMPATIBLE = Compatibility.GLVersion.GL31;

    private static GLVersion getOpenGLVersion() {
        return GLVersion.getGlVersion(GLWrapper.glGetString(GLWrapper.GL_VERSION).substring(0, 3));
    }

}
