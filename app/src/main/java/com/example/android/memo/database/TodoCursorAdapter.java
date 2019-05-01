package com.example.android.memo.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.memo.R;

/**
 * Created by 21poonkw1 on 1/5/2019.
 */

public class TodoCursorAdapter extends RecyclerView.Adapter<TodoCursorAdapter.TodoViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    public TodoCursorAdapter(Context context, Cursor cursor){

        mContext = context;
        mCursor = cursor;
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder{

        public TextView nameTextView;
        public ImageView flagImageView;
        public LinearLayout linearLayout;

        public TodoViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.name);
            flagImageView = itemView.findViewById(R.id.flagColor);
            linearLayout = itemView.findViewById(R.id.categoryColor);
        }
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)){
            return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_TODO_NAME));
        int cat = mCursor.getInt(mCursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_TODO_CATEGORY));
        int priority = mCursor.getInt(mCursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_TODO_PRIORITY));

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("ColorPreferences", Context.MODE_PRIVATE);

        int catColor = sharedPreferences.getInt("savedColor" + cat, 12597547);
        Log.v(getClass().getSimpleName(), "Category color: " + catColor);
        int priorityColor = sharedPreferences.getInt("savedColor" + String.valueOf(priority+4), 12597547);
        holder.nameTextView.setText(name);

        String hexColor = String.format("#%06X", (0xFFFFFF & catColor));
        holder.linearLayout.setBackgroundColor(Color.parseColor(hexColor));

        hexColor = String.format("#%06X", (0xFFFFFF & priorityColor));
        Log.v(getClass().getSimpleName(), "Priority color: " + hexColor);

        holder.flagImageView.setColorFilter(Color.parseColor(hexColor));



    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        if (mCursor != null){
            mCursor.close();
        }

        mCursor = newCursor;

        if(newCursor != null){
            notifyDataSetChanged();
        }
    }


}