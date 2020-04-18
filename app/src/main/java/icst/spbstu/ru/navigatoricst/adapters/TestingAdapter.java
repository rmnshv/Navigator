package icst.spbstu.ru.navigatoricst.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import icst.spbstu.ru.navigatoricst.R;
import icst.spbstu.ru.navigatoricst.listeners.ListItemClickListener;

public class TestingAdapter extends RecyclerView.Adapter<TestingAdapter.ViewHolder> {

    private Context context;
    private Activity activity;

    private ArrayList<String> mItemList;
    private ListItemClickListener mItemClickListener;

    public TestingAdapter(Context context, Activity activity,
                          ArrayList<String> mItemList) {
        this.context = context;
        this.activity = activity;
        this.mItemList = mItemList;
    }

    public void setItemClickListener(ListItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public TestingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test, parent, false);
        return new TestingAdapter.ViewHolder(view, viewType, mItemClickListener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvItemTitle;
        private CardView lytContainer;
        private ListItemClickListener itemClickListener;

        ViewHolder(View itemView, int viewType, ListItemClickListener itemClickListener) {
            super(itemView);

            this.itemClickListener = itemClickListener;
            // Find all views ids
            tvItemTitle = (TextView) itemView.findViewById(R.id.answer_text);
            lytContainer = (CardView) itemView.findViewById(R.id.card_view);

            lytContainer.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null){
                itemClickListener.onItemClick(getLayoutPosition(), v);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (null != mItemList ? mItemList.size() : 0);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String model = mItemList.get(position);
        holder.tvItemTitle.setText(Html.fromHtml(model));
    }
}
