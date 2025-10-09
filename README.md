📖 Project Overview
DineOut is a comprehensive desktop application, built entirely in Java, that simulates a modern restaurant management platform. Developed as a college project, its primary goal is to provide a strong, practical demonstration of Object-Oriented Programming (OOP) principles by modeling a complex, real-world scenario. The application provides a complete user journey—from discovering a restaurant to finalizing a bill—all through an intuitive graphical user interface (GUI) built with Java Swing.

✨ Core Features
User Authentication: A secure login system for personalized user sessions.

Restaurant Discovery: Browse a list of restaurants, view detailed information like cuisine and ratings, and perform targeted keyword searches.

Seamless Reservations: An interactive module allows users to select a restaurant, date, time, and number of guests to book a table instantly.

Interactive Ordering: View a restaurant's full menu, add multiple items to a cart, and see a dynamically calculated total bill before placing an order.

Payment Simulation: The system models different payment methods (e.g., Card, Wallet) to showcase polymorphism.

Personalized History: Logged-in users have access to a complete and detailed history of all their past reservations and food orders.

💻 Technical Design & OOP Concepts
The application’s architecture is fundamentally object-oriented, with a clean separation between the business logic (the "Controller") and the presentation layer (the "View"). This design ensures the codebase is modular, maintainable, and scalable.

Key OOP concepts demonstrated include:

Encapsulation: Data integrity is protected within classes like Restaurant and User. All data is kept private and is only accessible through public getter and setter methods.

Inheritance: A base User class defines common attributes, while a specialized Customer class extends it to add specific functionalities like order and reservation histories. This promotes code reuse.

Polymorphism: The payment system is a prime example. An abstract Payment class defines a contract that concrete classes like CardPayment and WalletPayment must follow, allowing different payment types to be processed interchangeably.

Abstraction: The Payment class is abstract, defining what a payment does (processPayment) without specifying how. This abstracts the payment concept and makes the system easily extensible to new payment methods.

🚀 How to Run the Project
Prerequisites

Java Development Kit (JDK) 8 or higher installed and configured on your system.

Steps to Run

Clone the Repository:

git clone [https://github.com/your-username/DineOut-Java-App.git](https://github.com/your-username/DineOut-Java-App.git)

Navigate to the Directory:

cd DineOut-Java-App

Compile all Java files:
Open a terminal or command prompt in the project's root directory and run the javac command.

javac *.java

Run the GUI Application:
Execute the main GUI class to launch the application.

java DineOutGUI
