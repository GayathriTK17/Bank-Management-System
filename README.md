# Bank Management System

A simple Bank Management System built using Core Java (OOP) for the backend and HTML, CSS, JavaScript for the frontend. No frameworks, no database — everything runs in memory.

---

## OOP Concepts Used

| Concept | Where |
|---|---|
| Encapsulation | `Account.java`, `Customer.java` — private fields with getters/setters |
| Inheritance | `SavingsAccount`, `CurrentAccount` extend `Account` |
| Polymorphism | `calculateInterest()` and `withdraw()` overridden per account type |
| Abstraction | Abstract class `Account` + `Transactionable` interface |
| Singleton | `BankService` — only one instance throughout the app |

---

## Tech Stack

- **Backend** — Core Java (no frameworks), Java built-in `HttpServer`
- **Frontend** — HTML, CSS, Vanilla JavaScript (Fetch API)
- **Storage** — In-memory (`HashMap`, `ArrayList`)
- **Architecture** — MVC pattern

---
