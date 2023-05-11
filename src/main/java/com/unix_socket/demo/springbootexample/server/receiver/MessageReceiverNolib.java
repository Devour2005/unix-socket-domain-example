package com.unix_socket.demo.springbootexample.server.receiver;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Component
public class MessageReceiverNolib {

    public void receive() {
        try {
            System.out.println("[INFO] No lib server.");

            Path socketPath = Path
                    .of(System.getProperty("user.home"))
                    .resolve("server.socket");
            Files.deleteIfExists(socketPath);

            UnixDomainSocketAddress address = UnixDomainSocketAddress.of(socketPath);
            StandardProtocolFamily family = StandardProtocolFamily.UNIX;
            ServerSocketChannel server = ServerSocketChannel.open(family);
            server.bind(address);

            System.out.println("[INFO] Waiting for client to connect...");
            SocketChannel channel = server.accept();
            System.out.println("[INFO] Client connected");
            System.out.println("[INFO] Reading message");
            if (channel.isOpen()) {
                readMessageFromSocket(channel)
                        .ifPresent(System.out::println);
                Thread.sleep(100);
            } else {
                System.out.println("[INFO] Channel is closed");
            }
            System.out.println("[INFO] End of a server communication.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Optional<String> readMessageFromSocket(SocketChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = channel.read(buffer);
        if (bytesRead < 0)
            return Optional.empty();

        byte[] bytes = new byte[bytesRead];
        buffer.flip();
        buffer.get(bytes);
        String message = new String(bytes);
        return Optional.of(message);
    }
}
