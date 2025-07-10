package com.jpmc.midascore.foundation;


public class Balance {
    private double balance;

    public Balance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "BEGIN_BALANCE:" + balance + ":END_BALANCE";
    }
}
