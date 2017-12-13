package Api;


import org.json.simple.JSONObject;
import tworunpos.DebugScreen;
import tworunpos.Transaction;
import tworunpos.TransactionList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("transaction")
public class TransactionResource
{

        DebugScreen debugScreen = DebugScreen.getInstance();




        /*@GET
        @Path("/datefrom/{datefrom}/dateto/{dateto}")
        public Response getTransactionsByDate(@PathParam("datefrom") String dateFrom,@PathParam("dateto") String dateTo) throws Exception {
                String output ="{}";
                debugScreen.print ("getTransactionsByDate called: "+dateFrom+" - "+dateTo);
                //todo
                debugScreen.print ("getTransactionsByDate done");
                return Response.status(200).entity("{}").build();
        }
*/

        @GET
        @Path("/{count}/{offset}")
        public Response getTransactions(@PathParam("count") Integer count,@PathParam("offset") Integer offset) throws Exception {
                String output ="{}";
                debugScreen.print ("getTransactions called");

                ArrayList<Transaction> transactionsList = null;
                try {
                        transactionsList = TransactionList.getInstance().getTransactions(count,offset);
                } catch (Exception e) {
                        debugScreen.print (e.getMessage());
                        return Response.status(204).entity(output).build();
                }

                //convert for output
                JSONObject json = new JSONObject();

                for (int i = 0; i < transactionsList.size(); i++) {
                        json.put(i,transactionsList.get(i).getMyDocument());
                }

                output = json.toJSONString().replaceAll("\\\\", "");

                debugScreen.print ("getTransactions done");
                return Response.status(200).entity(output).build();
        }


        @GET
        @Path("/last")
        public Response getTransactionLast()  {
                String output ="{}";
                debugScreen.print ("getTransactionsOfToday called");

                Transaction transaction = null;
                try {
                        transaction = TransactionList.getInstance().getLastTransaction();
                } catch (Exception e) {
                        debugScreen.print (e.getMessage());
                        return Response.status(204).entity(output).build();
                }

                output = transaction.getMyDocument().toString();

                debugScreen.print ("getTransactionsOfToday done");
                return Response.status(200).entity(output).build();
        }
}