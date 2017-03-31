package com.iskcon.isv.beasage;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sumitgarg on 27/03/17.
 */

public class TrackedBookAdapter extends RecyclerView.Adapter<TrackedBookAdapter.BookViewHolder>{

  private final Context context;
  private final LayoutInflater layoutInflater;
  private final TrackedBookListener trackedBookListener;
  private ArrayList<BookItem> previousBooks;

  public TrackedBookAdapter(Context context, ArrayList<BookItem> previousBooks,TrackedBookListener listener){
     this.context=context;
     this.previousBooks=previousBooks;
     this.trackedBookListener=listener;
     layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  public void updateList(ArrayList<BookItem> list){
    previousBooks=list;
  }

  @Override
  public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = layoutInflater.inflate(R.layout.book_tracked_item, parent, false);
    return new BookViewHolder(view);
  }

  @Override
  public void onBindViewHolder(BookViewHolder holder, final int position) {

      final BookItem item=previousBooks.get(position);
      holder.bookName.setText(previousBooks.get(position).name);
      int per=(item.pagesRead*100/item.pages);
      if(per==0){
        holder.bookProgress.setTextColor(context.getResources().getColor(R.color.red));
      }else if(per<=50){
        holder.bookProgress.setTextColor(context.getResources().getColor(R.color.orange));
      }else{
        holder.bookProgress.setTextColor(context.getResources().getColor(R.color.green));
      }
      holder.bookProgress.setText(per+ " % Completed ");
      Picasso.with(context).load(item.url).placeholder(R.drawable.img_books_d).into(holder.bookImageView);

      holder.cardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          trackedBookListener.openBookDetails(item.id,item);
        }
      });

       holder.btnDelete.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
           trackedBookListener.deleteRecord(item.id);
         }
       });

  }

  @Override
  public int getItemCount() {
    return previousBooks.size();
  }


  @Override
  public long getItemId(int position) {
    return super.getItemId(position);
  }

  protected static class BookViewHolder extends RecyclerView.ViewHolder{
    private final CardView cardView;
    private final TextView bookName;
    private final TextView bookProgress;
    private final Button btnDelete;
    private final ImageView bookImageView;

    public BookViewHolder(View view){
      super(view);
      cardView=(CardView) view.findViewById(R.id.book_card);;
      bookName = (TextView) view.findViewById(R.id.book_name);
      bookProgress = (TextView) view.findViewById(R.id.book_progress);
      btnDelete=(Button) view.findViewById(R.id.removeBtn);
      bookImageView=(ImageView)view.findViewById(R.id.book_image);
    }

  }
}
