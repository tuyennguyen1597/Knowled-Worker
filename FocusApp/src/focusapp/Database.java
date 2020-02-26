/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package focusapp;

/**
 *
 * @author moii1
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    public static Connection conn;

    /* NO NEED TO CALL THIS METHOD OUTSIDE THE DATABASE CLASS */
    public static void openConnection() {
        if (conn == null) {
            try {
                //conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\cryst\\Desktop\\test\\SingleAccountDB.db");
                conn = DriverManager.getConnection("jdbc:sqlite:focusAppDB.db");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /* Pass an SQL String into this method when querying the database */
    public ResultSet getResultSet(String sqlstatement) throws SQLException {
        openConnection();
        java.sql.Statement statement = conn.createStatement();
        ResultSet RS = statement.executeQuery(sqlstatement);
        return RS;
    }

    /* Pass an SQL String into this method when inserting data into the database */
    public void insertStatement(String insert_query) throws SQLException {
        java.sql.Statement stmt = null;
        openConnection();

        try {
            System.out.println("Database opened successfully");
            stmt = conn.createStatement();
            System.out.println("The query was: " + insert_query);
            stmt.executeUpdate(insert_query);
            stmt.close();
            //conn.commit();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        stmt.close();
    }

    public static void createCategoryTable() throws SQLException {
        //TODO: Fill in this method
        java.sql.Statement stmt = null;
        openConnection();
        try {
            stmt = conn.createStatement();

            String createLoginQuery = "CREATE TABLE IF NOT EXISTS Category "
                    + "(Cat_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT NOT NULL, "
                    + "COLOUR TEXT NOT NULL "
                    + ");";
            stmt.execute(createLoginQuery);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        stmt.close();
    }
    
    public static void createEntryTable() throws SQLException {
        //TODO: Fill in this method
        java.sql.Statement stmt = null;
        openConnection();
        try {
            stmt = conn.createStatement();

            String createLoginQuery = "CREATE TABLE IF NOT EXISTS Entry (E_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " T_ID INTEGER NOT NULL, START INTEGER, END INTEGER, DURATION REAL NOT NULL,"
                    + " CAT_ID INTEGER NOT NULL, DO_DATE TEXT NOT NULL,FOREIGN KEY (CAT_ID) REFERENCES CATEGORY(Cat_ID),"
                    + "FOREIGN KEY (T_ID) REFERENCES Task(T_ID))";
            stmt.execute(createLoginQuery);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        stmt.close();
    }
    
    public static void createStatusTable() throws SQLException {
        //TODO: Fill in this method
        java.sql.Statement stmt = null;
        openConnection();
        try {
            stmt = conn.createStatement();

            String createLoginQuery = "CREATE TABLE IF NOT EXISTS Status"
                    + " (Status_ID INTEGER,Name TEXT NOT NULL,PRIMARY KEY (Status_ID))";
            stmt.execute(createLoginQuery);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        stmt.close();
    }
    
    public static void createTaskTable() throws SQLException {
        //TODO: Fill in this method
        java.sql.Statement stmt = null;
        openConnection();
        try {
            stmt = conn.createStatement();

            String createLoginQuery = "CREATE TABLE IF NOT EXISTS Task "
                    + "(T_ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT NOT NULL, "
                    + "DESCRIPTION TEXT NOT NULL, DO_DATE TEXT NOT NULL, "
                    + "DUE_DATE TEXT NOT NULL, PRIORITY INTEGER NOT NULL, "
                    + "CAT_ID INTEGER NOT NULL, STATUS INTEGER,"
                    + "FOREIGN KEY (CAT_ID) REFERENCES CATEGORY(Cat_ID))";
            stmt.execute(createLoginQuery);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        stmt.close();
    }   
    
    public static void createViews() throws SQLException {
        //TODO: Fill in this method
        java.sql.Statement stmt = null;
        openConnection();
        try {
            stmt = conn.createStatement();

            String createLoginQuery = "CREATE VIEW IF NOT EXISTS WEEKLYTRENDS AS select title, "
                    + "duration, DO_DATE, name, strftime('%W', do_date) WeekNumb, "
                    + "now, max(date(do_date,'weekday 0', '-7 day')) WeekStart, "
                    + "max(date(do_date, 'weekday 0', '-1 day')) WeekEnd from "
                    + "activities group by do_date";
            stmt.execute(createLoginQuery);
            
            createLoginQuery = "CREATE VIEW IF NOT EXISTS WEEKLYTRENDS2 AS select a.title, "
                    + "sum(a.duration) as groupDuration, a.DO_DATE, a.name, "
                    + "a.weeknumb, a.now, a.WeekStart, a.WeekEnd,b.durationSum, "
                    + "round(((sum(a.duration))/b.durationSum)*100,1) "
                    + "as percentage from WEEKLYTRENDS a join "
                    + "(select weeknumb, name, sum(duration) "
                    + "as durationSum from weeklytrends group by weeknumb) "
                    + "b on a.weeknumb=b.weeknumb group by b.weeknumb,a.name";
            stmt.execute(createLoginQuery);
            
            createLoginQuery = "CREATE VIEW IF NOT EXISTS activities AS select t.title, "
                    + "e.duration, e.DO_DATE, c.name, (select date('now')) "
                    + "AS NOW, (select date(('now'),'-7 days')) AS WEEK_AGO "
                    + "from task t join entry e on e.t_id=t.t_id join category c "
                    + "on e.Cat_ID=c.Cat_ID order by duration";
            stmt.execute(createLoginQuery);
            
            createLoginQuery = "CREATE VIEW IF NOT EXISTS pieChart AS select SUM(duration) "
                    + "AS D, name, cat_id FROM entry JOIN category using "
                    + "(cat_id) group by cat_ID";
            stmt.execute(createLoginQuery);
            
            createLoginQuery = "CREATE VIEW IF NOT EXISTS pieChart2 AS select d, name, "
                    + "cat_id, round(((d/(select sum(d) from piechart))*100),1) "
                    + "as Total from piechart group by name, d";
            stmt.execute(createLoginQuery);
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        stmt.close();
    }
    
    public static void createSampleData() throws SQLException{
        java.sql.Statement stmt = null;
        openConnection();
        try {
            stmt = conn.createStatement();
            //sample data for category table
            String insertStatement = "INSERT OR IGNORE INTO Category "
                    + "VALUES (1,'Learning', '0xD92121'), (2,'Data Analysis','0x32527b'),"
                    + "(3,'Networking', '0x9B870C'),"
                    + "(4,'Project Proposal', '0x50C878'),"
                    + "(5,'Report', '0x7851a9')";
            stmt.execute(insertStatement);
            //sample data for status table
            
            insertStatement = "INSERT OR IGNORE INTO Status VALUES "
                    + "(1, 'Today'),"
                    + "(2, 'Tomorrow'),"
                    + "(3, '7 Days'),"
                    + "(4,'Completed')";
            stmt.execute(insertStatement);
            
            insertStatement = "INSERT OR IGNORE INTO Task VALUES"
                    + "(0, 'Upload Coles Data','Coles needs to be uploaded to FTP for data analysts', '2019-10-01', '2019-11-06', 40,2,4),"
                    + "(1, 'Liquid Data','Build pitch for Liquid Data proposal', '2019-10-01', '2019-10-15', 50,4,4),"
                    + "(2, 'OCT_REPORT','Finish monthly report for clients', '2019-11-01', '2019-11-06', 70,5,4),"
                    + "(3, 'The Lean Startup','Finish reading', '2019-10-01', '2019-10-30', 20,1,4),"
                    + "(4, 'SMART Goals','Update SMART goals to better align with career vision', '2019-10-15', '2019-10-30', 20,1,4),"
                    + "(5, 'Communication Workshop','', '2019-10-15', '2019-10-30', 20,3,4)";
            stmt.execute(insertStatement);
            
            insertStatement = "INSERT OR IGNORE INTO ENTRY VALUES "
                    + "(0, 0, 0, 0,3600000,2,'2019-10-1'),"
                    + "(1, 0, 0, 0,1800000,2,'2019-10-10'),"
                    + "(2, 0, 0, 0,1950000,2,'2019-10-20'),"
                    + "(14, 0, 0, 0,3600000,2,'2019-11-04'),"
                    + "(3, 1, 0, 0,3600000,4,'2019-10-3'),"
                    + "(4, 1, 0, 0,3600000,4,'2019-10-5'),"
                    + "(5, 1, 0, 0,7200000,4,'2019-10-13'),"
                    + "(6, 1, 0, 0,9990000,4,'2019-10-14'),"
                    + "(7, 2, 0, 0,7200000,5,'2019-11-03'),"
                    + "(8, 2, 0, 0,9900000,5,'2019-11-05'),"
                    + "(15, 2, 0, 0,1600000,5,'2019-11-05'),"
                    + "(9, 3, 0, 0,3600000,1,'2019-10-03'),"
                    + "(10, 3, 0, 0,3600000,1,'2019-10-05'),"
                    + "(16, 3, 0, 0,3600000,1,'2019-10-07'),"
                    + "(17, 3, 0, 0,3600000,1,'2019-10-09'),"
                    + "(11, 4, 0, 0,3600000,1,'2019-10-04'),"
                    + "(12, 4, 0, 0,3600000,3,'2019-10-04'),"
                    + "(13, 5, 0, 0,3600000,3,'2019-10-04')";
            stmt.execute(insertStatement);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        stmt.close();
        
    }
    
    

    

}
