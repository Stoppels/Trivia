package connectivity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QueryManager {

    private final Dbmanager dbmanager;

    public QueryManager(Dbmanager dbmanager) {
        this.dbmanager = dbmanager;
    }

    public void insertBaggage() {

        String sql = "INSERT INTO Trivia.Vraag VALUES(2, 'Hoe heette het vorige project?') ";

        System.out.println(sql);
        int luggage_id = 0;
        try {
            ResultSet result = dbmanager.insertQuery(sql);
            result.next();
            luggage_id = result.getInt(1);
            System.out.println(luggage_id);

        } catch (SQLException e) {

            System.out.println("FOUT" + e.getMessage());
        }

    }

    public void setVraag() throws SQLException {

        try {
            String sql = "SELECT Vraag FROM vraag WHERE VraagID = 1;";

            System.out.println(sql);

            ResultSet result = dbmanager.doQuery(sql);
            result.next();
            
           
        } catch (SQLException e) {

            System.out.println("FOUT" + e.getMessage());
        }

    }
}
