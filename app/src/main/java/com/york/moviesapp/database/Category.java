package com.york.moviesapp.database;

public class Category {
    private int id;
    private String name;

    public Category(int id) {
        this.id = id;
        switch (id) {
            case 12:
                this.name = "Adventure";
                break;
            case 14:
                this.name = "Fantasy";
                break;
            case 16:
                this.name = "Animation";
                break;
            case 18:
                this.name = "Drama";
                break;
            case 27:
                this.name = "Horror";
                break;
            case 28:
                this.name = "Action";
                break;
            case 35:
                this.name = "Comedy";
                break;
            case 36:
                this.name = "History";
                break;
            case 37:
                this.name = "Western";
                break;
            case 53:
                this.name = "Thriller";
                break;
            case 80:
                this.name = "Crime";
                break;
            case 99:
                this.name = "Documentary";
                break;
            case 878:
                this.name = "Science Fiction";
                break;
            case 9648:
                this.name = "Mystery";
                break;
            case 10402:
                this.name = "Music";
                break;
            case 10749:
                this.name = "Romance";
                break;
            case 10751:
                this.name = "Family";
                break;
            case 10752:
                this.name = "War";
                break;
            case 10770:
                this.name = "TV Movie";
                break;
            default:
                this.name = "Unknown";
                break;
        }
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

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
}
