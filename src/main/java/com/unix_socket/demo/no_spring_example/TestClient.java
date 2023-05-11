package com.unix_socket.demo.no_spring_example;

import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import org.newsclub.net.unix.AFUNIXSocketChannel;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;

public class TestClient {

    public static void main(String[] args) throws IOException {
        Path socketPath = Path
                .of(System.getProperty("user.home"))
                .resolve("server.socket");

        try (AFUNIXSocket socket = AFUNIXSocket.newInstance()) {
            try {
                AFUNIXSocketAddress address = AFUNIXSocketAddress.of(socketPath);
                socket.connect(address);
                AFUNIXSocketChannel chanel = socket.getChannel();
                System.out.println("Connected");
                writeMessageToSocket(chanel, "Hello socket!");
            } catch (SocketException e) {
                System.out.println("Cannot connect to server.");
                System.out.println();
                throw e;
            }

            System.out.println("****************************************");
            try (OutputStream os = socket.getOutputStream()) {
                System.out.println("Replying to server...");
                os.write("Hello Server! ".getBytes("UTF-8"));
                os.flush();
            }
        }

        System.out.println("End of communication client.");
    }

    private static void writeMessageToSocket(SocketChannel socketChannel, String message)
            throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();
        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }
}

