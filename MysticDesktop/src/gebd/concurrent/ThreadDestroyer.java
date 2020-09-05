package gebd.concurrent;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by p3te on 9/03/16.
 */
public class ThreadDestroyer {

    private static boolean shouldCloseGame = false;

    public static boolean shouldCloseGame() {
        return shouldCloseGame;
    }

    public static void setShouldCloseGame(boolean shouldCloseGame) {
        ThreadDestroyer.shouldCloseGame = shouldCloseGame;

        if (shouldCloseGame) {
            //Ask all open threads to close.
            ListIterator<DestoryableThread> destoryableThreadListIterator = destroyableThreads.listIterator();
            while (destoryableThreadListIterator.hasNext()) {
                DestoryableThread next = destoryableThreadListIterator.next();
                next.destroyThread();
            }
        }
    }

    private static LinkedList<DestoryableThread> destroyableThreads = new LinkedList<>();

    public static void registerDestroyableThread(DestoryableThread destoryableThread) {
        destroyableThreads.add(destoryableThread);
    }

    public static Thread registerAndStartDestroyableThread(DestoryableThread destoryableThread) {
        Thread newThread = new Thread(destoryableThread);
        newThread.start();
        destroyableThreads.add(destoryableThread);
        return newThread;
    }


}
