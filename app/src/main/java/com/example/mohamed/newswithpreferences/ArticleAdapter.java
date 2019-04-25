package com.example.mohamed.newswithpreferences;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class ArticleAdapter extends ArrayAdapter<Article> {

    private Context context;
    private Article currentArticle;

    public ArticleAdapter(Context context, List<Article> article) {
        super(context, 0, article);
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, View listItemView, ViewGroup parent) {

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_article_item, parent, false);
        }

        currentArticle = getItem(position);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.tv1);

        titleTextView.setText(currentArticle.getTitle());

        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.tv2);

        sectionTextView.setText(currentArticle.getSection());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.tv3);

        String date = currentArticle.getDate();


        TextView author_name_txt = (TextView) listItemView.findViewById(R.id.author_name);

        author_name_txt.setText(currentArticle.getAuthorName());


        if (date.contains("T")) {
            String[] separated = date.split("T");

            dateTextView.setText(separated[0]);
        } else {
            dateTextView.setText(date);
        }

        return listItemView;
    }

}
