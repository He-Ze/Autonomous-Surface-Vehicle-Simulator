package gebd.games.boat.camera;

import gebd.concurrent.DestoryableThread;
import org.lwjgl.opengl.GL11;
import javax.vecmath.Vector3f;

import java.nio.ByteBuffer;
import java.util.concurrent.Semaphore;

/**
 * Created by CaptainPete on 8/21/2016.
 */
public class CameraCalculationThread implements DestoryableThread {

    private static int resolutionWidth;
    private int resolutionHeight;
    public static final int BYTES_PER_PIXEL = 3; //RGB

    private static ByteBuffer currentRead;
    private ByteBuffer currentWrite;
    private ByteBuffer currentWait;

    private Semaphore waitWriteSemaphore = new Semaphore(1);
    private Semaphore readSemaphore = new Semaphore(1);
    private Semaphore newDataSemaphore = new Semaphore(0);

    boolean running = true;

    public static void aaa(int x,int y){
        System.out.printf(getPixelAt(x, y).toString());
        System.out.printf("\n");
    }

    public CameraCalculationThread(int resolutionWidth, int resolutionHeight) {
        this.resolutionWidth = resolutionWidth;
        this.resolutionHeight = resolutionHeight;

        int numPixels = resolutionWidth * resolutionHeight;
        int bufferSize = numPixels * BYTES_PER_PIXEL;

        currentWrite = ByteBuffer.allocateDirect(bufferSize);
        currentWait = ByteBuffer.allocateDirect(bufferSize);
        currentRead = ByteBuffer.allocateDirect(bufferSize);
    }


    public void acquireReadPermit() {
        readSemaphore.acquireUninterruptibly();
    }

    public void putRGBinBufferAtXY(int x, int y, ByteBuffer buffer) {
        int byteStartOffset = ((y * resolutionWidth) + x) * BYTES_PER_PIXEL;
        currentRead.position(byteStartOffset);

        byte red = currentRead.get();
        byte green = currentRead.get();
        byte blue = currentRead.get();

        //Load RGB.
        buffer.put(red);
        buffer.put(green);
        buffer.put(blue);
    }

    public static Vector3f getPixelAt(int x, int y) {
        int byteStartOffset = ((y * resolutionWidth) + x) * BYTES_PER_PIXEL;
        currentRead.position(byteStartOffset);

        //RGBA.
        float red = convertByteToFloat0to1(currentRead.get())*256.00f;
        float green = convertByteToFloat0to1(currentRead.get())*256.00f;
        float blue = convertByteToFloat0to1(currentRead.get())*256.00f;
        //float alpha = convertByteToFloat0to1(currentRead.get());

        return new Vector3f(red, green, blue);
    }

    private static float convertByteToFloat0to1(byte givenByte) {
        float unsignedValue = givenByte - Byte.MIN_VALUE;
        float byteRange = Byte.MAX_VALUE - Byte.MIN_VALUE;
        return (unsignedValue / byteRange);
    }

    public void releaseReadPermit(){
        readSemaphore.release();
    }

    public void writeNewColourData(){
        //Load into the currentWrite.
        currentWrite.position(0); //Position it at 0 and write the data.
        GL11.glReadPixels(0, 0, resolutionWidth, resolutionHeight, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, currentWrite);

        //Swap the two buffers.
        waitWriteSemaphore.acquireUninterruptibly();
        ByteBuffer tempBuffer = currentWait;
        currentWait = currentWrite;
        currentWrite = tempBuffer;
        waitWriteSemaphore.release();

        //Notify that new data is ready!
        if (newDataSemaphore.availablePermits() == 0) {
            newDataSemaphore.release();
        }
    }

    @Override
    public void run() {
        int i=0;
        while (running) {
            try {
                newDataSemaphore.acquire();
            } catch (InterruptedException e) {
                //Thread is being destroyed.
                return;
            }

            //Set the read buffer to be the latest buffer.
            acquireReadPermit();
            waitWriteSemaphore.acquireUninterruptibly();
            ByteBuffer tempBuffer = currentRead;
            currentRead = currentWait;
            currentWait = tempBuffer;
            waitWriteSemaphore.release();
            releaseReadPermit();
/*
            if(i%200==0) {
                System.out.printf(getPixelAt(1000, 1000).toString());
                System.out.printf("\n");
            }
            i++;

 */
        }
    }


    @Override
    public void destroyThread() {
        running = false;
        //Tell it to stop waiting for more data.
        newDataSemaphore.release();
    }

    public int getResolutionWidth() {
        return resolutionWidth;
    }

    public int getResolutionHeight() {
        return resolutionHeight;
    }

    public ByteBuffer getCurrentRead() {
        return currentRead;
    }
}
