package com.example.frontend;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Customer {

    private Bitmap image;
    private String name;
    private String cpf;
    private String birthday;
    private ArrayList<String> suggestions;
    private String lastPurchaseDate;
    private String lastPurchaseValue;
    private ArrayList<String> lastPurchaseList;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public ArrayList<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(ArrayList<String> suggestions) {
        this.suggestions = suggestions;
    }

    public String getLastPurchaseDate() {
        return lastPurchaseDate;
    }

    public void setLastPurchaseDate(String lastPurchaseDate) {
        this.lastPurchaseDate = lastPurchaseDate;
    }

    public String getLastPurchaseValue() {
        return lastPurchaseValue;
    }

    public void setLastPurchaseValue(String lastPurchaseValue) {
        this.lastPurchaseValue = lastPurchaseValue;
    }

    public ArrayList<String> getLastPurchaseList() {
        return lastPurchaseList;
    }

    public void setLastPurchaseList(ArrayList<String> lastPurchaseList) {
        this.lastPurchaseList = lastPurchaseList;
    }
}
