package es.indios.markn.ui.guide;

import android.os.Bundle;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.indios.markn.R;
import es.indios.markn.ui.base.BaseActivity;
import es.indios.markn.util.TouchImageView;

/**
 * Created by guille on 22/04/18.
 */

public class ImageActivity extends BaseActivity{

    public static final String IMAGE_URL = "image_url";
    public static final String INDICATION = "indication";

    @BindView(R.id.image_activity_textview)
    TextView mTextView;
    @BindView(R.id.image_activity_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.image_activity_touchimage)
    TouchImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);

        String image_url = getIntent().getExtras().getString(IMAGE_URL);
        String indication = getIntent().getExtras().getString(INDICATION);

        Picasso.get().load(image_url).into(mImageView);
        mTextView.setText(indication);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
