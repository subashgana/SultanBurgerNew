package com.sultanburger;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.sultanburger.data.item.CartItem;
import com.sultanburger.data.output.UserDetail;
import com.sultanburger.utils.FileUtil;
import com.sultanburger.utils.LruBitmapCache;
import com.sultanburger.utils.Validator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class SultanBurgerApplication extends Application implements AppConstants {

    private static SultanBurgerApplication instance;
    private static Context context;

    private UserDetail userDetail;
    private String selectedBranchId;
    private final Multimap<String, CartItem> cartItems;
    private RequestQueue mRequestQueue;


    private ImageLoader mImageLoader;
    public SultanBurgerApplication() {
        cartItems = ArrayListMultimap.create();
    }

    public static synchronized SultanBurgerApplication getInstance() {
        if (!Validator.isValid(instance))
            instance = new SultanBurgerApplication();

        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        SultanBurgerApplication.context = this;
        MultiDex.install(context);
    }




    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public String getSelectedBranchId() {
        return selectedBranchId;
    }

    public void setSelectedBranchId(String selectedBranchId) {
        this.selectedBranchId = selectedBranchId;
    }

    public void addCartItem(CartItem cartItem) {
        String key = cartItem.getListMenu().getMenuItemId();

        List<CartItem> cartItems = (List<CartItem>) this.cartItems.get(key);
        cartItems.add(cartItem);
    }

    public void removeCartItem(CartItem cartItem) {
        String key = cartItem.getListMenu().getMenuItemId();

        List<CartItem> listMenus = (List<CartItem>) cartItems.get(key);
        listMenus.remove(cartItem);
    }

    public Multimap<String, CartItem> getCartItems() {
        return cartItems;
    }

    private String getImageFileName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss-SSS");
        return "SB_" + simpleDateFormat.format(new Date()) + ".jpg";
    }

    private File getAvatarDirectory() throws Exception {
        return FileUtil.makeDirectory(context.getResources().getString(R.string.app_name), true, "Avatar");
    }

    public File getAvatarImage() throws Exception {
        File prescriptionDirectory = getAvatarDirectory();
        String imageFileName = getImageFileName();

        return new File(prescriptionDirectory, File.separator + imageFileName);
    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
