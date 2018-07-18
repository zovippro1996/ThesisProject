package com.example.thesis.booktrading.object;

public class Transaction {
    private int TransactionID;
    private String TransactionTargetFullName;
    private String TransactionTargetPhone;
    private String TransactionItemName;
    private float TransactionPrice;
    private int TransactionisBuy;


    public Transaction(int transactionID, String transactionTargetFullName, String
            transactionTargetPhone, String transactionItemName, float transactionPrice, int
            transactionisBuy) {
        TransactionID = transactionID;
        TransactionTargetFullName = transactionTargetFullName;
        TransactionTargetPhone = transactionTargetPhone;
        TransactionItemName = transactionItemName;
        TransactionPrice = transactionPrice;
        TransactionisBuy = transactionisBuy;
    }

    public Transaction(String transactionTargetFullName, String
            transactionTargetPhone, String transactionItemName, float transactionPrice, int
                               transactionisBuy) {
        TransactionID = 0;
        TransactionTargetFullName = transactionTargetFullName;
        TransactionTargetPhone = transactionTargetPhone;
        TransactionItemName = transactionItemName;
        TransactionPrice = transactionPrice;
        TransactionisBuy = transactionisBuy;

    }

    public int getTransactionID() {
        return TransactionID;
    }

    public void setTransactionID(int transactionID) {
        TransactionID = transactionID;
    }

    public String getTransactionTargetFullName() {
        return TransactionTargetFullName;
    }

    public void setTransactionTargetFullName(String transactionTargetFullName) {
        TransactionTargetFullName = transactionTargetFullName;
    }

    public String getTransactionTargetPhone() {
        return TransactionTargetPhone;
    }

    public void setTransactionTargetPhone(String transactionTargetPhone) {
        TransactionTargetPhone = transactionTargetPhone;
    }

    public String getTransactionItemName() {
        return TransactionItemName;
    }

    public void setTransactionItemName(String transactionItemName) {
        TransactionItemName = transactionItemName;
    }

    public float getTransactionPrice() {
        return TransactionPrice;
    }

    public void setTransactionPrice(float transactionPrice) {
        TransactionPrice = transactionPrice;
    }

    public int getTransactionisBuy() {
        return TransactionisBuy;
    }

    public void setTransactionisBuy(int transactionisBuy) {
        TransactionisBuy = transactionisBuy;
    }
}
