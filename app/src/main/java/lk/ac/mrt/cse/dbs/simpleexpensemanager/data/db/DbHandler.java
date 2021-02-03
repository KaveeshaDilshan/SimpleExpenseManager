package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;


import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DbHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "180148J";
    private static final String TABLE_NAME_ACCOUNT = "account";
    private static final String TABLE_NAME_TRANSACTION = "transction";

    // Column names for account

    private static final String ACCOUNT_NO = "accountNo";
    private static final String BANK_NAME = "bankName";
    private static final String ACCOUNT_HOLDER_NAME = "accountHolderName";
    private static final String BALANCE = "balance";

    // Column names for transaction
    private static final String TRANSACTION_NO = "transactionNo";
    private static final String DATE = "date";
//    private static final String ACCOUNT_NO = "accountNo";
    private static final String TYPE = "type";
    private static final String AMOUNT = "amount";

    public DbHandler(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String TABLE_CREATE_QUERY1 = "CREATE TABLE "+TABLE_NAME_ACCOUNT+" " +
                "("
                +ACCOUNT_NO+" TEXT PRIMARY KEY,"
                +BANK_NAME + " TEXT,"
                +ACCOUNT_HOLDER_NAME + " TEXT,"
                +BALANCE+" NUMERIC" +
                ");";


        String TABLE_CREATE_QUERY2 = "CREATE TABLE "+TABLE_NAME_TRANSACTION+" " +
                "("
                +TRANSACTION_NO+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +DATE + " DATETIME,"
                +ACCOUNT_NO + " TEXT,"
                +TYPE+ " TEXT CHECK( "+ TYPE +" IN ('EXPENSE','INCOME') ),"
                +AMOUNT+" NUMERIC,"
                +"FOREIGN KEY (" + ACCOUNT_NO + ") REFERENCES " + TABLE_NAME_ACCOUNT +"(" +ACCOUNT_NO + ") "
                +"ON DELETE CASCADE ON UPDATE CASCADE"
                +");";


        db.execSQL(TABLE_CREATE_QUERY1);
        db.execSQL(TABLE_CREATE_QUERY2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DROP_TABLE_QUERY1 = "DROP TABLE IF EXISTS "+ TABLE_NAME_ACCOUNT;
        String DROP_TABLE_QUERY2 = "DROP TABLE IF EXISTS "+ TABLE_NAME_TRANSACTION;
        // Drop older table if existed
        db.execSQL(DROP_TABLE_QUERY1);
        db.execSQL(DROP_TABLE_QUERY2);
        // Create tables again
        onCreate(db);
    }


    public List<Account> getAllAccounts() {

        List<Account> accounts = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME_ACCOUNT;

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do {
                // Create new Account object
                Account account = new Account();
                // mmgby6hh
                account.setAccountNo(cursor.getString(0));
                account.setBankName(cursor.getString(1));
                account.setAccountHolderName(cursor.getString(2));
                account.setBalance(cursor.getDouble(3));

                //toDos [obj,objs,asas,asa]
                accounts.add(account);
            }while (cursor.moveToNext());
        }
        return accounts;

    }


    public List<String> getAccountNumbers() {

        List<String> accountNums = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + ACCOUNT_NO  +" FROM "+TABLE_NAME_ACCOUNT;

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do {
                accountNums.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        return accountNums;

    }

    public Account getAccount(String accountNo){
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME_ACCOUNT,new String[]{ACCOUNT_NO,BANK_NAME,ACCOUNT_HOLDER_NAME,BALANCE},
                ACCOUNT_NO + "= ?",new String[]{String.valueOf(accountNo)}
                ,null,null,null);
        Account account;
        if(cursor.moveToFirst()){
            account = new Account(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getDouble(3)
            );
            return account;
        }
        return null;
    }

    public long addAccount(Account account){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ACCOUNT_NO,account.getAccountNo());
        contentValues.put(BANK_NAME, account.getBankName());
        contentValues.put(ACCOUNT_HOLDER_NAME,account.getAccountHolderName());
        contentValues.put(BALANCE,account.getBalance());

        //save to table
        long state = sqLiteDatabase.insert(TABLE_NAME_ACCOUNT,null,contentValues);
        // close database
        sqLiteDatabase.close();
        return state;
    }

    public int deleteAccount(String accountNo){
        SQLiteDatabase db = getWritableDatabase();
        int state = db.delete(TABLE_NAME_ACCOUNT,ACCOUNT_NO +"=?",new String[]{String.valueOf(accountNo)});
        db.close();
        return state;
    }

    public int updateBalance(Account account){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ACCOUNT_NO,account.getAccountNo());
        contentValues.put(BANK_NAME, account.getBankName());
        contentValues.put(ACCOUNT_HOLDER_NAME,account.getAccountHolderName());
        contentValues.put(BALANCE,account.getBalance());

        int status = db.update(TABLE_NAME_ACCOUNT,contentValues,ACCOUNT_NO +" =?",
                new String[]{String.valueOf(account.getAccountNo())});

        db.close();
        return status;
    }


//    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addTransaction(Transaction transaction ){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        contentValues.put(DATE, transaction.getDate().getTime());
//        System.out.println(transaction.getDate());
//        System.out.println(dateFormat.format(transaction.getDate()));
//        contentValues.put(DATE, transaction.getDate());
        contentValues.put(ACCOUNT_NO,transaction.getAccountNo());
        contentValues.put(TYPE,String.valueOf(transaction.getExpenseType()));
        contentValues.put(AMOUNT,transaction.getAmount());

        //save to table
        sqLiteDatabase.insert(TABLE_NAME_TRANSACTION,null,contentValues);
        // close database
        sqLiteDatabase.close();
    }


    public List<Transaction> getAllTransactionLogs() {

        List<Transaction> transactions = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME_TRANSACTION;

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do {
                // Create new transaction object
                Transaction transaction = new Transaction();
                // mmgby6hh
                transaction.setDate(new Date(cursor.getLong(1)));
                transaction.setAccountNo(cursor.getString(2));
                transaction.setExpenseType(ExpenseType.valueOf((cursor.getString(3)).toUpperCase()));
                transaction.setAmount(cursor.getDouble(4));

                //toDos [obj,objs,asas,asa]
                transactions.add(transaction);
            }while (cursor.moveToNext());
        }
        return transactions;

    }



}
