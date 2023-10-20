package org.ether.filetransfer;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.ether.filetransfer.server.FileTransferServer;

@Slf4j
public class Main {
    public static void main(String[] args) {
        // SpringApplication.run(Main.class, args);
        FileTransferServer server = new FileTransferServer();
        while (true) {
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();
            if ("q".equals(input)) {
                break;
            } else if ("start".equals(input)) {
                server.start();
            } else if ("close".equals(input)) {
                server.close();
            } else {
                Pattern pattern = Pattern.compile("set (.+)");
                Matcher matcher = pattern.matcher(input);
                if (matcher.find()) {
                    String trim = matcher.group(1).trim();
                    server.setFile(trim);
                } else {
                    log.info("输入不正确");
                }
            }
        }
    }
}
