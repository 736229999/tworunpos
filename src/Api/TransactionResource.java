package Api;



import tworunpos.TransactionList;
import tworunpos.DebugScreen;
import tworunpos.Transaction;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("transaction")
public class TransactionResource
{

        DebugScreen debugScreen = DebugScreen.getInstance();




/* NO CREATE FUNCTION FOR TRANSACTION - SINGLE POINT OF TRANSACTION IS THE POS
        @POST
        @Path("/add")
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        /*public Response createArticle(String msg) throws ParseException {
                String output ="{}";
                return Response.status(200).entity(output).build();
        }*/

        @GET
        @Path("/count/{count}/skip/{skip}")
        public Response getTransactionsByCount(@PathParam("count") String count, @PathParam("skip") String skip) throws Exception {
                String output ="{}";
                debugScreen.print ("getTransactionsByCount called: "+count+" - skip:"+skip);
                //todo
                debugScreen.print ("getTransactionsByCount done");
                return Response.status(200).entity("{}").build();
        }


        @GET
        @Path("/datefrom/{datefrom}/dateto/{dateto}")
        public Response getTransactionsByDate(@PathParam("datefrom") String dateFrom,@PathParam("dateto") String dateTo) throws Exception {
                String output ="{}";
                debugScreen.print ("getTransactionsByDate called: "+dateFrom+" - "+dateTo);
                //todo
                debugScreen.print ("getTransactionsByDate done");
                return Response.status(200).entity("{}").build();
        }


        @GET
        @Path("/today")
        public Response getTransactionsOfToday() throws Exception {
                String output ="{}";
                debugScreen.print ("getTransactionsOfToday called");

                Transaction transaction;

                debugScreen.print ("getTransactionsOfToday done");
                return Response.status(200).entity("{}").build();
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