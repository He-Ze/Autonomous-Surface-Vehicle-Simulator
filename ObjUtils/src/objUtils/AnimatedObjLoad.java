package objUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alec on 3/01/16.
 */
public class AnimatedObjLoad {

    public static void main(String[] args) {
        AnimatedObjLoad test = new AnimatedObjLoad("res/minecartGame/models/baked_animation_test/baked_animation_test_######.obj");
        EntWrite.write(test, new File("res/minecartGame/entities/baked_animation_test.ent"));
    }

    List<ObjLoad> objs = new ArrayList<>();

    String basePath;
    String extension;
    int numberOfHashes = 0;

    int objectIndex = 1;

    /**
     * @param basePath A string of format path/to/obj/name_######.obj
     */
    public AnimatedObjLoad(String basePath) {
        processBasePath(basePath);

        File file = nextFile();
        while (file.exists()) {
            objs.add(new ObjLoad(file));
            file = nextFile();
        }
    }

    private File nextFile() {
        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append(basePath);
        String index = Integer.toString(objectIndex);
        int numberOfZeros = numberOfHashes - index.length();

        for (int a = 0; a < numberOfZeros; a++) {
            pathBuilder.append("0");
        }

        pathBuilder.append(index);

        pathBuilder.append("." + extension);

        objectIndex++;

        return new File(pathBuilder.toString());
    }

    private void processBasePath(String basePath) {
        String[] path = basePath.split("\\.");
        extension = path[1];
        StringBuilder basebuilder = new StringBuilder();
        for (char c : path[0].toCharArray()) {
            if (c == '#') {
                numberOfHashes++;
            } else {
                basebuilder.append(c);
            }
        }
        this.basePath = basebuilder.toString();
    }

    public ObjLoad[] getObjs() {
        return objs.toArray(new ObjLoad[objs.size()]);
    }
}
