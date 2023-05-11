package com.unix_socket.demo.springbootexample.client.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;

@Service
public class MessageSenderServiceNolib {

    private static final String MESSAGE = "Hello, unix-socket server!";

    public void connectAndSendMessage() {
        Path socketFile = Path
                .of(System.getProperty("user.home"))
                .resolve("server.socket");

        try {
            UnixDomainSocketAddress address = UnixDomainSocketAddress.of(socketFile);
            SocketChannel channel = SocketChannel.open(StandardProtocolFamily.UNIX);
            channel.connect(address);
            writeMessageToSocket(channel, MESSAGE);
            System.out.println("****************************************");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("End of a client communication.");
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
