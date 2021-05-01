import View.MainWindow2;
import View.WelcomeScreen;

public class Main {
    public static void main(String[] args) {
        System.out.println("Udv Westworldben! :)");
        WelcomeScreen welcomeScreen= new WelcomeScreen("Westworld");
        MainWindow2 w=new MainWindow2();
        w.setVisible(true);
    }
}
