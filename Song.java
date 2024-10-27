package labs.lab9;

public class Song
{
	private String title;
    private String artist;
    private String category;
    private double runningTime;
    private String notes;
    private boolean sharedWithFriends;
    private boolean sharedWithFamily; 
    private boolean sharedWithCoworkers; 

    public Song(String title, String artist, String category, double runningTime, String notes, boolean sharedWithFriends, boolean sharedWithFamily, boolean sharedWithCoworkers)
    {
        this.title = title;
        this.artist = artist;
        this.category = category;
        this.runningTime = runningTime;
        this.notes = notes;
        this.sharedWithFriends = sharedWithFriends;
        this.sharedWithFamily = sharedWithFamily;
        this.sharedWithCoworkers = sharedWithCoworkers;
    }

    
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getArtist()
    {
        return artist;
    }

    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public double getRunningTime()
    {
        return runningTime;
    }

    public void setRunningTime(double runningTime)
    {
        this.runningTime = runningTime;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }
    
    public boolean isSharedWithFriends()
    {
        return sharedWithFriends;
    }

    public void setSharedWithFriends(boolean sharedWithFriends)
    {
        this.sharedWithFriends = sharedWithFriends;
    }

    public boolean isSharedWithFamily()
    {
        return sharedWithFamily;
    }

    public void setSharedWithFamily(boolean sharedWithFamily)
    {
        this.sharedWithFamily = sharedWithFamily;
    }

    public boolean isSharedWithCoworkers()
    {
        return sharedWithCoworkers;
    }

    public void setSharedWithCoworkers(boolean sharedWithCoworkers)
    {
        this.sharedWithCoworkers = sharedWithCoworkers;
    }

    public String toString() 
    {
        return title;
    }
}

