package com.nucsaping.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.sql.Blob;

@Entity
@Table(name = "files")
public class FileDB {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Lob
    @Column(name = "data", columnDefinition="LONGBLOB")
    @JsonIgnore
    private byte[] data;

    @Column(name = "display_image_uri")
    private String displayImageUri;


    @Column(name = "download_image_uri")
    private String downloadImageUri;

    public FileDB() {

    }

    public FileDB(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }

    public FileDB(String name, String type, byte[] data, String displayImageUri, String downloadImageUri) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.displayImageUri = displayImageUri;
        this.downloadImageUri = downloadImageUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getDisplayImageUri() {
        return displayImageUri;
    }

    public void setDisplayImageUri(String displayImageUri) {
        this.displayImageUri = displayImageUri;
    }

    public String getDownloadImageUri() {
        return downloadImageUri;
    }

    public void setDownloadImageUri(String downloadImageUri) {
        this.downloadImageUri = downloadImageUri;
    }
}
