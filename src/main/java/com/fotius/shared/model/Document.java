package com.fotius.shared.model;

import javax.persistence.*;

@Entity
@Table(name = "documents")
@SequenceGenerator(name = "documents_sequence", sequenceName = "documents_sequence", allocationSize = 1)

public class Document {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "documents_sequence")
    @Column(name = "document_id")
    private Integer documentId;

    @Column(name = "path")
    private String path;

    @Column(name="name")
    private String name;


    private Integer size;

    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name = "uploader_id")
    private User uploader;

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public User getUploader() {
        return uploader;
    }

    public void setUploader(User uploader) {
        this.uploader = uploader;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
