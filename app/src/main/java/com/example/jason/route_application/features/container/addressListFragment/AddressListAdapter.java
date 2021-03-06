package com.example.jason.route_application.features.container.addressListFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jason.route_application.R;
import com.example.jason.route_application.data.pojos.Address;

import java.util.List;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.CustomViewHolder>{

    private final String debugTag = "debugTag";

    private List<Address> addressList;
    private AdapterCallback callback;
    private Context context;

    AddressListAdapter(AdapterCallback callback, List<Address> addressList) {
        this.addressList = addressList;
        this.callback = callback;
    }

    interface AdapterCallback{
        void itemClick(Address address);
        void showAddress(Address address);
        void removeAddress(Address address);
    }

    void addContext(Context context){
        this.context = context;
    }

    void addTouchHelper(RecyclerView recyclerView){
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_address, parent, false);
        return new CustomViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Address address = addressList.get(position);

        if(address.isValid()){
            holder.positionTv.setText(Integer.toString(position + 1));
            holder.streetTv.setText(address.getStreet());
            holder.cityTv.setText(address.getPostCode() +" "+ address.getCity());

            if(address.isBusiness()){
                holder.addressType.setImageResource(R.drawable.business_ic);
            }else{
                holder.addressType.setImageResource(R.drawable.home_ic);
            }

        }else{
            holder.streetTv.setText(address.getAddress());
        }
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ViewGroup itemWrapper;
        private TextView positionTv;
        private TextView streetTv;
        private TextView cityTv;
        private ImageView showOnMapBtn;
        private ImageView addressType;

        CustomViewHolder(View itemView) {
            super(itemView);
            itemWrapper = itemView.findViewById(R.id.item_wrapper);
            positionTv = itemView.findViewById(R.id.positionTextView);
            streetTv = itemView.findViewById(R.id.street_tv);
            cityTv = itemView.findViewById(R.id.city_tv);
            showOnMapBtn = itemView.findViewById(R.id.show_on_map_iv);
            addressType = itemView.findViewById(R.id.address_type_iv);
            itemWrapper.setOnClickListener(this);
            showOnMapBtn.setOnClickListener(this);
        }

        public void onClick(View v) {

            if (v == itemWrapper) {
                callback.itemClick(addressList.get(getAdapterPosition()));
            } else if (v == showOnMapBtn) {
                callback.showAddress(addressList.get(getAdapterPosition()));
            }
        }
    }

    private ItemTouchHelper.Callback createHelperCallback(){

        return new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            //not used, as the first parameter above is 0
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                callback.removeAddress(addressList.get(position));
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;

                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    Paint backgroundPaint = new Paint();
                    Paint textPaint = new Paint();
                    Bitmap icon;

                    if (dX > 0) {

                        backgroundPaint.setColor(ResourcesCompat.getColor(context.getResources(), R.color.red, null));

                        textPaint.setColor(ResourcesCompat.getColor(context.getResources(), R.color.white, null));
                        textPaint.setTextSize(80f);

                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, backgroundPaint);

                        c.drawText("X Delete",
                                (float) itemView.getLeft() + width,
                                (float) itemView.getTop() + (height*(float)0.6),
                                textPaint);

//                        icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_delete_white_24dp);
//                        RectF iconDest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
//                        c.drawBitmap(icon, null, iconDest, p);


//                        Log.d("debug", "Top: "+itemView.getTop());
//                        Log.d("debug", "Bottom: "+itemView.getBottom());
//                        Log.d("debug", "Left: "+itemView.getLeft());
//                        Log.d("debug", "Right: "+itemView.getRight());
//                        Log.d("debug", "Height: "+height);
                    }

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };
    }
}
