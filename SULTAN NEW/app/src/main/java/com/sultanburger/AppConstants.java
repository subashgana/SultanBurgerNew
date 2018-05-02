package com.sultanburger;

public interface AppConstants {

    String SELECT = "Select Option";
    String CAMERA = "Camera";
    String GALLERY = "Gallery";

    int ZOOM_LEVEL_NORMAL = 16;
    int ZOOM_LEVEL_OUT = 10;
    int TILT_ANGLE = 0;

    // Data
    String ENTER = "Enter ";
    String UNABLE_TO_GET_CURRENT_LOCATION = "Unable to get current location";
    String SELECT_THE_BRANCH = "Select the branch";

    // Intent Result
    int INTENT_RESULT_CAMERA = 100;
    int INTENT_RESULT_GALLERY = 101;

    // Extras
    String EXTRA_OVERRIDE_TRANSITION = "extraOverrideTransition";
    String EXTRA_OTP = "extraOTP";
    String EXTRA_MOBILE_NUMBER = "extraMobileNumber";

    // Preference
    String PREF_IS_USER_SIGNED_IN = "prefIsUserSignedIn";
    String PREF_IS_GUEST_USER = "prefIsGuestUser";
    String PREF_OPTION_SELECTION_TYPE = "prefOptionSelectionType";
    String PREF_AUTH_TOKEN = "prefAuthToken";
    String USER_NAME = "username";
    String PASSWORD = "password";

    // Broadcast
    String BROADCAST_OTP_RECEIVER = "com.sultanburger.OTP_RECEIVER";
    String BROADCAST_OTP_RECEIVER_DATA = "OTP_RECEIVER_DATA";

    // Rest Uri
    String URI_SIGN_IN = "apiSignIn";
    String URI_REGISTER_GUEST_USER = "apiRegisterGuestUser";
    String URI_PHONE_NUMBER_SIGN_UP = "apiPhoneNumberSignUp";
    String URI_SIGN_UP_SUBMIT_OTP = "apiSignUpSubmitOTP";
    String URI_FORGOT_PASSWORD = "apiForgotPassword";
    String URI_SUBMIT_FORGOT_PASSWORD_CHANGE = "apiSubmitForgotPasswordOTP";
    String URI_CHECK_FORGOT_PASSWORD_CHANGE = "apiCheckForgotPasswordOTP";
    String URI_LOGOUT = "apiLogout";
    String URI_CHANGE_PASSWORD = "apiChangePassword";
    String URI_UPDATE_USER_LANGUAGE = "apiUpdateUserLanguage";
    String URI_CHANGE_USER_NAME = "apiChangeUserName";
    String URI_UPDATE_USER_IMAGE = "apiUpdateUserImage";
    String URI_VIEW_MY_DETAILS = "apiViewMyDetails";
    String URI_ADD_USER_ADDRESS = "apiAddUserAddress";
    String URI_LIST_USER_ADDRESS = "apiListUserAddress";
    String URI_LIST_USER_NEARBY_BRANCHES = "apiListUserNearbyBranches";
    String URI_SEARCH_USER_NEARBY_BRANCHES = "apiSearchUserNearbyBranches";
    String URI_GET_SLIDER_IMAGES = "apiGetSliderImage";
    String URI_LIST_MENU_CATEGORIES = "apiListMenuCategories";
    String URI_LIST_MENUS = "apiListMenus";
    String URI_LIST_MENUS_MODIFIERS = "apiListMenuModifiers";
    String URI_LIST_MENUS_ADDONS = "apiListMenuAddons";
    String URI_ADD_MENUS_TO_CART = "apiAddMenuToCart";
    String URI_REMOVE_MENUS_FROM_CART = "apiremoveMenuFromCart";
    String URI_CART_PRODUCT_LIST = "apiListMyCartProducts";
    String URI_REMOVE_PRODUCT_FROM_CART = "apiRemoveProductFromCart";
    String URI_PLACE_ORDER = "apiPlaceOrder";
    String URI_MY_PENDING_ORDERS = "apiListPendingOrders";
    String URI_MY_ORDERS = "apiListMyOrders";
    String URI_ORDERS_PROGRESS_DETAIL = "apiOrderProgressDetail";

    // TODO - Remove
    String AVATAR = "http://www.newsshare.in/wp-content/uploads/2017/04/Miniclip-8-Ball-Pool-Avatar-16.png";
}
