package model.cameras;

import java.awt.image.BufferedImage;

/**
 * Created by Andrew on 15.08.2016.
 */
public interface ICamera {
    /**
     * Sets the address of camera.
     * It could be either local or remote address
     * The local address can be used for CameraVirtual entity
     * @param address the address to the camera
     */

    void setAddress(String address);

    String getAddress();

    String getName();

    void setPassword(String password);

    void setUser(String user);

    String getPassword();

    String getUser();

    void setName(String name);

    /**
     * Returns the image.
     * @return Snapshot from the camera.
     */
    BufferedImage getBufferedImage() throws CameraConnectionIssueException;

    /**
     * Closes connection to the camera
     */
    void close();
}
