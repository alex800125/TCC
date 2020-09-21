package com.example.frontend;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Customer {

    @SerializedName("image")
    private StringBuilder imageBase64;

    private Bitmap imageBitmap;

    @SerializedName("name")
    private String name;

    @SerializedName("cpf")
    private String cpf;

    @SerializedName("birthday")
    private String birthday;

    @SerializedName("suggestion")
    private ArrayList<ItemSuggestion> suggestions;

    @SerializedName("ultima_compra_data")
    private String lastPurchaseDate;

    @SerializedName("ultima_compra_valor")
    private String lastPurchaseValue;

    @SerializedName("itens_comprados")
    private ArrayList<Item> lastPurchaseList;

    @SerializedName("sexo")
    private String sex;

    public StringBuilder getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(StringBuilder imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
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

    public ArrayList<ItemSuggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(ArrayList<ItemSuggestion> suggestions) {
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

    public ArrayList<Item> getLastPurchaseList() {
        return lastPurchaseList;
    }

    public void setLastPurchaseList(ArrayList<Item> lastPurchaseList) {
        this.lastPurchaseList = lastPurchaseList;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}

class Item {
    @SerializedName("item")
    private String item;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}

class ItemSuggestion {
    @SerializedName("suggestion_item")
    private String itemSuggestion;

    public String getItemSuggestion() {
        return itemSuggestion;
    }

    public void setItemSuggestion(String item) {
        this.itemSuggestion = item;
    }
}