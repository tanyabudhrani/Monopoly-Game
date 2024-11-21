# Monopoly Game

## Project Overview
This repository contains a command-line implementation of the Monopoly Game for the COMP3211 Software Engineering course. The game provides a simplified Monopoly experience with features like property management, player interactions, and board customization. Designed with scalability and reliability in mind, the project adheres to object-oriented programming principles and includes thorough testing to ensure functionality.

---

## Repository Contents
1. **Source Code**:
   - `MonopolyGame.java`: Main game implementation containing the core mechanics and logic.
   - `MonopolyGameTest.java`: Unit tests covering various game scenarios.

2. **Documentation**:
   - **System Requirements Specification (SRS)**: Outlines user and functional requirements for the game.
   - **Developer Manual**: Includes setup instructions, build/run commands, and debugging tips.
   - **User Manual**: Explains how to play the game, including game rules, controls, and special squares.

3. **Design Documents**:
   - Activity Diagram: Visualizes the game's workflow, from initialization to game-ending conditions.

4. **Presentation Slides**:
   - Explains the project’s features, design, and key lessons learned.

---

## Key Features
- **Core Mechanics**:
  - 2-6 players compete by rolling dice, moving on a 20-square board, buying properties, and collecting rent.
  - Supports special actions such as jail mechanics, chance cards, and income tax.

- **Customization**:
  - Players can modify board squares, including names, property prices, and rent values.

- **Save and Load**:
  - Save game states to a file and resume later.

- **Error Handling**:
  - Robust input validation for smooth gameplay.

- **Testing**:
  - Comprehensive unit tests using JUnit 5.

---

## Setup Instructions
### Prerequisites
- **Java Version**: 8 or higher (tested with Java 17)
- **Build Tool**: Maven 3.8.0 or higher
- **IDE**: IntelliJ IDEA (recommended)

### How to Run
1. Clone the repository:
   ```bash
   git clone [repository-url]
   cd [project-directory]
   ```
2. Open in IntelliJ IDEA or your preferred IDE and build the project.

3. Run the game:
   - **Using IntelliJ**: Right-click on `MonopolyGame.java` and select `Run`.
   - **Command Line**:
     ```bash
     mvn clean package
     java -cp target/classes mono.MonopolyGame
     ```

### Running Tests
- In IntelliJ, open `MonopolyGameTest.java` and run tests using the green play button.
- Or, run tests from the command line:
   ```bash
   mvn test
   ```

---

## Authors
Group 13:
- Tanya Sanjay Budhrani
- Tanchhoma Limbu
- Tahmin Anower
- Jyotsna Venkatesan
