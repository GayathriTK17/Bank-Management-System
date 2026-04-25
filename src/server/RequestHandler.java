package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Account;
import model.Customer;
import service.BankService;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHandler implements HttpHandler {

    private BankService bankService = BankService.getInstance();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().add("Content-Type", "application/json");

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(200, -1);
            return;
        }

        String path = exchange.getRequestURI().getPath();
        String response;

        try {
            switch (path) {
                case "/api/create":   response = handleCreate(exchange);   break;
                case "/api/deposit":  response = handleDeposit(exchange);  break;
                case "/api/withdraw": response = handleWithdraw(exchange); break;
                case "/api/transfer": response = handleTransfer(exchange); break;
                case "/api/balance":  response = handleBalance(exchange);  break;
                case "/api/history":  response = handleHistory(exchange);  break;
                case "/api/interest": response = handleInterest(exchange); break;
                case "/api/accounts": response = handleAllAccounts();      break;
                default:              response = error("Unknown endpoint");
            }
        } catch (Exception e) {
            response = error("Server error: " + e.getMessage());
        }

        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private String handleCreate(HttpExchange ex) throws IOException {
        Map<String, String> p = parseBody(ex);
        String name    = p.get("name");
        String phone   = p.get("phone");
        String pin     = p.get("pin");
        String type    = p.get("type");
        double deposit = Double.parseDouble(p.getOrDefault("deposit", "0"));

        if (name == null || pin == null || type == null) return error("Missing fields");
        if (deposit < 500) return error("Minimum initial deposit is Rs.500");

        Customer c = bankService.createAccount(name, phone, pin, type, deposit);
        return "{\"success\":true,\"accountNumber\":\"" + c.getAccount().getAccountNumber()
             + "\",\"message\":\"Account created successfully\"}";
    }

    private String handleDeposit(HttpExchange ex) throws IOException {
        Map<String, String> p = parseBody(ex);
        String acc    = p.get("accountNumber");
        double amount = Double.parseDouble(p.getOrDefault("amount", "0"));

        if (!bankService.accountExists(acc)) return error("Account not found");
        boolean ok = bankService.deposit(acc, amount);
        if (!ok) return error("Deposit failed. Enter a valid amount.");

        double balance = bankService.getCustomer(acc).getAccount().getBalance();
        return "{\"success\":true,\"balance\":" + balance + ",\"message\":\"Deposited Rs." + amount + " successfully\"}";
    }

    private String handleWithdraw(HttpExchange ex) throws IOException {
        Map<String, String> p = parseBody(ex);
        String acc    = p.get("accountNumber");
        String pin    = p.get("pin");
        double amount = Double.parseDouble(p.getOrDefault("amount", "0"));

        if (!bankService.accountExists(acc)) return error("Account not found");
        boolean ok = bankService.withdraw(acc, amount, pin);
        if (!ok) return error("Withdrawal failed. Check PIN, amount, or minimum balance.");

        double balance = bankService.getCustomer(acc).getAccount().getBalance();
        return "{\"success\":true,\"balance\":" + balance + ",\"message\":\"Withdrew Rs." + amount + " successfully\"}";
    }

    private String handleTransfer(HttpExchange ex) throws IOException {
        Map<String, String> p = parseBody(ex);
        String from   = p.get("fromAccount");
        String to     = p.get("toAccount");
        String pin    = p.get("pin");
        double amount = Double.parseDouble(p.getOrDefault("amount", "0"));

        boolean ok = bankService.transfer(from, to, amount, pin);
        if (!ok) return error("Transfer failed. Check accounts, PIN, or balance.");
        return "{\"success\":true,\"message\":\"Rs." + amount + " transferred to " + to + "\"}";
    }

    private String handleBalance(HttpExchange ex) throws IOException {
        Map<String, String> p = parseBody(ex);
        String acc = p.get("accountNumber");
        String pin = p.get("pin");

        Customer c = bankService.getCustomer(acc);
        if (c == null) return error("Account not found");
        if (!c.checkPin(pin)) return error("Invalid PIN");

        Account a = c.getAccount();
        return "{\"success\":true,\"name\":\"" + c.getName()
             + "\",\"type\":\"" + a.getAccountType()
             + "\",\"balance\":" + a.getBalance() + "}";
    }

    private String handleHistory(HttpExchange ex) throws IOException {
        Map<String, String> p = parseBody(ex);
        String acc = p.get("accountNumber");
        String pin = p.get("pin");

        Customer c = bankService.getCustomer(acc);
        if (c == null) return error("Account not found");
        if (!c.checkPin(pin)) return error("Invalid PIN");

        List<String> history = c.getAccount().getTransactionHistory();
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < history.size(); i++) {
            sb.append("\"").append(history.get(i).replace("\"", "'")).append("\"");
            if (i < history.size() - 1) sb.append(",");
        }
        sb.append("]");
        return "{\"success\":true,\"history\":" + sb + "}";
    }

    private String handleInterest(HttpExchange ex) throws IOException {
        Map<String, String> p = parseBody(ex);
        String acc = p.get("accountNumber");
        String pin = p.get("pin");

        Customer c = bankService.getCustomer(acc);
        if (c == null) return error("Account not found");
        if (!c.checkPin(pin)) return error("Invalid PIN");

        double interest = bankService.applyInterest(acc);
        double balance  = c.getAccount().getBalance();
        return "{\"success\":true,\"interest\":" + interest + ",\"balance\":" + balance
             + ",\"message\":\"Interest of Rs." + interest + " credited\"}";
    }

    private String handleAllAccounts() {
        List<Customer> all = bankService.getAllCustomers();
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < all.size(); i++) {
            Customer c = all.get(i);
            Account  a = c.getAccount();
            sb.append("{\"name\":\"").append(c.getName())
              .append("\",\"accountNumber\":\"").append(a.getAccountNumber())
              .append("\",\"type\":\"").append(a.getAccountType())
              .append("\",\"balance\":").append(a.getBalance()).append("}");
            if (i < all.size() - 1) sb.append(",");
        }
        sb.append("]");
        return "{\"success\":true,\"accounts\":" + sb + "}";
    }

    private Map<String, String> parseBody(HttpExchange ex) throws IOException {
        String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> map = new HashMap<>();
        if (body.isEmpty()) return map;
        for (String pair : body.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2)
                map.put(URLDecoder.decode(kv[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
        }
        return map;
    }

    private String error(String msg) {
        return "{\"success\":false,\"message\":\"" + msg + "\"}";
    }
}