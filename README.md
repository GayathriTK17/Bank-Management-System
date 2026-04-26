# Bank Management System

A web-based Bank Management System built using Core Java demonstrating all four OOP concepts — without any framework or database.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Core Java, Java built-in `HttpServer` |
| Frontend | HTML, CSS, Vanilla JavaScript |
| Storage | In-memory (`HashMap`, `ArrayList`) |
| Architecture | MVC Pattern |

---

## OOP Concepts

**Abstraction**
- `Account.java` — abstract class with abstract methods `calculateInterest()` and `getAccountType()`
- `Transactionable.java` — interface implemented by `BankService`

**Encapsulation**
- `Account.java` and `Customer.java` — all fields are private, accessed only through getters and setters

**Inheritance**
- `SavingsAccount` extends `Account` — 4% interest, minimum balance Rs.500
- `CurrentAccount` extends `Account` — 2% interest, overdraft up to Rs.1000

**Polymorphism**
- `calculateInterest()` — overridden in both subclasses with different rates
- `withdraw()` — overridden with different rules per account type

**Singleton**
- `BankService.java` — only one instance of the bank exists throughout the app

---

## Features

- Open Savings / Current account
- Deposit, Withdraw, Transfer money
- Check balance and mini statement
- Apply annual interest
- View all accounts

---

## How to Run

Compile - javac -d out src/model/*.java src/service/*.java src/server/*.java

Run - java -cp out server.Main

Open **http://localhost:8080** in your browser.

---

## Demo Accounts

| Account No. | Name | PIN | Type |
|---|---|---|---|
| ACC1001 | Arun Kumar | 1234 | Savings |
| ACC1002 | Priya Sharma | 5678 | Current |
| ACC1003 | Rahul Mehta | 9999 | Savings |
