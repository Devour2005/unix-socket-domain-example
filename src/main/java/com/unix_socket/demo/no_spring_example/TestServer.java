package com.unix_socket.demo.no_spring_example;

import org.newsclub.net.unix.*;

import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class TestServer {

    private static final String PATH_TO_YOUR_SOCKET_SOCK = "D:\\TheCoon\\Projects\\Examples\\unix-try\\demo\\src\\main\\java\\com\\unix\\demo\\test\\socket.sock";

    public static void main(String[] args) throws IOException {
        Path socketPath = Path
                .of(System.getProperty("user.home"))
                .resolve("server.socket");


        // in case the file is left over from the last run,
        // this makes the demo more robust
        Files.deleteIfExists(socketPath);

        try (AFUNIXServerSocket server = AFUNIXServerSocket.newInstance();) {
            try {
                AFUNIXSocketAddress address = AFUNIXSocketAddress.of(socketPath);
                server.bind(address);
                System.out.println("isOpen " + server.getChannel().isOpen());
                System.out.println("isBound " + server.isBound());
                AFUNIXSocket accept = server.accept();
                AFUNIXSocketChannel channel = accept.getChannel();

                System.out.println("channel " + channel.toString());

//                providerChannel(socketFile);
                Optional<String> message = readMessageFromSocket(channel);
                message.ifPresent(System.out::println);
            } catch (SocketException e) {
                System.out.println("Server not started");
                System.out.println();
                throw e;
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

        System.out.println("End of communication server.");
    }

    private static void providerChannel(Path socketFile) throws IOException {
        AFUNIXSelectorProvider provider = AFUNIXSelectorProvider.provider();
        AFUNIXSocketChannel sc = provider.openSocketChannel();
        sc.connect(AFUNIXSocketAddress.of(socketFile));
        SocketChannel providerChannel = provider.openSocketChannel();

        System.out.println("provider channel open " + providerChannel.isOpen());
        System.out.println("provider channel connected " + providerChannel.isConnected());
        System.out.println("provider channel getLocalAddress " + providerChannel.getLocalAddress());
        readMessageFromSocket(providerChannel);
    }

    private static Optional<String> readMessageFromSocket(SocketChannel channel) throws IOException {
        System.out.println("Reading");
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

