package com.it.girlaccount.test.Model;

public class Category {

    int id;
    String category;   //类别

    //contructor
    public Category(){

    }
    public Category(int id, String category) {
        this.id = id;
        this.category = category;
    }

    //setter
    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    //getter
    public int getId() {
        return id;
    }
    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return category ;
    }


}

