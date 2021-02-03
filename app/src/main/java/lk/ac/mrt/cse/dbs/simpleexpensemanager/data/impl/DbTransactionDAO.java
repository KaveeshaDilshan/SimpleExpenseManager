package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;
import java.io.Serializable;
import java.util.Date;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DbHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

import android.content.Context;

import android.os.Build;
import android.support.annotation.RequiresApi;


public class DbTransactionDAO implements TransactionDAO {
    private Context context;
    private DbHandler dbHandler;

    public DbTransactionDAO(Context context) {
        this.context = context;
        this.dbHandler = new DbHandler(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        dbHandler.addTransaction(transaction);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
         return dbHandler.getAllTransactionLogs();
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = dbHandler.getAllTransactionLogs();
        int size = transactions.size();
        if (size <= limit) {
           return transactions;
        }

        return transactions.subList(size - limit, size);
    }
}
