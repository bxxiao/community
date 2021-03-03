package com.bx.community.model;

public class Notification {
    private Long id;

    private Long notifier;

    private Long receiver;

    // 表示该通知对应的问题（question）的id
    private Long outerid;

    private Integer type;

    private Long gmtCreate;

    private Integer status;

    private String notifierName;

    private String outerTitle;

    public Notification(Long id, Long notifier, Long receiver, Long outerid, Integer type, Long gmtCreate, Integer status, String notifierName, String outerTitle) {
        this.id = id;
        this.notifier = notifier;
        this.receiver = receiver;
        this.outerid = outerid;
        this.type = type;
        this.gmtCreate = gmtCreate;
        this.status = status;
        this.notifierName = notifierName;
        this.outerTitle = outerTitle;
    }

    public Notification() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNotifier() {
        return notifier;
    }

    public void setNotifier(Long notifier) {
        this.notifier = notifier;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public Long getOuterid() {
        return outerid;
    }

    public void setOuterid(Long outerid) {
        this.outerid = outerid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNotifierName() {
        return notifierName;
    }

    public void setNotifierName(String notifierName) {
        this.notifierName = notifierName;
    }

    public String getOuterTitle() {
        return outerTitle;
    }

    public void setOuterTitle(String outerTitle) {
        this.outerTitle = outerTitle;
    }
}