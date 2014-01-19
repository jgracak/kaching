package com.moneyapp;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockListFragment;
import com.kaching.R;
import com.moneyapp.database.Account;
import com.moneyapp.database.Category;
import com.moneyapp.database.Image;
import com.moneyapp.database.MoneyAppDatabaseHelper;
import com.moneyapp.database.Transaction;

import android.content.Context;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
 
public class TransactionsFragment extends SherlockListFragment {

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

	private class TransactionViewHolder {
		Integer id;
		Integer catId;
		Integer accId;
		Time date;
		TextView subCatTxt;
		TextView amount;
		TextView accTxt;
		TextView catTxt;
		ImageView catImg;
   }
	
   public class TransactionAdapter extends ArrayAdapter<Transactions> {
       private ArrayList<Transactions> items;
       private TransactionViewHolder transactionHolder;

       public TransactionAdapter(Context context, int tvResId, ArrayList<Transactions> items) {
           super(context, tvResId, items);
           this.items = items;
       }

       @Override
       public View getView(int pos, View convertView, ViewGroup parent) {
           View v = convertView;
           
           Transactions  transaction = items.get(pos);
           
           if (v == null) {
               LayoutInflater vi = (LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               v = vi.inflate(R.layout.transaction_list_item, null);
               transactionHolder = new TransactionViewHolder();
               transactionHolder.id = transaction.getTransactionId();
               transactionHolder.catId = transaction.getCategoryId();
               transactionHolder.accId = transaction.getAccountId();
               transactionHolder.subCatTxt = (TextView)v.findViewById(R.id.transaction_subCatTxt);
               transactionHolder.amount = (TextView)v.findViewById(R.id.transaction_amount);
               transactionHolder.accTxt = (TextView)v.findViewById(R.id.transaction_accTxt);
               transactionHolder.catTxt = (TextView)v.findViewById(R.id.transaction_catTxt);
               transactionHolder.catImg = (ImageView)v.findViewById(R.id.transaction_catImg);
               v.setTag(transactionHolder);
           } else transactionHolder = (TransactionViewHolder)v.getTag(); 
           
           MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(null);
           Account account = db.getAccount(transaction.getAccountId());
           Category category = db.getCategory(transaction.getCategoryId());
           Image image = db.getImage(category.getIdImage());
           
           transactionHolder.catImg.setImageResource(image.getImage());
           transactionHolder.amount.setText(Float.toString(transaction.getAmount()));
           transactionHolder.catTxt.setText(category.getCatDesc());
           if (category.getSubCatDesc() != null)
        	   transactionHolder.subCatTxt.setText(category.getSubCatDesc());

           if (account != null) {
        	   transactionHolder.accTxt.setText(account.getDescription());
           }
           else {
        	   // This should never happen
        	   transactionHolder.accTxt.setText("No Acc");
           }
           
           db.close();
           
           /*
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
		*/
           return v;
       }
   }
   
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);

		SetTransactionView();	
	}
	
	
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
  
        View v = inflater.inflate(R.layout.fragment_transaction, container, false);
          
        return v;
    }
    
	@Override	
	public void onResume()
	{
	   super.onResume();
	   
	   SetTransactionView();
	}
	
	public void SetTransactionView() {
	       // Reading all accounts
	       MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(null);
	       List<Transaction> transactions = db.getAllTransactions();    
	       ArrayList<Transactions> TransactionList = new ArrayList<Transactions>();
	        
	       for (Transaction transaction : transactions) {
	    	   TransactionList.add(new Transactions(transaction.getId(),transaction.getTransDate(),
	    			   transaction.getIdCategory(),transaction.getAmount(),transaction.getIdAccount()));
	       }
      
	       ListAdapter listAdapter = new TransactionAdapter(getActivity(), R.layout.transaction_list_item, TransactionList);
	       
			setListAdapter(listAdapter);
	}
	
	@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
		
	    ListView v = new ListView(getActivity().getBaseContext().getApplicationContext());
	    v = getListView();
	    
	    /*
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
        */
	    
    }
}
