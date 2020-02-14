package com.app.technofunda.model;

import java.util.HashMap;
import java.util.Map;

public class EquityResponce {
    private String segment;
    private String recommendation;
    private String symbol;
    private Integer entryPrice;
    private Integer sL;
    private Integer tG1;
    private Integer tG2;
    private Integer quantity;
    private Integer exitValue;
    private Integer profitLoss;
    private String time;
    private Integer srno;
    private String pinname;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getEntryPrice() {
        return entryPrice;
    }

    public void setEntryPrice(Integer entryPrice) {
        this.entryPrice = entryPrice;
    }

    public Integer getSL() {
        return sL;
    }

    public void setSL(Integer sL) {
        this.sL = sL;
    }

    public Integer getTG1() {
        return tG1;
    }

    public void setTG1(Integer tG1) {
        this.tG1 = tG1;
    }

    public Integer getTG2() {
        return tG2;
    }

    public void setTG2(Integer tG2) {
        this.tG2 = tG2;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getExitValue() {
        return exitValue;
    }

    public void setExitValue(Integer exitValue) {
        this.exitValue = exitValue;
    }

    public Integer getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(Integer profitLoss) {
        this.profitLoss = profitLoss;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getSrno() {
        return srno;
    }

    public void setSrno(Integer srno) {
        this.srno = srno;
    }

    public String getPinname() {
        return pinname;
    }

    public void setPinname(String pinname) {
        this.pinname = pinname;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
