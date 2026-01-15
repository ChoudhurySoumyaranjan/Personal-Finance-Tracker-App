# Personal-Finance-Tracker-App

# 💰 Personal Finance Tracker

A full-stack **Personal Finance Tracking Web Application** built using **Spring Boot, Thymeleaf, Spring Security, and MySQL**.  
This application helps users **track income, expenses, budgets**, and visualize their financial health in one place.

---

## 🚀 Features

### 👤 User Management
- Secure user authentication & authorization (Spring Security)
- Role-based access control
- Profile management
- Password reset support

### 💸 Expense Management
- Add, edit, delete expenses
- Categorize expenses
- Filter by date and category
- Monthly expense tracking

### 💵 Income Management
- Record income sources
- Track income frequency
- Filter by date and source

### 📊 Dashboard
- Total Income
- Total Expenses
- Current Balance
- Recent Transactions
- Category-wise expense summary

### 🎯 Budget Management
- Set monthly budgets per category
- Track spending against budget
- Budget validation per user & month

### 🔍 Search & Filters
- Search income/expense by keyword
- Filter by date range
- Category-based filters

---

## 🛠 Tech Stack

### Backend
- **Java 21**
- **Spring Boot**
- **Spring Security**
- **Spring Data JPA (Hibernate)**
- **MySQL**

### Frontend
- **Thymeleaf**
- **Bootstrap 5**
- **HTML5 / CSS3**
- **Bootstrap Icons**

### Tools & Libraries
- Lombok
- Maven
- Jakarta Validation API

---

## 📂 Project Structure
src/main/java/com/soumya/main
├── config
│ └── SecurityConfig, CustomUserDetails
├── controllers
│ └── DashboardController, ExpenseController, IncomeController
├── dtos
│ └── TransactionDTO, CategorySummaryDTO, BudgetFormDTO
├── entity
│ └── User, Expense, Income, Budget
├── repository
│ └── UserRepository, ExpenseRepository, IncomeRepository
├── service
│ └── DashboardService, DashboardServiceImpl
└── PersonalFinanceTrackerApplication.java

src/main/resources
├── templates
│ ├── dashboard.html
│ ├── expenses.html
│ ├── incomes.html
│ ├── budgets.html
│ └── fragments
│ ├── header.html
│ └── footer.html
├── static
│ └── style.css
└── application.properties 


---

## 🧪 Database Design

### Entities
- **User**
- **Expense**
- **Income**
- **Budget**


