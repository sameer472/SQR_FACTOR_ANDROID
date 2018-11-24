package com.hackerkernel.user.sqrfactor.Pojo;

public class AttachmentClass {
    private String id;
    private String attachmentUrl;

    public AttachmentClass(String id, String attachmentUrl) {
        this.id = id;
        this.attachmentUrl = attachmentUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }
}
