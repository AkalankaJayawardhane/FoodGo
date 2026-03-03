package com.example.foodgo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.foodgo.models.FoodItem;
import com.example.foodgo.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.example.foodgo.models.CartItem;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FoodOrderDB";
    private static final int DATABASE_VERSION = 4;

    private static final String TABLE_USERS = "users";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";


    private static final String TABLE_FOOD_ITEMS = "food_items";
    private static final String COL_FOOD_NAME = "name";
    private static final String COL_FOOD_DESCRIPTION = "description";
    private static final String COL_FOOD_PRICE = "price";
    private static final String COL_FOOD_CATEGORY = "category";
    private static final String COL_FOOD_IMAGE = "image_resource";

    //Ake
    private static final String TABLE_CART = "cart_items";
    private static final String COL_CART_USER_ID = "user_id";
    private static final String COL_CART_FOOD_ID = "food_id";
    private static final String COL_CART_QUANTITY = "quantity";


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

        //Ake
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_CART_USER_ID + " INTEGER, "
                + COL_CART_FOOD_ID + " INTEGER, "
                + COL_CART_QUANTITY + " INTEGER"
                + ")";

        db.execSQL(CREATE_CART_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
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

    //Ake
    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM users WHERE email=?",
                new String[]{email});

        int userId = -1;

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return userId;
    }



    private void populateFoodItems(SQLiteDatabase db) {
        // Burger
        insertFoodItem(db, "Beef Burger", "Juicy beef patty with lettuce, tomato, and cheese", 600, "Burger",
                R.drawable.beef_burger);
        insertFoodItem(db, "Chicken Burger", "Crispy chicken with mayo and lettuce", 550, "Burger",
                R.drawable.chicken_burger);
        insertFoodItem(db, "Cheese Burger", "Double cheese with beef patty", 650, "Burger",
                R.drawable.cheese_burger);

        // Wings
        insertFoodItem(db, "BBQ Wings", "Smoky BBQ chicken wings", 500, "Wings", R.drawable.bbq_wings);
        insertFoodItem(db, "Buffalo Wings", "Spicy buffalo sauce wings", 530, "Wings",
                R.drawable.buffalo_wings);
        insertFoodItem(db, "Honey Glazed Wings", "Sweet honey glazed wings", 550, "Wings",
                R.drawable.honey_wings);

        // Pizza
        insertFoodItem(db, "Margarita Pizza", "Classic tomato and mozzarella", 1000, "Pizza",
                R.drawable.margarita_pizza);
        insertFoodItem(db, "Pepperoni Pizza", "Loaded with pepperoni slices", 1200, "Pizza",
                R.drawable.pepperoni_pizza);
        insertFoodItem(db, "Veggie Pizza", "Fresh vegetables and cheese", 950, "Pizza",
                R.drawable.veggie_pizza);
        insertFoodItem(db, "Supreme Pizza", "All toppings included", 1400, "Pizza", R.drawable.supreme_pizza);
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


    //Ake Add to Cart
    public void addToCart(int userId, int foodId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_CART +
                        " WHERE user_id=? AND food_id=?",
                new String[]{String.valueOf(userId), String.valueOf(foodId)});

        if (cursor.moveToFirst()) {
            int currentQty = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CART_QUANTITY));
            int newQty = currentQty + quantity;

            ContentValues values = new ContentValues();
            values.put(COL_CART_QUANTITY, newQty);

            db.update(TABLE_CART, values,
                    "user_id=? AND food_id=?",
                    new String[]{String.valueOf(userId), String.valueOf(foodId)});
        } else {
            ContentValues values = new ContentValues();
            values.put(COL_CART_USER_ID, userId);
            values.put(COL_CART_FOOD_ID, foodId);
            values.put(COL_CART_QUANTITY, quantity);

            db.insert(TABLE_CART, null, values);
        }

        cursor.close();
        db.close();
    }

    /// Get Cart Items with quantity
    // Get Cart Items (Correct Version)
    public List<CartItem> getCartItems(int userId) {

        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT c.id, c.user_id, c.quantity, " +
                "f.id, f.name, f.description, f.price, f.category, f.image_resource " +
                "FROM " + TABLE_CART + " c " +
                "INNER JOIN " + TABLE_FOOD_ITEMS + " f ON c.food_id = f.id " +
                "WHERE c.user_id=?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {

                int cartId = cursor.getInt(0);
                int uId = cursor.getInt(1);
                int quantity = cursor.getInt(2);

                // Create FoodItem object
                FoodItem foodItem = new FoodItem(
                        cursor.getInt(3),     // food id
                        cursor.getString(4),  // name
                        cursor.getString(5),  // description
                        cursor.getInt(6),     // price
                        cursor.getString(7),  // category
                        cursor.getInt(8)      // image
                );

                // Create CartItem using your constructor
                CartItem cartItem = new CartItem(cartId, uId, foodItem, quantity);

                cartItems.add(cartItem);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return cartItems;
    }

    //Cart Clear
    public void clearCart(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART,
                "user_id=?",
                new String[]{String.valueOf(userId)});
        db.close();
    }

    // Update Cart Quantity using cartId
    public int updateCartItemQuantity(int cartId, int quantity) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CART_QUANTITY, quantity);

        int rowsAffected = db.update(
                TABLE_CART,
                values,
                COL_ID + "=?",
                new String[]{String.valueOf(cartId)}
        );

        db.close();
        return rowsAffected;
    }

    // Remove item from cart using cartId
    public void removeFromCart(int cartId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART,
                COL_ID + "=?",
                new String[]{String.valueOf(cartId)});
        db.close();
    }

    // Get total cart price
    public int getCartTotal(int userId) {

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT SUM(f.price * c.quantity) " +
                "FROM " + TABLE_CART + " c " +
                "INNER JOIN " + TABLE_FOOD_ITEMS + " f ON c.food_id = f.id " +
                "WHERE c.user_id=?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        int total = 0;

        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return total;
    }


}