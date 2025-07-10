package com.jpmc.midascore.model;

public class Transaction {
    private String id;
    private String senderId;
    private String recipientId;
    private double amount;

    // Getters
    public String getId() {
        return id;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public double getAmount() {
        return amount;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    // For logging
    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", senderId='" + senderId + '\'' +
                ", recipientId='" + recipientId + '\'' +
                ", amount=" + amount +
                '}';
    }
}
