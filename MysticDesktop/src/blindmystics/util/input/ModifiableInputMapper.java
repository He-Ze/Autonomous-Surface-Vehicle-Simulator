package blindmystics.util.input;

import blindmystics.util.input.mouse.ButtonStatus;

/**
 * Created by p3te on 15/08/16.
 */
public class ModifiableInputMapper extends InputMapper {

    private String name;
    private String description;

    private InputMapper currentInputMapper;
    private InputMapper defaultInputMapper;

    public ModifiableInputMapper(String name, String description, InputMapper currentMapper) {
        this.defaultInputMapper = currentMapper;
        this.currentInputMapper = currentMapper;
        this.name = name;
        this.description = description;
    }

    public void setCurrentInputMapper(InputMapper currentInputMapper) {
        this.currentInputMapper = currentInputMapper;
    }

    @Override
    boolean alreadyConsumedThisFrame() {
        return currentInputMapper.alreadyConsumedThisFrame();
    }

    @Override
    ButtonStatus getButtonStatus() {
        return currentInputMapper.getButtonStatus();
    }
}
