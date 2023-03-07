package com.iriartesoft.dfgi;

import java.util.ArrayList;
import com.iriartesoft.dfgi.database.useDBAdapter;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class CurrentList extends ListActivity {
	
	private myListAdapter myListAdapter;
	useDBAdapter usedbadapter; //Connection with SQLite
	Cursor cur;
	private String how_much;
	 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.currentlist);
		//(ListView)findViewById(R.id.list);
			
		/// prepare BD
        usedbadapter = new useDBAdapter();
        usedbadapter.open(getApplicationContext());
        
        fillList();
        
	
    	
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) { 
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.currentlist_menu, menu);
    	return true;
    }
	
	// To check/uncheck all products
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
     String listempty = getResources().getString(R.string.listempty);
     switch (item.getItemId()) {
	     case R.id.btcheckall:
	    	 if (isThere()==true){
	    	 usedbadapter.updateCheckAll(true);
	    	 fillList();
	    	 myListAdapter.notifyDataSetChanged();
	    	 }else{
	    		 Toast.makeText(this, listempty, Toast.LENGTH_LONG).show();
	    		 }
	    	 return true;
	     case R.id.btuncheckall:
	    	 if (isThere()==true){
	    	 usedbadapter.updateCheckAll(false);
	    	 fillList();
	    	 myListAdapter.notifyDataSetChanged();
	    	 }else{
	    		 Toast.makeText(this, listempty, Toast.LENGTH_LONG).show();
	    		 }
	    	 return true;
	     case R.id.btshare:
	    	 if (isThere()==true){
	     	 Intent i = new Intent(Intent.ACTION_SEND);
	         i.setType("text/plain");
	         final String title = getResources().getString(R.string.app_name);
	         final String toShare = getResources().getString(R.string.share);
	         final ArrayList<String> listtoshare = null;
	         final String mylistshare = filltoshare(listtoshare).toString();
	         i.putExtra(Intent.EXTRA_SUBJECT, title);
	         i.putExtra(Intent.EXTRA_TEXT, mylistshare);
	         startActivity(Intent.createChooser(i, toShare));
	    	 }else{
	    		 Toast.makeText(this, listempty, Toast.LENGTH_LONG).show();
	    		 }
	     	 return true;
     default:
    return super.onOptionsItemSelected(item);}
    }
    
    private boolean isThere(){
    	Cursor curis = usedbadapter.fetchAllProduct();
    	if (curis.getCount()>0){
    		curis.close();
    		return true;
    	}else{
    		curis.close();
    		return false;
    	}
    }
    private ArrayList<String> filltoshare(ArrayList<String> listtosharelisttoshare){
    	ArrayList<String> listtoshare = new ArrayList<String>();
    	Cursor curshare = usedbadapter.fetchAllProduct();
    	if (curshare.getCount()>0){
    	//iteration
    	curshare.moveToFirst();
    	
    	while (curshare.isAfterLast() == false){
    		String productshare = new String(curshare.getString(1));
    		
    		listtoshare.add(productshare);
    		curshare.moveToNext();
    	}
    	
    } //end if cursor has rows
	else{
		String listempty = getResources().getString(R.string.listempty);
		Toast.makeText(this, listempty, Toast.LENGTH_LONG).show();
		listtoshare.clear();
	}
	
	curshare.close();
	return listtoshare;  
}
    private void fillList(){
    	// to use the myListAdapter
        ArrayList<Product> listProductDB = new ArrayList<Product>();
    	
    	Cursor cur = usedbadapter.fetchAllProduct();
    	if (cur.getCount()>0){
    	//iteration
    	cur.moveToFirst();
    	
    	while (cur.isAfterLast() == false){
    		try{
    			int dbcan = Integer.parseInt(cur.getString(2));
    			if (dbcan==1){
        			how_much="";}
        		else {
        			how_much=cur.getString(2);
        		}
    		} catch (NumberFormatException e) {
    			how_much="";}
    		Boolean checado=false;
    		if (cur.getString(3).equals("1")){
    			checado=true;
    			}
    		Product product = new Product(Integer.parseInt(cur.getString(0)), cur.getString(1), how_much, checado);
    		
    		listProductDB.add(product);
    					
    		cur.moveToNext();
    	}
    	
        myListAdapter = new myListAdapter(this, R.layout.rownewlist, listProductDB);
        int i=0;
        
        for (i=0;i>=listProductDB.size();i++){
        	myListAdapter.add(listProductDB.get(i));
        }
        myListAdapter.notifyDataSetChanged();
        
        setListAdapter(myListAdapter);
    	
    
    } //end if cursor has rows
	else{
		String listempty = getResources().getString(R.string.listempty);
		Toast.makeText(this, listempty, Toast.LENGTH_LONG).show();
	}
	
	cur.close();
	
}
	//class adapter for newlist
    public class myListAdapter extends ArrayAdapter<Product> {
    	 
    	private ViewHolder holder;
    	private ArrayList<Product> arrayproducts = null;
    	private Context appContext = null;
		class ViewHolder {
            TextView name;
            TextView cantity;
            CheckBox checked;
        }
    	public myListAdapter(Context context, int textViewResourceId, ArrayList<Product> arrayproducts) {
            super(context, textViewResourceId, arrayproducts);
            this.appContext = context;
            this.arrayproducts = arrayproducts;
        }

    	public String getText() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
        public View getView(final int position, View convertView, ViewGroup parent) {
    		
            View v = convertView;
            if(v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row, null);
                
                holder = new ViewHolder();
                holder.name = (CheckBox) v.findViewById(R.id.chkItem);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }
            final Product listViewItem = arrayproducts.get(position);
            
            //if (listViewItem != null) {
            	final TextView name = (TextView) v.findViewById(R.id.chkItem);
            	final TextView cantity = (TextView) v.findViewById(R.id.tvcantity);
            	final CheckBox checked = (CheckBox) v.findViewById(R.id.chkItem);
            	checked.setOnCheckedChangeListener(new OnCheckedChangeListener(){
            		@Override
            		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            		//usedbadapter.open();            		
            		listViewItem.setChecked(isChecked);
            		try {
            			int cant=0;
            			cant=Integer.parseInt(listViewItem.getCantity());
            			usedbadapter.updateProduct(Long.valueOf(listViewItem.getId()), 
            				                listViewItem.getName(), 
            				                cant, 
            				                isChecked);
            			notifyDataSetChanged();
            			
            		} catch (NumberFormatException e) {
            			usedbadapter.updateProduct(Long.valueOf(listViewItem.getId()), 
            				                listViewItem.getName(), 
            				                1, 
            				                isChecked);
            			notifyDataSetChanged();
            									
					}
            		}
            		});
            	//holder.checked.setChecked(itemSelection[position]);
            	//convertView.setTag(holder);
            	//return convertView;
                        	
            	//if (name != null) {
                	name.setText(listViewItem.getName());
                	boolean cheqo = new Boolean(listViewItem.getChecked());
                	if (cheqo == true){
                		name.setPaintFlags(name.getPaintFlags()|Paint.STRIKE_THRU_TEXT_FLAG);
                	}
                	else {
                		name.setPaintFlags(name.getPaintFlags()&(~Paint.STRIKE_THRU_TEXT_FLAG));
                	}
                	cantity.setText(listViewItem.getCantity());
                	checked.setChecked(listViewItem.getChecked());
            	//}
                //}
                //usedbadapter.close();
            	return v;
        }
        }
    	

    @Override
    protected void onDestroy() {
	     super.onDestroy();
	     
	     if (cur != null){
	    	 cur.close();
	     }
	     
	     usedbadapter.close();
	      
	     
     }
    
}