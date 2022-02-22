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
        ContentTable.createTable();
        PricePointsTable.createTable();
        PrizeCategoriesTable.createTable();
        WagerStatsTable.createTable();
        WinningNumbersListTable.createTable();
        WinningNumbersBonusTable.createTable();
//        WinningNumbersListTable.dropTable();
 //       WinningNumbersBonusTable.dropTable();
  //      WagerStatsTable.dropTable();
   //     PrizeCategoriesTable.dropTable();
    //    PricePointsTable.dropTable();
     //   ContentTable.dropTable();
    }  
}