package tworunpos;

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

    public Counter() {
        init();
    }

    private void init(){
        db = DatabaseClient.getInstance().getConnectedDB();
        collection = db.getCollection("counter");
    }

    public Counter(String className) {
        init();
        counterName = className;
    }


    public Integer getCounter() throws Exception {

        Integer counter = 0;
        DBCursor cursor = collection.find().sort(new BasicDBObject().append("_id",counterName));
        if(  cursor.one() != null  ){
            counter = Integer.parseInt(cursor.one().get("counter").toString());
            DebugScreen.getInstance().print(getMyDocument().toString());
        }
        else{
            throw new Exception("Counter '"+counterName+"' not found.");
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

    public Integer getNext() throws Exception {
        return getCounter()+1;
    }

    public void increment() throws Exception {
        Integer counter = getCounter();
        updateCounter(counter+1);
    }

    public void decrement() throws Exception {
        Integer counter = getCounter();
        updateCounter(counter-1);
    }

    /*
    private because this is not relevant from outside, just for debugging and logs
     */
    private BasicDBObject getMyDocument() throws Exception {

        //create basic transaction attributes
        BasicDBObject mainDocument = new BasicDBObject();
        Integer counter = getCounter();
        if(counter != null )
            mainDocument.put("counter",counter);


        return mainDocument;
    }


}
