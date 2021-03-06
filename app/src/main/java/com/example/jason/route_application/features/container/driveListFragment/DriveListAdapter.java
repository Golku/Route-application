package com.example.jason.route_application.features.container.driveListFragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.jason.route_application.R;
import com.example.jason.route_application.data.pojos.Address;
import com.example.jason.route_application.data.pojos.api.Drive;

import java.util.List;

/**
 * Created by Jason on 07-Feb-18.
 */

public class DriveListAdapter extends RecyclerView.Adapter<DriveListAdapter.CustomViewHolder>{

    private List<Drive> driveList;
    private AdapterCallback callback;
    private Context context;

    public DriveListAdapter(AdapterCallback callback, List<Drive> driveList) {
        this.driveList = driveList;
        this.callback = callback;
    }

    interface AdapterCallback{
        void itemClick(Address address);
        void goButtonClick(String address);
        void completeDrive(Drive drive);
    }

    public void addContext(Context context){
        this.context = context;
    }

    public void addTouchHelper(RecyclerView recyclerView){
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    List<Drive> getList(){
        return this.driveList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_drive, parent, false);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        Drive drive = driveList.get(position);

        String city = drive.getDestinationAddress().getPostCode() + " " + drive.getDestinationAddress().getCity();
        String distance = "Distance: "+drive.getDriveDistanceHumanReadable();
        String duration = "Duration: "+drive.getDriveDurationHumanReadable();
        String arrivalTime = drive.getDeliveryTimeHumanReadable();

        holder.positionTextView.setText(String.valueOf(drive.getPosition()));
        holder.streetTextView.setText(drive.getDestinationAddress().getStreet());
        holder.cityTextView.setText(city);
        holder.distanceTextView.setText(distance);
        holder.durationTextView.setText(duration);
        holder.estimatedArrivalTime.setText(arrivalTime);

        if(drive.isDestinationIsABusiness()){
            holder.addressType.setImageResource(R.drawable.business_ic);
        }else{
            holder.addressType.setImageResource(R.drawable.home_ic);
        }

        if(drive.getDone() == 1){
            holder.itemView.setAlpha(0.5f);
        }
    }

    @Override
    public int getItemCount() {
        return driveList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView positionTextView;
        private TextView streetTextView;
        private TextView cityTextView;
        private TextView distanceTextView;
        private TextView durationTextView;
        private TextView estimatedArrivalTime;
        private ImageView addressType;

        private ViewGroup itemWrapper;
        private ImageView goIv;

        CustomViewHolder(View itemView) {
            super(itemView);
            this.positionTextView = itemView.findViewById(R.id.positionTextView);
            this.streetTextView = itemView.findViewById(R.id.streetTextView);
            this.cityTextView = itemView.findViewById(R.id.cityTextView);
            this.distanceTextView = itemView.findViewById(R.id.distanceTextView);
            this.durationTextView = itemView.findViewById(R.id.durationTextView);
            this.estimatedArrivalTime = itemView.findViewById(R.id.estimatedArrivalTimeTextView);
            this.addressType = itemView.findViewById(R.id.addressTypeImageView);
            this.goIv = itemView.findViewById(R.id.go_iv);
            this.itemWrapper = itemView.findViewById(R.id.item_wrapper);

            this.itemWrapper.setOnClickListener(this);
            this.goIv.setOnClickListener(this);
        }

        public void onClick(View v) {

            if(v == this.itemWrapper){
                callback.itemClick(driveList.get(this.getAdapterPosition()).getDestinationAddress());
            }
            else if(v == this.goIv){
                callback.goButtonClick(driveList.get(this.getAdapterPosition()).getDestinationAddress().getAddress());
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
                callback.completeDrive(driveList.get(position));
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

                        backgroundPaint.setColor(ResourcesCompat.getColor(context.getResources(), R.color.green, null));

                        textPaint.setColor(ResourcesCompat.getColor(context.getResources(), R.color.white, null));
                        textPaint.setTextSize(80);

                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, backgroundPaint);

                        c.drawText("✔ Done",
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