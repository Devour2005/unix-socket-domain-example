package com.unix_socket.demo.springbootexample.server.receiver;

import org.newsclub.net.unix.AFUNIXServerSocket;
import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import org.newsclub.net.unix.AFUNIXSocketChannel;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Component
public class MessageReceiver {

    public void receive() throws Exception {
        System.out.println("[INFO] Using lib server.");

        Path socketFile = Path
                .of(System.getProperty("user.home"))
                .resolve("server.socket");
        Files.deleteIfExists(socketFile);

        try (AFUNIXServerSocket server = AFUNIXServerSocket.newInstance();) {
            try {
                AFUNIXSocketAddress address = AFUNIXSocketAddress.of(socketFile);
                server.bind(address);

                System.out.println("[INFO] Waiting for client to connect...");
                AFUNIXSocket accept = server.accept();
                System.out.println("[INFO] Client connected");
                AFUNIXSocketChannel channel = accept.getChannel();

                //                providerChannel(socketFile);
                System.out.println("[INFO] Reading message");
                Optional<String> message = readMessageFromSocket(channel);
                message.ifPresent(System.out::println);
            } catch (SocketException e) {
                System.out.println("Server not started");
                System.out.println();
                e.printStackTrace();
            }

            /*try (InputStream is = sock.getInputStream(); //
                 OutputStream os = sock.getOutputStream();) {

                byte[] buf = new byte[128];

                int read = is.read(buf);
                System.out.println("Server says: " + new String(buf, 0, read, "UTF-8"));

                System.out.println("Replying to server...");
                os.write("Hello Server".getBytes("UTF-8"));
                os.flush();
            }*/
        }

        System.out.println("End of a server communication.");
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
