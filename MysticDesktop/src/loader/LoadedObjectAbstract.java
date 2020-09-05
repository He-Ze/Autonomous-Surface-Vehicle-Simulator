package loader;

/**
 * Created by alec on 15/06/16.
 */
public abstract class LoadedObjectAbstract implements LoadedObject {
    @Override
    public boolean isLoaded() {
        return (getLoadStatus() == LoadedObjectHandler.LoadStatus.COMPLETE_LOAD);
    }
}
