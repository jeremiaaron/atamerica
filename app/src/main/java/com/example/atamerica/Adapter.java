package com.example.atamerica;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class Adapter extends PagerAdapter {

    private Context context;

    private int[] imageArray = new int[] {
        R.drawable.banner_1, R.drawable.banner_2, R.drawable.eeeeee
    };

    private String[] textArray = new String[] {
        "Learn about US Embassy", "Join US Events Now!"
    };

    public Adapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageArray.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
        imageView.setImageResource(imageArray[position]);

//        Typeface sfBoldTypeface = context.getResources().getFont(R.font.sf_pro_text_bold);
//
//        TextView textView = new TextView(context);
//        textView.setTypeface(sfBoldTypeface);
//        textView.setText(textArray[position]);

        container.addView(imageView, 0);

        return imageView;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}
