import java.util.Scanner;
import java.util.Random;

public class ArcadeNumberGame {
    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 100;
    private static final int MAX_ATTEMPTS = 7;
    private static final String[] LEVEL_NAMES = {
            "Rookie", "Explorer", "Detective", "Master", "Psychic"
    };

    private Random random;
    private Scanner scanner;
    private int playerLevel;
    private int totalPoints;
    private int consecutiveWins;

    public ArcadeNumberGame() {
        random = new Random();
        scanner = new Scanner(System.in);
        playerLevel = 0;
        totalPoints = 0;
        consecutiveWins = 0;
    }

    public void startGame() {
        displayWelcomeBanner();
        String playerName = getPlayerName();
        boolean continuePlaying = true;

        while (continuePlaying) {
            playRound(playerName);
            continuePlaying = askForNextRound();
        }

        displayGameOver(playerName);
        scanner.close();
    }

    private void displayWelcomeBanner() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║      MYSTERIOUS NUMBER CHALLENGE       ║");
        System.out.println("║    Can you read the computer's mind?   ║");
        System.out.println("╚════════════════════════════════════════╝");
    }

    private String getPlayerName() {
        System.out.print("\n🎮 Enter your challenger name: ");
        return scanner.nextLine().trim();
    }

    private void playRound(String playerName) {
        int mysteryNumber = random.nextInt(MAX_NUMBER - MIN_NUMBER + 1) + MIN_NUMBER;
        int attempts = 0;
        boolean solved = false;

        System.out.println("\n🌟 ROUND " + (playerLevel + 1) + " - " + LEVEL_NAMES[Math.min(playerLevel, LEVEL_NAMES.length - 1)] + " LEVEL 🌟");
        System.out.println("💭 I'm thinking of a number between " + MIN_NUMBER + " and " + MAX_NUMBER);
        System.out.println("🎯 You have " + MAX_ATTEMPTS + " crystal ball gazes to find it!");

        while (attempts < MAX_ATTEMPTS && !solved) {
            int remainingAttempts = MAX_ATTEMPTS - attempts;
            displayRemainingAttempts(remainingAttempts);

            int guess = getPlayerGuess();
            attempts++;

            if (guess == mysteryNumber) {
                solved = true;
                handleCorrectGuess(playerName, attempts);
            } else {
                provideHint(guess, mysteryNumber, remainingAttempts);
            }
        }

        if (!solved) {
            handleGameLoss(mysteryNumber, playerName);
        }
    }

    private void displayRemainingAttempts(int remaining) {
        System.out.print("\n💫 Crystal ball charges: ");
        for (int i = 0; i < remaining; i++) {
            System.out.print("○ ");
        }
        System.out.println();
    }

    private int getPlayerGuess() {
        while (true) {
            System.out.print("🔮 Focus your mind and enter your guess: ");
            try {
                int guess = Integer.parseInt(scanner.nextLine());
                if (guess >= MIN_NUMBER && guess <= MAX_NUMBER) {
                    return guess;
                } else {
                    System.out.println("⚠️ Your mind wandered! Stay between " + MIN_NUMBER + " and " + MAX_NUMBER);
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ That's not a number! Focus harder!");
            }
        }
    }

    private void handleCorrectGuess(String playerName, int attempts) {
        int roundPoints = calculatePoints(attempts);
        totalPoints += roundPoints;
        consecutiveWins++;
        playerLevel = Math.min(playerLevel + 1, LEVEL_NAMES.length - 1);

        System.out.println("\n🎉 EXTRAORDINARY MENTAL POWERS, " + playerName.toUpperCase() + "! 🎉");
        System.out.println("✨ You unveiled the mystery in " + attempts + " attempts!");
        System.out.println("💫 Round Points: " + roundPoints);
        System.out.println("🏆 Total Points: " + totalPoints);
        System.out.println("⭐ Consecutive Wins: " + consecutiveWins);
        System.out.println("📈 Current Level: " + LEVEL_NAMES[playerLevel]);
    }

    private void provideHint(int guess, int target, int remaining) {
        String[] lowResponses = {
                "The spirits whisper of higher numbers... 📈",
                "Your guess needs to ascend further! ⬆️",
                "The mystical forces point upward! 🔝"
        };

        String[] highResponses = {
                "The spirits speak of lower numbers... 📉",
                "Your guess must descend! ⬇️",
                "The mystical forces point downward! 🔽"
        };

        String[] responses = guess < target ? lowResponses : highResponses;
        System.out.println("\n" + responses[random.nextInt(responses.length)]);

        // Provide additional hint when running low on attempts
        if (remaining <= 2) {
            int difference = Math.abs(guess - target);
            if (difference > 20) {
                System.out.println("💫 You're quite far from the truth...");
            } else if (difference > 10) {
                System.out.println("💫 You're getting warmer...");
            } else {
                System.out.println("💫 You're very close to enlightenment!");
            }
        }
    }

    private void handleGameLoss(int mysteryNumber, String playerName) {
        consecutiveWins = 0;
        System.out.println("\n💔 The crystal ball has gone dark, " + playerName + "!");
        System.out.println("🎲 The mystery number was: " + mysteryNumber);
        if (playerLevel > 0) playerLevel--;
        System.out.println("📉 You've been demoted to " + LEVEL_NAMES[playerLevel] + " level");
    }

    private int calculatePoints(int attempts) {
        int basePoints = (MAX_ATTEMPTS - attempts + 1) * 100;
        int levelBonus = playerLevel * 50;
        int streakBonus = consecutiveWins * 25;
        return basePoints + levelBonus + streakBonus;
    }

    private boolean askForNextRound() {
        System.out.print("\n🎯 Shall we consult the crystal ball again? (Y/N): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.startsWith("y");
    }

    private void displayGameOver(String playerName) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║           FINAL PROPHECY                ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("🌟 Mystic: " + playerName);
        System.out.println("🏆 Final Level: " + LEVEL_NAMES[playerLevel]);
        System.out.println("💫 Total Points: " + totalPoints);
        System.out.println("✨ Best Streak: " + consecutiveWins);
        System.out.println("\nThe crystal ball awaits your return...");
    }

    public static void main(String[] args) {
        ArcadeNumberGame game = new ArcadeNumberGame();
        game.startGame();
    }
}