import java.sql.Connection;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class TVSeriesTask extends Task<ObservableList<TVSeries>> {
    @Override protected ObservableList<TVSeries> call() throws Exception {
        for (int i = 0; i < 500; i++) {
            updateProgress(i, 500);
            //Thread.sleep(0,01);
        }
        ObservableList<TVSeries> TVSeriesList = FXCollections.observableArrayList();
        Connection c ;
        c = DBConnector.connect();
        //SQL FOR SELECTING ALL OF USER ACCOUNTS
        System.out.println("TVSeries");
        String SQL = "SELECT * from TVSeries";
        //ResultSet
        ResultSet rs = c.createStatement().executeQuery(SQL);
        while(rs.next()){
            TVSeriesList.add(new TVSeries(rs.getInt("id"),rs.getString("name"),rs.getInt("current_season"),rs.getInt("current_episode"),rs.getString("nextEpisodeDate"),rs.getString("lastUpdate")));
        }
        return TVSeriesList;
    }
}