/*
 * Copyright (C) 2016 Kieran
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pkg3dprintme;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;

/** 
* The NetworkController class contains all of the back-end logic of the
* application. An instance of this class is created by the application
* controller, and that controller will call public methods in this class
* to interface with external resources such as the ImageServers and the
* client disk.
*/
class NetworkController {
    
    private String SERVER_IP;
    private int UDP_PORT;
    private int TCP_PORT;
    private DatagramSocket UDP_SOCKET;
    private ServerSocket TCP_SOCKET;
    private InetAddress BROADCAST_ADDRESS;
    private boolean listening;

    /**
     * The constructor for this class performs all of the required
     * instantiation.
     * 
     * @author Kieran Hannigan
     */
    public NetworkController() {
        try {
            UDP_SOCKET = new DatagramSocket();
            BROADCAST_ADDRESS = InetAddress.getByName("255.255.255.255");
            UDP_PORT = 8888;
            TCP_PORT = 12345;
            TCP_SOCKET = new ServerSocket(TCP_PORT);
            listening = false;
        } catch (Exception e) {
            // TODO: Implement retries, error handling, and rethrowing
            // Do not rethrow exceptions - display an alert and close
            // the platform in these cases.
        }
    }

    /**
     * TODO: Write JavaDoc
     */
    private void acceptConnections() throws Exception {
        try {
            while (listening) {
                Socket piSocket = TCP_SOCKET.accept();
                new Thread(new Transfer(piSocket)).start();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * TODO: Write JavaDoc
     */
    private void announceAddress() throws Exception {
        try {
            SERVER_IP = InetAddress.getLocalHost().getHostAddress();
            byte[] buf = SERVER_IP.getBytes();
            DatagramPacket announcement;
            announcement = new DatagramPacket(buf, buf.length, 
                                    BROADCAST_ADDRESS, UDP_PORT);
            UDP_SOCKET.send(announcement);
            UDP_SOCKET.close();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * The capture function captures images from all of the ImageServers
     * that are available, and then stores them to the file system. 
     * The parameters to this function are provided by the 
     * interface controller, which retrieves them from the layout, and
     * performs sanitization before sending them here.
     * 
     * @author Kieran Hannigan
     * @param name the name of the client/job.
     * @param date the datetime of the job.
     * @param path the path to save the job.
     * @throws java.lang.Exception
     */
    public void capture(String name, LocalDate date, String path) 
                                                        throws Exception {
        ImageSet imageSet;
        try {
            imageSet = getImages();
            storeImages(imageSet, path);
        } catch(Exception e) {
            // TODO: Implement retries, error handling, and rethrowing
            throw e;
        }
    }

    /**
     * The getImages function captures images from all of the detected
     * ImageServers. This function is called by the capture function,
     * but can also be called by the interface controller for previewing.
     * 
     * @author Kieran Hannigan
     * @return an ArrayList of images.
     * @throws java.lang.Exception
     */
    public ImageSet getImages() throws Exception {
        HostList hostList;
        ImageSet imageSet;
        imageSet = new ImageSet();
        try {
            hostList = getHosts();
            if (!hostList.validate())  {
                throw new Exception();
            }
            imageSet = shoot(hostList);
        } catch(Exception e) {
            // TODO: Implement retries, error handling, and rethrowing
            throw e;
        }
        return imageSet;
    }

    /**
     * The storeImages function saves images to the local file system.
     * 
     * @author Kieran Hannigan
     * @param imageSet the images to be saved.
     * @param savePath the path where the images should be saved.
     */
    private void storeImages(ImageSet imageSet, String savePath) 
                                                        throws Exception{
        try {
            // TODO: implement image storing
        } catch (Exception e) {
            // TODO: Implement retries, error handling, and rethrowing
            throw e;
        }
    }

    /**
     * This function queries all available ImageServers for their IP
     * addresses using a UDP protocol broadcast, then builds a HostList
     * from the results.
     * 
     * @author Kieran Hannigan
     * @return a map from String to String of all the hosts that were 
     *         detected, where the key is the name of the host and the 
     *         value is that host's IP address.
     * @throws java.lang.Exception
     */
    public HostList getHosts() throws Exception {
        HostList hostList;
        hostList = new HostList();
        try {
            // TODO: Use UDP to scan for hosts and build up hostList.
        } catch (Exception e) {
            // TODO: Implement retries, error handling, and rethrowing
            throw e;
        }
        return hostList;
    }

    /**
     * The shoot function coordinates requests to the ImageServers by 
     * first calling the sync function to build a table of delay 
     * adjustments, and then calling the snap function to issue a command 
     * for the ImageServers to capture.
     * 
     * @author Kieran Hannigan
     * @param hostTable the table of ImageServers
     * @return the images captured.
     */
    private ImageSet shoot(HostList hostList) throws Exception {
        ImageSet imageSet;
        SyncTable syncTable;
        imageSet = new ImageSet();
        try {
            syncTable = sync(hostList);
            imageSet = snap(hostList, syncTable);
        } catch (Exception e) {
            // TODO: Implement retries, error handling, and rethrowing
            throw e;
        }

        return imageSet;
    }

    /**
     * The sync function builds up a table of delays based on the time
     * that it takes to reach each of a group of hosts.
     * 
     * @author Kiearn Hannigan
     * @param hostTable the hosts to be reached.
     * @return 
     */
    private SyncTable sync(HostList hostTable) throws Exception {
        SyncTable syncTable = new SyncTable();
        try {
            // TODO: Build sync table using custom TCP handshake
        } catch (Exception e) {
            // TODO: Implement retries, error handling, and rethrowing
            throw e;
        }
        return syncTable;
    }

    /**
     * The snap function commands a group of ImageServers to capture and
     * return their payload at a given unified time.
     * 
     * @author Kieran Hannigan
     * @param syncTable the table of hosts and their addresses.
     * @param syncTable the table of host delays.
     * @return 
     */
    private ImageSet snap(HostList hostList, SyncTable syncTable) 
                                                        throws Exception {
        ImageSet imageSet = new ImageSet();
        try {
            // TODO: Capture images from ImageServers
        } catch (Exception e) {
            // TODO: Implement retries, error handling, and rethrowing
            throw e;
        }
        return imageSet;
    }
}
