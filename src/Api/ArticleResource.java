package Api;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import tworunpos.Article;
import tworunpos.ArticleList;
import tworunpos.ArticleUnit;
import tworunpos.DebugScreen;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("article")
public class ArticleResource
{

        DebugScreen debugScreen = DebugScreen.getInstance();



        private class ApiArticle{
                String name;
        }

        @POST
        @Path("/add")
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        public Response createArticle(String msg) throws ParseException {
                String output ="{}";

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = null;
                if(msg != null){
                        jsonObject = ((JSONObject) jsonParser.parse(msg));
                }else{
                        System.out.println("Cannot read POSTed JSON data.");
                        return Response.status(202).entity(output).build(); //todo right code  for failure
                }

                // get Article together from posted JSON
                String postArticleName = (String) jsonObject.get("name");
                String postArticleBarcode = (String) jsonObject.get("barcode");
                ArticleUnit postArticleUnit = new ArticleUnit ((String) jsonObject.get("unit"));
                Double postArticleSalesprice = (double) jsonObject.get("salesprice");


                Double postArticleVatPercent =  Double.parseDouble(jsonObject.get("vatpercent").toString());
                Article a = new Article(postArticleBarcode,postArticleName,postArticleUnit,postArticleSalesprice,postArticleVatPercent);
                debugScreen.print ("API: Article for Input created: "+a.getName());
                debugScreen.print (a.getBarcode());

                //write article into db
                ArticleList.getInstance().addArticle(a);
                debugScreen.print ("API: add called "+postArticleName);



                return Response.status(200).entity(output).build();
        }

        @GET
        @Path("/{barcode}")
        public Response getArticle(@PathParam("barcode") String barcode) throws Exception {
                String output ="{}";
                debugScreen.print ("get called: "+barcode);

                Article article = ArticleList.getInstance().lookupArticleByBarcode(barcode);
                return Response.status(200).entity(article.getMyDocument().toString()).build();
        }


        @GET
        @Path("/drop/{code}")
        public Response dropArticles(@PathParam("code") String code) throws Exception {
                String output ="{}";
                debugScreen.print ("drop called: "+code);
                if(code.equals("tworun")){
                        ArticleList.getInstance().drop();
                }
                debugScreen.print ("drop done");
                return Response.status(200).entity("{}").build();
        }

}