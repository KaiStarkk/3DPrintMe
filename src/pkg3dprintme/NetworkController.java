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

import java.io.InputStream;
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
    
    private static final int MAX_PIS = 10;
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
            BROADCAST_ADDRESS = InetAddress.getByName("255.255.255.255");
            UDP_PORT = 8888;
            TCP_PORT = 12345;
            listening = false;
        } catch (Exception e) {
            // TODO: Implement retries, error handling, and rethrowing
            // Do not rethrow exceptions - display an alert and close
            // the platform in these cases.
        }
    }

    /**
     * This function announces the client machine's address on UDP broadcast.
     * The image servers on the network will subsequently try to connect to
     * the client to provide their connection information and status.
     * 
     * @author Siyuan Ji
     * @author Kieran Hannigan
     * @throws java.lang.Exception
     */
    private void announceAddress() throws Exception {
        try {
            UDP_SOCKET = new DatagramSocket();
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
     * This function accepts connections from the image servers and receives
     * their connection information and status. This completes the initial
     * polling/handshake stage of the protocol. From this point forward, the
     * client is a true client, requesting images from each of the image servers
     * as required.
     * 
     * @author Siyuan Ji
     * @author Kieran Hannigan
     * @return a host list containing the respondents' information.
     * @throws java.lang.Exception
     */
    private HostList receiveAddresses() throws Exception {
        HostList hostList;
        hostList = new HostList();
        listening = true;
        System.out.println("Starting...");
        new java.util.Timer().schedule( 
            new java.util.TimerTask() {
                @Override
                public void run() {
                    listening = false;
                }
            }, 
            5000 // timeout
        );
        try {
            while (listening) {
                TCP_SOCKET = new ServerSocket(TCP_PORT);
                TCP_SOCKET.setSoTimeout(3000);
                System.out.println("Scanning...");
                Socket client = TCP_SOCKET.accept();
                System.out.println("Reading...");
                InputStream piInputStream = client.getInputStream();
                byte[] piInputStreamBuffer = new byte[1024];
                int piInputStreamBufferLength;
                piInputStreamBufferLength = piInputStream.read(piInputStreamBuffer);
                String name = new String(piInputStreamBuffer, 0, 
                                                  piInputStreamBufferLength);
                String address = client.getInetAddress().getHostAddress();
                hostList.add(new Host(name, address, "Connected"));
                System.out.println(String.format("Received... %s %s", name, address));
                if(!client.isClosed()) {
                    client.close();
                }
                if(!TCP_SOCKET.isClosed()) {
                    TCP_SOCKET.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Timeout / Error.");
            if(!TCP_SOCKET.isClosed()) {
                    TCP_SOCKET.close();
            }
        }
        if(!TCP_SOCKET.isClosed()) {
                    TCP_SOCKET.close();
                }
        System.out.println("Stopping.");
        return hostList;
    }

    /**
     * This function queries all available ImageServers for their IP
     * addresses using a UDP protocol broadcast, then builds a host list
     * from the results.
     * 
     * @author Kieran Hannigan
     * @return a host list containing all of the respondents' information.
     * @throws java.lang.Exception
     */
    public HostList getHosts() throws Exception {
        HostList hostList;
        hostList = new HostList();
        try {
            announceAddress();
            hostList = receiveAddresses();
        } catch (Exception e) {
            throw e;
        }
        return hostList;
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
     * @return an image set containing the images from all of the available image servers.
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
     * @throws java.lang.Exception
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
     * The shoot function coordinates requests to the ImageServers by 
     * first calling the sync function to build a table of delay 
     * adjustments, and then calling the snap function to issue a command 
     * for the ImageServers to capture.
     * 
     * @author Kieran Hannigan
     * @param hostList the list of ImageServers
     * @return the images captured.
     * @throws java.lang.Exception
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
     * The shoot(Host) function captures Images from a single image server.
     * @param host the image server to poll
     * @return an image set containing the images from the given image server.
     * @throws java.lang.Exception
     */
    public ImageSet shoot(Host host) throws Exception {
        ImageSet imageSet;
        imageSet = new ImageSet();
        try {
            if(!host.validate()) {
                throw new Exception();
            }
            imageSet = snap(host);
        } catch (Exception e) {
            // TODO: Implement retries, error handling, and rethrowing
            throw e;
        }
        return imageSet;
    }

    /**
     * The sync function builds up a table of delays based on the time
     * that it takes to reach each hosts.
     * 
     * @author Kieran Hannigan
     * @param hostTable the hosts to be reached.
     * @return a table of hosts and the time delay to each host.
     * @throws java.lang.Exception
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
     * The snap function commands a group of image servers to capture and
     * return their payload at a given unified time.
     * 
     * @author Kieran Hannigan
     * @param syncTable the table of hosts and their addresses.
     * @param syncTable the table of host delays.
     * @return an image set containing all of the images.
     * @throws java.lang.Exception
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
    
    /**
     * The snap(Host) commands a given image server to capture and deliver
     * its images.
     * 
     * @author Kieran Hannigan
     * @param host the image server to be polled.
     * @return an image set containing the images from the given image server.
     * @throws Exception 
     */
    private ImageSet snap(Host host) throws Exception {
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
