# LINFO2252: Software Maintenance and Evolution

## Overview
This Java-based application is designed to manage user profiles, medical history, and appointments. It is structured using the Model-View-Controller (MVC) architectural pattern, ensuring a scalable and maintainable codebase by separating data, presentation, and application logic.

### 1. Model (`linfo2252.model`)
The Model represents the core business logic, application state, and data structures. It is completely independent of the user interface.
* **Core Entities:** Includes data structures like `UserProfile`, `Appointment`, `Feature`, and `MedicalHistoryEvent`.
* **State Management:** Manages the system's operational and user interface states through classes like `SystemState`, `UiState`, and `StateService`.
* **Time Simulation:** Handles temporal data via `TimeEvent` and `TimeEventSystem`.

### 2. View (`linfo2252.view`)
The View is responsible for rendering the interface and presenting data to the user. It reads data from the Model but does not directly modify it.
* **Graphical & Console Interfaces:** Includes distinct presentation layers such as `MainView`, `WelcomeView`, `UserView`, and `ConsoleView`.
* **Component Views:** Modular UI segments like `AppointmentView`, `AppointmentHistoryView`, and `TimeControlPanel` manage specific feature displays.

### 3. Controller (`linfo2252.controller`)
The Controller acts as the mediator between the View and the Model. It listens for user interactions from the View, processes the input, and executes the appropriate updates on the Model.
* **Interfaces & Implementations:** Structured around `ControllerInterface.java` and `Controller.java` to ensure loose coupling.

### 4. Observer Pattern (`linfo2252.observer`)
To bridge the MVC components efficiently, the project relies on the Observer pattern.
* **Decoupled Communication:** Using `Observable.java` and `Observer.java`, the Model can broadcast state changes to any registered Views without needing to know the specific details of those Views[cite: 1]. This ensures the UI updates automatically whenever the underlying data changes.


## Running the project
The project can be compiled and executed directly via the command line.

### Prerequisites
* Ensure you have a standard **Java Development Kit (JDK)** installed.
* Verify your installation by running `javac -version` and `java -version` in your terminal.

### Step 1: Navigate to the Source Directory
Open your terminal and change your directory to the `src` folder where the root package (`linfo2252`) is located.

### Step 2: Compile the application

```terminal
javac linfo2252/LINFO2252.java
```

### Step 3: Run the application

```terminal
java linfo2252.LINFO2252
```
