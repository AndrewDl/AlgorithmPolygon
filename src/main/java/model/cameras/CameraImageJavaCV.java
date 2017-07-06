package model.cameras;

import com.googlecode.javacv.FFmpegFrameGrabber;
import com.googlecode.javacv.FrameGrabber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;


/**
 * Contains tools for taking image using RTSP
 * Created by Sergiy on 9/13/2016.
 */
public class CameraImageJavaCV implements ICamera{

    private String camName = "DefaultRTSP";
    private String addressRTSP;// = "rtsp://176.104.45.235:554/Streaming/channels/1";

    private Thread T = null;
    private boolean connectionLost = false;
    private BufferedImage imageRTSP = null;
    private final Logger userLogger = LogManager.getLogger("MainLogger");
    private FFmpegFrameGrabber grabber = null;// new FFmpegFrameGrabber(addressRTSP);

    private boolean interrupted = false;

    public CameraImageJavaCV(String address) {
        addressRTSP=address;

        init();
    }

    private void init(){
        if (grabber == null)
            grabber = new FFmpegFrameGrabber(addressRTSP);

        //initialize thread, if it was not initialized before
        if ((T == null) || (!T.isAlive())){

            System.out.println("Start cam");

            T = new Thread(() -> {

                try {
                    //start grabber
                    grabber.start();
                } catch (FrameGrabber.Exception e) {
                    //if something went wrong - indicate that connection was lost or can't be opened
                    e.printStackTrace();
                    interrupt();
                    setConnectionLost(true);
                } catch (Exception e){
                    e.printStackTrace();
                    interrupt();
                    setConnectionLost(true);
                }

                //Thread cycle
                while(!interrupted){

                    try {
                        //read Buffered image
                        imageRTSP = grabber.grab().getBufferedImage();
                    } catch (FrameGrabber.Exception e) {
                        //imageRTSP = null;
                        userLogger.error("JavaCV error: "+e.getMessage());
                        e.printStackTrace();
                        interrupt();
                        setConnectionLost(true);
                    } catch (NullPointerException e){
                        //Зупиняємо все. По хорошому треба спричинити від'єднання від камери
                        e.printStackTrace();
                        //imageRTSP = null;
                        interrupt();
                        setConnectionLost(true);
                    }

                    //Interrupt Handling

                }

                try {
                    //stop grabber
                    imageRTSP = null;
                    System.out.println("Thread " + T.getId() + " is interrupted");
                    grabber.stop();
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }
            });

            //interrupt Thread on main frame close
//            Main.getPrimaryStage().setOnCloseRequest(event -> T.interrupt());
            //start Thread with image grabbing
            T.setDaemon(true);
            T.start();

        }
    }

    /**
     * This method set's the address to the video.
     * The format of the address should be: rtsp://cameraNetworkAddress/videoPath
     * or localVideoAddress/videoName
     * @param address the address to the camera
     */
    @Override
    public void setAddress(String address) {
        addressRTSP = address;
    }

    @Override
    public String getAddress() {
        return addressRTSP;
    }

    @Override
    public String getName() {
        return camName;
    }

    @Override
    public void setName(String name) {
        camName=name;
    }

    @Override
    public void setPassword(String password) {

    }
    @Override
    public String toString(){
        return camName;
    }

    /**
     *
     * @return the password for the image
     */
    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public void setUser(String user) {

    }

    /**
     *
     * @return the user of the image
     */
    @Override
    public String getUser() {
        return "";
    }

    @Override
    public BufferedImage getBufferedImage() throws CameraConnectionIssueException{




        //wait untill we receive first image from stream
        while (imageRTSP == null){

            if (getConnectionLost()) {
                setConnectionLost(false);
                throw new CameraConnectionIssueException("Can't access camera on: " + addressRTSP);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                interrupt();
                break;
            }
        }

        return imageRTSP;
    }

    /**
     * Interrupts current thread
     */
    private synchronized void interrupt(){
        interrupted = true;
    }

    @Override
    public void close() {
        interrupt();
    }

    private synchronized void setConnectionLost (boolean state){
        connectionLost = state;
    }

    private synchronized boolean getConnectionLost(){
        return connectionLost;
    }

    private synchronized void setImageRTSP (BufferedImage image){
        imageRTSP = image;
    }

    private synchronized BufferedImage getImageRTSP(){
        return imageRTSP;
    }

}