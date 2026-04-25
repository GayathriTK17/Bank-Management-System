package server;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8080;

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api", new RequestHandler());
        server.createContext("/", new StaticFileHandler());
        server.setExecutor(java.util.concurrent.Executors.newFixedThreadPool(4)); // ADD THIS LINE
        server.start();

        System.out.println("=========================================");
        System.out.println("  Bank Management System started!");
        System.out.println("  Open: http://localhost:" + port);
        System.out.println("=========================================");
        System.out.println("  Demo Accounts:");
        System.out.println("  ACC1001 | PIN: 1234 (Savings - Arun)");
        System.out.println("  ACC1002 | PIN: 5678 (Current - Priya)");
        System.out.println("  ACC1003 | PIN: 9999 (Savings - Rahul)");
        System.out.println("=========================================");
    }
}