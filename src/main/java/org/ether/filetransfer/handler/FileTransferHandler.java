package org.ether.filetransfer.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileTransferHandler implements HttpHandler {

    @Setter private File file;

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        //设置响应头，必须在sendResponseHeaders方法之前设置！
        exchange.getResponseHeaders().add("Content-Disposition", "attachment;filename=" + file.getName());

        byte[] bytes = new byte[1024 * 1024 * 50];
        int len;
        exchange.sendResponseHeaders(200, file.length());
        try (InputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = exchange.getResponseBody()) {
            exchange.setStreams(inputStream, outputStream);
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0 ,len);
                outputStream.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //设置响应码和响应体长度，必须在getResponseBody方法之前调用！
    }
}
