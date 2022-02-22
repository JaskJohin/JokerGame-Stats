package model;

/**
 *
 * @author Thanos Theodoropoulos
 */

public class CreateDB {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DBTablesManager.createContentTable();
        DBTablesManager.createPPTable();
        DBTablesManager.createPCTable();
        DBTablesManager.createWSTable();
        DBTablesManager.createWNLTable();
        DBTablesManager.createWNBTable();
//        DBTablesManager.dropWNLTable();
 //       DBTablesManager.dropWNBTable();
  //      DBTablesManager.dropWSTable();
   //     DBTablesManager.dropPCTable();
    //    DBTablesManager.dropPPTable();
     //   DBTablesManager.dropContentTable();
    }  
}