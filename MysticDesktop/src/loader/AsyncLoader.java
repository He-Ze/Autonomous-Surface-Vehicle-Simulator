package loader;

import renderables.texture.TextureInfo;

/**
 * Created by p3te on 28/12/15.
 */
public class AsyncLoader extends LoadedObjectHandler<LoadedObject> {

    private static class Nothing extends LoadedObjectAbstract {

        private LoadedObjectHandler.LoadStatus currentStatus;

        @Override
        public LoadStage[] stagesToPerform(){
            return new LoadStage[] {
//              LoadStage.LOAD_DATA_FROM_FILE,
//				LoadStage.HANDLE_RAW_DATA,
//				LoadStage.LOAD_DEPENDENCIES,
            };
        }

        @Override
        public LoadStatus getLoadStatus() {
            return currentStatus;
        }

        @Override
        public void setLoadStatus(LoadStatus newLoadStatus) {
            this.currentStatus = newLoadStatus;
        }

        @Override
        public void loadRawDataFromFile(LoadedObjectHandler<?> handler){

        }

        @Override
        public void handleRawData(LoadedObjectHandler<?> handler){

        }

        @Override
        public void loadDependencies(LoadedObjectHandler<?> handler){

        }

        @Override
        public void completeLoad(LoadedObjectHandler<?> handler){

        }
    }

    public static final AsyncLoader ASYNC_LOADER = new AsyncLoader();

    public AsyncLoader(){
        super(new Nothing());
        handleLoad();
    }

    @Override
    public void notifyLoadedDependancy(int localDependencyLocation, LoadedObjectHandler<?> dependency){
        //Do nothing.
    };

    @Override
    public void addWaiting(LoadedObjectHandler<?> waitingJob, int loadPosition){
        //Do nothing.
    }
}
