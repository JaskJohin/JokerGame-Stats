package model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 * @author Thanos Theodoropoulos
 */

public class DBTablesManager {
    
    private static Connection connection;
    private static Statement statement;
    private static String createTableSQL, dropTableSQL;
    
    /*---------------------CREATE TABLES SECTION-----------------------*/
    //Content Table constructor
    public static void createContentTable() {
        try {
            connection = DbConnect.connect();
            if(!DbConnect.tableExists(connection, "CONTENT")) {
                createTableSQL = "CREATE TABLE content ("
                        + "gameID INTEGER NOT NULL, "
                        + "drawID INTEGER NOT NULL, "
                        + "drawTime BIGINT NOT NULL, "
                        + "status VARCHAR(30) NOT NULL, "
                        + "drawBreak INTEGER NOT NULL, "
                        + "visualDraw INTEGER NOT NULL, "
                        + "PRIMARY KEY (gameID, drawID))";
                statement = connection.createStatement();
                statement.execute(createTableSQL);
                statement.close();
                connection.close();
            }
            } catch (SQLException ex) {
            Logger.getLogger(DBTablesManager.class.getName()).log(Level.SEVERE, null, ex);          
        }     
    }
    
    //Price Points Table constructor
    public static void createPPTable() {
        try {
            connection = DbConnect.connect();
            if(!DbConnect.tableExists(connection, "PRICEPOINTS")) {
                createTableSQL = "CREATE TABLE pricePoints ("
                        + "gameID INTEGER NOT NULL, "
                        + "drawID INTEGER NOT NULL, "
                        + "amount DOUBLE NOT NULL, "
                        + "PRIMARY KEY (gameID, drawID), "
                        + "CONSTRAINT pp_FK FOREIGN KEY (gameID, drawID) REFERENCES content(gameID, drawID) ON DELETE CASCADE ON UPDATE RESTRICT)";
                statement = connection.createStatement();
                statement.execute(createTableSQL);
                statement.close();
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBTablesManager.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    //Prize Categories Table constructor
    public static void createPCTable() {
        try {
            connection = DbConnect.connect();
            if(!DbConnect.tableExists(connection, "PRIZECATEGORIES")) {
                createTableSQL = "CREATE TABLE prizeCategories ("
                        + "gameID INTEGER NOT NULL, "
                        + "drawID INTEGER NOT NULL, "
                        + "categoryID INTEGER NOT NULL, "
                        + "divident DOUBLE NOT NULL, "
                        + "winners INTEGER NOT NULL, "
                        + "distributed DOUBLE NOT NULL, "
                        + "jackpot DOUBLE NOT NULL, "
                        + "fixed DOUBLE NOT NULL, "
                        + "categoryType INTEGER NOT NULL, "
                        + "gameType VARCHAR(30) NOT NULL, "
                        + "PRIMARY KEY (gameID, drawID, categoryID), "
                        + "CONSTRAINT pc_FK FOREIGN KEY (gameID, drawID) REFERENCES content(gameID, drawID) ON DELETE CASCADE ON UPDATE RESTRICT)";
                statement = connection.createStatement();
                statement.execute(createTableSQL);
                statement.close();
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBTablesManager.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    
    //Wager Statistics Table constructor
    public static void createWSTable() {
        try {
            connection = DbConnect.connect();
            if(!DbConnect.tableExists(connection, "WAGERSTATISTICS")) {
                createTableSQL = "CREATE TABLE wagerStatistics ("
                        + "gameID INTEGER NOT NULL, "
                        + "drawID INTEGER NOT NULL, "
                        + "columns INTEGER NOT NULL, "
                        + "wagers INTEGER NOT NULL, "
                        + "PRIMARY KEY (gameID, drawID), "
                        + "CONSTRAINT ws_FK FOREIGN KEY (gameID, drawID) REFERENCES content(gameID, drawID) ON DELETE CASCADE ON UPDATE RESTRICT)";
                statement = connection.createStatement();
                statement.executeUpdate(createTableSQL);
                statement.close();
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBTablesManager.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    
    //Winning Numbers Bonus Table constructor
    public static void createWNBTable() {
        try {
            connection = DbConnect.connect();
            if(!DbConnect.tableExists(connection, "WINNINGNUMBERSBONUS")) {
                createTableSQL = "CREATE TABLE winningNumbersBonus ("
                        + "index INTEGER NOT NULL, "
                        + "gameID INTEGER NOT NULL, "
                        + "drawID INTEGER NOT NULL, "
                        + "bonus INTEGER NOT NULL,"
                        + "PRIMARY KEY (INDEX, gameID, drawID), "
                        + "CONSTRAINT wnb_FK FOREIGN KEY (gameID, drawID) REFERENCES content(gameID, drawID) ON DELETE CASCADE ON UPDATE RESTRICT)";
                statement = connection.createStatement();
                statement.executeUpdate(createTableSQL);
                statement.close();
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBTablesManager.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    
    //Table constructor
    public static void createWNLTable() {
        try {
            connection = DbConnect.connect();
            if(!DbConnect.tableExists(connection, "WINNINGNUMBERSLIST")) {
                    createTableSQL = "CREATE TABLE winningNumbersList ("
                        + "index INTEGER NOT NULL, "
                        + "gameID INTEGER NOT NULL, "
                        + "drawID INTEGER NOT NULL, "
                        + "number INTEGER NOT NULL,"
                        + "PRIMARY KEY (INDEX, gameID, drawID), "
                        + "CONSTRAINT wnl_FK FOREIGN KEY (gameID, drawID) REFERENCES content(gameID, drawID) ON DELETE CASCADE ON UPDATE RESTRICT)";
                statement = connection.createStatement();
                statement.executeUpdate(createTableSQL);
                statement.close();
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBTablesManager.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    
    /*----------------------DROP TABLES SECTOIN-----------------------*/
    //Method to drop existing Content table
    public static void dropContentTable() {
        try {
            connection = DbConnect.connect();
            dropTableSQL = "DROP TABLE content";    
            statement = connection.createStatement();
            statement.executeUpdate(dropTableSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBTablesManager.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    
    //Method to drop existing Price Points table
    public static void dropPPTable() {
        try {
            connection = DbConnect.connect();
            dropTableSQL = "DROP TABLE pricePoints";    
            statement = connection.createStatement();
            statement.executeUpdate(dropTableSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBTablesManager.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    
    //Method to drop existing Prize Categories table
    public static void dropPCTable() {
        try {
            connection = DbConnect.connect();
            dropTableSQL = "DROP TABLE prizeCategories";    
            statement = connection.createStatement();
            statement.executeUpdate(dropTableSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBTablesManager.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    
    //Method to drop existing Wager Statistics table
    public static void dropWSTable() {
        try {
            connection = DbConnect.connect();
            dropTableSQL = "DROP TABLE wagerStatistics";    
            statement = connection.createStatement();
            statement.executeUpdate(dropTableSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBTablesManager.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    
    //Method to drop existing Winning Numbers Bonus table
    public static void dropWNBTable() {
        try {
            connection = DbConnect.connect();
            dropTableSQL = "DROP TABLE winningNumbersBonus";    
            statement = connection.createStatement();
            statement.executeUpdate(dropTableSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBTablesManager.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    
    //Method to drop existing Winning Numnbers List table
    public static void dropWNLTable() {
        try {
            connection = DbConnect.connect();
            dropTableSQL = "DROP TABLE winningNumbersList";    
            statement = connection.createStatement();
            statement.executeUpdate(dropTableSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBTablesManager.class.getName()).log(Level.SEVERE, null, ex);          
        }
    }
    
    public static void createDatabaseTables() {
        createContentTable();
        createPPTable();
        createPCTable();
        createWSTable();
        createWNLTable();
        createWNBTable();
    }
    public static void dropDatabaseTables() {
        dropWNLTable();
        dropWNBTable();
        dropWSTable();
        dropPCTable();
        dropPPTable();
        dropContentTable();
    }
}