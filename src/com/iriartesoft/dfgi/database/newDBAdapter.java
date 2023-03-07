package com.iriartesoft.dfgi.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class newDBAdapter {

	//attribute of BD
	public static final String KEY_ROWID = "_id";
	public static final String KEY_PRODUCT = "_product";
	public static final String KEY_CANTITY = "_cantity";
	public static final String KEY_CHECKED = "_checked";
	private static final String DATABASE_TABLE = "tbproducts";
	//to create BD
	private static final String DATABASE_CREATE = "create table if not exists tbproducts " +
			"(_id integer primary key autoincrement, "
					+ "_product text not null, _cantity int not null, _checked boolean not null);";
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;

	public newDBAdapter() {
		}
	
	public newDBAdapter open(Context context) throws SQLException {
		
		dbHelper = new DatabaseHelper(context);
		database = dbHelper.getWritableDatabase();
        database.execSQL(DATABASE_CREATE);
        return this;
	}

	public void close() {
		
		database.close();
		
	}
	
	/**
	 * creates new task, if OK returns the
	 * rowId of task, else returns -1
	 */
	public long createProduct(String _product, int _cantity, boolean _checked) {
		ContentValues initialValues = createContentValues(_product, _cantity,
				_checked);

		return database.insert(DATABASE_TABLE, null, initialValues);
	}

	//update a product
	public boolean updateProduct(long rowId, String _product, int _cantity,
			boolean _checked) {
		ContentValues updateValues = createContentValues(_product, _cantity,
				_checked);

		return database.update(DATABASE_TABLE, updateValues, KEY_ROWID + "="
				+ rowId, null) > 0;
	}

	//delete a product
	public boolean deleteProduct(long rowId) {
		return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	//delete all products
	public boolean deleteAllProduct() {
		
		return database.delete(DATABASE_TABLE, null, null) > 0;
	}
		
	//Return a Cursor with all items
	public Cursor fetchAllProduct() {
		return database.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_PRODUCT, KEY_CANTITY, KEY_CHECKED }, null, null, null,
				null, null);
	} 

	//Return a Cursor with a item
	public Cursor fetchProduct(long rowId) throws SQLException {
		Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_PRODUCT, KEY_CANTITY, KEY_CHECKED },
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	private ContentValues createContentValues(String _product, int _cantity,
			boolean _checked) {
		ContentValues values = new ContentValues();
		values.put(KEY_PRODUCT, _product);
		values.put(KEY_CANTITY, _cantity);
		values.put(KEY_CHECKED, _checked);
		return values;
	}

	
}
