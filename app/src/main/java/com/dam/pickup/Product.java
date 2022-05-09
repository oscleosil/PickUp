package com.dam.pickup;

public class Product {

    private String title;
    private String price;
    private String link;
    private String provider_name;
    private String description;

    public Product(String title, String price, String link, String provider_name, String description){
        this.title = title;
        this.price = price;
        this.link = link;
        this.provider_name = provider_name;
        this.description = description;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String s){
        this.title = s;
    }

    public String getPrice(){
        return this.price;
    }

    public void setPrice(String s){
        this.price = s;
    }

    public String getLink(){
        return this.link;
    }

    public void setLink(String s){
        this.link = s;
    }

    public String getProvider_name(){
        return this.provider_name;
    }

    public void setProvider_name(String s){
        this.provider_name = s;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String s){
        this.description = s;
    }

}
