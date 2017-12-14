package tworunpos;

import java.util.Observable;




public class Session extends Observable {

    // Singleton
    static Session instance;

    Integer loginKey;

    public Session() {

    }

    //Singleton get Instantce
    public static synchronized Session getInstance () {
        if (instance == null) {
            instance = new Session ();
        }
        return instance;
    }

}
