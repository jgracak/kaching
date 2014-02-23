package com.moneyapp;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.kaching.R;
import com.moneyapp.database.MoneyAppDatabaseHelper;
import com.moneyapp.database.TableAccount;
import com.moneyapp.database.TableCategory;
import com.moneyapp.database.TableImage;
import com.moneyapp.database.TableTransaction;
 
public class TransactionsFragment extends SherlockListFragment {

	public static final Integer FIRST_TRANSACTION_IN_GROUP = 1;
	public static final Integer COPY_MODE = 1;
	public static final Integer EDIT_MODE = 0;
	
	private static final int ID_EDIT     = 1;
	private static final int ID_COPY   = 2;
	private static final int ID_DELETE = 3;
	
	TextView textViewBalance;
	
	class Transactions {
		private Integer transactionId;
	    private Time transDate;
	    private Integer categoryId;
	    private Float amount;
	    private Integer accountId;

	    public Integer getTransactionId() {
	    	return transactionId;
	    }
	   
	    public Time getTransDate() {
	        return transDate;
	    }
	    
	    public void setTransDate(Time tDate) {
	    	transDate = tDate;
	    }
	
	    public Integer getCategoryId() {
	        return categoryId;
	    }
	
	    public void setCategoryId(Integer catId) {
	    	categoryId = catId;
	    }
	    
	    public Float getAmount() {
	    	return amount;
	    }
	    
	    public void setAmount(Float amt) {
	    	amount = amt;
	    }
	    
	    public Integer getAccountId() {
	    	return accountId;
	    }
	    
	    public void setAccountId(Integer accId) {
	    	accountId = accId;
	    }	
	
	    public Transactions(Integer id, Time date, Integer catId, Float amt, Integer accId) {
	    	transactionId = id;
	    	transDate = date;
	    	categoryId = catId;
	    	amount = amt;
	    	accountId = accId; 
	    }
	}

	private class TransactionGroupViewDateHolder {
		TextView groupDate;
		TextView groupBalance;
   }
	
	public class TransactionAdapter extends ArrayAdapter<Long> {
		private ArrayList<Long> items;
		private TransactionGroupViewDateHolder transactionHolder;

		public TransactionAdapter(Context context, int tvResId, ArrayList<Long> items) {
           super(context, tvResId, items);
           this.items = items;
       }
    	  
		private class MyOnClickListener implements OnClickListener
       {
         @Override
         public void onClick(View view) {
            //Do what needs to be done.
        	final TableTransaction transaction = (TableTransaction)view.getTag();
        	
        	PopupMenu popupMenu = new PopupMenu(getActivity().getApplicationContext(),view);

        	try {
        	    Field[] fields = popupMenu.getClass().getDeclaredFields();
        	    for (Field field : fields) {
        	        if ("mPopup".equals(field.getName())) {
        	            field.setAccessible(true);
        	            Object menuPopupHelper = field.get(popupMenu);
        	            Class<?> classPopupHelper = Class.forName(menuPopupHelper
        	                    .getClass().getName());
        	            Method setForceIcons = classPopupHelper.getMethod(
        	                    "setForceShowIcon", boolean.class);
        	            setForceIcons.invoke(menuPopupHelper, true);
        	            break;
        	        }
        	    }
        	} catch (Exception e) {
        	    e.printStackTrace();
        	}
        	
			popupMenu.getMenuInflater().inflate(R.menu.transaction_options,popupMenu.getMenu());
			popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(android.view.MenuItem arg0) {
					if (arg0.getTitle().equals(getResources().getString(R.string.cnt_menu_edit))) {
			    		Intent i = new Intent(getActivity().getBaseContext().getApplicationContext(), TransactionAddActivity.class);
			    		i.putExtra("transaction_id",transaction.getId());
			    		i.putExtra("mode", EDIT_MODE);
			    		startActivity(i);
			        	return true;
					} else if (arg0.getTitle().equals(getResources().getString(R.string.cnt_menu_copy))) {
						//TODO
						// Nisam napravio copy
						// Treba poslat neki copy parametar, to je add transaction
			    		Intent i = new Intent(getActivity().getBaseContext().getApplicationContext(), TransactionAddActivity.class);
			    		i.putExtra("transaction_id",transaction.getId());
			    		i.putExtra("mode", COPY_MODE);
			    		startActivity(i);
						return true;
					} else if (arg0.getTitle().equals(getResources().getString(R.string.cnt_menu_delete))){
						callDialog(transaction.getId());
						return true;
					}
					
					return false;
				}
			});
			popupMenu.show();
         }
       }

       @Override
       	public View getView(int pos, View convertView, ViewGroup parent) {
           View v = convertView;
           LayoutInflater vi = (LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           
           Long longDate = items.get(pos);

           if (v == null) {
               
               v = vi.inflate(R.layout.transaction_list_header_item, null);
               transactionHolder = new TransactionGroupViewDateHolder();
               transactionHolder.groupDate = (TextView)v.findViewById(R.id.GroupDate);
               transactionHolder.groupBalance = (TextView)v.findViewById(R.id.GroupBalance);
               v.setTag(transactionHolder);
           } else transactionHolder = (TransactionGroupViewDateHolder)v.getTag(); 
           
           RelativeLayout rlGroup = (RelativeLayout) v.findViewById(R.id.RelativeLayout2);
           RelativeLayout rlContainer = (RelativeLayout) v.findViewById(R.id.RelativeLayoutContainer);
           
           if (items.size() == (pos + 1)) {
        	   View transactionBottomSpaceView = vi.inflate(R.layout.transaction_bottom_space, null);
        	   
        	   RelativeLayout.LayoutParams rlp2 = new RelativeLayout.LayoutParams(
                       RelativeLayout.LayoutParams.MATCH_PARENT,
                       RelativeLayout.LayoutParams.MATCH_PARENT);
               
        	   rlp2.addRule(RelativeLayout.BELOW, rlGroup.getId());

        	   transactionBottomSpaceView.setLayoutParams(rlp2);
        	   
        	   rlContainer.addView(transactionBottomSpaceView);
           } else {
        	   View transactionBottomSpaceView = (View) rlContainer.findViewById(R.id.spaceContainer);
        	   if (transactionBottomSpaceView != null) {
        		   rlContainer.removeView(transactionBottomSpaceView);
        	   }
        	   
           }
           
           Integer childCounter = rlGroup.getChildCount();
           View viewLine = (View) v.findViewById(R.id.Line1);
           
           for(int i=(childCounter - 1); i > 1; --i) {
        	    View nextChild = rlGroup.getChildAt(i);
        	    
        	    if ((nextChild.getId() != transactionHolder.groupDate.getId()) &&
        	    	(nextChild.getId() != transactionHolder.groupBalance.getId()) &&
        	    	(nextChild.getId() != viewLine.getId()))
        	    	{
        	    	rlGroup.removeView(nextChild);
        	    }
        	}
           
           MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(null);
           
           Date date = new Date(longDate);
           
           SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
           
           transactionHolder.groupDate.setText(fmtOut.format(date));
           transactionHolder.groupBalance.setText(db.getBalanceByDate(longDate));
    	   if (Float.parseFloat(db.getBalanceByDate(longDate)) < 0)
    		   transactionHolder.groupBalance.setTextColor(Color.RED);
    	   else
    		   transactionHolder.groupBalance.setTextColor(getResources().getColor(R.color.DarkGreen));
           
           // Adding transactions to group
          
           
           List<TableTransaction> transactionList = db.getTransactionByDate(longDate);
           
           Integer intTextViewId = 1;
           
           for (TableTransaction transaction : transactionList) {  
        	   View transactionLayoutView = vi.inflate(R.layout.transaction_list_item, null);
        	   transactionLayoutView.setId(intTextViewId);

        	   // Category
        	   TableCategory category = db.getCategory(transaction.getIdCategory()); 
        	   TextView textviewCategory = (TextView)transactionLayoutView.findViewById(R.id.transaction_catTxt);
        	   textviewCategory.setText(category.getCatDesc());     	   
        	   
        	   // SubCategory
        	   TextView textviewSubCategory = (TextView)transactionLayoutView.findViewById(R.id.transaction_subCatTxt);
        	   if (category.getSubCatDesc() != null) {
        		   textviewSubCategory.setText(category.getSubCatDesc());
               } 
        	   
        	   // Image
        	   TableImage image = db.getImage(category.getIdImage());
        	   ImageView imageViewCategory = (ImageView)transactionLayoutView.findViewById(R.id.transaction_catImg);
        	   imageViewCategory.setImageResource(image.getImage());    	   
        	   
        	   // Amount
        	   TextView textviewAmount = (TextView)transactionLayoutView.findViewById(R.id.transaction_amount);
        	   textviewAmount.setText(String.valueOf(transaction.getAmount()));
        	   if (transaction.getAmount() < 0)
        		   textviewAmount.setTextColor(Color.RED);
        	   else
        		   textviewAmount.setTextColor(getResources().getColor(R.color.DarkGreen));
        	   
        	   // Account
        	   TextView textviewAccount = (TextView)transactionLayoutView.findViewById(R.id.transaction_accTxt);
        	   TableAccount account = db.getAccount(transaction.getIdAccount());
        	   textviewAccount.setText(account.getDescription());    	   
        	   
               RelativeLayout.LayoutParams rlp2 = new RelativeLayout.LayoutParams(
                       RelativeLayout.LayoutParams.WRAP_CONTENT,
                       RelativeLayout.LayoutParams.WRAP_CONTENT);
               
               if (intTextViewId == FIRST_TRANSACTION_IN_GROUP) {
            	   rlp2.addRule(RelativeLayout.BELOW, viewLine.getId());
            	   rlp2.addRule(RelativeLayout.ALIGN_LEFT,transactionHolder.groupDate.getId());
            	   rlp2.addRule(RelativeLayout.ALIGN_RIGHT,transactionHolder.groupBalance.getId());
               } else {
            	   rlp2.addRule(RelativeLayout.BELOW, intTextViewId - 1); 
            	   rlp2.addRule(RelativeLayout.ALIGN_LEFT,intTextViewId - 1);            	   
            	   rlp2.addRule(RelativeLayout.ALIGN_RIGHT,intTextViewId - 1);
               }
               
               transactionLayoutView.setLayoutParams(rlp2);
               
               transactionLayoutView.setTag(transaction);
               transactionLayoutView.setOnClickListener(new MyOnClickListener());
               
               rlGroup.addView(transactionLayoutView);
               
               intTextViewId++; 
           }     
           
           db.close();

           return v;
       }
	}
   
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		SetTransactionView();	
	}
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
  
        View v = inflater.inflate(R.layout.fragment_transaction, container, false);
        
        // Add balance to bottom
        setTotalBalance(v);
 
        return v;
    }
    
	@Override	
	public void onResume()
	{
	   super.onResume();
	   
	   SetTransactionView();
       // Add balance to bottom
       setTotalBalance(getView());
	}
	
	public void setTotalBalance(View v) {
		MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(getActivity());
		
		Float balance = db.getAllAccountsBalance();
		
		db.close();
		
		textViewBalance = (TextView)v.findViewById(R.id.balance);
		textViewBalance.setText(balance.toString());
		
 	   	if (balance < 0)
 	   		textViewBalance.setTextColor(getResources().getColor(R.color.FireBrick));
 	   	else
 	   		textViewBalance.setTextColor(getResources().getColor(R.color.GreenYellow));
		
		db.close();
	}
	
	public void SetTransactionView() {
		// Reading all accounts
		MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(getActivity());
		
		/*
		List<TableTransaction> transactions = db.getAllTransactions();    
		ArrayList<Transactions> TransactionList = new ArrayList<Transactions>();
        
		for (TableTransaction transaction : transactions) {
			TransactionList.add(new Transactions(transaction.getId(),transaction.getTransDate(),
					transaction.getIdCategory(),transaction.getAmount(),transaction.getIdAccount()));
		}
		*/
		ArrayList<Long> dateList = db.getTransactionDates();
  
		ListAdapter listAdapter = new TransactionAdapter(getActivity(), R.layout.transaction_list_item, dateList);
       
		setListAdapter(listAdapter);
	}	
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    // Inflate the menu items for use in the action bar
	    inflater.inflate(R.menu.transactions_menu, menu);
	    super.onCreateOptionsMenu(menu, inflater);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
        case R.id.transactionAdd:
    		Intent i = new Intent(getActivity().getBaseContext().getApplicationContext(), TransactionAddActivity.class);
    		startActivity(i);
        	return true;
        case R.id.transactionFilter:
        	Intent intentFilter = new Intent(getActivity().getBaseContext().getApplicationContext(), TransactionsFilter.class);
    		startActivity(intentFilter);
        	return true;
        case R.id.transactionViewPeriod:
        	return true;
        case R.id.transactionGroupBy:
        	return true;
        case R.id.transactionSearch:
        	
        	return true;        	
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	public void callDialog(final Integer transactionId) {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		        	MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(getActivity());
		        	
		        	db.deleteTransaction(db.getTransaction(transactionId));	
		        	db.close();
		        	SetTransactionView();
		        	
		            break;
		        case DialogInterface.BUTTON_NEGATIVE:
		            //No button clicked
		            break;
		        }
		    }
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
		builder.setMessage("Do you want to delete the transaction?")
		    .setNegativeButton("No", dialogClickListener).setPositiveButton("Yes", dialogClickListener).show();
	}
	
	@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
		
	    ListView v = new ListView(getActivity().getBaseContext().getApplicationContext());
	    v = getListView();
		
	    /*
	    v.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
            	
            	//TODO
            	// napravit novi onItemClick jer sam promjenio grupiranje
            	 
            	TransactionGroupViewDateHolder transactionHolder = (TransactionGroupViewDateHolder) view.getTag();
            	final Integer transactionId = transactionHolder.id;
            
            	PopupMenu popupMenu = new PopupMenu(getActivity().getApplicationContext(),view);
				popupMenu.getMenuInflater().inflate(R.menu.transaction_options,popupMenu.getMenu());
				popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(android.view.MenuItem arg0) {
						if (arg0.getTitle().equals(getResources().getString(R.string.cnt_menu_edit))) {
				    		Intent i = new Intent(getActivity().getBaseContext().getApplicationContext(), TransactionAddActivity.class);
				    		i.putExtra("transaction_id",transactionId);
				    		startActivity(i);
				    		
				        	return true;
						} else if (arg0.getTitle().equals(getResources().getString(R.string.cnt_menu_copy))) {
				    		Intent i = new Intent(getActivity().getBaseContext().getApplicationContext(), TransactionAddActivity.class);
				    		i.putExtra("transaction_id",transactionId);
				    		startActivity(i);
							return true;
						} else if (arg0.getTitle().equals(getResources().getString(R.string.cnt_menu_delete))){
							callDialog(transactionId);
							return true;
						}
						
						return false;
					}
				});
				popupMenu.show();
				
            }
        });
        */
    }
}
