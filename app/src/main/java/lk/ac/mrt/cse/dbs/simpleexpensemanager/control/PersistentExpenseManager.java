package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DbAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DbTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

@SuppressLint("ParcelCreator")
public class PersistentExpenseManager extends ExpenseManager implements Serializable, Parcelable {
    private Context context;
    public PersistentExpenseManager(Context context) {
        this.context = context;
        setup();

    }

    public void setup() {
        /*** Setup the persistent storage implementation ***/

        TransactionDAO dbTransactionDAO = new DbTransactionDAO(context);
        setTransactionsDAO(dbTransactionDAO);

        AccountDAO dbAccountDAO = new DbAccountDAO(context);
        setAccountsDAO(dbAccountDAO);

        // dummy data
//        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
//        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
//        getAccountsDAO().addAccount(dummyAcct1);
//        getAccountsDAO().addAccount(dummyAcct2);

        /*** End ***/
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
