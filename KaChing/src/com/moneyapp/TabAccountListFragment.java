package com.moneyapp;

import java.util.ArrayList;
import java.util.List;

import com.kaching.R;
import com.moneyapp.database.Account;
import com.moneyapp.database.MoneyAppDatabaseHelper;

import android.os.Bundle;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class TabAccountListFragment extends ListFragment{
	
   class Accounts {
        private String accountDescription;
        private Float accountBalance;

        public String getDescription() {
            return accountDescription;
        }

        public void setDescription(String description) {
            accountDescription = description;
        }

        public Float getBalance() {
            return accountBalance;
        }

        public void setBalance(Float balance) {
            accountBalance = balance;
        }

        public Accounts(String description, Float balance) {
            accountDescription = description;
            accountBalance = balance;
        }
    }
   
   public class AccountAdapter extends ArrayAdapter<Accounts> {
       private ArrayList<Accounts> items;
       private AccountViewHolder accountHolder;

       private class AccountViewHolder {
           TextView description;
           TextView balance; 
       }

       public AccountAdapter(Context context, int tvResId, ArrayList<Accounts> items) {
           super(context, tvResId, items);
           this.items = items;
       }

       @Override
       public View getView(int pos, View convertView, ViewGroup parent) {
           View v = convertView;
           if (v == null) {
               LayoutInflater vi = (LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               v = vi.inflate(R.layout.account_list_item, null);
               accountHolder = new AccountViewHolder();
               accountHolder.description = (TextView)v.findViewById(R.id.account_description);
               accountHolder.balance = (TextView)v.findViewById(R.id.account_balance);
               v.setTag(accountHolder);
           } else accountHolder = (AccountViewHolder)v.getTag(); 

           Accounts  account = items.get(pos);

           if (account != null) {
        	   if (account.getBalance() < 0)
        		   accountHolder.balance.setTextColor(Color.RED);
        	   else
        		   accountHolder.balance.setTextColor(getResources().getColor(R.color.DarkGreen));
        		
        	   accountHolder.balance.setText(Float.toString(account.getBalance()));
        	   accountHolder.description.setText(account.getDescription());
           }

           return v;
       }
   }

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		/*
		Bundle data = getArguments();
		int index = data.getInt("idx");
		*/
				
        // Reading all accounts
        MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(null);
        List<Account> accounts = db.getAllAccounts();    
        ArrayList<Accounts> AccountList = new ArrayList<Accounts>();
         
        for (Account account : accounts) {
        	AccountList.add(new Accounts(account.getDescription(),account.getStartingBalance()));
        }
        
        ListAdapter listAdapter = new AccountAdapter(getActivity(), R.layout.account_list_item, AccountList);
        //setListAdapter(new AccountAdapter(this, R.layout.account_list_item, AccountList));  
        
		//ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.account_list_item, AccountList);
		setListAdapter(listAdapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.account_fragment, container, false);
		/*
		View v = inflater.inflate(R.layout.account_fragment, null);
		TextView tv = (TextView) v.findViewById(R.id.msg);
		tv.setText("Fragment " + (index + 1));
		
		return v;
		*/
	}
}
