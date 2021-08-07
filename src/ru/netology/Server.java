package ru.netology;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {
    public static final int PORT = 8181;

    // В данной задаче выбран способ взаимодействия (NonBlocking),
    // так как тут данные передаются в буфере постоянно, целыми строками

    public static void main(String[] args) throws IOException {
        final ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("localhost", PORT));

        while (true) {
            SocketChannel socketChannel = serverChannel.accept();
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
            while (socketChannel.isConnected()) {
                int bytesCount = socketChannel.read(inputBuffer);

                if (bytesCount == -1) {
                    break;
                }
                final String message = new String(inputBuffer.array(), 0, bytesCount,
                        StandardCharsets.UTF_8);
                inputBuffer.clear();
                String answer = delAllSpace(message);
                socketChannel.write(ByteBuffer.wrap(("Результат после удаления пробелов: "
                        + answer).getBytes(StandardCharsets.UTF_8)));
            }
        }
    }

    private static String delAllSpace(String message) {
        StringBuffer buffer = new StringBuffer();
        char[] array = message.toCharArray();
        for (char c : array) {
            if (c != ' ') {
                buffer.append(c);
            }
        }
        return buffer.toString();
        /*
        Ну ил вот так лаконично...
        return message.replaceAll(" ", "");
         */
    }
}
