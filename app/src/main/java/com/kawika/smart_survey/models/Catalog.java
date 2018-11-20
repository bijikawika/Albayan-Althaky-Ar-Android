package com.kawika.smart_survey.models;
/*
 * Created by akhil on 06/02/18.
 */

public class Catalog {
    private String name ;
    private String imagePath ;

    public Catalog(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
