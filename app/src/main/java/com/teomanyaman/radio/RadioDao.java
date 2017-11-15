package com.teomanyaman.radio;

public class RadioDao {

    private int id;
    private String Name;
    private String URL;
    private int Favourite;
    private String Icon;

    public RadioDao(int id, String Name, String URL, int Favourite, String Icon){
        this.id = id;
        this.Name = Name;
        this.URL = URL;
        this.Favourite = Favourite;
        this.Icon = Icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public int getFavourite() {
        return Favourite;
    }

    public void setFavourite(int favourite) {
        Favourite = favourite;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }
}
