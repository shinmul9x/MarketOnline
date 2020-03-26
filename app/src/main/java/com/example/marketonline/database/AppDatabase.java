package com.example.marketonline.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.marketonline.utils.AppConstants;
import com.example.marketonline.utils.AppUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class AppDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_TAME = "MARKET ONLINE";
    private static final String TABLE_USER = "USER";
    private static final String ID_USER = "ID";
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String AVATAR = "AVATAR";
    private static final String PHONE = "PHONENUMBER";
    private static final String NAME = "NAME";

    private static final String TABLE_PRODUCT = "PRODUCT";
    private static final String ID_PRODUCT = "ID";
    private static final String NAME_PRODUCT = "NAME";
    private static final String IMAGE = "IMAGE";
    private static final String COST = "COST";
    private static final String STATUS = "STATUS";
    private static final String USER_ID = "USER_ID";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String ADDRESS = "ADDRESS";

    private static AppDatabase mDatabase;
    private Context mContext;

    private AppDatabase(Context context) {
        super(context, DATABASE_TAME, null, 1);
        mContext = context;
    }

    public synchronized static void createDatabase(Context context) {
        if (mDatabase == null && context != null) {
            mDatabase = new AppDatabase(context);
        }
    }

    public synchronized static AppDatabase getDatabase() {
        return mDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE " + TABLE_USER + " (" +
                ID_USER + " integer primary key, " +
                USERNAME + " TEXT," +
                PASSWORD + " TEXT," +
                AVATAR + " TEXT," +
                PHONE + " TEXT," +
                NAME + " TEXT)";

        String sqlQuery2 = "CREATE TABLE " + TABLE_PRODUCT + "(" +
                ID_PRODUCT + " integer primary key, " +
                NAME_PRODUCT + " TEXT," +
                IMAGE + " TEXT," +
                COST + " integer," +
                STATUS + " TEXT," +
                USER_ID + " integer," +
                DESCRIPTION + " TEXT," +
                ADDRESS + " TEXT)";

        db.execSQL(sqlQuery);
        db.execSQL(sqlQuery2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        onCreate(db);
    }

    public boolean addUser(User user) {
        if (getUserId(user.getUserName(), user.getPassword()) != Integer.valueOf(AppConstants.NON_USER_ID)) {
            return false;  // user id != 0 -> user exist -> don't add user
        }
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERNAME, user.getUserName());
        contentValues.put(PASSWORD, user.getPassword());
        contentValues.put(AVATAR, user.getAvatar());
        contentValues.put(PHONE, user.getPhoneNumber());
        contentValues.put(NAME, user.getName());
        sqLiteDatabase.insert(TABLE_USER, null, contentValues);
        sqLiteDatabase.close();
        return true;
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> listUsers = new ArrayList<>();

        String sqlQuery = "SELECT * FROM " + TABLE_USER;
        AppUtils.log(mContext, sqlQuery);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String userName = cursor.getString(1);
                String password = cursor.getString(2);
                String avatar = cursor.getString(3);
                String phone = cursor.getString(4);
                String name = cursor.getString(5);
                listUsers.add(new User(id, userName, password, avatar, phone, name));
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return listUsers;
    }

    public User getUser(int userId) {
        User user = null;
        String sqlQuery = "SELECT * FROM " + TABLE_USER + " WHERE " + ID_USER + "=" + userId;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlQuery, null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String userName = cursor.getString(1);
            String password = cursor.getString(2);
            String avatar = cursor.getString(3);
            String phone = cursor.getString(4);
            String name = cursor.getString(5);
            user = new User(id, userName, password, avatar, phone, name);
        }
        cursor.close();
        sqLiteDatabase.close();
        return user;
    }

    public String getNameUser(int userId) {
        String name = "";
        String sqlQuery = "SELECT " + NAME + " FROM " + TABLE_USER + " WHERE " + ID_USER + "=" + userId;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlQuery, null);
        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }
        cursor.close();
        sqLiteDatabase.close();
        return name;
    }

    public int getUserId(String userName, String password) {
        int id = Integer.valueOf(AppConstants.NON_USER_ID);
        String sqlQuery = "SELECT " + ID_USER + " FROM " + TABLE_USER
                + " WHERE " + USERNAME + "=\'" + userName + "\' AND " + PASSWORD + "=\'" + password + "\'";
//        AppUtils.log(mContext, sqlQuery);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlQuery, null);
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        AppUtils.log(mContext, id + "");
        return id;
    }

    public void updateUser(User user) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERNAME, user.getUserName());
        contentValues.put(PASSWORD, user.getPassword());
        contentValues.put(AVATAR, user.getAvatar());
        contentValues.put(PHONE, user.getPhoneNumber());
        contentValues.put(NAME, user.getName());

        sqLiteDatabase.update(TABLE_USER, contentValues, ID_USER + " = ?"
                , new String[]{String.valueOf(user.getId())});
        sqLiteDatabase.close();
    }

    public void deleteUser(int userId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_USER, ID_USER + " = ?"
                , new String[]{String.valueOf(userId)});
        sqLiteDatabase.close();
    }

    public void addProduct(Product product) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_PRODUCT, product.getProductName());
        contentValues.put(IMAGE, getImages(product.getImages()));
        contentValues.put(COST, product.getCost());
        contentValues.put(STATUS, product.getStatus());
        contentValues.put(USER_ID, product.getUserId());
        contentValues.put(DESCRIPTION, product.getDescription());
        contentValues.put(ADDRESS, product.getAddress());
        sqLiteDatabase.insert(TABLE_PRODUCT, null, contentValues);
        sqLiteDatabase.close();
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> listProducts = new ArrayList<>();

        String sqlQuery = "SELECT * FROM " + TABLE_PRODUCT;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String productName = cursor.getString(1);
                String images = cursor.getString(2);
                int cost = cursor.getInt(3);
                String status = cursor.getString(4);
                int userId = cursor.getInt(5);
                String description = cursor.getString(6);
                String address = cursor.getString(7);
                listProducts.add(new Product(id, productName, getImages(images), cost, status, userId, description, address));
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        return listProducts;
    }

    public ArrayList<Product> getAllProductsExcept(int userid) {
        ArrayList<Product> listProducts = new ArrayList<>();

//        String sqlQuery = "SELECT * FROM " + TABLE_PRODUCT + " WHERE " + ID_PRODUCT + ">" + productId + " OR " + ID_PRODUCT + "<" + productId;
        String sqlQuery = "SELECT * FROM " + TABLE_PRODUCT;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String productName = cursor.getString(1);
                String images = cursor.getString(2);
                int cost = cursor.getInt(3);
                String status = cursor.getString(4);
                int userId = cursor.getInt(5);
                if (userId == userid) continue;
                String description = cursor.getString(6);
                String address = cursor.getString(7);
                listProducts.add(new Product(id, productName, getImages(images), cost, status, userId, description, address));
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        return listProducts;
    }

    public ArrayList<Product> getAllProducts(String content) {
        ArrayList<Product> listProducts = new ArrayList<>();

        String sqlQuery = "SELECT * FROM " + TABLE_PRODUCT + " WHERE " + NAME_PRODUCT + " LIKE \'%" + content + "%\'";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String productName = cursor.getString(1);
                String images = cursor.getString(2);
                int cost = cursor.getInt(3);
                String status = cursor.getString(4);
                int userId = cursor.getInt(5);
                String description = cursor.getString(6);
                String address = cursor.getString(7);
                listProducts.add(new Product(id, productName, getImages(images), cost, status, userId, description, address));
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        return listProducts;
    }

    public ArrayList<Product> getAllProducts(int userId) {
        ArrayList<Product> listProducts = new ArrayList<>();

        String sqlQuery = "SELECT * FROM " + TABLE_PRODUCT + " WHERE " + USER_ID + "=" + userId;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String productName = cursor.getString(1);
                String images = cursor.getString(2);
                int cost = cursor.getInt(3);
                String status = cursor.getString(4);
                String description = cursor.getString(6);
                String address = cursor.getString(7);
                listProducts.add(new Product(id, productName, getImages(images), cost, status, userId, description, address));
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        return listProducts;
    }

    public Product getProduct(int productId) {
        Product product = null;

        String sqlQuery = "SELECT * FROM " + TABLE_PRODUCT + " WHERE " + ID_PRODUCT + "=" + productId;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlQuery, null);
        if (cursor.moveToFirst()) {
            String productName = cursor.getString(1);
            String images = cursor.getString(2);
            int cost = cursor.getInt(3);
            String status = cursor.getString(4);
            int userId = cursor.getInt(5);
            String description = cursor.getString(6);
            String address = cursor.getString(7);
            product = new Product(productId, productName, getImages(images), cost, status, userId, description, address);
        }
        cursor.close();
        sqLiteDatabase.close();

        return product;
    }

    public void updateProduct(Product product) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_PRODUCT, product.getProductName());
        contentValues.put(IMAGE, getImages(product.getImages()));
        contentValues.put(COST, product.getCost());
        contentValues.put(STATUS, product.getStatus());
        contentValues.put(USER_ID, product.getUserId());
        contentValues.put(DESCRIPTION, product.getDescription());
        contentValues.put(ADDRESS, product.getAddress());

        sqLiteDatabase.update(TABLE_PRODUCT, contentValues, ID_PRODUCT + " = ?"
                , new String[]{String.valueOf(product.getId())});
        sqLiteDatabase.close();
    }

    public void deleteProduct(int productId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_PRODUCT, ID_PRODUCT + " = ?"
                , new String[]{String.valueOf(productId)});
        sqLiteDatabase.close();
    }

    public void delete() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_PRODUCT, "", new String[]{});
        sqLiteDatabase.delete(TABLE_USER, "", new String[]{});
    }

    private String getImages(ArrayList<String> images) {
        if (images.size() == 0) return "";
        StringBuilder content = new StringBuilder(images.get(0));
        for (int i = 1; i < images.size(); ++i) {
            content.append(";");
            content.append(images.get(i));
        }
        return content.toString();
    }

    private ArrayList<String> getImages(String images) {
        return new ArrayList<>(Arrays.asList(images.split(";")));
    }
}
