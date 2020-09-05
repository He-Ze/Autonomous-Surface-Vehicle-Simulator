package gebd.concurrent;

/**
 * Created by CaptainPete on 8/9/2016.
 */
public interface DestoryableThread extends Runnable {

    /**
     * When the game closes, all threads should be shutdown correctly.
     * This is a call to tell the thread to shutdown immediately as the
     * entire game is closing.
     */
    void destroyThread();

}
