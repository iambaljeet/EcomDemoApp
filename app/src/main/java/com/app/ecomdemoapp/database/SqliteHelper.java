package com.app.ecomdemoapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.app.ecomdemoapp.models.CartModel;
import com.app.ecomdemoapp.models.ProductsModel;

import java.util.ArrayList;

public class SqliteHelper extends SQLiteOpenHelper {

    private static final String TAG = "SqliteHelper";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "ProductsDatabase";

    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_CART = "cart";

    private static final String PRODUCT_ID = "product_id";
    private static final String PRODUCT_NAME = "product_name";
    private static final String PRODUCT_CATEGORY = "product_category";
    private static final String PRODUCT_CURRENCY = "product_currency";
    private static final String PRODUCT_DESCRIPTION = "product_description";
    private static final String PRODUCT_DATE = "product_date";
    private static final String PRODUCT_PRICE = "product_price";
    private static final String PRODUCT_DISCOUNTED_PRICE = "product_discounted_price";

    private static final String CART_ID = "cart_id";
    private static final String CART_PRODUCTS_ID = "cart_products_id";

    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE "
            + TABLE_PRODUCTS + "(" + PRODUCT_ID + " INTEGER PRIMARY KEY," + PRODUCT_NAME
            + " TEXT," + PRODUCT_DESCRIPTION + " TEXT," + PRODUCT_CATEGORY
            + " TEXT," + PRODUCT_PRICE
            + " TEXT," + PRODUCT_CURRENCY
            + " TEXT," + PRODUCT_DATE
            + " TEXT," + PRODUCT_DISCOUNTED_PRICE + " TEXT" + ")";

    private static final String CREATE_TABLE_CART = "CREATE TABLE " + TABLE_CART
            + "(" + CART_ID + " INTEGER PRIMARY KEY," + CART_PRODUCTS_ID + " INTEGER" + ")";

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_CART);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    public long addNewProduct(ProductsModel productsModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PRODUCT_NAME, productsModel.getProduct_name());
        values.put(PRODUCT_CATEGORY, productsModel.getProduct_category());
        values.put(PRODUCT_CURRENCY, productsModel.getProduct_currency());
        values.put(PRODUCT_DESCRIPTION, productsModel.getProduct_description());
        values.put(PRODUCT_DATE, productsModel.getProduct_time());
        values.put(PRODUCT_DISCOUNTED_PRICE, productsModel.getProduct_discounted_price());
        values.put(PRODUCT_PRICE, productsModel.getProduct_price());

        return db.insert(TABLE_PRODUCTS, null, values);
    }

    public ProductsModel getProductById(long product_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS + " WHERE "
                + PRODUCT_ID + " = " + product_id;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        ProductsModel productsModel = new ProductsModel();
        if (cursor != null) {
            productsModel.setProduct_id(cursor.getInt(cursor.getColumnIndex(PRODUCT_ID)));
            productsModel.setProduct_name((cursor.getString(cursor.getColumnIndex(PRODUCT_NAME))));
            productsModel.setProduct_currency((cursor.getString(cursor.getColumnIndex(PRODUCT_CURRENCY))));
            productsModel.setProduct_category((cursor.getString(cursor.getColumnIndex(PRODUCT_CATEGORY))));
            productsModel.setProduct_description(cursor.getString(cursor.getColumnIndex(PRODUCT_DESCRIPTION)));
            productsModel.setProduct_time(cursor.getLong(cursor.getColumnIndex(PRODUCT_DATE)));
            productsModel.setProduct_discounted_price((cursor.getLong(cursor.getColumnIndex(PRODUCT_DISCOUNTED_PRICE))));
            productsModel.setProduct_price(cursor.getLong(cursor.getColumnIndex(PRODUCT_PRICE)));
            cursor.close();
        }
        return productsModel;
    }

    public ArrayList<ProductsModel> getAllProducts() {
        ArrayList<ProductsModel> productsModelArrayList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS + " ORDER BY " + PRODUCT_DATE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ProductsModel productsModel = new ProductsModel();
                productsModel.setProduct_id(cursor.getInt((cursor.getColumnIndex(PRODUCT_ID))));
                productsModel.setProduct_name((cursor.getString(cursor.getColumnIndex(PRODUCT_NAME))));
                productsModel.setProduct_currency((cursor.getString(cursor.getColumnIndex(PRODUCT_CURRENCY))));
                productsModel.setProduct_category((cursor.getString(cursor.getColumnIndex(PRODUCT_CATEGORY))));
                productsModel.setProduct_description(cursor.getString(cursor.getColumnIndex(PRODUCT_DESCRIPTION)));
                productsModel.setProduct_time(cursor.getLong(cursor.getColumnIndex(PRODUCT_DATE)));
                productsModel.setProduct_discounted_price((cursor.getLong(cursor.getColumnIndex(PRODUCT_DISCOUNTED_PRICE))));
                productsModel.setProduct_price(cursor.getLong(cursor.getColumnIndex(PRODUCT_PRICE)));

                productsModelArrayList.add(productsModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productsModelArrayList;
    }


    //Cart
    public long addProductToCart(int product_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CART_PRODUCTS_ID, product_id);

        return db.insert(TABLE_CART, null, values);
    }

    public ArrayList<CartModel> getCartItems() {
        ArrayList<CartModel> cartModelArrayList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_CART;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CartModel cartModel = new CartModel();
                cartModel.setCart_id(cursor.getInt((cursor.getColumnIndex(CART_ID))));
                cartModel.setProduct_id((cursor.getInt(cursor.getColumnIndex(CART_PRODUCTS_ID))));

                cartModelArrayList.add(cartModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartModelArrayList;
    }

    public long removeProductFromCart(int product_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_CART, CART_PRODUCTS_ID + " = ?",
                new String[] { String.valueOf(product_id) });
    }
}
