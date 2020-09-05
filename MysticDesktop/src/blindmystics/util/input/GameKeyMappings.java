package blindmystics.util.input;

import java.util.ArrayList;

/**
 * Created by p3te on 15/08/16.
 */
public class GameKeyMappings {

    private static ArrayList<ModifiableInputMapper> inputMappers = new ArrayList<>();

    protected static ModifiableInputLatch addMapper(ModifiableInputMapper newMapper) {
        inputMappers.add(newMapper);
        return new ModifiableInputLatch(newMapper);
    }

    public ArrayList<ModifiableInputMapper> getInputMappers() {
        return inputMappers;
    }
}
