package com.example.thesis.booktrading.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.thesis.booktrading.object.Book;
import com.example.thesis.booktrading.object.PersonalInfo;
import com.example.thesis.booktrading.object.Transaction;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Thesis_C2C_Mobile";

    // Associated columns for User table
    private static final String TABLE_NAME_PERSONALINFO = "PersonalInfo";
    public static final String COL_PERSONALINFO_FULLNAME = "PersonalInfo_FullName";
    public static final String COL_PERSONALINFO_PHONE = "PersonalInfo_Phone";


    // Associated columns for Inventory table
    public static final String TABLE_NAME_INVENTORY = "Inventory";
    public static final String COL_BOOK_NAME = "Book_Name";
    public static final String COL_BOOK_ISPOSSESS = "Book_isPossess";
    public static final String COL_BOOK_PRICE = "Book_Price";

    // Associated columns for Transaction table
    public static final String TABLE_NAME_TRANSACT = "Transact";
    public static final String COL_TRANS_TARGET_FULLNAME = "Transact_TargetFullName";
    public static final String COL_TRANS_TARGET_PHONE = "Transact_TargetPhone";
    public static final String COL_TRANS_BOOK_NAME = "Transact_BookName";
    public static final String COL_TRANS_PRICE = "Transact_Price";
    public static final String COL_TRANS_isBuy = "Transact_isBuy";

    // Associated columns for LogChat table
    public static final String TABLE_NAME_LOGCHAT = "LogChat";
    public static final String COL_LOGCHAT_SENDERPHONENUMBER = "LogChat_SenderPhoneNumber";
    public static final String COL_LOGCHAT_RECEIVERPHONENUMBER = "LogChat_ReceiverPhoneNumber";
    public static final String COL_LOGCHAT_MESSAGE = "LogChat_Message";
    public static final String COL_LOGCHAT_STATUS = "LogChat_Status";
    public static final String COL_LOGCHAT_CREATEDAT = "LogChat_CreatedAt";


    private SQLiteDatabase database;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

        // Reference to database for later uses
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // Establishment table
        database.execSQL("CREATE TABLE " + TABLE_NAME_PERSONALINFO + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_PERSONALINFO_FULLNAME + " TEXT," +
                COL_PERSONALINFO_PHONE + " TEXT);");

        // Review table
        database.execSQL("CREATE TABLE " + TABLE_NAME_INVENTORY + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_BOOK_NAME + " TEXT," +
                COL_BOOK_ISPOSSESS + " INTEGER," +
                COL_BOOK_PRICE + " REAL);");

        // TRANSACTION table
        database.execSQL(" CREATE TABLE " + TABLE_NAME_TRANSACT + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_TRANS_TARGET_FULLNAME + " TEXT," +
                COL_TRANS_TARGET_PHONE + " TEXT," +
                COL_TRANS_BOOK_NAME + " TEXT," +
                COL_TRANS_PRICE + " REAL," +
                COL_TRANS_isBuy + " INTEGER);");

        // LOGCHAT table
        database.execSQL(" CREATE TABLE " + TABLE_NAME_LOGCHAT + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_LOGCHAT_SENDERPHONENUMBER + " TEXT," +
                COL_LOGCHAT_RECEIVERPHONENUMBER + " TEXT," +
                COL_LOGCHAT_MESSAGE + " TEXT," +
                COL_LOGCHAT_STATUS + " INTEGER," +
                COL_LOGCHAT_CREATEDAT + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // User will lose old data but at least we'll warn them this is happening
        Log.w(this.getClass().getName(), DATABASE_NAME
                + " database upgrade to version " + newVersion
                + " old data lost");

        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PERSONALINFO);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_INVENTORY);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TRANSACT);

        onCreate(database);
    }

    /***
     *
     * @param personalInfo
     * @return PersonalInfo
     */
    public long insertPersonalInfo(PersonalInfo personalInfo) {
        ContentValues rowValues = new ContentValues();

        String COL_PERSONALINFO_FULLNAME = "PersonalInfo_FullName";
        String COL_PERSONALINFO_PHONE = "PersonalInfo_Phone";

        // Assemble row of data in ContentValues object
        rowValues.put(COL_PERSONALINFO_FULLNAME, personalInfo.getPersonalInfoFullName());
        rowValues.put(COL_PERSONALINFO_PHONE, personalInfo.getPersonalInfoPhone());

        // Record the id of the 'establishment' just been inserted
        return database.insertOrThrow(TABLE_NAME_PERSONALINFO, null, rowValues);
    }

    /***
     *
     * @return
     */
    public long getNumberOfPersonalInfo() {
        Cursor cursor = database.query(TABLE_NAME_PERSONALINFO, null, null,
                null, null, null, null);

        long counter = cursor.getCount();
        cursor.close();

        return counter;
    }

    /***
     *
     * @return
     */
    public PersonalInfo getPersonalInfoRecord() {
        Cursor cursor = database.query(TABLE_NAME_PERSONALINFO, null, null,
                null, null, null, null);

        String FullName = "";
        String Phone = "";

        long counter = cursor.getCount();
        cursor.moveToFirst();
        if(counter > 0){
            try{
                FullName = cursor.getString(cursor.getColumnIndex(COL_PERSONALINFO_FULLNAME));
                Phone = cursor.getString(cursor.getColumnIndex(COL_PERSONALINFO_PHONE));
            } catch (Exception ex){

            } finally {
                cursor.close();
            }
        }

        cursor.close();

        PersonalInfo newPersonalInfo = new PersonalInfo(FullName, Phone);
        return newPersonalInfo;
    }

    /***
     *
     * @param book
     * @return
     */
    public long insertBook(Book book) {
        ContentValues rowValues = new ContentValues();

        // Assemble row of data in the ContentValues object
        rowValues.put(COL_BOOK_NAME, book.getBookName());
        rowValues.put(COL_BOOK_ISPOSSESS, book.isBookisPossess());
        rowValues.put(COL_BOOK_PRICE, book.getBookPrice());

        return database.insertOrThrow(TABLE_NAME_INVENTORY, null, rowValues);
    }


    public long insertTrans(Transaction newTransaction) {
        ContentValues rowValues = new ContentValues();

        // Assemble row of data in the ContentValues object
        rowValues.put(COL_TRANS_TARGET_FULLNAME, newTransaction.getTransactionTargetFullName());
        rowValues.put(COL_TRANS_TARGET_PHONE, newTransaction.getTransactionTargetPhone());
        rowValues.put(COL_TRANS_BOOK_NAME, newTransaction.getTransactionItemName());
        rowValues.put(COL_TRANS_PRICE, newTransaction.getTransactionPrice());
        rowValues.put(COL_TRANS_isBuy, newTransaction.getTransactionisBuy());

        return database.insertOrThrow(TABLE_NAME_TRANSACT, null, rowValues);
    }

    /***
     *
     * @return
     */
    public long getPersonalInfoCount() {
        Cursor cursor = database.query(TABLE_NAME_PERSONALINFO, null, null,
                null, null, null, null);

        long counter = cursor.getCount();
        cursor.close();

        return counter;
    }

    /***
     *
     * @return
     */
    public String getPersonalInfoPhone() {
        Cursor cursor = database.query(TABLE_NAME_PERSONALINFO, null, null,
                null, null, null, null);

        String arrData = "";
        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                arrData = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_PERSONALINFO_PHONE));
            }
            cursor.close();
        }
        return arrData;
    }

    /***
     *
     * @return
     */
    public String getPersonalInfoName() {
        Cursor cursor = database.query(TABLE_NAME_PERSONALINFO, null, null,
                null, null, null, null);

        String arrData = "";
        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                arrData = cursor.getString(cursor.getColumnIndex(DatabaseHelper
                        .COL_PERSONALINFO_FULLNAME));
            }
            cursor.close();
        }
        return arrData;
    }

    /***
     *
     * @return
     */
    public long getNumberOfBooks() {
        Cursor cursor = database.query(TABLE_NAME_INVENTORY, null, null,
                null, null, null, null);

        long counter = cursor.getCount();
        cursor.close();

        return counter;
    }

    /***
     *
     * @return
     */
    public long getNumberOfTransacts() {
        Cursor cursor = database.query(TABLE_NAME_TRANSACT, null, null,
                null, null, null, null);

        long counter = cursor.getCount();
        cursor.close();

        return counter;
    }

    /***
     *
     * @return
     */
    public Cursor getAllBooks() {
        return database.query(TABLE_NAME_INVENTORY, null, null,
                null, null, null, null);
    }

    public Cursor getAllBooksWishList(){
        return database.query(TABLE_NAME_INVENTORY, null, COL_BOOK_ISPOSSESS + " = ?",
                new String[] {"0"}, null, null, null);
    }

    public Cursor getAllBooksInventory(){
        return database.query(TABLE_NAME_INVENTORY, null, COL_BOOK_ISPOSSESS + " = ?",
                new String[] {"1"}, null, null, null);
    }

    //Get Book from BookID
    public Book getBookfromID(String ID){

        Cursor cursor = database.query(TABLE_NAME_INVENTORY, null, "_id = ?",
                new String[] {ID}, null, null, null,"1");

        int BookID = 0;
        String BookName = "";
        float BookPrice = 0;
        int isProcess = 0;

        long counter = cursor.getCount();
        cursor.moveToFirst();
        if(counter > 0){
            try{
                BookID = Integer.parseInt(cursor.getString(cursor.getColumnIndex
                        ("_id")));
                BookName = cursor.getString(cursor.getColumnIndex(COL_BOOK_NAME));
                BookPrice = cursor.getFloat(cursor.getColumnIndexOrThrow(COL_BOOK_PRICE));
                isProcess = cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOK_ISPOSSESS));
            } catch (Exception ex){
                ex.printStackTrace();
            } finally {
                cursor.close();
            }
        }

        cursor.close();
        return new Book(BookID, BookName, BookPrice, isProcess);
    }


    /***
     * Delete Personal Information
     */
    public void deletePersonalInfo(){
        database.delete(TABLE_NAME_PERSONALINFO, null, null);
    }

    /**
     * Delete all Books
     */
    public void deleteAllBooks() {
        database.delete(TABLE_NAME_INVENTORY, null, null);
    }

    /**
     * Delete all Transactions
     */
    public void deleteAllTransacts() {
        database.delete(TABLE_NAME_TRANSACT, null, null);
    }

    /**
     * Delete the record with the matching _id
     * @param id ID deleted
     */
    public void deleteBook(int id) {
        database.delete(TABLE_NAME_INVENTORY, "_id = ?", new String[] {String.valueOf(id)});
    }

    /**
     * Delete the record with the matching _id
     * @param id for Transact
     */
    public void deleteTransact(int id) {
        database.delete(TABLE_NAME_TRANSACT, "_id = ?", new String[] {String.valueOf(id)});
    }

    public Cursor getAllTransacts() {
        return database.query(TABLE_NAME_TRANSACT, null, null,
                null, null, null, null);
    }
}
