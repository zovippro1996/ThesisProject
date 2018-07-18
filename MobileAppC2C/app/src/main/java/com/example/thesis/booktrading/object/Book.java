package com.example.thesis.booktrading.object;

import java.util.Locale;

public class Book {
    private int BookID;
    private String BookName;
    private float BookPrice;
    private int BookisPossess;

    public Book(int bookID, String bookName, float bookPrice, int bookisPossess) {
        BookID=bookID;
        BookName = bookName;
        BookPrice = bookPrice;
        BookisPossess = bookisPossess;
    }

    public Book(String bookName, float bookPrice, int bookisPossess) {
        BookID=0;
        BookName = bookName;
        BookPrice = bookPrice;
        BookisPossess = bookisPossess;
    }

    public Book(){
        BookID = 0;
        BookName = "";
        BookPrice = 0;
        BookisPossess = 0;
    }

    public int getBookID() {
        return BookID;
    }

    public void setBookID(int bookID) {
        BookID = bookID;
    }

    public String getBookName() {

        return BookName;
    }

    public void setBookName(String bookName) {
        BookName = bookName;
    }

    public float getBookPrice() {
        return BookPrice;
    }

    public void setBookPrice(float bookPrice) {
        BookPrice = bookPrice;
    }

    public int isBookisPossess() {
        return BookisPossess;
    }

    public void setBookisPossess(int bookisPossess) {
        BookisPossess = bookisPossess;
    }

    public static String getStringPrice(float money){
        String currencySymbol;
        currencySymbol = "\u0024";
        return String.format(Locale.getDefault(), "%s %.2f", currencySymbol, money);
    }
}
