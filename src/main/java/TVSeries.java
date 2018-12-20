public class TVSeries {
    /* Parameters:
     * Username - Holds Name of the Account Holder;
     * Locked: To indicate if Account is Locked*/

    int id;
    String name;
    int current_season;
    int current_episode;
    String lastUpdate;
    String nextEpisodeDate;


    /*Constructor which takes and sets Parameters of the class.*/
    public TVSeries(int id, String name, int current_season, int current_episode, String nextEpisodeDate) {
        this.id = id;
        this.name = name;
        this.current_season = current_season;
        this.current_episode = current_episode;
        this.nextEpisodeDate = nextEpisodeDate;
    }

    public TVSeries(int id, String name, int current_season, int current_episode, String nextEpisodeDate, String lastUpdate) {
        this.id = id;
        this.name = name;
        this.current_season = current_season;
        this.current_episode = current_episode;
        this.nextEpisodeDate = nextEpisodeDate;
        this.lastUpdate = lastUpdate;
    }

    public TVSeries(int id, String name) {
        this.id = id;
        this.name = name;
    }

    //Getters and Setters for Parameters Username and Locked.

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrent_season() {
        return current_season;
    }

    public void setCurrent_season(int current_season) {
        this.current_season = current_season;
    }

    public int getCurrent_episode() {
        return current_episode;
    }

    public void setCurrent_episode(int current_episode) {
        this.current_episode = current_episode;
    }

    public String getNextEpisodeDate() {
        return nextEpisodeDate;
    }

    public void setNextEpisodeDate(String nextEpisodeDate) {
        this.nextEpisodeDate = nextEpisodeDate;
    }

    public String getLastUpdate() {return lastUpdate;}

    public void setLastUpdate(String lastUpdate) {this.lastUpdate = lastUpdate;}
}
