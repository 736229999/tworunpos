package Api;


import tworunpos.Article;
import tworunpos.DebugScreen;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Path("article")
public class ArticleResource
{

        private static Map<Integer, Article> articles = new HashMap<Integer, Article>();
        DebugScreen debugScreen = DebugScreen.getInstance();


        private class ApiArticle{
                String name;
        }

        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        @Path("/add")
        public void createArticle() {
                debugScreen.print ("add called: ");

        }

        @GET
        @Path("/{barcode}")
        public String getArticle(@PathParam("barcode") String barcode) throws Exception {

                debugScreen.print ("get called: "+barcode);

                //Article article = ArticleList.getInstance().lookupArticleByBarcode(barcode);
                return "{}";
        }
}