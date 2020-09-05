package renderables.r3D;

import composites.entities.Entity;
import gebd.camera.Camera;
import gebd.concurrent.DestoryableThread;
import gebd.concurrent.ThreadDestroyer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by p3te on 7/11/16.
 */
public class TransparentEntitySorter implements DestoryableThread {

    public static TransparentEntitySorter instance = new TransparentEntitySorter();

    private Semaphore waitingSemaphore = new Semaphore(0);
    private Lock completionMutex = new ReentrantLock();
    private Camera currentCamera = null;
    private ArrayList<Entity> transparentEntites;
    private volatile boolean running = true;
    private final Thread thisThread;

    public TransparentEntitySorter() {
        this.thisThread = ThreadDestroyer.registerAndStartDestroyableThread(this);
    }

    /**
     * Queues the thread to update the current view.
     *
     * @param camera
     */
    public static void startConcurrentOrdering(Camera camera, ArrayList<Entity> transparentEntites) {
        if (camera == null) {
            //If there is no current view, do nothing.
            return;
        }
        instance.currentCamera = camera;
        instance.transparentEntites = transparentEntites;
        instance.completionMutex.lock();
        instance.waitingSemaphore.release();
    }

    @Override
    public void run() {
        while (running) {
            try {
                waitingSemaphore.acquire();
            } catch (InterruptedException e) {
                //Thread has been shut down.
            }

            if (!running) {
                break;
            }

            performOrderOperation();
            completionMutex.unlock();

        }
    }

    /**
     * Order all objects from furthest to closest.
     */
    private void performOrderOperation() {

        ClosestEntityComparitor closestEntityComparitor = new ClosestEntityComparitor(currentCamera);
        Collections.sort(transparentEntites, closestEntityComparitor);

    }

    /**
     * Wait on a mutex (Doesn't require a context switch) for
     * the ordering to complete.
     */
    public static void waitForSortingToComplete() {
        //Lock, IE - Wait for the other thread to unlock it.
        instance.completionMutex.lock();

        //Once the thread has finished, we can unlock it.
        instance.completionMutex.unlock();
    }


    @Override
    public void destroyThread() {
        running = false;
        waitingSemaphore.release();
    }

}
