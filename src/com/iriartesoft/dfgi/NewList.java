package com.iriartesoft.dfgi;

import java.util.ArrayList;

import com.iriartesoft.dfgi.database.newDBAdapter;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NewList extends ListActivity 
{
	 
    ListView mListView;
    private Button mAdd;
  	private String cadena;
  	private String cantidad;
  	private NewListAdapter myNewListAdapter;
    newDBAdapter newdbadapter;
    Cursor cur;
    /**
     * Called with the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newlist);
        
        // list of proposal products
        final String[] listofproducts = getResources().getStringArray(R.array.products);
        // where write the products and his adapter
        final AutoCompleteTextView myMulty = (AutoCompleteTextView)findViewById(R.id.autocompletetextview);
        
        myMulty.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, listofproducts)); 
        
        // prepare BD
        newdbadapter = new newDBAdapter();
        newdbadapter.open(getApplicationContext());
                
        fillList();
        
        // the button to add
        mAdd = (Button) findViewById(R.id.addbutton);
        // and his listener
        mAdd.setOnClickListener(new OnClickListener() {
        	@Override
			public void onClick(View v) {
        		final String toempty = null;
        		cantidad = ""; 
				cadena = new String(myMulty.getText().toString());
				
		        Product product = new Product(0, cadena, cantidad, false);
		        if (myMulty.getText().toString().length() > 1)
		        {
		        myNewListAdapter.add(product);
		        newdbadapter.createProduct(cadena, 1, false);
				
				
				myMulty.setText(toempty);
				}
		        myNewListAdapter.notifyDataSetChanged();
		        fillList();
        	}
		});
    
           
    }
    //class adapter for newlist
    public class NewListAdapter extends ArrayAdapter<Product> {
    	 
    	private ViewHolder holder;
    	private ArrayList<Product> arrayproducts = null;
		private Context appContext = null;
    	class ViewHolder {
    		TextView name;
    		TextView cantity;
    		Button btnAdd;
            Button btnDelete;
        }
    	public NewListAdapter(Context context, int textViewResourceId, ArrayList<Product> arrayproducts) {
            super(context, textViewResourceId, arrayproducts);
            this.appContext = context;
            this.arrayproducts = arrayproducts;
        }

    	@Override
        public View getView(final int position, View convertView, ViewGroup parent) {
    		
            View v = convertView;
            if(v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.rownewlist, null);
                
                holder = new ViewHolder();
                holder.name = (TextView) v.findViewById(R.id.tv_rownew_name);
                
                v.setTag(holder);
                } else {
                	holder = (ViewHolder) v.getTag();
                }
            final Product listViewItem = arrayproducts.get(position);
            
                        	
            	Button btnAdd = (Button)v.findViewById(R.id.btrownew_add);
            	Button btnDelete = (Button)v.findViewById(R.id.btrownew_delete);
            	final TextView cantity = (TextView) v.findViewById(R.id.tv_cantity);
            	final TextView name = (TextView) v.findViewById(R.id.tv_rownew_name);
            	final String delete = getResources().getString(R.string.delete);
            	          	
            	
            	btnDelete.setOnClickListener(new OnClickListener() {
                @Override
            	public void onClick(View view) {
            	String Stcantity = cantity.getText().toString();
            	
            	
            	if (Stcantity.length()==0){
            		Toast.makeText(appContext,delete + " " + name.getText().toString(), Toast.LENGTH_SHORT).show();
            		arrayproducts.remove(listViewItem);
            		newdbadapter.deleteProduct(listViewItem.getId());
            		notifyDataSetChanged();}
            	else {
            		if (Stcantity.compareTo("2")==0) { 
            			cantity.setText("");
            			listViewItem.setCantity(cantity.getText().toString());
            			newdbadapter.updateProduct(listViewItem.getId(), listViewItem.getName(), 1, false);
            			notifyDataSetChanged();}
            		else { 
            			try {
                            int Lcantity = Integer.parseInt(Stcantity);
                            Lcantity--;
                            cantity.setText("" + Lcantity);
                            listViewItem.setCantity(cantity.getText().toString());
                            newdbadapter.updateProduct(listViewItem.getId(), listViewItem.getName(), Lcantity, false);
                            
                        	} catch(NumberFormatException expD) { Stcantity = "2";}
            			notifyDataSetChanged();}
            		} //else 2
            	}     //else 1
            	}); //close btnDelete.setOnClickListener
            	
            	btnAdd.setOnClickListener(new OnClickListener() {
                    @Override
                	public void onClick(View view) {
                	String Stcantity = cantity.getText().toString();
                	
                    if (Stcantity.length()==0){
                		String two = "2";
                		cantity.setText(two);
                		listViewItem.setCantity(cantity.getText().toString());
                		newdbadapter.updateProduct(listViewItem.getId(), listViewItem.getName(), 2, false);
                		notifyDataSetChanged();}
                	else {
                		try {
                		   int Lcantity = Integer.parseInt(Stcantity);
                		   Lcantity++;
                		   cantity.setText("" + Lcantity);
                		   listViewItem.setCantity(cantity.getText().toString());
                		   newdbadapter.updateProduct(listViewItem.getId(), listViewItem.getName(), Lcantity, false);
                		   
                    	} catch(NumberFormatException expA) { Stcantity = "2";}
                		 notifyDataSetChanged();}
                    }  //else
                	});  //close btnAdd.setOnClickListener
                
            	
            	name.setText(listViewItem.getName());
            	cantity.setText(listViewItem.getCantity());
            	return v;
         } //close v == null
    	}  //close View getView
        
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { 
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.newlist_menu, menu);
    	return true;
    }
    
 // To delete all products
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
     switch (item.getItemId()) {
     case R.id.bteraseall:
    	 newdbadapter.deleteAllProduct();
    	 myNewListAdapter.clear();
    	 myNewListAdapter.notifyDataSetChanged();
    	 Toast.makeText(this,getResources().getString(R.string.allproddel), Toast.LENGTH_SHORT).show();
     return true;
     default:
    return super.onOptionsItemSelected(item);}
    }
    
    private void fillList(){
    	// use the NewListAdapter
        ArrayList<Product> listProductDB = new ArrayList<Product>();
    	
    	Cursor cur = newdbadapter.fetchAllProduct();
    	
    	if (cur.getCount()>0){
    	//iteration
    	cur.moveToFirst();
    	while (cur.isAfterLast() == false){
    		try{
    			int dbcan = Integer.parseInt(cur.getString(2));
    			if (dbcan==1){
        			cantidad="";}
        		else {
        			cantidad=cur.getString(2);
        		}
    		} catch (NumberFormatException e) {
    			cantidad="";}
    		
    		Product product = new Product(Integer.parseInt(cur.getString(0)), cur.getString(1), cantidad, false);
    		listProductDB.add(product);
    					
    		cur.moveToNext();
    	}
    	
        myNewListAdapter = new NewListAdapter(this, R.layout.rownewlist, listProductDB);
        int i=0;
        
        for (i=0;i>=listProductDB.size();i++){
        	myNewListAdapter.add(listProductDB.get(i));
        }
        myNewListAdapter.notifyDataSetChanged();
        
        setListAdapter(myNewListAdapter);
        }  //end if cursor has rows
    	else {
    		myNewListAdapter = new NewListAdapter(this, R.layout.rownewlist, listProductDB);
    	}
    	
    	cur.close();
    	
    }
    
    
    @Override
    protected void onDestroy() {
     super.onDestroy();
             	
    	if (cur != null){
    	cur.close();
    	}
    	newdbadapter.close();
    	
    } 
    

}
 
    
     
 
    