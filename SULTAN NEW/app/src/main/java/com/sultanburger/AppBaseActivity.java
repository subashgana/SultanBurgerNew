package com.sultanburger;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.sultanburger.data.Location;
import com.sultanburger.data.input.AddMenutToCartInput;
import com.sultanburger.data.input.AddOnsInput;
import com.sultanburger.data.input.AddToCartInput;
import com.sultanburger.data.input.AddUserAddressInput;
import com.sultanburger.data.input.ChangePasswordInput;
import com.sultanburger.data.input.ChangeUserNameInput;
import com.sultanburger.data.input.ForgotPasswordChangeInput;
import com.sultanburger.data.input.ForgotPasswordInput;
import com.sultanburger.data.input.GuestSignInInput;
import com.sultanburger.data.input.ListCartProduct;
import com.sultanburger.data.input.ListMenuInput;
import com.sultanburger.data.input.ListMenuInputNew;
import com.sultanburger.data.input.ListUserNearByBranchesInput;
import com.sultanburger.data.input.ModifiersInput;
import com.sultanburger.data.input.SignInInput;
import com.sultanburger.data.input.SignUpInput;
import com.sultanburger.data.input.SignUpOTPInput;
import com.sultanburger.data.output.AddOns;
import com.sultanburger.data.output.AddOnsOutput;
import com.sultanburger.data.output.AddUserAddressOutput;
import com.sultanburger.data.output.AddtoCartOutput;
import com.sultanburger.data.output.AuthToken;
import com.sultanburger.data.output.GuestSignInOutput;
import com.sultanburger.data.output.ListCartOutput;
import com.sultanburger.data.output.ListMenuOutput;
import com.sultanburger.data.output.ListUserAddressOutput;
import com.sultanburger.data.output.MenuCategoryOutput;
import com.sultanburger.data.output.Modifiers;
import com.sultanburger.data.output.ModifiersOutput;
import com.sultanburger.data.output.MyOrdersOutput;
import com.sultanburger.data.output.OTPOutput;
import com.sultanburger.data.output.PagedBranchOutput;
import com.sultanburger.data.output.SlidingImagesOutput;
import com.sultanburger.data.output.UserDetailsOutput;
import com.sultanburger.helper.gson.GsonHelper;
import com.sultanburger.helper.map.MapHelper;
import com.sultanburger.helper.permission.PermissionActivity;
import com.sultanburger.helper.preference.PreferenceHelper;
import com.sultanburger.rest.RestHelper;
import com.sultanburger.rest.Result;
import com.sultanburger.rest.ResultReceiver;
import com.sultanburger.utils.DataReceiver;
import com.sultanburger.utils.LocationUtil;
import com.sultanburger.utils.Logger;
import com.sultanburger.utils.ToastUtil;
import com.sultanburger.utils.Utils;
import com.sultanburger.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AppBaseActivity extends PermissionActivity implements AppConstants {

    private static final String TAG = AppBaseActivity.class.getSimpleName();
    private static final String DEVICE_TYPE_ID = "1";
    private static final String LANGUAGE_ID = "1";

    private ProgressDialog progressDialog;
    private GsonHelper gsonHelper;
    private PreferenceHelper preferenceHelper;
    private MapHelper mapHelper;

    private FusedLocationProviderClient fusedLocationClient;
    private String deviceId;
    private String deviceToken;

    public abstract void init();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        gsonHelper = GsonHelper.init(getApplicationContext());
        preferenceHelper = PreferenceHelper.init(getApplicationContext(), getResources().getString(R.string.app_name));
        mapHelper = MapHelper.init(getApplicationContext());

        progressDialog = new ProgressDialog(AppBaseActivity.this);
        progressDialog.setCancelable(false);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setIndeterminateDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_progessbar_spinner));

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        deviceId = Utils.getDeviceId(getApplicationContext());
        deviceToken = Utils.getDeviceToken(getApplicationContext());

        processBundleData();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ToastUtil.clearAllToast();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void showProgressBar(final String message) {
        if (!progressDialog.isShowing()) {
            progressDialog.setMessage(message + "...");
            progressDialog.show();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.setMessage(message + "...");
                }
            });
        }
    }

    public void dismissProgressBar() {
        progressDialog.dismiss();
    }

    public GsonHelper getGsonHelper() {
        return gsonHelper;
    }

    public PreferenceHelper getPreferenceHelper() {
        return preferenceHelper;
    }

    public MapHelper getMapHelper() {
        return mapHelper;
    }

    @SuppressLint("MissingPermission")
    public void getLastKnownLocation(final DataReceiver<Location> dataReceiver) {
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
            @Override
            public void onSuccess(android.location.Location data) {
                try {
                    Location location = new Location(data.getLatitude(), data.getLongitude(), System.currentTimeMillis());
                    location.setAccuracy(data.getAccuracy());
                    location.setBearings(data.getBearing());
                    location.setSpeed(data.getSpeed());
                    location.setProvider(data.getProvider());
                    location.setAddress(LocationUtil.getAddress(AppBaseActivity.this, new LatLng(data.getLatitude(), data.getLongitude())));
                    dataReceiver.receiveData(location);
                } catch (Exception e) {
                    dataReceiver.receiveData(null);
                    Logger.writeLog(TAG, "getLastKnownLocation -> Exception : " + e.getLocalizedMessage());
                }
            }
        });
    }

    private void processBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (Validator.isValid(bundle)) {
            boolean overrideTransition = bundle.getBoolean(EXTRA_OVERRIDE_TRANSITION, true);
            if (overrideTransition)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    public boolean isUserSignedIn() {
        return Validator.isValid(SultanBurgerApplication.getInstance().getUserDetail());
    }

    public void signIn(final String username, final String password) {
        SignInInput signInInput = new SignInInput();
        signInInput.setUsername(username);
        signInInput.setPassword(password);
        signInInput.setDeviceTypeId(DEVICE_TYPE_ID);
        signInInput.setDeviceId(deviceId);
        signInInput.setDeviceToken(deviceToken);
        signInInput.setLanguageId(LANGUAGE_ID);

        boolean valid = signInInput.isValid(getApplicationContext());
        if (valid) {
            showProgressBar("Validating Credentials");
            RestHelper.sendPost(AppBaseActivity.this, URI_SIGN_IN, signInInput, AuthToken.class, new ResultReceiver<AuthToken>() {
                @Override
                public void onCompleted(Result<AuthToken> result) {
                    dismissProgressBar();

                    AuthToken authToken = result.getData();
                    getPreferenceHelper().setString(USER_NAME, username);
                    getPreferenceHelper().setString(PASSWORD, password);
                    getPreferenceHelper().setString(PREF_AUTH_TOKEN, authToken.getAuthToken());
                    getPreferenceHelper().setString(PREF_AUTH_TOKEN, authToken.getAuthToken());
                    Log.d("PREF_AUTH_TOKEN",authToken.getAuthToken());
                    getPreferenceHelper().setBoolean(PREF_IS_USER_SIGNED_IN, true);
                    getPreferenceHelper().setBoolean(PREF_IS_GUEST_USER, false);

                    getUserDetails(new DataReceiver<UserDetailsOutput>() {
                        @Override
                        public void receiveData(UserDetailsOutput result) {
                            SultanBurgerApplication.getInstance().setUserDetail(result.getUserDetails().get(0));
                            ActivityConfig.startDashBoardActivity(AppBaseActivity.this);
                        }
                    });
                }

                @Override
                public void onFailed(Exception exception) {
                    dismissProgressBar();
                    ToastUtil.showToast(getApplicationContext(), exception.getMessage());
                }
            });
        }
    }

    public void guestSignIn() {
        GuestSignInInput guestSignInInput = new GuestSignInInput();
        guestSignInInput.setDeviceTypeId(DEVICE_TYPE_ID);
        guestSignInInput.setDeviceId(deviceId);
        guestSignInInput.setDeviceToken(deviceToken);
        guestSignInInput.setLanguageId(LANGUAGE_ID);

        boolean valid = guestSignInInput.isValid(getApplicationContext());
        if (valid) {
            showProgressBar("Logging guest user");
            RestHelper.sendPost(AppBaseActivity.this, URI_REGISTER_GUEST_USER, guestSignInInput, GuestSignInOutput.class, new ResultReceiver<AuthToken>() {
                @Override
                public void onCompleted(Result<AuthToken> result) {
                    dismissProgressBar();

                    AuthToken authToken = result.getData();
                    getPreferenceHelper().setString(PREF_AUTH_TOKEN, authToken.getAuthToken());
                    getPreferenceHelper().setBoolean(PREF_IS_USER_SIGNED_IN, false);
                    getPreferenceHelper().setBoolean(PREF_IS_GUEST_USER, true);

                    getUserDetails(new DataReceiver<UserDetailsOutput>() {
                        @Override
                        public void receiveData(UserDetailsOutput result) {
                            SultanBurgerApplication.getInstance().setUserDetail(result.getUserDetails().get(0));
                            ActivityConfig.startDashBoardActivity(AppBaseActivity.this);
                        }
                    });
                }

                @Override
                public void onFailed(Exception exception) {
                    dismissProgressBar();
                    ToastUtil.showToast(getApplicationContext(), exception.getMessage());
                }
            });
        }
    }

    public void signUp(String name, String email, String mobileNumber, String password, String confirmPassword) {
        final SignUpInput signUpInput = new SignUpInput();
        signUpInput.setName(name);
        signUpInput.setEmail(email);
        signUpInput.setMobileNumber(mobileNumber);
        signUpInput.setPassword(password);
        signUpInput.setConfirmPassword(confirmPassword);
        signUpInput.setDeviceTypeId(DEVICE_TYPE_ID);
        signUpInput.setDeviceId(deviceId);
        signUpInput.setDeviceToken(deviceToken);
        signUpInput.setLanguageId(LANGUAGE_ID);

        boolean valid = signUpInput.isValid(getApplicationContext());
        if (valid) {
            showProgressBar("Registering User");
            RestHelper.sendPost(AppBaseActivity.this, URI_PHONE_NUMBER_SIGN_UP, signUpInput, OTPOutput.class, new ResultReceiver<OTPOutput>() {
                @Override
                public void onCompleted(Result<OTPOutput> result) {
                    dismissProgressBar();

                    OTPOutput otpOutput = result.getData();
                    long otp = otpOutput.getOtp();

                    String authToken = otpOutput.getAuthToken();
                    getPreferenceHelper().setString(PREF_AUTH_TOKEN, authToken);

                    ActivityConfig.startOTPVerifyActivity(AppBaseActivity.this, otp, signUpInput.getMobileNumber());
                }

                @Override
                public void onFailed(Exception exception) {
                    dismissProgressBar();
                    ToastUtil.showToast(getApplicationContext(), exception.getMessage());
                }
            });
        }
    }

    public void signUpOTPVerify(String otp) {
        SignUpOTPInput signUpOTPInput = new SignUpOTPInput();
        signUpOTPInput.setOtp(otp);

        boolean valid = signUpOTPInput.isValid(getApplicationContext());
        if (valid) {
            showProgressBar("Validating OTP");
            RestHelper.sendPost(AppBaseActivity.this, URI_SIGN_UP_SUBMIT_OTP, signUpOTPInput, Void.class, new ResultReceiver<Void>() {
                @Override
                public void onCompleted(Result<Void> result) {
                    dismissProgressBar();

                    ActivityConfig.startDashBoardActivity(AppBaseActivity.this);
                }

                @Override
                public void onFailed(Exception exception) {
                    dismissProgressBar();
                    ToastUtil.showToast(getApplicationContext(), exception.getMessage());
                }
            });
        }
    }

    public void forgotPassword(String mobileNumber) {
        final ForgotPasswordInput forgotPasswordInput = new ForgotPasswordInput();
        forgotPasswordInput.setMobileNumber(mobileNumber);
        forgotPasswordInput.setDeviceTypeId(DEVICE_TYPE_ID);
        forgotPasswordInput.setDeviceId(deviceId);
        forgotPasswordInput.setDeviceToken(deviceToken);

        boolean valid = forgotPasswordInput.isValid(getApplicationContext());
        if (valid) {
            showProgressBar("Validating mobile number");
            RestHelper.sendPost(AppBaseActivity.this, URI_FORGOT_PASSWORD, forgotPasswordInput, OTPOutput.class, new ResultReceiver<OTPOutput>() {
                @Override
                public void onCompleted(Result<OTPOutput> result) {
                    dismissProgressBar();

                    OTPOutput otpOutput = result.getData();
                    long otp = otpOutput.getOtp();

                    String authToken = otpOutput.getAuthToken();
                    getPreferenceHelper().setString(PREF_AUTH_TOKEN, authToken);

                    ActivityConfig.startChangePasswordActivity(AppBaseActivity.this, otp, forgotPasswordInput.getMobileNumber());
                }

                @Override
                public void onFailed(Exception exception) {
                    dismissProgressBar();
                    ToastUtil.showToast(getApplicationContext(), exception.getMessage());
                }
            });
        }
    }

    public void forgotPasswordChange(String newPassword, String confirmPassword, String otp) {
        ForgotPasswordChangeInput forgotPasswordChangeInput = new ForgotPasswordChangeInput();
        forgotPasswordChangeInput.setOtp(otp);
        forgotPasswordChangeInput.setPassword(newPassword);
        forgotPasswordChangeInput.setConfirmPassword(confirmPassword);

        boolean valid = forgotPasswordChangeInput.isValid(getApplicationContext());
        if (valid) {
            showProgressBar("Saving password");
            RestHelper.sendPost(AppBaseActivity.this, URI_SUBMIT_FORGOT_PASSWORD_CHANGE, forgotPasswordChangeInput, Void.class, new ResultReceiver<Void>() {
                @Override
                public void onCompleted(Result<Void> result) {
                    dismissProgressBar();
                    ActivityConfig.startSignInActivity(AppBaseActivity.this);
                }

                @Override
                public void onFailed(Exception exception) {
                    dismissProgressBar();
                    ToastUtil.showToast(getApplicationContext(), exception.getMessage());
                }
            });
        }
    }

    public void logout() {
        showProgressBar("Logging out");
        RestHelper.sendPost(AppBaseActivity.this, URI_LOGOUT, null, Void.class, new ResultReceiver<Void>() {
            @Override
            public void onCompleted(Result<Void> result) {
                dismissProgressBar();
                getPreferenceHelper().setBoolean(PREF_IS_USER_SIGNED_IN, false);

                getPreferenceHelper().remove(PREF_AUTH_TOKEN, PREF_IS_USER_SIGNED_IN, PREF_IS_GUEST_USER, PREF_OPTION_SELECTION_TYPE);
                SultanBurgerApplication.getInstance().setUserDetail(null);
                ActivityConfig.startOptionActivity(AppBaseActivity.this);
            }

            @Override
            public void onFailed(Exception exception) {
                dismissProgressBar();
                ToastUtil.showToast(getApplicationContext(), exception.getMessage());
            }
        });
    }

    public void changePassword(String oldPassword, String newPassword, String confirmPassword) {
        ChangePasswordInput changePasswordInput = new ChangePasswordInput();
        changePasswordInput.setOldPassword(oldPassword);
        changePasswordInput.setNewPassword(newPassword);
        changePasswordInput.setConfirmPassword(confirmPassword);

        boolean valid = changePasswordInput.isValid(getApplicationContext());
        if (valid) {
            showProgressBar("Saving password");
            RestHelper.sendPost(AppBaseActivity.this, URI_CHANGE_PASSWORD, changePasswordInput, Void.class, new ResultReceiver<Void>() {
                @Override
                public void onCompleted(Result<Void> result) {
                    dismissProgressBar();
                    finish();
                }

                @Override
                public void onFailed(Exception exception) {
                    dismissProgressBar();
                    ToastUtil.showToast(getApplicationContext(), exception.getMessage());
                }
            });
        }
    }

    public void changeUserName(String userName, final DataReceiver<Boolean> dataReceiver) {
        showProgressBar("updating username");

        ChangeUserNameInput changeUserNameInput = new ChangeUserNameInput();
        changeUserNameInput.setName(userName);

        RestHelper.sendPost(AppBaseActivity.this, URI_CHANGE_USER_NAME, changeUserNameInput, Void.class, new ResultReceiver<Void>() {
            @Override
            public void onCompleted(Result<Void> result) {
                dismissProgressBar();
                dataReceiver.receiveData(true);
            }

            @Override
            public void onFailed(Exception exception) {
                dismissProgressBar();
                ToastUtil.showToast(getApplicationContext(), exception.getMessage());
            }
        });
    }

    public void getUserDetails(final DataReceiver<UserDetailsOutput> dataReceiver) {
        showProgressBar("Fetching userInfo");
        RestHelper.sendPost(AppBaseActivity.this, URI_VIEW_MY_DETAILS, null, UserDetailsOutput.class, new ResultReceiver<UserDetailsOutput>() {
            @Override
            public void onCompleted(Result<UserDetailsOutput> result) {
                dismissProgressBar();
                dataReceiver.receiveData(result.getData());
            }

            @Override
            public void onFailed(Exception exception) {
                dismissProgressBar();
                dataReceiver.receiveData(null);
                ToastUtil.showToast(getApplicationContext(), exception.getMessage());
            }
        });
    }

    public void addUserAddress(AddUserAddressInput addUserAddressInput, final DataReceiver<AddUserAddressOutput> dataReceiver) {
        showProgressBar("Adding new address");
        RestHelper.sendPost(AppBaseActivity.this, URI_ADD_USER_ADDRESS, addUserAddressInput, AddUserAddressOutput.class, new ResultReceiver<AddUserAddressOutput>() {
            @Override
            public void onCompleted(Result<AddUserAddressOutput> result) {
                dismissProgressBar();
                dataReceiver.receiveData(result.getData());
            }

            @Override
            public void onFailed(Exception exception) {
                dismissProgressBar();
                dataReceiver.receiveData(null);
                ToastUtil.showToast(getApplicationContext(), exception.getMessage());
            }
        });
    }

    public void getUserAddress(final DataReceiver<ListUserAddressOutput> dataReceiver) {
        showProgressBar("Getting address");
        RestHelper.sendPost(AppBaseActivity.this, URI_LIST_USER_ADDRESS, null, ListUserAddressOutput.class, new ResultReceiver<ListUserAddressOutput>() {
            @Override
            public void onCompleted(Result<ListUserAddressOutput> result) {
                dismissProgressBar();
                dataReceiver.receiveData(result.getData());
            }

            @Override
            public void onFailed(Exception exception) {
                dismissProgressBar();
                dataReceiver.receiveData(null);
                ToastUtil.showToast(getApplicationContext(), exception.getMessage());
            }
        });
    }

    public void getListUserNearByBranches(LatLng latLng, String page, final DataReceiver<PagedBranchOutput> dataReceiver) {
        ListUserNearByBranchesInput listUserNearByBranchesInput = new ListUserNearByBranchesInput();
        listUserNearByBranchesInput.setLatitude(String.valueOf(latLng.latitude));
        listUserNearByBranchesInput.setLongitude(String.valueOf(latLng.longitude));
        listUserNearByBranchesInput.setPage(page);

        boolean valid = listUserNearByBranchesInput.isValid(getApplicationContext());
        if (valid) {
            showProgressBar("Looking for nearby branches");
            RestHelper.sendPost(AppBaseActivity.this, URI_LIST_USER_NEARBY_BRANCHES, listUserNearByBranchesInput, PagedBranchOutput.class, new ResultReceiver<PagedBranchOutput>() {
                @Override
                public void onCompleted(Result<PagedBranchOutput> result) {
                    dismissProgressBar();
                    dataReceiver.receiveData(result.getData());
                }

                @Override
                public void onFailed(Exception exception) {
                    dismissProgressBar();
                    dataReceiver.receiveData(null);
                    ToastUtil.showToast(getApplicationContext(), exception.getMessage());
                }
            });
        }
    }

    public void getSlidingImages(String branchId, final DataReceiver<SlidingImagesOutput> dataReceiver) {
        RestHelper.sendPost(AppBaseActivity.this, URI_GET_SLIDER_IMAGES, branchId, SlidingImagesOutput.class, new ResultReceiver<SlidingImagesOutput>() {
            @Override
            public void onCompleted(Result<SlidingImagesOutput> result) {
                dataReceiver.receiveData(result.getData());
            }

            @Override
            public void onFailed(Exception exception) {
                dataReceiver.receiveData(null);
                ToastUtil.showToast(getApplicationContext(), exception.getMessage());
            }
        });
    }

    public void getMenuCategory(String branchId, final DataReceiver<MenuCategoryOutput> dataReceiver) {
        RestHelper.sendPost(AppBaseActivity.this, URI_LIST_MENU_CATEGORIES, branchId, MenuCategoryOutput.class, new ResultReceiver<MenuCategoryOutput>() {
            @Override
            public void onCompleted(Result<MenuCategoryOutput> result) {
                dataReceiver.receiveData(result.getData());
            }

            @Override
            public void onFailed(Exception exception) {
                dataReceiver.receiveData(null);
                ToastUtil.showToast(getApplicationContext(), exception.getMessage());
            }
        });
    }

    public void getListMenu(String branchId, String menuCategoryId, int orderType, String page, final DataReceiver<ListMenuOutput> dataReceiver) {
        ListMenuInputNew listMenuInput = new ListMenuInputNew();
        listMenuInput.setBranchId(branchId);
        listMenuInput.setMenuCategoryId(menuCategoryId);
        listMenuInput.setOrderType(orderType);
        listMenuInput.setUserAddressId(null);
        listMenuInput.setPage(page);

        ObjectMapper mapperObj = new ObjectMapper();


        try {
            // get Employee object as a json string
            String jsonStr = mapperObj.writeValueAsString(listMenuInput);
            Log.d("jsonmessage",jsonStr);
            System.out.println(jsonStr);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }

        RestHelper.sendPost(AppBaseActivity.this, URI_LIST_MENUS, listMenuInput, ListMenuOutput.class, new ResultReceiver<ListMenuOutput>() {
            @Override
            public void onCompleted(Result<ListMenuOutput> result) {
                dataReceiver.receiveData(result.getData());
            }

            @Override
            public void onFailed(Exception exception) {
                dataReceiver.receiveData(null);
                ToastUtil.showToast(getApplicationContext(), exception.getMessage());
            }
        });
    }

    public void getListCartProduct(String branchId, int orderType, String useraddressId ,final DataReceiver<ListCartOutput> dataReceiver) {
        ListCartProduct listCartProduct = new ListCartProduct();
        listCartProduct.setBranchId(branchId);
        listCartProduct.setOrderType(orderType);
        listCartProduct.setUserAddressId(useraddressId);
        RestHelper.sendPost(AppBaseActivity.this,URI_CART_PRODUCT_LIST, listCartProduct, ListCartOutput.class, new ResultReceiver<ListCartOutput>() {
            @Override
            public void onCompleted(Result<ListCartOutput> result) {
                dataReceiver.receiveData(result.getData());

                Log.d("result",result.getData()+"");

            }

            @Override
            public void onFailed(Exception exception) {
                dataReceiver.receiveData(null);
                ToastUtil.showToast(getApplicationContext(), exception.getMessage());
            }
        });
    }

    public void AddMenuItemToCart(String branchId, int orderType, String UserAddressId , String menuItemId, List<Integer> modifiers , List<Integer> addons,
                                  final DataReceiver<AddtoCartOutput> dataReceiver) {

        ListMenuInput listMenuInput = new ListMenuInput();
        listMenuInput.setBranchId(branchId);
        listMenuInput.setMenuItemid(menuItemId);
        listMenuInput.setOrderType(orderType);
        listMenuInput.setUserAddressId(UserAddressId);
       /* listMenuInput.setModifiers(modifiers);
        listMenuInput.setAddons(addons);*/

        RestHelper.sendPost(AppBaseActivity.this,URI_ADD_MENUS_TO_CART, listMenuInput, AddtoCartOutput.class, new ResultReceiver<AddtoCartOutput>() {
            @Override
            public void onCompleted(Result<AddtoCartOutput> result) {
                dataReceiver.receiveData(result.getData());
                ToastUtil.showToast(getApplicationContext(),result.getData()+"");

            }

            @Override
            public void onFailed(Exception exception) {
                dataReceiver.receiveData(null);

                Log.d("message",exception.getMessage());
                ToastUtil.showToast(getApplicationContext(), exception.getMessage());
            }
        });
    }

    public void getModifiers(String menuItemId, final DataReceiver<ModifiersOutput> dataReceiver) {
        ModifiersInput modifiersInput = new ModifiersInput();
        modifiersInput.setMenuItemId(menuItemId);

        showProgressBar("Loading modifiers");
        RestHelper.sendPost(AppBaseActivity.this, URI_LIST_MENUS_MODIFIERS, modifiersInput, ModifiersOutput.class, new ResultReceiver<ModifiersOutput>() {
            @Override
            public void onCompleted(Result<ModifiersOutput> result) {
                dismissProgressBar();
                dataReceiver.receiveData(result.getData());
            }

            @Override
            public void onFailed(Exception exception) {
                dismissProgressBar();
                dataReceiver.receiveData(null);
                ToastUtil.showToast(getApplicationContext(), exception.getMessage());
            }
        });
    }

    public void getAddOns(String menuItemId, final DataReceiver<AddOnsOutput> dataReceiver) {
        AddOnsInput addOnsInput = new AddOnsInput();
        addOnsInput.setMenuItemId(menuItemId);

        showProgressBar("Loading addons");
        RestHelper.sendPost(AppBaseActivity.this, URI_LIST_MENUS_ADDONS, addOnsInput, AddOnsOutput.class, new ResultReceiver<AddOnsOutput>() {
            @Override
            public void onCompleted(Result<AddOnsOutput> result) {
                dismissProgressBar();
                dataReceiver.receiveData(result.getData());
            }

            @Override
            public void onFailed(Exception exception) {
                dismissProgressBar();
                dataReceiver.receiveData(null);
                ToastUtil.showToast(getApplicationContext(), exception.getMessage());
            }
        });
    }

    public void getMyPendingOrders(final DataReceiver<MyOrdersOutput> dataReceiver) {
        showProgressBar("Loading my pending orders");
        RestHelper.sendPost(AppBaseActivity.this, URI_MY_PENDING_ORDERS, null, MyOrdersOutput.class, new ResultReceiver<MyOrdersOutput>() {
            @Override
            public void onCompleted(Result<MyOrdersOutput> result) {
                dismissProgressBar();
                dataReceiver.receiveData(result.getData());
            }

            @Override
            public void onFailed(Exception exception) {
                dismissProgressBar();
                dataReceiver.receiveData(null);
                ToastUtil.showToast(getApplicationContext(), exception.getMessage());
            }
        });
    }

    public void getMyOrders(final DataReceiver<MyOrdersOutput> dataReceiver) {
        showProgressBar("Loading my orders");
        RestHelper.sendPost(AppBaseActivity.this, URI_MY_ORDERS, null, MyOrdersOutput.class, new ResultReceiver<MyOrdersOutput>() {
            @Override
            public void onCompleted(Result<MyOrdersOutput> result) {
                dismissProgressBar();
                dataReceiver.receiveData(result.getData());
            }

            @Override
            public void onFailed(Exception exception) {
                dismissProgressBar();
                dataReceiver.receiveData(null);
                ToastUtil.showToast(getApplicationContext(), exception.getMessage());
            }
        });
    }
}
