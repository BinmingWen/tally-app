package com.it.girlaccount.test.Model;

public class Report {

    int idk;
    int amount;
    int total;
    String nameCategory;

    public Report( ) {
    }

    public Report(int idk, int amount, int total, String nameCategory) {
        this.idk = idk;
        this.amount = amount;
        this.total = total;
        this.nameCategory = nameCategory;
    }

    public int getIdk() {
        return idk;
    }

    public void setIdk(int idk) {
        this.idk = idk;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    @Override
    public String toString() {
        return "Report{" +
                "idk=" + idk +
                ", amount=" + amount +
                ", total=" + total +
                ", nameCategory='" + nameCategory + '\'' +
                '}';
    }
}
