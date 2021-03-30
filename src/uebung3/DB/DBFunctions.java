package uebung3.DB;

import uebung3.DTO.Bestellung;
import uebung3.DTO.Kunde;
import uebung3.secrets;

import java.sql.*;
import java.util.ArrayList;

public class DBFunctions implements secrets {

    public static void print(String msg){
        System.out.println(msg);
    }

    public static long startTime() {
        long start = System.nanoTime();
        return start;
    }

    public static void stopTime(){
        long start = startTime();
        long stop = System.nanoTime();
        long time = stop - start;
        print("Zeit des Querys: " + time);
        print("");
    }

    public static Connection conn() throws SQLException{
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    //insert Statement
    public static void insertKunde(String name, String vorname, int plz, String ort, String land, String strassehnr) throws SQLException {
        Connection conn = conn();
        String insert = "INSERT INTO Kunde (Name, Vorname, PLZ, Ort, Land, StrasseHnr) VALUES ('" + name + "', '" + vorname + "', '" + plz +"', '" + ort + "', '" + land + "', '" + strassehnr + "');";

        Statement stmt = conn.createStatement();
        stmt.executeUpdate(insert);
        print("Insert Statement");
        conn.close();
    }

    //insert PreparedStatement
    public static void insertKundePS(String name, String vorname, int plz, String ort, String land, String strassehnr) throws SQLException {
        Connection conn = conn();
        String insert = "INSERT INTO Kunde (Name, Vorname, PLZ, Ort, Land, StrasseHnr) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = conn.prepareStatement(insert);

        statement.setString(1, name);
        statement.setString(2, vorname);
        statement.setInt(3, plz);
        statement.setString(4, ort);
        statement.setString(5, land);
        statement.setString(6, strassehnr);

        statement.executeUpdate();
        print("Insert Prepared Statement");
        conn.close();
    }

    //select
    public static void getKunden(ArrayList<Kunde> kunden) throws  SQLException {
        Connection conn = conn();
        String query = "SELECT * FROM Kunde";
        Statement statement = conn.createStatement();
        ResultSet result = statement.executeQuery(query);

        while(result.next()) {
            String name = result.getString("Name");
            String vorname = result.getString("Vorname");
            int plz = result.getInt("PLZ");
            String ort = result.getString("Ort");
            String land = result.getString("Land");
            int k_id = result.getInt("K-ID");
            String strassehnr = result.getString("StrasseHnr");

            kunden.add(new Kunde(k_id, name, vorname, plz, ort, land, strassehnr));
        }
        conn.close();
    }

    //select
    public static void getBestellungen(ArrayList<Bestellung> bestellungen) throws  SQLException {
        Connection conn = conn();
        String query = "SELECT * FROM Bestellung";
        Statement statement = conn.createStatement();
        ResultSet result = statement.executeQuery(query);

        while(result.next()) {
            long bID = result.getLong("B-ID");
            long kID = result.getLong("K-ID");

            bestellungen.add(new Bestellung(bID, kID));
        }
        conn.close();
    }

    //delete Statement
    public static void emptyTable(String table) throws SQLException {
        Connection conn = conn();

        String delete = "DELETE FROM " + "`" + table + "`;";
        Statement stmt = conn.createStatement();
        stmt.execute(delete);

        conn.close();

        print("Leere Tabelle '" + table + "'");

    }

    public static void selectKunde(String name) throws SQLException{
        Connection conn = conn();

        String select = "SELECT * FROM Kunde WHERE Name= " + "'" + name + "';";
        Statement statement = conn.createStatement();

        print("Gebe Kunden: " + name + " aus");
        ResultSet result = statement.executeQuery(select);
        while(result.next()) {
            String nameKunde = result.getString("Name");
            String vorname = result.getString("Vorname");
            int plz = result.getInt("PLZ");
            String ort = result.getString("Ort");
            String land = result.getString("Land");
            int k_id = result.getInt("K-ID");
            String strassehnr = result.getString("StrasseHnr");

            print(nameKunde + " " + vorname + " " + plz + " " + ort + " " + land + " " + k_id + " " + strassehnr);
        }
        print("");
        print("Select Statement");
        conn.close();
    }

    public  static void selectKundePS(String name) throws SQLException{
        Connection conn = conn();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Kunde WHERE Name= ?");
        ps.setString(1, name);
        print("Gebe Kunden: " + name + " aus");

        ResultSet result = ps.executeQuery();
        while(result.next()) {
            String nameKunde = result.getString("Name");
            String vorname = result.getString("Vorname");
            int plz = result.getInt("PLZ");
            String ort = result.getString("Ort");
            String land = result.getString("Land");
            int k_id = result.getInt("K-ID");
            String strassehnr = result.getString("StrasseHnr");

            print(nameKunde + " " + vorname + " " + plz + " " + ort + " " + land + " " + k_id + " " + strassehnr);
        }
        print("");
        print("Select PreparedStatement");
        ps.close();
        conn.close();
    }

    public static void deleteKunde(String name) throws SQLException{
        Connection conn = conn();

        String delete = "DELETE FROM KUNDE WHERE Name= " + "`" + name + "`;";
        Statement stmt = conn.createStatement();
        print("Lösche Kunde'" + name + "'.");
        stmt.execute(delete);
        print("Delete Statement");
        conn.close();
    }

    public static void deleteKundePS(String name) throws SQLException{
        Connection conn = conn();

        PreparedStatement ps = conn.prepareStatement("DELETE FROM Kunde WHERE Name= ?");

        ps.setString(1, name);

        print("Lösche Kunde'" + name + "'.");

        ps.executeUpdate();
        print("Delete PreparedStatement");
        ps.close();
        conn.close();

    }

    //update Statement
    public static void updateTable(String table, String changingColumn, String changingValue, String column, String value) throws SQLException {
        Connection conn = conn();

        String update = "UPDATE "  + table  + " SET " +  changingColumn + "= " + "'" + changingValue + "'" + " WHERE " + column + "= " + "'" + value + "'" + ";";
        Statement stmt = conn.createStatement();
        print("Verändere Tabelle '" + table + "'.");
        stmt.execute(update);
        print("Update Statement");
        conn.close();



    }

    //update PreparedStatement
    public static void updateTablePS(String table, String changingColumn, String changingValue, String column, String value) throws SQLException {

        Connection conn = conn();

        PreparedStatement ps = conn.prepareStatement("UPDATE Kunde SET Name= ? WHERE Vorname= ?;");

        ps.setString(1, changingValue);
        ps.setString(2, value);

        print("Verändere Tabelle '" + table + "'.");

        ps.executeUpdate();
        ps.close();
        print("Update PreparedStatment");
        conn.close();
    }
}