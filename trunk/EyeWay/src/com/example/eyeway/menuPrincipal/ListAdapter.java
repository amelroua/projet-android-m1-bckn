package com.example.eyeway.menuPrincipal;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.eyeway.R;
import com.example.eyeway.R.id;
 
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<Fonctionnalite> {
	//utiliser ArrayAdapter
    private Activity activity;
    
    private static LayoutInflater inflater=null;
    Context context; 
    int layoutResourceId;    
    ArrayList<Fonctionnalite> data= null;
    
    public ListAdapter(Context context, int layoutResourceId, ArrayList<Fonctionnalite> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
 	public void add(Fonctionnalite f){
 		if(!data.contains(f)){
 			data.add(f);
 			notifyDataSetChanged();
 		}
 	}
 	public void remove(int index){
 		data.remove(index);
 		notifyDataSetChanged();
 	}
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        FonctionnaliteHolder holder=null;
        if(row==null){
        	LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        	row=inflater.inflate(layoutResourceId, null);
        	holder=new FonctionnaliteHolder();
        	holder.imgIcon=(ImageView)row.findViewById(R.id.icon_action);
        	holder.txtTitle=(TextView)row.findViewById(R.id.texte_ligne);
        	row.setTag(holder);
        }
        else {	
        	holder=(FonctionnaliteHolder)row.getTag();
        }
        
        Fonctionnalite f=data.get(position);
        holder.txtTitle.setText(f.title);
        holder.imgIcon.setImageResource(f.icon);   
        return row;
    }
    static class FonctionnaliteHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}