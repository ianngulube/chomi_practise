package za.co.biza.adverts.marbl;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Advert {

    @JsonProperty("kcm-sts")
    protected int kcmSts;
    @JsonProperty("cid")
    protected int cid;
    @JsonProperty("description")
    protected String description;
    @JsonProperty("media")
    protected String media;
    @JsonProperty("postback-url")
    protected String postbackUrl;

    public int getKcmSts() {
        return kcmSts;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public void setKcmSts(int kcmSts) {
        this.kcmSts = kcmSts;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostbackUrl() {
        return postbackUrl;
    }

    public void setPostbackUrl(String postbackUrl) {
        this.postbackUrl = postbackUrl;
    }

    @Override
    public String toString() {
        return "kcmSts: " + kcmSts + ", cid: " + cid + ", description: " + description + ", media: " + media + ", postbackUrl: " + postbackUrl;
    }

}
