package com.optima.rememberthings.activities.adapters;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.optima.rememberthings.R;
import com.optima.rememberthings.models.NotesModel;
import com.optima.rememberthings.utils.Globals;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Case;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmRecyclerViewAdapter;

public class MainListAdapter
        extends RealmRecyclerViewAdapter<NotesModel, RecyclerView.ViewHolder>
        implements Filterable {

    Context mContext;
    Realm realm;
    ItemClickListener listener;

    public MainListAdapter(Context context, Realm realm, OrderedRealmCollection<NotesModel> notes,ItemClickListener listener) {
        super(notes, true);
        mContext = context;
        this.realm = realm;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_main, parent, false);
        NotesClass holder = new NotesClass(view);

        view.findViewById(R.id.share_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onShare(v,holder.getLayoutPosition(),holder);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NotesModel notesModel = getData().get(position);

        NotesClass mHolder = (NotesClass) holder;
        mHolder.bind(notesModel);

        mHolder.mTitle.setBackgroundColor(
                Globals.COLORS[(int) (position % Globals.COLORS.length)]
        );
    }
    public void filterResults(String text) {
        text = text == null ? null : text.toLowerCase().trim();
        RealmQuery<NotesModel> query = realm.where(NotesModel.class);
        if(!(text == null || "".equals(text))) {
            query.contains("Title", text, Case.INSENSITIVE);
        }
        updateData(query.findAllAsync());
    }

    public Filter getFilter() {
        return new NotesFilter(this);
    }

    private class NotesFilter
            extends Filter {
        private final MainListAdapter adapter;

        private NotesFilter(MainListAdapter adapter) {
            super();
            this.adapter = adapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            return new FilterResults();
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.filterResults(constraint.toString());
        }
    }

    public class NotesClass
        extends RecyclerView.ViewHolder {

    View view;
    CardView mCardView;
    public TextView mTitle;
    public TextView mBody;
    ImageView mImageView;
    TextView mDate;
    ImageButton mShareButton;

    public NotesClass(View itemView) {
        super(itemView);
        view = itemView;

        this.mCardView = itemView.findViewById(R.id.card_view);
        this.mTitle = itemView.findViewById(R.id.title_text_view);
        this.mBody = itemView.findViewById(R.id.body_text_view);
        this.mDate = itemView.findViewById(R.id.date_text_view);
        this.mImageView = itemView.findViewById(R.id.camera_img);
        this.mShareButton = itemView.findViewById(R.id.share_btn);
    }

    public void bind(NotesModel notes) {
        mTitle.setText(notes.getTitle());
        mBody.setText(notes.getBody());
        mDate.setText(notes.getDate());

        if(notes.getImageBytes() != null){
            Bitmap bmp = BitmapFactory.decodeByteArray( notes.getImageBytes(), 0,  notes.getImageBytes().length);
            mImageView.setImageBitmap(bmp);
        }
    }
}

    public interface ItemClickListener {
        void onShare(View view, int p,NotesClass notesClass);
    }
}