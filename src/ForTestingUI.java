public class ForTestingUI {
    public static void main(String[] args){
        // use this code if the database is loaded and your localhost
//        Runnable loginServer = new LoginServer();
//        Thread loginThread = new Thread(loginServer);
//        loginThread.start();
        Login obj = new Login();
        obj.render();
//        Register obj1 = new Register();
//        Runnable registerServer = new RegisterServer();
//        Thread serverThread = new Thread(registerServer);
//        serverThread.start();
    }
}