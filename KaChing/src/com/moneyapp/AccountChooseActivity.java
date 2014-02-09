package com.moneyapp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.kaching.R;
import com.moneyapp.TabAccountListFragment.AccountAdapter;
import com.moneyapp.database.MoneyAppDatabaseHelper;
import com.moneyapp.database.TableAccount;

public class AccountChooseActivity extends SherlockActivity {
	ListView listView;
	ImageButton buttonAdd;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.account_chose_activity);		
		
		listView = (ListView) findViewById(R.id.accountList);
		
		SetAccountView();
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
	               LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	               v = vi.inflate(R.layout.account_list_item, null);
	               accountHolder = new AccountViewHolder();
	               accountHolder.id = account.getAccountId();
	               accountHolder.description = (TextView)v.findViewById(R.id.account_description);
	               accountHolder.balance = (TextView)v.findViewById(R.id.account_balance);
	               accountHolder.image = (ImageView)v.findViewById(R.id.account_image);
	               v.setTag(accountHolder);
	           } else accountHolder = (AccountViewHolder)v.getTag(); 
	
	           if (account != null) {
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
	
	           return v;
	       }
	   }

	private void addListenerOnButtonNew() {
		buttonAdd = (ImageButton) findViewById(R.id.buttonAdd);
		
		buttonAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) { 
				Intent i = new Intent(getApplicationContext(), AccountAddActivity.class);
				startActivity(i);
			}
		});
	}	
	
	@Override	
	public void onResume()
	{
	   super.onResume();
	   
	   SetAccountView();
	}

	private void SetAccountView() {
	       // Reading all accounts
	     MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(null);
	     List<TableAccount> accounts = db.getAllAccounts();    
	     ArrayList<Accounts> AccountList = new ArrayList<Accounts>();
	        
	     for (TableAccount account : accounts) {
	     	AccountList.add(new Accounts(account.getId(),account.getDescription(),account.getStartingBalance(),account.getType()));
	     }
	     
	     db.close();
	       
	     ListAdapter listAdapter = new AccountAdapter(this, R.layout.account_list_item, AccountList);
	      
	     listView.setAdapter(listAdapter);

		addListenerOnButtonNew();	
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
         public void onItemClick(AdapterView<?> parent, View view, int position,
                 long id) {
				final AccountViewHolder accountHolder = (AccountViewHolder) view.getTag();
				
				Intent resultData = new Intent();
				resultData.putExtra("accountId", accountHolder.id);
				setResult(SherlockActivity.RESULT_OK, resultData);
				finish();
			}
		});
	}
}
