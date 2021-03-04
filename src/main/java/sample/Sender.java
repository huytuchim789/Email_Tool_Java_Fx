package sample;

import javax.mail.Session;

public final class Sender {
    private static String userName;
    private static String passWord;
    private static Session session;

    protected static void setSession(Session session) {
        Sender.session = session;
    }

    protected static Session getSession() {
        return session;
    }

    protected static void setPassWord(String passWord) {
        Sender.passWord = passWord;
    }

    protected static void setUserName(String userName) {
        Sender.userName = userName;
    }

    protected static String getUserName() {
        return userName;
    }

    protected static String getPassWord() {
        return passWord;
    }
}
