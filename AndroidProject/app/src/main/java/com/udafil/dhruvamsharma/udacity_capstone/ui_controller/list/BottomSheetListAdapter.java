package com.udafil.dhruvamsharma.udacity_capstone.ui_controller.list;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.drawable.DrawableUtils;
import com.onearticleoneweek.wahadatkashmiri.roomlib.database.domain.List;
import com.udafil.dhruvamsharma.udacity_capstone.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

public class BottomSheetListAdapter extends RecyclerView.Adapter<BottomSheetListAdapter.ListViewHolder> {

    java.util.List<List> mList;
    private WeakReference<Context> mWeakReference;
    private ListClickListener mListener;
    int[] state = new int[] {-android.R.attr.state_enabled};

    public BottomSheetListAdapter(Context context) {

        mWeakReference = new WeakReference<>(context);
        mList = new ArrayList();
        mListener = (ListClickListener) context;

    }


    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_main_single_list_layout, parent, false);

        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

        holder.list.setText(mList.get(position).getListName());
        //setState(holder.itemView);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView list;
        public ListViewHolder(@NonNull final View itemView) {
            super(itemView);

            //TODO 4: Change the layout. It should be different for the single list
            list = itemView.findViewById(R.id.list_layout_text_main_activity_list_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mListener.onListClick(mList.get(getAdapterPosition()).getListId());

//                    Parcelable parcelable = Parcels.wrap(mList.get(getAdapterPosition()));
//                    Intent intent = new Intent(mWeakReference.get(), UpdateListActivity.class);
//                    intent.putExtra("current_list", parcelable);
//
//                    mWeakReference.get().startActivity(intent);

                }
            });
        }
    }

    public  void updateListsData(java.util.List<List> list) {

        mList = list;
        notifyDataSetChanged();

    }

    public interface ListClickListener {
        void onListClick(int listId);
    }

    private void setState(View view) {

        view.setSelected(true);

    }
}
