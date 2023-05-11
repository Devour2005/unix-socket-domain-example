package com.unix_socket.demo.springbootexample.client.service;

import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import org.newsclub.net.unix.AFUNIXSocketChannel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@Service
public class MessageSenderService {

    private static final String MESSAGE = "Hello, unix-socket server!";

    public void connectAndSendMessage() {
        Path socketFile = Path
                .of(System.getProperty("user.home"))
                .resolve("server.socket");

        try (AFUNIXSocket socket = AFUNIXSocket.newInstance();) {
            AFUNIXSocketAddress address = AFUNIXSocketAddress.of(socketFile);
            socket.connect(address);

            System.out.println("Connected");
            System.out.println("****************************************");

            //Uncomment to choose the method of sending
//            AFUNIXSocketChannel socketChannel = socket.getChannel();
//            writeMessageToSocket(socketChannel, MESSAGE);
            writeMessageToSocketWithOutputStream(socket);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("End of a client communication.");
    }

    private static void writeMessageToSocketWithOutputStream(AFUNIXSocket socket) {
        System.out.println("Sending message to server...");
        try (OutputStream os = socket.getOutputStream()) {
            os.write(MESSAGE.getBytes(StandardCharsets.UTF_8));
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeMessageToSocket(SocketChannel socketChannel, String message) throws IOException {
        System.out.println("Sending message to server...");
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();
        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }
}
