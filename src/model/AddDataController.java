package model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import POJOs.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 * @author Thanos Theodoropoulos
 */

public class AddDataController {
    
    //attributes declaration
    private static  EntityManagerFactory emf;
    private static EntityManager em;
    
    //method to create Entity Manager and Factory for the database
    public static void createEMandEMF() {
        emf = Persistence.createEntityManagerFactory("PLH24_GE3PU");
        em = emf.createEntityManager();
    }
    //method to store data in table Content
    public static void storeDrawsDataByDateRange(JsonObject response) throws Exception {
        
        //Content for API call for dates range returns content as an Array of draws
        JsonArray contentDetails = response.getAsJsonArray("content");
        
        //loop to iteratively parse and store the data for all draws included in the JSON response
        for(int i = 0; i < contentDetails.size(); i++){
            
            //parse Winning Numbers object and its nested List of number and list of bonus
            JsonObject winningNumbers = contentDetails.get(i).getAsJsonObject().get("winningNumbers").getAsJsonObject();
            JsonArray wnListArray = winningNumbers.getAsJsonArray("list");
            JsonArray bonusArray = winningNumbers.getAsJsonArray("bonus");
            
            /*-----------------PARSING JSON ELEMENTS FROM JSON STRING-------------------*/
            /*..........................................................................*/
            //parsing pricePoints Json Object
            JsonObject pricePointsObj = contentDetails.get(i).getAsJsonObject().get("pricePoints").getAsJsonObject();
            
            /*..........................................................................*/
            //parsing prize categories Json Array
            JsonArray prizeCatArray = contentDetails.get(i).getAsJsonObject().get("prizeCategories").getAsJsonArray();
                        
            /*..........................................................................*/
            //parsing wager statistics Json Object
            JsonObject wagerObg = contentDetails.get(i).getAsJsonObject().get("wagerStatistics").getAsJsonObject();

            /*..........................................................................*/
            //Entity Manager & Entity Manager Factory creation
            AddDataController.createEMandEMF();
            
            /*----------------------SETTING FIELDS OF CONTENT TABLE---------------------*/
            //create new Content & ContentPK objects
            Content content = new Content();
            ContentPK contentPK = new ContentPK();
            //set gameId
            contentPK.setGameid(contentDetails.get(i).getAsJsonObject().get("gameId").getAsInt());
            //set drawId
            contentPK.setDrawid(contentDetails.get(i).getAsJsonObject().get("drawId").getAsInt());
            //set composite primary key
            content.setContentPK(contentPK);
            //set other content fields for the current draw
            content.setDrawtime(contentDetails.get(i).getAsJsonObject().get("drawTime").getAsLong());
            content.setStatus(contentDetails.get(i).getAsJsonObject().get("status").getAsString());
            content.setDrawbreak(contentDetails.get(i).getAsJsonObject().get("drawBreak").getAsInt());
            content.setVisualdraw(contentDetails.get(i).getAsJsonObject().get("visualDraw").getAsInt());
                       
            //checking if record exists in database
            boolean control = QueriesSQL.checkIfRecordExists(content);
            if(!control) {
                /*--------------------SETTING FIELDS OF PRICEPOINTS TABLE--------------------*/
                //set values for pricePoints Element
                Pricepoints pricePoints = new Pricepoints();
                PricepointsPK pricePointsPK = new PricepointsPK();
                pricePoints.setContent(content);
                pricePointsPK.setGameid(content.getContentPK().getGameid());
                pricePointsPK.setDrawid(content.getContentPK().getDrawid());
                pricePoints.setPricepointsPK(pricePointsPK);
                pricePoints.setAmount(pricePointsObj.get("amount").getAsDouble());

                /*-----------------SETTING FIELDS OF WINNINGNUMBERSLIST TABLE----------------*/
                //array list to store all the winning numbers
                List<Winningnumberslist> quintet = new ArrayList<>();
                //loop to parse numbers and add to the List
                for(int j = 0; j < wnListArray.size(); j++) {
                    Winningnumberslist wnList = new Winningnumberslist();
                    WinningnumberslistPK wnListPK = new WinningnumberslistPK();
                    wnList.setContent(content);
                    wnListPK.setIndex(j + 1);
                    wnListPK.setGameid(content.getContentPK().getGameid());
                    wnListPK.setDrawid(content.getContentPK().getDrawid());
                    wnList.setWinningnumberslistPK(wnListPK);
                    wnList.setNumber(wnListArray.get(j).getAsInt());
                    quintet.add(wnList);
                }
                /*----------------SETTING FIELDS OF WINNINGNUMBERSBONUS TABLE-----------------*/
                //ArrayList to store bonus numbers (for Tzoker is 1 but bonus is defined as Array in JSON
                List<Winningnumbersbonus> bonuses = new ArrayList<>();
                for(int k = 0; k < bonusArray.size(); k++) {
                    Winningnumbersbonus bonusList = new Winningnumbersbonus();
                    WinningnumbersbonusPK bonusListPK = new WinningnumbersbonusPK();
                    bonusList.setContent(content);
                    bonusListPK.setIndex(k + 1);
                    bonusListPK.setGameid(content.getContentPK().getGameid());
                    bonusListPK.setDrawid(content.getContentPK().getDrawid());
                    bonusList.setWinningnumbersbonusPK(bonusListPK);
                    bonusList.setBonus(bonusArray.get(k).getAsInt());
                    bonuses.add(bonusList);
                }
                /*------------------SETTING FIELDS OF PRIZECATEGORIES TABLE-------------------*/
                //ArrayList to store values for subsequent prize categories
                List<Prizecategories> pkList = new ArrayList<>();
                for(int l = 0; l < prizeCatArray.size(); l++){
                    Prizecategories prizeCategory = new Prizecategories();
                    PrizecategoriesPK prizecatPK = new PrizecategoriesPK();
                    prizeCategory.setContent(content);
                    prizecatPK.setCategoryid(prizeCatArray.get(l).getAsJsonObject().get("id").getAsInt());
                    prizecatPK.setGameid(content.getContentPK().getGameid());
                    prizecatPK.setDrawid(content.getContentPK().getDrawid());
                    prizeCategory.setPrizecategoriesPK(prizecatPK);
                    prizeCategory.setDivident(prizeCatArray.get(l).getAsJsonObject().get("divident").getAsDouble());
                    prizeCategory.setWinners(prizeCatArray.get(l).getAsJsonObject().get("winners").getAsInt());
                    prizeCategory.setDistributed(prizeCatArray.get(l).getAsJsonObject().get("distributed").getAsDouble());
                    prizeCategory.setJackpot(prizeCatArray.get(l).getAsJsonObject().get("jackpot").getAsDouble());
                    prizeCategory.setFixed(prizeCatArray.get(l).getAsJsonObject().get("fixed").getAsDouble());
                    prizeCategory.setCategorytype(prizeCatArray.get(l).getAsJsonObject().get("categoryType").getAsInt());
                    prizeCategory.setGametype(prizeCatArray.get(l).getAsJsonObject().get("gameType").getAsString());
                    pkList.add(prizeCategory);
                }

                /*--------------------SETTING FIELDS OF WAGERSTATISTICS TABLE------------------*/
                Wagerstatistics wagerStats = new Wagerstatistics();
                WagerstatisticsPK wagerPK = new WagerstatisticsPK();
                wagerStats.setContent(content);
                wagerPK.setGameid(content.getContentPK().getGameid());
                wagerPK.setDrawid(content.getContentPK().getDrawid());
                wagerStats.setWagerstatisticsPK(wagerPK);
                wagerStats.setColumns(wagerObg.get("columns").getAsInt());
                wagerStats.setWagers(wagerObg.get("wagers").getAsInt());
                
                
                /*-------------JPA SECTION FOR THE INSERTION OF DATA TO THE TALBES--------------*/
                //create & execute Entity Transaction to commit the data to the DB table
                EntityTransaction entityTransaction = null;
                try {
                    entityTransaction = em.getTransaction();
                    entityTransaction.begin();
                    //store to Content table
                    em.persist(content);
                    //sotre to PricePoints Table
                    em.persist(pricePoints);
                    //store number to winning number table
                    for(Winningnumberslist wNumElement : quintet)
                        em.persist(wNumElement);

                    //store bonus to winning number bonus
                    for(Winningnumbersbonus bonus: bonuses)
                        em.persist(bonus);

                    //store Prize Categories data
                    for(Prizecategories pk: pkList)
                        em.persist(pk);
                    //store wager statistics
                    em.persist(wagerStats);
                    //commit changes to the database
                    entityTransaction.commit();
                }catch(RuntimeException e) {
                        if(entityTransaction.isActive())
                            entityTransaction.rollback();
                }
            }
            //closing element manager & element manage factory
            em.close();
            emf.close();
        }
    }
    
    public static void storeDrawsDataByDrawID(JsonObject response) throws Exception {
        
        //Content for API call for specific Draw ID returns single draw data as a JSON Object
        JsonObject contentDetails = response.getAsJsonObject();
            
        //parsing Winning Numbers object and its nested List of number and list of bonus
        JsonObject winningNumbers = contentDetails.get("winningNumbers").getAsJsonObject();
        JsonArray wnListArray = winningNumbers.getAsJsonArray("list");
        JsonArray bonusArray = winningNumbers.getAsJsonArray("bonus");

        /*-----------------PARSING JSON ELEMENTS FROM JSON STRING-------------------*/
        /*..........................................................................*/
        //parsing pricePoints Json Object
        JsonObject pricePointsObj = contentDetails.get("pricePoints").getAsJsonObject();

        /*..........................................................................*/
        //parsing prize categories Json Array
        JsonArray prizeCatArray = contentDetails.get("prizeCategories").getAsJsonArray();

        /*..........................................................................*/
        //parsing wager statistics Json Object
        JsonObject wagerObg = contentDetails.get("wagerStatistics").getAsJsonObject();

        /*..........................................................................*/
        //Entity Manager & Entity Manager Factory creation
        AddDataController.createEMandEMF();

        /*----------------------SETTING FIELDS OF CONTENT TABLE---------------------*/
        //create new Content & ContentPK objects
        Content content = new Content();
        ContentPK contentPK = new ContentPK();
        //set gameId
        contentPK.setGameid(contentDetails.get("gameId").getAsInt());
        //set drawId
        contentPK.setDrawid(contentDetails.get("drawId").getAsInt());
        //set composite primary key
        content.setContentPK(contentPK);
        //set other content fields for the current draw
        content.setDrawtime(contentDetails.get("drawTime").getAsLong());
        content.setStatus(contentDetails.get("status").getAsString());
        content.setDrawbreak(contentDetails.get("drawBreak").getAsInt());
        content.setVisualdraw(contentDetails.get("visualDraw").getAsInt());

        //checking if record exists in database
        boolean control = QueriesSQL.checkIfRecordExists(content);
        if(!control) {

            /*--------------------SETTING FIELDS OF PRICEPOINTS TABLE--------------------*/
            //set values for pricePoints Element
            Pricepoints pricePoints = new Pricepoints();
            PricepointsPK pricePointsPK = new PricepointsPK();
            pricePoints.setContent(content);
            pricePointsPK.setGameid(content.getContentPK().getGameid());
            pricePointsPK.setDrawid(content.getContentPK().getDrawid());
            pricePoints.setPricepointsPK(pricePointsPK);
            pricePoints.setAmount(pricePointsObj.get("amount").getAsDouble());

            /*-----------------SETTING FIELDS OF WINNINGNUMBERSLIST TABLE----------------*/
            //array list to store all the winning numbers
            List<Winningnumberslist> quintet = new ArrayList<>();
            //loop to parse numbers and add to the List
            for(int j = 0; j < wnListArray.size(); j++) {
                Winningnumberslist wnList = new Winningnumberslist();
                WinningnumberslistPK wnListPK = new WinningnumberslistPK();
                wnList.setContent(content);
                wnListPK.setIndex(j + 1);
                wnListPK.setGameid(content.getContentPK().getGameid());
                wnListPK.setDrawid(content.getContentPK().getDrawid());
                wnList.setWinningnumberslistPK(wnListPK);
                wnList.setNumber(wnListArray.get(j).getAsInt());
                quintet.add(wnList);
            }
            /*----------------SETTING FIELDS OF WINNINGNUMBERSBONUS TABLE-----------------*/
            //ArrayList to store bonus numbers (for Tzoker is 1 but bonus is defined as Array in JSON
            List<Winningnumbersbonus> bonuses = new ArrayList<>();
            for(int k = 0; k < bonusArray.size(); k++) {
                Winningnumbersbonus bonusList = new Winningnumbersbonus();
                WinningnumbersbonusPK bonusListPK = new WinningnumbersbonusPK();
                bonusList.setContent(content);
                bonusListPK.setIndex(k + 1);
                bonusListPK.setGameid(content.getContentPK().getGameid());
                bonusListPK.setDrawid(content.getContentPK().getDrawid());
                bonusList.setWinningnumbersbonusPK(bonusListPK);
                bonusList.setBonus(bonusArray.get(k).getAsInt());
                bonuses.add(bonusList);
            }
            /*------------------SETTING FIELDS OF PRIZECATEGORIES TABLE-------------------*/
            //ArrayList to store values for subsequent prize categories
            List<Prizecategories> pkList = new ArrayList<>();
            for(int l = 0; l < prizeCatArray.size(); l++){
                Prizecategories prizeCategory = new Prizecategories();
                PrizecategoriesPK prizecatPK = new PrizecategoriesPK();
                prizeCategory.setContent(content);
                prizecatPK.setCategoryid(prizeCatArray.get(l).getAsJsonObject().get("id").getAsInt());
                prizecatPK.setGameid(content.getContentPK().getGameid());
                prizecatPK.setDrawid(content.getContentPK().getDrawid());
                prizeCategory.setPrizecategoriesPK(prizecatPK);
                prizeCategory.setDivident(prizeCatArray.get(l).getAsJsonObject().get("divident").getAsDouble());
                prizeCategory.setWinners(prizeCatArray.get(l).getAsJsonObject().get("winners").getAsInt());
                prizeCategory.setDistributed(prizeCatArray.get(l).getAsJsonObject().get("distributed").getAsDouble());
                prizeCategory.setJackpot(prizeCatArray.get(l).getAsJsonObject().get("jackpot").getAsDouble());
                prizeCategory.setFixed(prizeCatArray.get(l).getAsJsonObject().get("fixed").getAsDouble());
                prizeCategory.setCategorytype(prizeCatArray.get(l).getAsJsonObject().get("categoryType").getAsInt());
                prizeCategory.setGametype(prizeCatArray.get(l).getAsJsonObject().get("gameType").getAsString());
                pkList.add(prizeCategory);
            }

            /*--------------------SETTING FIELDS OF WAGERSTATISTICS TABLE------------------*/
            Wagerstatistics wagerStats = new Wagerstatistics();
            WagerstatisticsPK wagerPK = new WagerstatisticsPK();
            wagerStats.setContent(content);
            wagerPK.setGameid(content.getContentPK().getGameid());
            wagerPK.setDrawid(content.getContentPK().getDrawid());
            wagerStats.setWagerstatisticsPK(wagerPK);
            wagerStats.setColumns(wagerObg.get("columns").getAsInt());
            wagerStats.setWagers(wagerObg.get("wagers").getAsInt());

            /*-------------JPA SECTION FOR THE INSERTION OF DATA TO THE TALBES--------------*/
            //create & execute Entity Transaction to commit the data to the DB table
            EntityTransaction entityTransaction = null;
            try {
                entityTransaction = em.getTransaction();
                entityTransaction.begin();
                //store to Content table
                em.persist(content);
                //sotre to PricePoints Table
                em.persist(pricePoints);
                //store number to winning number table
                for(Winningnumberslist wNumElement : quintet)
                    em.persist(wNumElement);

                //store bonus to winning number bonus
                for(Winningnumbersbonus bonus: bonuses)
                    em.persist(bonus);

                //store Prize Categories data
                for(Prizecategories pk: pkList)
                    em.persist(pk);
                //store wager statistics
                em.persist(wagerStats);
                //commit changes to the database
                entityTransaction.commit();
            }catch(RuntimeException e) {
                    if(entityTransaction.isActive())
                        entityTransaction.rollback();
            }
        }
        //closing element manager & element manage factory
        em.close();
        emf.close();
    }
}

