package com.sultanburger.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.sultanburger.AppBaseFragment;
import com.sultanburger.R;
import com.sultanburger.activity.handler.DashBoardHandler;
import com.sultanburger.data.output.ListCartOutput;
import com.sultanburger.data.output.ListMenu;
import com.sultanburger.data.output.ListMenuOutput;
import com.sultanburger.fragment.adapter.MenuTabDataAdapter;
import com.sultanburger.helper.recyclerview.RVItemTouchHelper;
import com.sultanburger.utils.DataReceiver;
import com.sultanburger.utils.Validator;

import java.util.List;

import static com.sultanburger.helper.permission.PermissionHelper.init;

public class CartFragment extends AppBaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = CartFragment.class.getSimpleName();

    private DashBoardHandler dashBoardHandler;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView customizeone,customizetwo;
    String[] font = new String[]{ "6:00 PM","7:00 PM","8:00 PM" };
    Spinner spinnertime;
    LinearLayout linearLayout;
    Button addproduct, placeorder;
    private MediaDescriptionCompat.Builder supportActionBar;
    int minteger = 0;


    public CartFragment() {

    }

    public static CartFragment newInstance() {
        CartFragment cartFragment = new CartFragment();
        return cartFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        initdata();
        customizeone = (TextView) view.findViewById(R.id.customizeones);
        customizetwo = (TextView) view.findViewById(R.id.customizetwo);
        addproduct = (Button) view.findViewById(R.id.addproduct);
        spinnertime = (Spinner)view. findViewById(R.id.spinnertime);
        spinnertime.setOnItemSelectedListener(CartFragment.this);


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.spinnertime, font);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinnertime);
        spinnertime.setAdapter(spinnerArrayAdapter);



        placeorder = (Button) view.findViewById(R.id.placeorder);
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent placeord = new Intent(getActivity(), LoginTwoActivity.class);
                startActivity(placeord);*/
            }
        });
        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        customizetwo.setOnClickListener(new View.OnClickListener() {
            private MediaDescriptionCompat.Builder supportActionBar;

            public MediaDescriptionCompat.Builder getSupportActionBar() {
                return supportActionBar;
            }

            @Override
            public void onClick(View view) {

            }
        });
        customizeone.setOnClickListener(new View.OnClickListener() {


            private MediaDescriptionCompat.Builder supportActionBar;


            @Override
            public void onClick(View view) {


            }
        });

        return view;
    }

    private void initdata() {


        getAppBaseActivity().getListCartProduct("1", 2, "7", "1", new DataReceiver<ListCartOutput>() {


            @Override
            public void receiveData(ListCartOutput result) {
                if (Validator.isValid(result)) {

                    List<ListMenu> menuLists = result.getMenuLists();
                    if (Validator.isValid(menuLists) && !menuLists.isEmpty()) {
                        for (ListMenu listMenu : menuLists) {
                            String menuImageUrl = listMenu.getMenuImageUrl();
                            listMenu.setMenuImageUrl(imageBaseUrl + menuImageUrl);
                        }
/*
                        MenuTabDataAdapter menuTabDataAdapter = new MenuTabDataAdapter(menuLists, dashBoardHandler, MenuTabDataFragment.this);
                        recyclerViewMenus.setAdapter(menuTabDataAdapter);

                        RVItemTouchHelper rvItemTouchHelper = new RVItemTouchHelper(getContext(), menuTabDataAdapter);
                        rvItemTouchHelper.setSwipeEnabled(false);
                        rvItemTouchHelper.setSwipeDirectionRight(false);

                        itemTouchHelper = new ItemTouchHelper(rvItemTouchHelper);
                        itemTouchHelper.attachToRecyclerView(recyclerViewMenus);*/
                    }
                }
            }
        });
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
        inflater.inflate(R.menu.menu_notification_only, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_notification:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



}
