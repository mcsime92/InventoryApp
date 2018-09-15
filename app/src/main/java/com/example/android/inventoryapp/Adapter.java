package com.example.android.inventoryapp;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.inventoryapp.data.ItemContract;

import java.util.Objects;

import static com.example.android.inventoryapp.MainActivity.dummy_img_paddle;
import static com.example.android.inventoryapp.data.ItemContract.ItemEntry.COLUMN_PRODUCT_CATEGORY;

public class Adapter extends CursorAdapter {

    public Adapter(Context context, Cursor c) {
        super(context, c, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void bindView(final View view, Context context, Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.product_name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        ImageView categoryImageView = (ImageView) view.findViewById(R.id.item_category);

        // Find the columns of item attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_PRODUCT_QUANTITY);
        int itemIdColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry._ID);
        int categoryImgColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCT_CATEGORY);

        // Read the item attributes from the Cursor for the current item
        String categoryString = cursor.getString(categoryImgColumnIndex);
        //if there is no image, provide image blank
        if (TextUtils.isEmpty(categoryString)) {
            categoryString = ItemContract.NO_IMAGE;
        }

        if (Objects.equals(categoryString, dummy_img_paddle)) {

            categoryImageView.setImageResource(R.drawable.img_paddle);

        } else {
            Uri categoryUri = Uri.parse(categoryString);
            categoryImageView.setImageURI(categoryUri);
        }

        final String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        final String productQuantity = cursor.getString(quantityColumnIndex);
        final long itemId = cursor.getLong(itemIdColumnIndex);

        // Update the Views with the attributes for the current item
        nameTextView.setText(productName);
        priceTextView.setText(productPrice + "€");
        quantityTextView.setText(productQuantity + " units");


        //declare and initialize Button
        Button unit_sold = (Button) view.findViewById(R.id.unit_sold);
        unit_sold.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ContentResolver resolver = view.getContext().getContentResolver();
                ContentValues values = new ContentValues();

                if (Integer.parseInt(productQuantity) > 0) {

                    values.put(ItemContract.ItemEntry.COLUMN_PRODUCT_QUANTITY, Integer.parseInt(productQuantity) -1);

                    Uri uri = ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI, itemId);
                    resolver.update(
                            uri,
                            values,
                            null,
                            null);
                }
            }

        });

    }

}
