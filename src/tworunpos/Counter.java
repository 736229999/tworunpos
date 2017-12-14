package tworunpos;

import Exceptions.CounterException;
import Exceptions.ZSessionException;
import com.mongodb.*;

import java.util.Date;

/*
This class is connected to mongo db "Counters"-Collection and will create a unqiue id for other classe and store
them in the db.
 */
public class Counter {

    private static DB db;
    private static DBCollection collection;
    private String counterName;




    public Counter(String className) {
        db = DatabaseClient.getInstance().getConnectedDB();
        collection = db.getCollection("counter");
        counterName = className;
    }


    public Integer getCounter() throws CounterException {

        Integer counter = 0;
        BasicDBObject searchObject = new BasicDBObject().append("_id",counterName);
        DBCursor cursor = collection.find(searchObject);
        if(  cursor.one() != null  ){
            counter = Integer.parseInt(cursor.one().get("counter").toString());
//            DebugScreen.getInstance().print(getMyDocument().toString());
        }
        else{
            //if counter was not found, create a new one and start with 0 as counter value
            BasicDBObject newDocument = new BasicDBObject();
            newDocument.put("_id", counterName);
            newDocument.put("counter", 0);

            try{
            WriteResult result = collection.insert(WriteConcern.SAFE,newDocument);
            }catch(Exception e){
                throw new CounterException("Datenbankfehler beim Schreiben des Counters in in Counter.");
            }

            counter = getCounter(); //recursive
        }

        return counter;
    }

    public void updateCounter(Integer counter) {

        DebugScreen.getInstance().print("DB: Setting Counter into collection");

        BasicDBObject newDocument = new BasicDBObject();
        newDocument.append("$set", new BasicDBObject().append("counter", counter));

        BasicDBObject searchQuery = new BasicDBObject().append("_id", counterName);

        collection.update(searchQuery, newDocument);

    }

    public Integer getNext() throws CounterException {
        return getCounter()+1;
    }

    public void increment() throws CounterException {
        Integer counter = getCounter();
        updateCounter(counter+1);
    }

    public void decrement() throws CounterException {
        Integer counter = getCounter();
        updateCounter(counter-1);
    }

    /*
    private because this is not relevant from outside, just for debugging and logs
     */
    private BasicDBObject getMyDocument() throws CounterException {

        //create basic transaction attributes
        BasicDBObject mainDocument = new BasicDBObject();
        Integer counter = getCounter();
        if(counter != null )
            mainDocument.put("counter",counter);


        return mainDocument;
    }


}
