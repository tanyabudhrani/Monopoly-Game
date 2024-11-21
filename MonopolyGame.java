import java.util.*;
import java.io.*;
import java.io.Serializable;


abstract class Square implements Serializable {
    private final int position;
    private final String name;

    public Square(int position, String name) {
        this.position = position;
        this.name = name;
    }

    public int getPosition() { return position; }
    public String getName() { return name; }

    abstract void landOn(Player player, Game game);
    void passBy(Player player, Game game) {}
}

class PropertySquare extends Square {
    private static final long serialVersionUID = 1L;
    private final int price;
    private final int rent;
    private Player owner;

    public PropertySquare(int position, String name, int price, int rent) {
        super(position, name);
        this.price = price;
        this.rent = rent;
    }


    @Override
    void landOn(Player player, Game game) {
        if (owner == null) {
            if (player.getMoney() >= price) {
                boolean validInput = false;
                while (!validInput) {
                    System.out.printf("%s can buy %s for $%d. Do you want to buy? (y/n): ",
                            player.getName(), getName(), price);
                    Scanner scanner = new Scanner(System.in);
                    String choice = scanner.nextLine().trim().toLowerCase();

                    if (choice.equals("y")) {
                        player.reduceMoney(price);
                        owner = player;
                        System.out.printf("%s bought %s for $%d%n",
                                player.getName(), getName(), price);
                        validInput = true;
                    } else if (choice.equals("n")) {
                        System.out.printf("%s chose not to buy %s%n",
                                player.getName(), getName());
                        validInput = true;
                    } else {
                        System.out.println("Invalid input! Please enter 'y' for yes or 'n' for no.");
                    }
                }
            } else {
                System.out.printf("%s cannot afford to buy %s%n",
                        player.getName(), getName());
            }
        } else if (owner != player) {
            System.out.printf("%s pays $%d rent to %s%n",
                    player.getName(), rent, owner.getName());
            player.reduceMoney(rent);
            owner.addMoney(rent);
        } else {
            System.out.printf("%s owns %s - no rent to pay%n",
                    player.getName(), getName());
        }
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }
}

class GoSquare extends Square {
    private static final long serialVersionUID = 1L;

    private static final int SALARY = 1500;

    public GoSquare() {
        super(1, "Go");
    }

    @Override
    void landOn(Player player, Game game) {

        player.addMoney(SALARY);
        System.out.printf("%s receives $%d salary for landing on GO%n",
                player.getName(), SALARY);
    }

    @Override
    void passBy(Player player, Game game) {

        if (player.getPosition() != 1) {
            player.addMoney(SALARY);
            System.out.printf("%s receives $%d salary for passing GO%n",
                    player.getName(), SALARY);
        }
    }
}

class ChanceSquare extends Square {
    private static final long serialVersionUID = 1L;

    private final Random random = new Random();

    public ChanceSquare(int position) {
        super(position, "Chance");
    }

    @Override
    void landOn(Player player, Game game) {
        if (random.nextBoolean()) {
            int gain = (random.nextInt(20) + 1) * 10;
            player.addMoney(gain);
            System.out.printf("%s gains $%d from Chance%n",
                    player.getName(), gain);
        } else {
            int loss = (random.nextInt(30) + 1) * 10;
            player.reduceMoney(loss);
            System.out.printf("%s loses $%d from Chance%n",
                    player.getName(), loss);
        }
    }
}

class IncomeTaxSquare extends Square {
    private static final long serialVersionUID = 1L;

    public IncomeTaxSquare(int position) {
        super(position, "Income Tax");
    }

    @Override
    void landOn(Player player, Game game) {
        System.out.printf("%s landed on Income Tax%n", player.getName());
        int tax = (player.getMoney() / 10) / 10 * 10;
        player.reduceMoney(tax);
        System.out.printf("%s pays $%d in tax%n",
                player.getName(), tax);
    }
}

class JailSquare extends Square {
    private static final long serialVersionUID = 1L;

    public JailSquare() {
        super(11, "In Jail/Just Visiting");
    }

    @Override
    void landOn(Player player, Game game) {
        if (!player.isInJail()) {
            System.out.printf("%s is just visiting jail%n",
                    player.getName());
        }
    }
}

class GoToJailSquare extends Square {
    private static final long serialVersionUID = 1L;

    public GoToJailSquare(int position) {
        super(position, "Go to Jail");
    }

    @Override
    void landOn(Player player, Game game) {
        player.goToJail();
        player.setPosition(11);
        System.out.printf("%s goes to jail%n", player.getName());
    }
}

class FreeParkingSquare extends Square {
    private static final long serialVersionUID = 1L;

    public FreeParkingSquare(int position) {
        super(position, "Free Parking");
    }

    @Override
    void landOn(Player player, Game game) {
        System.out.printf("%s lands on Free Parking. Nothing happens.%n",
                player.getName());
    }
}

class Player implements Serializable {
    private final String name;
    private int money;
    private int position;
    private boolean inJail;
    private int turnsInJail;

    public Player(String name) {
        this.name = name;
        this.money = 1500;
        this.position = 1;
        this.inJail = false;
        this.turnsInJail = 0;
    }

    public String getName() { return name; }
    public int getMoney() { return money; }
    public int getPosition() { return position; }
    public boolean isInJail() { return inJail; }

    public void addMoney(int amount) {
        money += amount;
    }

    public void reduceMoney(int amount) {
        money -= amount;
        if (money < 0) {
            System.out.printf("%s has gone bankrupt!%n", name);
        }
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void goToJail() {
        inJail = true;
        turnsInJail = 0;
    }

    public boolean tryToGetOutOfJail(int dice1, int dice2) {
        turnsInJail++;
        if (dice1 == dice2 || turnsInJail >= 3) {
            inJail = false;
            if (turnsInJail >= 3) {
                reduceMoney(150);
            }
            turnsInJail = 0;
            return true;
        }
        return false;
    }

    public void payJailFine() {
        if (inJail && money >= 150) {
            reduceMoney(150);
            inJail = false;
            turnsInJail = 0;
            System.out.printf("%s pays $150 to get out of jail%n", name);
        }
    }

    public String getStatus() {
        return String.format("%s - $%d - Position: %d%s",
                name, money, position, inJail ? " (In Jail)" : "");
    }
}

class Dice implements Serializable{

    private final Random random = new Random();

    public int roll() {
        return random.nextInt(4) + 1; // 4-sided die (1-4)
    }
}

class Game implements Serializable {
    private List<Player> players;
    private Square[] board;
    private final Dice dice1, dice2;
    private int currentPlayerIndex;
    private int currentRound;
    private static final int MAX_ROUNDS = 100;

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public void incrementRound() {
        currentRound++;
    }

    public void moveToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public Game(List<String> playerNames) {
        if (playerNames.size() < 2 || playerNames.size() > 6) {
            throw new IllegalArgumentException("Must have 2-6 players");
        }

        this.players = new ArrayList<>();
        for (String name : playerNames) {
            players.add(new Player(name));
        }

        this.board = new Square[21];
        initializeBoard();

        this.dice1 = new Dice();
        this.dice2 = new Dice();
        this.currentPlayerIndex = 0;
        this.currentRound = 0;
    }

    private void initializeBoard() {
        board[1] = new GoSquare();
        board[2] = new PropertySquare(2, "Central", 800, 90);
        board[3] = new PropertySquare(3, "Wan Chai", 700, 65);
        board[4] = new IncomeTaxSquare(4);
        board[5] = new PropertySquare(5, "Stanley", 600, 60);
        board[6] = new JailSquare();
        board[7] = new PropertySquare(7, "Shek O", 400, 10);
        board[8] = new PropertySquare(8, "Mong Kok", 500, 40);
        board[9] = new ChanceSquare(9);
        board[10] = new PropertySquare(10, "Tsing Yi", 400, 15);
        board[11] = new FreeParkingSquare(11);
        board[12] = new PropertySquare(12, "Shatin", 700, 75);
        board[13] = new PropertySquare(13, "Tuen Mun", 400, 20);
        board[14] = new PropertySquare(14, "Tai Po", 500, 25);
        board[15] = new PropertySquare(15, "Sai Kung", 400, 10);
        board[16] = new GoToJailSquare(16);
        board[17] = new PropertySquare(17, "Yuen Long", 400, 25);
        board[18] = new PropertySquare(18, "Tai O", 600, 25);
        board[19] = new ChanceSquare(19);
        board[20] = new PropertySquare(20, "Peak", 850, 100);
    }

    public void play() {
        while (!isGameOver()) {
            currentRound++;
            System.out.printf("%nRound %d%n", currentRound);

            Iterator<Player> iterator = players.iterator();
            while (iterator.hasNext()) {
                Player player = iterator.next();
                takeTurn(player);

                if (player.getMoney() < 0) {
                    System.out.printf("%s retires from the game%n", player.getName());

                    for (Square square : board) {
                        if (square instanceof PropertySquare) {
                            PropertySquare property = (PropertySquare) square;
                            property.setOwner(null);
                        }
                    }
                    iterator.remove();
                }
            }
        }

        announceWinner();
    }

    public void takeTurn(Player player) {
        if (player.isInJail()) {
            handleJailTurn(player);
            return;
        }

        int roll1 = dice1.roll();
        int roll2 = dice2.roll();
        int totalRoll = roll1 + roll2;

        System.out.printf("%s rolled %d + %d = %d%n",
                player.getName(), roll1, roll2, totalRoll);

        int oldPosition = player.getPosition();
        int newPosition = oldPosition + totalRoll;

        // Handle passing Go
        if (newPosition > 20) {
            newPosition = newPosition - 20;
            board[1].passBy(player, this);
        }

        player.setPosition(newPosition);
        board[newPosition].landOn(player, this);
    }

    private void handleJailTurn(Player player) {
        System.out.printf("%s is in jail.%n", player.getName());


        if (player.getMoney() >= 150 && player.isInJail()) {
            System.out.printf("Do you want to pay $150 to get out of jail? (y/n): ");
            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("y")) {
                player.payJailFine();
                if (!player.isInJail()) {
                    takeTurn(player);
                    return;
                }
            }
        }

        int roll1 = dice1.roll();
        int roll2 = dice2.roll();

        System.out.printf("%s rolled %d + %d%n",
                player.getName(), roll1, roll2);

        if (player.tryToGetOutOfJail(roll1, roll2)) {
            if (roll1 == roll2) {
                System.out.printf("%s got out of jail with doubles!%n",
                        player.getName());
            } else {
                System.out.printf("%s got out of jail after three turns and paid $150%n",
                        player.getName());
            }
            player.setPosition(player.getPosition() + roll1 + roll2);
            board[player.getPosition()].landOn(player, this);
        } else {
            System.out.printf("%s stays in jail%n", player.getName());
        }
    }

    public boolean isGameOver() {
        return currentRound == MAX_ROUNDS || players.size() == 1;
    }

    public void announceWinner() {
        if (players.isEmpty()) {
            System.out.println("Game over! No winners - everyone went bankrupt!");
            return;
        }

        int maxMoney = players.stream()
                .mapToInt(Player::getMoney)
                .max()
                .getAsInt();

        List<Player> winners = players.stream()
                .filter(p -> p.getMoney() == maxMoney)
                .toList();

        if (winners.size() == 1) {
            System.out.printf("Game over! %s wins with $%d%n",
                    winners.get(0).getName(), maxMoney);
        } else {
            System.out.printf("Game over! It's a tie between: %s with $%d each%n",
                    winners.stream().map(Player::getName).reduce((a, b) -> a + " and " + b).get(),
                    maxMoney);
        }
    }

    public String getGameStatus() {
        StringBuilder status = new StringBuilder();
        status.append("Current Round: ").append(currentRound).append("\n");
        status.append("Current Player: ").append(players.get(currentPlayerIndex).getName()).append("\n");
        status.append("Board Status:\n");
        for (int i = 1; i < board.length; i++) {
            status.append(String.format("%d. %s", i, board[i].getName()));
            if (board[i] instanceof PropertySquare) {
                PropertySquare property = (PropertySquare) board[i];
                status.append(String.format(" (Owner: %s)",
                        property.getOwner() != null ? property.getOwner().getName() : "None"));
            }
            status.append("\n");
        }
        return status.toString();
    }

    public String getAllPlayersStatus() {
        StringBuilder status = new StringBuilder();
        for (Player player : players) {
            status.append(player.getStatus()).append("\n");
        }
        return status.toString();
    }

    public Player getNextPlayer() {
        int nextPlayerIndex = (currentPlayerIndex + 1) % players.size();
        return players.get(nextPlayerIndex);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void saveGame(String fileName) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(this);
        }
    }

    public static Game loadGame(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            return (Game) in.readObject();
        }
    }
}

class GameBoard implements Serializable {
    private Square[] squares;

    public GameBoard() {
        squares = new Square[21];
        initializeDefaultBoard();
    }

    private void initializeDefaultBoard() {

    }

    public void setSquare(int position, Square square) {
        if (position < 1 || position > 20) {
            throw new IllegalArgumentException("Invalid position. Must be between 1 and 20.");
        }
        squares[position] = square;
    }

    public Square getSquare(int position) {
        return squares[position];
    }

    public void saveBoard(String fileName) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(this);
        }
    }

    public static GameBoard loadBoard(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            return (GameBoard) in.readObject();
        }
    }
}

public class MonopolyGame {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("Monopoly Game Menu:");
            System.out.println("1. Start New Game");
            System.out.println("2. Load Game");
            System.out.println("3. Design New Board");
            System.out.println("4. Customize Existing Board");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    startNewGame();
                    break;
                case 2:
                    loadGame();
                    break;
                case 3:
                    designNewBoard();
                    break;
                case 4:
                    customizeExistingBoard();
                    break;
                case 5:
                    System.out.println("Thank you for playing!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void startNewGame() {
        System.out.print("Enter the number of players: ");
        int numPlayers = scanner.nextInt();
        scanner.nextLine();

        List<String> playerNames = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            System.out.printf("Enter name for Player %d (or press Enter for random name): ", i + 1);
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                name = generateRandomName();
            }
            playerNames.add(name);
        }

        Game game = new Game(playerNames);
        playGame(game);
    }

    private static void loadGame() {
        System.out.print("Enter the filename to load the game from: ");
        String fileName = scanner.nextLine();
        try {
            Game game = Game.loadGame(fileName);
            System.out.println("Game loaded successfully!");
            playGame(game);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading game: " + e.getMessage());
        }
    }

    private static void playGame(Game game) {
        while (!game.isGameOver()) {
            System.out.println("\n" + game.getGameStatus());
            System.out.println("1. Roll Dice");
            System.out.println("2. View All Players Status");
            System.out.println("3. View Next Player");
            System.out.println("4. Save Game");
            System.out.println("5. Exit to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    game.takeTurn(game.getCurrentPlayer());
                    game.moveToNextPlayer();
                    if (game.getCurrentPlayer() == game.getPlayers().get(0)) {
                        game.incrementRound();
                    }
                    break;
                case 2:
                    System.out.println(game.getAllPlayersStatus());
                    break;
                case 3:
                    System.out.println("Next player: " + game.getNextPlayer().getName());
                    break;
                case 4:
                    System.out.print("Enter filename to save the game: ");
                    String saveFileName = scanner.nextLine();
                    try {
                        game.saveGame(saveFileName);
                        System.out.println("Game saved successfully!");
                    } catch (IOException e) {
                        System.out.println("Error saving game: " + e.getMessage());
                    }
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        game.announceWinner();
    }

    private static void designNewBoard() {
        GameBoard board = new GameBoard();
        customizeBoard(board);
    }

    private static void customizeExistingBoard() {
        System.out.print("Enter the filename of the board to load: ");
        String fileName = scanner.nextLine();
        try {
            GameBoard board = GameBoard.loadBoard(fileName);
            System.out.println("Board loaded successfully!");
            customizeBoard(board);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading board: " + e.getMessage());
        }
    }

    private static void customizeBoard(GameBoard board) {
        while (true) {
            System.out.println("\nBoard Customization:");
            System.out.println("1. Modify Square");
            System.out.println("2. Save Board");
            System.out.println("3. Exit to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    modifySquare(board);
                    break;
                case 2:
                    System.out.print("Enter filename to save the board: ");
                    String saveFileName = scanner.nextLine();
                    try {
                        board.saveBoard(saveFileName);
                        System.out.println("Board saved successfully!");
                    } catch (IOException e) {
                        System.out.println("Error saving board: " + e.getMessage());
                    }
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void modifySquare(GameBoard board) {
        System.out.print("Enter the position of the square to modify (1-20): ");
        int position = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter the new name for the square: ");
        String name = scanner.nextLine();

        System.out.println("Select the type of square:");
        System.out.println("1. Property");
        System.out.println("2. Go");
        System.out.println("3. Chance");
        System.out.println("4. Income Tax");
        System.out.println("5. Jail");
        System.out.println("6. Go to Jail");
        System.out.println("7. Free Parking");
        System.out.print("Enter your choice: ");

        int typeChoice = scanner.nextInt();
        scanner.nextLine();

        Square newSquare;
        switch (typeChoice) {
            case 1:
                System.out.print("Enter property price: ");
                int price = scanner.nextInt();
                System.out.print("Enter property rent: ");
                int rent = scanner.nextInt();
                newSquare = new PropertySquare(position, name, price, rent);
                break;
            case 2:
                newSquare = new GoSquare();
                break;
            case 3:
                newSquare = new ChanceSquare(position);
                break;
            case 4:
                newSquare = new IncomeTaxSquare(position);
                break;
            case 5:
                newSquare = new JailSquare();
                break;
            case 6:
                newSquare = new GoToJailSquare(position);
                break;
            case 7:
                newSquare = new FreeParkingSquare(position);
                break;
            default:
                System.out.println("Invalid choice. Square not modified.");
                return;
        }

        board.setSquare(position, newSquare);
        System.out.println("Square modified successfully!");
    }

    private static String generateRandomName() {
        String[] adjectives = {"Happy", "Lucky", "Clever", "Brave", "Witty"};
        String[] nouns = {"Player", "Gamer", "Tycoon", "Mogul", "Investor"};
        Random random = new Random();
        return adjectives[random.nextInt(adjectives.length)] + " " + nouns[random.nextInt(nouns.length)];
    }
}