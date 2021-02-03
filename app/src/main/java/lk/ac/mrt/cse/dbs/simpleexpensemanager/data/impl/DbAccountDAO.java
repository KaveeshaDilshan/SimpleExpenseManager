package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;


import android.content.Context;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DbHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;


public class DbAccountDAO implements AccountDAO {
    private Context context;
    private DbHandler dbHandler;

    public DbAccountDAO(Context context) {
        this.context = context;
        this.dbHandler = new DbHandler(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        return dbHandler.getAccountNumbers();
    }

    @Override
    public List<Account> getAccountsList() {
         return dbHandler.getAllAccounts();
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account = dbHandler.getAccount(accountNo);
        if (account != null) {
            return account;
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) throws InvalidAccountException {
        Account accnt = dbHandler.getAccount(account.getAccountNo());
        if (accnt != null) {
            String msg = "This Account is already added.";
            throw new InvalidAccountException(msg);
        }
        dbHandler.addAccount(account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        int state = dbHandler.deleteAccount(accountNo);
        if (state < 1) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        if(accountNo ==null){
            throw new InvalidAccountException("Invalid Account Number");

        }
       Account account = getAccount(accountNo);
        if (account == null) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        // specific implementation based on the transaction type
        double balance = account.getBalance();
        switch (expenseType) {
            case EXPENSE:

                if((balance - amount)<0 ){
                    throw new InvalidAccountException("Insufficient credit");
                }

                account.setBalance(balance - amount);

                break;
            case INCOME:
                account.setBalance(balance + amount);
                break;
        }
        dbHandler.updateBalance(account);
    }
}
