package com.moneyapp;

import java.util.ArrayList;
import java.util.List;

import com.kaching.R;
import com.moneyapp.database.Account;
import com.moneyapp.database.MoneyAppDatabaseHelper;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TabAccountListFragment extends ListFragment{
	
   class Accounts {
	   private Integer accountId;
        private String accountDescription;
        private Float accountBalance;
        private Integer accountImage;

        public Integer getAccountId() {
        	return accountId;
        }
       
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
        
        public void setImage(Integer imageNo) {
        	accountImage = imageNo;
        }
        
        public Integer getImage() {
        	return accountImage;
        }

        public Accounts(Integer id, String description, Float balance, Integer image) {
        	accountId = id;
            accountDescription = description;
            accountBalance = balance;
            accountImage = image;
        }
    }

   private class AccountViewHolder {
	   Integer id;
       TextView description;
       TextView balance; 
       ImageView image;
   }
   
   public class AccountAdapter extends ArrayAdapter<Accounts> {
       private ArrayList<Accounts> items;
       private AccountViewHolder accountHolder;

       public AccountAdapter(Context context, int tvResId, ArrayList<Accounts> items) {
           super(context, tvResId, items);
           this.items = items;
       }

       @Override
       public View getView(int pos, View convertView, ViewGroup parent) {
           View v = convertView;
           
           Accounts  account = items.get(pos);
           
           if (v == null) {
               LayoutInflater vi = (LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               v = vi.inflate(R.layout.account_list_item, null);
               accountHolder = new AccountViewHolder();
               accountHolder.id = account.getAccountId();
               accountHolder.description = (TextView)v.findViewById(R.id.account_description);
               accountHolder.balance = (TextView)v.findViewById(R.id.account_balance);
               accountHolder.image = (ImageView)v.findViewById(R.id.account_image);
               v.setTag(accountHolder);
           } else accountHolder = (AccountViewHolder)v.getTag(); 

           if (account != null) {
        	   if (account.getImage() == 0) {
        		   accountHolder.description.setText(R.string.account_add);       		 
        		   accountHolder.balance.setText(""); 
        		   accountHolder.image.setImageResource(R.drawable.plus);
        	   } else {
            	   if (account.getBalance() < 0)
            		   accountHolder.balance.setTextColor(Color.RED);
            	   else
            		   accountHolder.balance.setTextColor(getResources().getColor(R.color.DarkGreen));
            		
            	   accountHolder.balance.setText(Float.toString(account.getBalance()));
            	   accountHolder.description.setText(account.getDescription());
            	   
            	   if (account.getImage() == 1) {
            		   accountHolder.image.setImageResource(R.drawable.cash);
            	   } else if (account.getImage() == 2){
            		   accountHolder.image.setImageResource(R.drawable.bank);
            	   } else if (account.getImage() == 3) {
            		   accountHolder.image.setImageResource(R.drawable.credit_card);            		   
            	   }
        	   }
           }

           return v;
       }
   }

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);

		SetAccountView();	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.account_fragment, container,false);
		
		return v;		
	}
	
	@Override	
	public void onResume()
	{
	   super.onResume();
	   
	   SetAccountView();
	}
	
	public void SetAccountView() {
	       // Reading all accounts
	       MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(null);
	       List<Account> accounts = db.getAllAccounts();    
	       ArrayList<Accounts> AccountList = new ArrayList<Accounts>();
	        
	       for (Account account : accounts) {
	       	AccountList.add(new Accounts(account.getId(),account.getDescription(),account.getStartingBalance(),account.getType()));
	       }
	       
	       // Adding one last item which is used for inserting
	       AccountList.add(new Accounts(null,null,null,0));
	       
	       ListAdapter listAdapter = new AccountAdapter(getActivity(), R.layout.account_list_item, AccountList);
	       
			setListAdapter(listAdapter);
	}
	
	@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
		
	    ListView v = new ListView(getActivity().getBaseContext().getApplicationContext());
	    v = getListView();
	    v.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
            	TextView c = (TextView) view.findViewById(R.id.account_description);
            	
            	AccountViewHolder accountHolder = (AccountViewHolder) view.getTag();
            	if (c.getText().toString().equals(getResources().getString(R.string.account_add)))
            	{
            		//Add new account is clicked
            		startActivity(new Intent(getActivity().getBaseContext().getApplicationContext(),AccountAddActivity.class));
            	} else {
            		//An account is clicked, a view/edit opens
            		Intent i = new Intent(getActivity().getBaseContext().getApplicationContext(), AccountEditActivity.class);
            		i.putExtra("account_id",accountHolder.id);
            		startActivity(i);
            	}
            }
        });
	    
    }
}
