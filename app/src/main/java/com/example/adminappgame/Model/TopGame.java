package com.example.adminappgame.Model;

public class TopGame {
    private transient String documentId;
    private String name;
    private int price;
    private int genre;
    private String img_url;
    private String description;
    private String storage;
    private int downloaded;
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public TopGame(String documentId, String name, int price, int genre, String img_url, String description, String storage, int downloaded) {
        this.documentId = documentId;
        this.name = name;
        this.price = price;
        this.genre = genre;
        this.img_url = img_url;
        this.description = description;
        this.storage = storage;
        this.downloaded = downloaded;
    }

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

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public int getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(int downloaded) {
        this.downloaded = downloaded;
    }

    public TopGame(String name, int price, int genre, int downloaded, String storage, String description, String img_url) {
        this.name = name;
        this.price = price;
        this.genre = genre;
        this.img_url = img_url;
        this.description = description;
        this.storage = storage;
        this.downloaded = downloaded;
    }

    public TopGame() {
    }
}
