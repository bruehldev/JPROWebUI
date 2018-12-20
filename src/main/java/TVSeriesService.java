import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class TVSeriesService extends Service<ObservableList<TVSeries>> {
    @Override
    protected Task createTask() {
        return new TVSeriesTask();
    }
}