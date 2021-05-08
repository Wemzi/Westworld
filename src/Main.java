import View.WelcomeScreen;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Westworld!");
        WelcomeScreen welcomeScreen= new WelcomeScreen("Westworld");
        welcomeScreen.setVisible(true);
    }
}
