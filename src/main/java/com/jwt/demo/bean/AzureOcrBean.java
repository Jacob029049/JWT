package com.jwt.demo.bean;

import java.io.Serializable;
import java.util.List;

public class AzureOcrBean implements Serializable {

    private String language;
    private float textAngle;
    private String orientation;
    private String boundingBox;
    private String text;
    private List<AzureOcrBean> words;
    private List<AzureOcrBean> lines;
    private List<AzureOcrBean> regions;

    public String getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(String boundingBox) {
        this.boundingBox = boundingBox;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<AzureOcrBean> getWords() {
        return words;
    }

    public void setWords(List<AzureOcrBean> words) {
        this.words = words;
    }

    public List<AzureOcrBean> getLines() {
        return lines;
    }

    public void setLines(List<AzureOcrBean> lines) {
        this.lines = lines;
    }

    public List<AzureOcrBean> getRegions() {
        return regions;
    }

    public void setRegions(List<AzureOcrBean> regions) {
        this.regions = regions;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public float getTextAngle() {
        return textAngle;
    }

    public void setTextAngle(float textAngle) {
        this.textAngle = textAngle;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
}
