package com.sultanburger.view.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sultanburger.AppConstants;
import com.sultanburger.R;
import com.sultanburger.data.Dimens;
import com.sultanburger.data.output.SlidingImage;
import com.sultanburger.helper.imagecache.ImageUtil;
import com.sultanburger.utils.Logger;
import com.sultanburger.utils.Utils;
import com.sultanburger.utils.Validator;
import com.sultanburger.view.circlePageIndicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

public class BannerView extends LinearLayout implements AppConstants {

    private static final String TAG = BannerView.class.getSimpleName();
    private Context context;

    private ViewPager viewPager;
    private BannerPagerAdapter bannerPagerAdapter;
    private CirclePageIndicator circlePageIndicator;

    private final int BANNER_IMAGE_CHANGE_DELAY = 3000;
    private List<SlidingImage> listBannerImages = new ArrayList<>();

    public BannerView(Context context) {
        super(context);

        this.context = context;
        init();
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        init();
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_banner, this, true);

        viewPager = view.findViewById(R.id.banner_viewPager);
        viewPager.setPageMargin((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));

        circlePageIndicator = view.findViewById(R.id.banner_circularPageIndicator);
    }

    public class BannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return listBannerImages.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.view_banner_item, container, false);

            SlidingImage slidingImage = listBannerImages.get(position);

            Dimens dimens = Utils.getBannerImageSize(context);

            ImageView imageViewBanner = view.findViewById(R.id.banner_item_imageView);
            imageViewBanner.getLayoutParams().width = dimens.getWidth();
            imageViewBanner.getLayoutParams().height = dimens.getHeight();
            ImageUtil.setBannerImage(context, imageViewBanner, slidingImage.getImageUrl(), R.drawable.ic_onloading);

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    private void scrollBannerImages() {
        if (Validator.isValid(bannerPagerAdapter)) {
            Utils.makeDelay(BANNER_IMAGE_CHANGE_DELAY, new Utils.HandlerCallBack() {
                @Override
                public void onDelayCompleted() {
                    int currentItem = viewPager.getCurrentItem();
                    int totalCount = bannerPagerAdapter.getCount() - 1;

                    if (currentItem != totalCount)
                        viewPager.setCurrentItem(currentItem + 1, true);
                    else if (currentItem == totalCount)
                        viewPager.setCurrentItem(0, false);

                    scrollBannerImages();
                }
            });
        }
    }

    public void setBannerImages(List<SlidingImage> listBannerImages) {
        if (Validator.isValid(listBannerImages) && !listBannerImages.isEmpty())
            this.listBannerImages = listBannerImages;
    }

    public void populateBanner() {
        if (Validator.isValid(listBannerImages) && !listBannerImages.isEmpty()) {
            bannerPagerAdapter = new BannerPagerAdapter();
            viewPager.setAdapter(bannerPagerAdapter);
            circlePageIndicator.setViewPager(viewPager);

            scrollBannerImages();
        } else {
            Logger.writeLog(TAG, "init -> No Images for banner");
        }
    }
}
