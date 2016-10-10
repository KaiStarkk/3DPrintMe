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
import java.net.Socket;

/**
 *
 * @author Kieran
 */
public class Transfer implements Runnable {
    
    private final Socket piSocket;
    private String piName;
    private String piAddress;

    public Transfer(Socket piSocket) {
        this.piSocket = piSocket;
    }

    @Override
    public void run() {
        try {
            InputStream piInputStream = piSocket.getInputStream();
            byte[] piInputStreamBuffer = new byte[1024];
            int piInputStreamBufferLength = piInputStream.read(piInputStreamBuffer);
            piName = new String(piInputStreamBuffer, 0, piInputStreamBufferLength);
            piAddress = piSocket.getInetAddress().getHostAddress();
            // Receive HostList object
        } catch (Exception e) {

        }
    }
}
