package blindmystics.util.input;

/**
 * Created by CaptainPete on 8/16/2016.
 */
public class ModifiableInputLatch extends InputLatch {

    private ModifiableInputMapper modifiableInputMapper;

    public ModifiableInputLatch(ModifiableInputMapper modifiableInputMapper) {
        super(modifiableInputMapper);
        this.modifiableInputMapper = modifiableInputMapper;
    }

    public ModifiableInputMapper getModifiableInputMapper() {
        return modifiableInputMapper;
    }
}
