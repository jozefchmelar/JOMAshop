package com.joma.jomashop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jozef on 4.11.2015.
 */
public class ProductAdapter extends ArrayAdapter<Product> {

    DataTransferInterface dtInterface;
    private Context context;
    private ArrayList<Product> ShoppingList;


    public ProductAdapter(Context context, ArrayList<Product> ShoppingList, DataTransferInterface dtInterface) {
        super(context, R.layout.listview_element, R.id.textView, ShoppingList);
        this.context = context;
        this.ShoppingList = ShoppingList;
        this.dtInterface = dtInterface;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // get row

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listview_element, parent, false);
        final Product product = ShoppingList.get(position);
        TextView currency1= (TextView) rowView.findViewById(R.id.currency1); currency1.setText(lib.CurrencySymbol());
        TextView currency2= (TextView) rowView.findViewById(R.id.currency2);  currency2.setText(lib.CurrencySymbol());
        //assigning elements to variables
        final TextView textViewTotalPrice = (TextView) rowView.findViewById(R.id.textViewPrice);
        TextView textViewNameOfProduct = (TextView) rowView.findViewById(R.id.textViewNameOfProduct);
        TextView textViewPriceForOne = (TextView) rowView.findViewById(R.id.textViewPriceForOneProduct);
        EditText editTextQuantity = (EditText) rowView.findViewById(R.id.editTextQuantity);
        Button btnDelete = (Button) rowView.findViewById(R.id.buttonDelete);
        Button btnEdit = (Button) rowView.findViewById(R.id.buttonEdit);
        Button btnPlus = (Button) rowView.findViewById(R.id.buttonPlus);
        Button btnMinus = (Button) rowView.findViewById(R.id.buttonMinus);
        //setting data
        textViewTotalPrice.setText(product.getTotalPrice() + "");
        textViewNameOfProduct.setText(product.getProductname());
        textViewPriceForOne.setText(product.getPrice() + "" );
        editTextQuantity.setText(product.getQuantity() + "");
        //visibility of  elements.
        if (product.getQuantity() == 1) {
        // if quantity is 1 don't display stupid stuff
            btnMinus.setVisibility(View.INVISIBLE);
            editTextQuantity.setVisibility(View.INVISIBLE);
            textViewPriceForOne.setVisibility(View.INVISIBLE);
        }
        if (product.isVisiblesettings()) {
        // should I display Edit and delete button? ..probably
            btnDelete.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
        }


        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.QuantityPlus(1);  //on click I add +1
                notifyDataSetChanged(product, position);
            }
        });
        btnPlus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                product.QuantityPlus(5);//on  longclick I add +5   --the same stuff but for Minus bellow
                notifyDataSetChanged(product, position);
                return true;
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.QuantityMinus(1);
                notifyDataSetChanged(product, position);
            }
        });

        btnMinus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                product.QuantityMinus(5);
                notifyDataSetChanged(product, position);
                return true;
            }
        });


        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //on click  will display settings
                product.setVisiblesettings(!product.isVisiblesettings());
                notifyDataSetChanged(product, position);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//delete button
                Product toRemove = ShoppingList.get(position);
                ShoppingList.remove(toRemove);
                dtInterface.deletedProductValue(toRemove.getTotalPrice());
                notifyDataSetChanged();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // I will send the current product to EditProduct actvitiy
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("product", product);
                intent.putExtra("position", position);
                context.startActivity(intent);

            }
        });


        return rowView;
    }

    public void notifyDataSetChanged(Product product, int position) {
        super.notifyDataSetChanged();
        dtInterface.productToEdit(product, position);
    }



}
