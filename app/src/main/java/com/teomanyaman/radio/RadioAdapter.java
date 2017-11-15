package com.teomanyaman.radio;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.List;

public class RadioAdapter extends ArrayAdapter<RadioDao> {

    private Context context;
    private List<RadioDao> originalItems;
    private List<RadioDao> filteredItems;
    private List<RadioDao> notFilteredItems;
    private Filter filter;


    RadioAdapter(Context context, List<RadioDao> arrayList, List<RadioDao> notfiltered) {

        super(context, R.layout.radio_adapter, arrayList);
        this.context = context;
        this.originalItems = arrayList;
        this.filteredItems = arrayList;
        this.notFilteredItems = notfiltered;
        this.filter = new ModelFilter();

    }

    @Nullable
    @Override
    public RadioDao getItem(int position) {
        return originalItems.get(position);
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new ModelFilter();
        }
        return filter;
    }

    @Override
    public int getCount() {
        return originalItems.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        final RadioDao radioDao = originalItems.get(position);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.radio_adapter, null);

            holder = new ViewHolder();
            holder.text_radioName = convertView.findViewById(R.id.text_radioName);
            holder.image_star = convertView.findViewById(R.id.image_star);
            holder.image_symbol = convertView.findViewById(R.id.image_symbol);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int drawableId=context.getResources().getIdentifier(radioDao.getIcon().replace(".png",""), "drawable", context.getPackageName());

        RequestOptions options = new RequestOptions();
        options.fitCenter();
        Glide.with(context).asBitmap().load(drawableId).apply(options).into(new BitmapImageViewTarget(holder.image_symbol) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.image_symbol.setImageDrawable(circularBitmapDrawable);
            }
        });

        holder.text_radioName.setText(radioDao.getName());

        if (radioDao.getFavourite() == 1) {
            holder.image_star.setImageResource(R.drawable.star);
        } else if (radioDao.getFavourite() == 2) {
            holder.image_star.setImageResource(R.drawable.stardolu);
        }

        holder.image_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (radioDao.getFavourite() == 1) {
                    Database database = new Database(getContext());
                    database.updateFavourite(radioDao);
                    radioDao.setFavourite(2);
                    holder.image_star.setImageResource(R.drawable.stardolu);
                } else if (radioDao.getFavourite() == 2) {
                    Database database = new Database(getContext());
                    database.deleteFavourite(radioDao);
                    radioDao.setFavourite(1);
                    holder.image_star.setImageResource(R.drawable.star);
                }
            }
        });
        return convertView;
    }

    private class ModelFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String prefix = constraint.toString().toLowerCase();

            if (prefix.length() == 0) {
                ArrayList<RadioDao> list = new ArrayList<>(notFilteredItems);
                results.values = list;
                results.count = list.size();
            } else {
                final ArrayList<RadioDao> list = new ArrayList<>(notFilteredItems);
                final ArrayList<RadioDao> nlist = new ArrayList<>();
                int count = list.size();

                for (int i = 0; i < count; i++) {
                    final RadioDao dataNames = list.get(i);
                    final String value = dataNames.getName().toLowerCase();
                    final String venue = dataNames.getName().toLowerCase();
                    //Başka bir field'a göre daha arama.

                    if (value.contains(prefix) || venue.contains(prefix)) {
                        nlist.add(dataNames);
                    }

                }
                results.values = nlist;
                results.count = nlist.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if (results.values == null)
                return;
            filteredItems = (ArrayList<RadioDao>) results.values;
            originalItems.clear();
            notifyDataSetChanged();
            int count = filteredItems.size();
            //..
            for (int i = 0; i < count; i++) {
                originalItems.add(filteredItems.get(i));
                notifyDataSetInvalidated();
            }
        }
    }

    static class ViewHolder {
        TextView text_radioName;
        ImageView image_symbol, image_star;
    }
}
