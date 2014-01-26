package com.moneyapp;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.kaching.R;
import com.moneyapp.database.TableAccount;
import com.moneyapp.database.MoneyAppDatabaseHelper;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

public class TabAccountListFragment extends SherlockListFragment {
   
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

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
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
	       List<TableAccount> accounts = db.getAllAccounts();    
	       ArrayList<Accounts> AccountList = new ArrayList<Accounts>();
	        
	       for (TableAccount account : accounts) {
	       	AccountList.add(new Accounts(account.getId(),account.getDescription(),account.getStartingBalance(),account.getType()));
	       }
	       
	       ListAdapter listAdapter = new AccountAdapter(getActivity(), R.layout.account_list_item, AccountList);
	       
	       setListAdapter(listAdapter);
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    // Inflate the menu items for use in the action bar
	    inflater.inflate(R.menu.activity_account, menu);
	    super.onCreateOptionsMenu(menu, inflater);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
        case R.id.account_add1:
    		Intent i = new Intent(getActivity().getBaseContext().getApplicationContext(), AccountAddActivity.class);
    		startActivity(i);
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
		
	    ListView v = new ListView(getActivity().getBaseContext().getApplicationContext());
	    v = getListView();
	    
	    v.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
            	final AccountViewHolder accountHolder = (AccountViewHolder) view.getTag();
            	
            	PopupMenu popupMenu = new PopupMenu(getActivity().getApplicationContext(),view);
				
				popupMenu.getMenuInflater().inflate(R.menu.menu_account,popupMenu.getMenu());
				popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
						
							@Override
							public boolean onMenuItemClick(android.view.MenuItem arg0) {
								
								if (arg0.getTitle().equals(getResources().getString(R.string.cnt_menu_edit))) {
						    		Intent i = new Intent(getActivity().getBaseContext().getApplicationContext(), AccountEditActivity.class);
						    		i.putExtra("account_id",accountHolder.id);
						    		startActivity(i);
						        	return true;
								} else if (arg0.getTitle().equals(getResources().getString(R.string.cnt_menu_delete))) {
									DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
									    @Override
									    public void onClick(DialogInterface dialog, int which) {
									        switch (which){
									        case DialogInterface.BUTTON_POSITIVE:
									        	MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(getActivity());
									        	TableAccount account = new TableAccount(accountHolder.id);
												
									        	db.deleteTransactionAccount(account);
												db.deleteAccount(account);
												
												SetAccountView();
									        	db.close();
									            break;
									        case DialogInterface.BUTTON_NEGATIVE:
									            //No button clicked
									            break;
									        }
									    }
									};
									
									AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
									builder.setMessage("Do you want to delete " + accountHolder.description.getText().toString() + 
											" account? All transaction linked to this account will be deleted.").setPositiveButton("Yes", dialogClickListener)
									    .setNegativeButton("No", dialogClickListener).show();
									return true;
								} 

								return false;
							}
						});
				popupMenu.show();
            }
        });
	    
    }
}
