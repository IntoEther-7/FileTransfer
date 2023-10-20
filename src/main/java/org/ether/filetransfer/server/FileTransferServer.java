package org.ether.filetransfer.server;

import com.sun.net.httpserver.HttpServer;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.ether.filetransfer.handler.FileTransferHandler;

@Slf4j
public class FileTransferServer implements AutoCloseable {
    private HttpServer server;
    private final FileTransferHandler handler;
    int port;
    int backlog;

    public FileTransferServer() {
        this(17890, 20);
    }

    public FileTransferServer(int port, int backlog) {
        this.port = port;
        this.backlog = backlog;
        handler = new FileTransferHandler();
    }

    public void setFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            handler.setFile(file);
        } else {
            log.error("文件不存在：{}", file.getAbsolutePath());
        }
    }

    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(port), backlog);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.createContext("/file", handler);
        server.setExecutor(Executors.newFixedThreadPool(backlog));
        server.start();
    }

    public void close() {
        server.stop(0);
    }
}
