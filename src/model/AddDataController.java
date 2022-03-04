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
    
    //Attributes declaration
    private static  EntityManagerFactory emf;
    private static EntityManager em;
    
    //Method to create Entity Manager and Factory for the database
    public static void createEMandEMF() {
        emf = Persistence.createEntityManagerFactory("PLH24_GE3PU");
        em = emf.createEntityManager();
    }
    
    //Method to store data in table Content
    public static void storeDrawsDataByDateRange(JsonObject response) throws Exception {
        
        //Content for API call for dates range returns content as an Array of draws
        JsonArray contentDetails = response.getAsJsonArray("content");
        
        //Loop to iteratively parse and store the data for all draws included in the JSON response
        for(int i = 0; i < contentDetails.size(); i++){
            
            //Parse Winning Numbers object and its nested List of number and list of bonus
            JsonObject winningNumbers = contentDetails.get(i).getAsJsonObject().get("winningNumbers").getAsJsonObject();
            JsonArray wnListArray = winningNumbers.getAsJsonArray("list");
            JsonArray bonusArray = winningNumbers.getAsJsonArray("bonus");
            
            /*-----------------PARSING JSON ELEMENTS FROM JSON STRING-------------------*/
            /*..........................................................................*/
            //Parsing pricePoints Json Object
            JsonObject pricePointsObj = contentDetails.get(i).getAsJsonObject().get("pricePoints").getAsJsonObject();
            
            /*..........................................................................*/
            //Parsing prize categories Json Array
            JsonArray prizeCatArray = contentDetails.get(i).getAsJsonObject().get("prizeCategories").getAsJsonArray();
                        
            /*..........................................................................*/
            //Parsing wager statistics Json Object
            JsonObject wagerObg = contentDetails.get(i).getAsJsonObject().get("wagerStatistics").getAsJsonObject();

            /*..........................................................................*/
            //Entity Manager & Entity Manager Factory creation
            AddDataController.createEMandEMF();
            
            /*----------------------SETTING FIELDS OF CONTENT TABLE---------------------*/
            //Create new Content & ContentPK objects
            Content content = new Content();
            ContentPK contentPK = new ContentPK();
            
            //Set gameId
            contentPK.setGameid(contentDetails.get(i).getAsJsonObject().get("gameId").getAsInt());
            
            //Set drawId
            contentPK.setDrawid(contentDetails.get(i).getAsJsonObject().get("drawId").getAsInt());
            
            //Set composite primary key
            content.setContentPK(contentPK);
            
            //Set other content fields for the current draw
            content.setDrawtime(contentDetails.get(i).getAsJsonObject().get("drawTime").getAsLong());
            content.setStatus(contentDetails.get(i).getAsJsonObject().get("status").getAsString());
            content.setDrawbreak(contentDetails.get(i).getAsJsonObject().get("drawBreak").getAsInt());
            content.setVisualdraw(contentDetails.get(i).getAsJsonObject().get("visualDraw").getAsInt());
                       
            //Checking if record exists in database
            boolean control = QueriesSQL.checkIfRecordExists(content);
            if(!control) {
                
                /*--------------------SETTING FIELDS OF PRICEPOINTS TABLE--------------------*/
                //Set values for pricePoints Element
                Pricepoints pricePoints = new Pricepoints();
                PricepointsPK pricePointsPK = new PricepointsPK();
                pricePoints.setContent(content);
                pricePointsPK.setGameid(content.getContentPK().getGameid());
                pricePointsPK.setDrawid(content.getContentPK().getDrawid());
                pricePoints.setPricepointsPK(pricePointsPK);
                pricePoints.setAmount(pricePointsObj.get("amount").getAsDouble());

                /*-----------------SETTING FIELDS OF WINNINGNUMBERSLIST TABLE----------------*/
                //Array list to store all the winning numbers
                List<Winningnumberslist> quintet = new ArrayList<>();
                
                //Loop to parse numbers and add to the List
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
                //Create & execute Entity Transaction to commit the data to the DB table
                EntityTransaction entityTransaction = null;
                try {
                    entityTransaction = em.getTransaction();
                    entityTransaction.begin();
                    
                    //Store to Content table
                    em.persist(content);
                    
                    //Store to PricePoints Table
                    em.persist(pricePoints);
                    
                    //Store number to winning number table
                    quintet.forEach((wNumElement) -> {
                        em.persist(wNumElement);
                    });

                    //Store bonus to winning number bonus
                    bonuses.forEach((bonus) -> {
                        em.persist(bonus);
                    });

                    //Store Prize Categories data
                    pkList.forEach((pk) -> {
                        em.persist(pk);
                    });
                    
                    //Store wager statistics
                    em.persist(wagerStats);
                    
                    //Commit changes to the database
                    entityTransaction.commit();
                }catch(RuntimeException e) {
                        if(entityTransaction.isActive())
                            entityTransaction.rollback();
                }
            }
            
            //Closing Element Manager & Element Manager Factory
            em.close();
            emf.close();
        }
    }
    
    public static void storeDrawsDataByDrawID(JsonObject response) throws Exception {
        
        //Content for API call for specific Draw ID returns single draw data as a JSON Object
        JsonObject contentDetails = response.getAsJsonObject();
            
        //Parsing Winning Numbers object and its nested List of number and list of bonus
        JsonObject winningNumbers = contentDetails.get("winningNumbers").getAsJsonObject();
        JsonArray wnListArray = winningNumbers.getAsJsonArray("list");
        JsonArray bonusArray = winningNumbers.getAsJsonArray("bonus");

        /*-----------------PARSING JSON ELEMENTS FROM JSON STRING-------------------*/
        /*..........................................................................*/
        //Parsing pricePoints Json Object
        JsonObject pricePointsObj = contentDetails.get("pricePoints").getAsJsonObject();

        /*..........................................................................*/
        //Parsing prize categories Json Array
        JsonArray prizeCatArray = contentDetails.get("prizeCategories").getAsJsonArray();

        /*..........................................................................*/
        //Parsing wager statistics Json Object
        JsonObject wagerObg = contentDetails.get("wagerStatistics").getAsJsonObject();

        /*..........................................................................*/
        //Entity Manager & Entity Manager Factory creation
        AddDataController.createEMandEMF();

        /*----------------------SETTING FIELDS OF CONTENT TABLE---------------------*/
        //Create new Content & ContentPK objects
        Content content = new Content();
        ContentPK contentPK = new ContentPK();
        
        //Set gameId
        contentPK.setGameid(contentDetails.get("gameId").getAsInt());
        
        //Set drawId
        contentPK.setDrawid(contentDetails.get("drawId").getAsInt());
        
        //Set composite primary key
        content.setContentPK(contentPK);
        
        //Set other content fields for the current draw
        content.setDrawtime(contentDetails.get("drawTime").getAsLong());
        content.setStatus(contentDetails.get("status").getAsString());
        content.setDrawbreak(contentDetails.get("drawBreak").getAsInt());
        content.setVisualdraw(contentDetails.get("visualDraw").getAsInt());

        //Checking whether record exists in database
        boolean control = QueriesSQL.checkIfRecordExists(content);
        if(!control) {

            /*--------------------SETTING FIELDS OF PRICEPOINTS TABLE--------------------*/
            //Set values for pricePoints Element
            Pricepoints pricePoints = new Pricepoints();
            PricepointsPK pricePointsPK = new PricepointsPK();
            pricePoints.setContent(content);
            pricePointsPK.setGameid(content.getContentPK().getGameid());
            pricePointsPK.setDrawid(content.getContentPK().getDrawid());
            pricePoints.setPricepointsPK(pricePointsPK);
            pricePoints.setAmount(pricePointsObj.get("amount").getAsDouble());

            /*-----------------SETTING FIELDS OF WINNINGNUMBERSLIST TABLE----------------*/
            //Array list to store all the winning numbers
            List<Winningnumberslist> quintet = new ArrayList<>();
            
            //Loop to parse numbers and add to the List
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
            //Create & execute Entity Transaction to commit the data to the DB table
            EntityTransaction entityTransaction = null;
            try {
                entityTransaction = em.getTransaction();
                entityTransaction.begin();
                
                //Store to Content table
                em.persist(content);
                
                //Store to PricePoints Table
                em.persist(pricePoints);
                
                //Store number to winning number table
                quintet.forEach((wNumElement) -> {
                    em.persist(wNumElement);
                });

                //Store bonus to winning number bonus
                bonuses.forEach((bonus) -> {
                    em.persist(bonus);
                });

                //Store Prize Categories data
                pkList.forEach((pk) -> {
                    em.persist(pk);
                });
                
                //Store wager statistics
                em.persist(wagerStats);
                
                //Commit changes to the database
                entityTransaction.commit();
            }catch(RuntimeException e) {
                    if(entityTransaction.isActive())
                        entityTransaction.rollback();
            }
        }
        
        //Closing Element Manager & Element Manager Factory
        em.close();
        emf.close();
    }
}