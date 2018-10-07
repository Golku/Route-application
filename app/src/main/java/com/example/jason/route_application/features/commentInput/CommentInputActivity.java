package com.example.jason.route_application.features.commentInput;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jason.route_application.R;
import com.example.jason.route_application.data.pojos.Address;
import com.example.jason.route_application.data.pojos.Session;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class CommentInputActivity extends DaggerAppCompatActivity implements MvpCommentInput.View{

    @Inject
    CommentInputPresenter presenter;

    @BindView(R.id.username_tv)
    TextView usernameTv;
    @BindView(R.id.date_tv)
    TextView dateTextView;
    @BindView(R.id.commentEditText)
    EditText commentEditText;
    @BindView(R.id.addCommentBtn)
    Button addCommentBtn;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.messageToUserTextView)
    TextView messageToUserTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_input);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        Address address = getIntent().getParcelableExtra("address");
        presenter.setUpInfo(new Session(this), address);
    }

    @Override
    public void updateTextViews(String employeeId, String date) {
        usernameTv.setText(employeeId);
        dateTextView.setText(date);
    }

    @OnClick(R.id.addCommentBtn)
    @Override
    public void onAddCommentBtnClick() {
        String comment = commentEditText.getText().toString();
        presenter.onAddCommentBtnClick(comment);
    }

    @Override
    public void onStartNetworkOperation() {
        addCommentBtn.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        messageToUserTextView.setVisibility(View.VISIBLE);
        messageToUserTextView.setText("Adding list_item_comment...");
    }

    @Override
    public void onFinishNetworkOperation() {
        addCommentBtn.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        messageToUserTextView.setVisibility(View.GONE);
    }

    @Override
    public void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void closeActivity() {
        finish();
    }
}