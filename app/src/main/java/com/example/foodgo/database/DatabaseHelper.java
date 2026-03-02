package com.example.foodgo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.foodgo.models.FoodItem;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FoodOrderDB";
    private static final int DATABASE_VERSION = 4;

    private static final String TABLE_USERS = "users";
    private static final String TABLE_FOOD_ITEMS = "food_items";

    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";

    private static final String COL_FOOD_NAME = "name";
    private static final String COL_FOOD_DESCRIPTION = "description";
    private static final String COL_FOOD_PRICE = "price";
    private static final String COL_FOOD_CATEGORY = "category";
    private static final String COL_FOOD_IMAGE = "image_resource";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_NAME + " TEXT,"
                + COL_EMAIL + " TEXT UNIQUE,"
                + COL_PASSWORD + " TEXT)";

        db.execSQL(CREATE_TABLE);

        String CREATE_FOOD_TABLE = "CREATE TABLE " + TABLE_FOOD_ITEMS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_FOOD_NAME + " TEXT, "
                + COL_FOOD_DESCRIPTION + " TEXT, "
                + COL_FOOD_PRICE + " INTEGER, "
                + COL_FOOD_CATEGORY + " TEXT, "
                + COL_FOOD_IMAGE + " INTEGER"
                + ")";

        db.execSQL(CREATE_FOOD_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD_ITEMS);
        onCreate(db);
    }


    private void insertFoodItem(SQLiteDatabase db, String name, String description, int price, String category,
                                int imageResource) {
        ContentValues values = new ContentValues();
        values.put(COL_FOOD_NAME, name);
        values.put(COL_FOOD_DESCRIPTION, description);
        values.put(COL_FOOD_PRICE, price);
        values.put(COL_FOOD_CATEGORY, category);
        values.put(COL_FOOD_IMAGE, imageResource);
        db.insert(TABLE_FOOD_ITEMS, null, values);
    }

    // 🔐 SHA-256 Hashing Function
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Register User
    public boolean registerUser(String name, String email, String password) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String hashedPassword = hashPassword(password);

        values.put(COL_NAME, name);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, hashedPassword);

        long result = db.insert(TABLE_USERS, null, values);

        return result != -1;
    }

    // Login Check
    public boolean checkUser(String email, String password) {

        SQLiteDatabase db = this.getReadableDatabase();
        String hashedPassword = hashPassword(password);

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS +
                        " WHERE email=? AND password=?",
                new String[]{email, hashedPassword});

        boolean exists = cursor.getCount() > 0;
        cursor.close();

        return exists;
    }

    // Check if Email Exists
    public boolean emailExists(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS +
                        " WHERE email=?",
                new String[]{email});

        boolean exists = cursor.getCount() > 0;
        cursor.close();

        return exists;
    }

// ========== FOOD ITEM CRUD OPERATIONS ==========

    /**
     * Get all food items
     */
    public List<FoodItem> getAllFoodItems() {
        List<FoodItem> foodItems = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_FOOD_ITEMS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FoodItem item = new FoodItem(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getInt(5));
                foodItems.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return foodItems;
    }

    /**
     * Get food items by category
     */
    public List<FoodItem> getFoodItemsByCategory(String category) {
        List<FoodItem> foodItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FOOD_ITEMS,
                null,
                COL_FOOD_CATEGORY + "=?",
                new String[] { category },
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                FoodItem item = new FoodItem(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getInt(5));
                foodItems.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return foodItems;
    }

    /**
     * Get food item by ID
     */
    public FoodItem getFoodItemById(int foodId) {
        SQLiteDatabase db = this.getReadableDatabase();
        FoodItem item = null;

        Cursor cursor = db.query(TABLE_FOOD_ITEMS,
                null,
                COL_ID + "=?",
                new String[] { String.valueOf(foodId) },
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            item = new FoodItem(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getInt(5));
            cursor.close();
        }

        db.close();
        return item;
    }
}