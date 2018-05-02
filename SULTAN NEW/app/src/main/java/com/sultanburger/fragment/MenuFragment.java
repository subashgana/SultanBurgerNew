package com.sultanburger.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sultanburger.AppBaseFragment;
import com.sultanburger.R;
import com.sultanburger.SultanBurgerApplication;
import com.sultanburger.activity.handler.DashBoardHandler;
import com.sultanburger.data.Dimens;
import com.sultanburger.data.output.MenuCategory;
import com.sultanburger.data.output.MenuCategoryOutput;
import com.sultanburger.data.output.SlidingImage;
import com.sultanburger.data.output.SlidingImagesOutput;
import com.sultanburger.utils.DataReceiver;
import com.sultanburger.utils.Utils;
import com.sultanburger.utils.Validator;
import com.sultanburger.view.banner.BannerView;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends AppBaseFragment implements View.OnClickListener {

    private static final String TAG = MenuFragment.class.getSimpleName();

    private BannerView bannerView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String branchId;
    private DashBoardHandler dashBoardHandler;

    public MenuFragment() {

    }

    public static MenuFragment newInstance(String branchId) {
        SultanBurgerApplication.getInstance().setSelectedBranchId(branchId);

        Bundle bundle = new Bundle();
        bundle.putString("branchId", branchId);

        MenuFragment menuFragment = new MenuFragment();
        menuFragment.setArguments(bundle);
        return menuFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        branchId = getArguments().getString("branchId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        Dimens dimens = Utils.getBannerImageSize(getContext());
        bannerView = view.findViewById(R.id.menu_bannerView);
        bannerView.getLayoutParams().width = dimens.getWidth();
        bannerView.getLayoutParams().height = dimens.getHeight();

        viewPager = view.findViewById(R.id.menu_viewpager);

        tabLayout = view.findViewById(R.id.menu_tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        populateBanner();
        populateMenuCategory();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        dashBoardHandler = (DashBoardHandler) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        dashBoardHandler = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_empty, menu);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

    private void populateBanner() {
        getAppBaseActivity().getSlidingImages(branchId, new DataReceiver<SlidingImagesOutput>() {
            @Override
            public void receiveData(SlidingImagesOutput result) {
                if (Validator.isValid(result)) {
                    List<SlidingImage> slidingImages = result.getSliderImages();
                    if (Validator.isValid(slidingImages) && !slidingImages.isEmpty()) {
                        String imageBaseUrl = result.getImageBaseUrl();

                        for (SlidingImage slidingImage : slidingImages) {
                            String imageUrl = slidingImage.getImageUrl();
                            slidingImage.setImageUrl(imageBaseUrl + imageUrl);
                        }

                        bannerView.setBannerImages(slidingImages);
                        bannerView.populateBanner();
                    }
                }
            }
        });
    }

    private void populateMenuCategory() {
        getAppBaseActivity().getMenuCategory(branchId, new DataReceiver<MenuCategoryOutput>() {
            @Override
            public void receiveData(MenuCategoryOutput result) {
                if (Validator.isValid(result)) {
                    List<MenuCategory> menuCategories = result.getMenuCategoryDetails();
                    if (Validator.isValid(menuCategories) && !menuCategories.isEmpty()) {
                        try {
                            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

                            for (MenuCategory menuCategoryOutput : menuCategories) {
                                MenuTabDataFragment menuTabDataFragment = MenuTabDataFragment.newInstance();
                                menuTabDataFragment.setBranchId(branchId);
                                menuTabDataFragment.setMenuCategoryOutput(menuCategoryOutput);

                                viewPagerAdapter.addFragment(menuTabDataFragment, menuCategoryOutput.getCategory());
                            }

                            viewPager.setAdapter(viewPagerAdapter);
                            viewPager.setOffscreenPageLimit(viewPagerAdapter.getCount());
                        }
                        catch (Exception e){

                        }
                    }
                }
            }
        });
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }
    }
}
