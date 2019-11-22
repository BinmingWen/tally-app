package com.it.girlaccount.test.Model;

public class Remain {

    int id;
    int remain;      //余额

    public Remain() {
    }
    public Remain(int id, int remain) {
        this.id = id;
        this.remain = remain;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }
}

