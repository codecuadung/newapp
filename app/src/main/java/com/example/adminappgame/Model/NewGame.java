package com.example.adminappgame.Model;

public class NewGame {
    private transient String documentId;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public NewGame(String documentId, String name, int price, int genre, String storage, String description, String img_url, int downloaded) {
        this.documentId = documentId;
        this.name = name;
        this.price = price;
        this.genre = genre;
        this.storage = storage;
        this.description = description;
        this.img_url = img_url;
        this.downloaded = downloaded;
    }

    private String name;
    private int price;
    private int genre;
    private String storage;
    private String description;
    private String img_url;
    private int downloaded;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getGenre() {
        return genre;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(int downloaded) {
        this.downloaded = downloaded;
    }

    public NewGame(String name, int price, int genre, String storage, String description, String img_url, int downloaded) {
        this.name = name;
        this.price = price;
        this.genre = genre;
        this.storage = storage;
        this.description = description;
        this.img_url = img_url;
        this.downloaded = downloaded;
    }

    public NewGame() {
    }
}
