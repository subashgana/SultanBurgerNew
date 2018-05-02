package com.sultanburger.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.sultanburger.AppBaseFragment;
import com.sultanburger.R;
import com.sultanburger.activity.handler.DashBoardHandler;
import com.sultanburger.data.Location;
import com.sultanburger.data.output.BranchOutput;
import com.sultanburger.data.output.PagedBranchOutput;
import com.sultanburger.fragment.adapter.BranchInfoAdapter;
import com.sultanburger.fragment.handler.BranchHandler;
import com.sultanburger.helper.imagecache.ImageUtil;
import com.sultanburger.helper.imagecache.PauseOnScrollListener;
import com.sultanburger.helper.recyclerview.RVGridSpacingDecoration;
import com.sultanburger.helper.recyclerview.RVItemDragCallback;
import com.sultanburger.helper.recyclerview.RVItemTouchHelper;
import com.sultanburger.utils.DataReceiver;
import com.sultanburger.utils.ToastUtil;
import com.sultanburger.utils.Validator;

import java.util.List;

public class PromosFragment extends AppBaseFragment implements View.OnClickListener, BranchHandler, RVItemDragCallback {

    private static final String TAG = PromosFragment.class.getSimpleName();

    private EditText editTextSelectBranch;
    private RecyclerView recyclerView;
    private ItemTouchHelper itemTouchHelper;

    private DashBoardHandler dashBoardHandler;

    public PromosFragment() {

    }

    public static PromosFragment newInstance() {
        PromosFragment promosFragment = new PromosFragment();
        return promosFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promos, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        editTextSelectBranch = view.findViewById(R.id.promos_editText_typeToSelectBranch);
        editTextSelectBranch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                BranchInfoAdapter branchInfoAdapter = (BranchInfoAdapter) recyclerView.getAdapter();
                if (Validator.isValid(branchInfoAdapter))
                    branchInfoAdapter.getFilter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView = view.findViewById(R.id.promos_recyclerView_branchInfo);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RVGridSpacingDecoration(1, 20, true));
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new PauseOnScrollListener(ImageUtil.getImageLoader(getContext()), false, false));

        populateRecyclerView();
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
    public void onBranchSelected(BranchOutput branchOutput) {

    }

    @Override
    public void onDrag(RecyclerView.ViewHolder viewHolder) {

    }

    private void populateRecyclerView() {
        getLastKnownLocation(new DataReceiver<Location>() {
            @Override
            public void receiveData(Location result) {
                if (Validator.isValid(result)) {
                    LatLng latLng = new LatLng(result.getLatitude(), result.getLongitude());

                    getAppBaseActivity().getListUserNearByBranches(latLng, "1", new DataReceiver<PagedBranchOutput>() {
                        @Override
                        public void receiveData(PagedBranchOutput result) {
                            if (Validator.isValid(result)) {
                                List<BranchOutput> branchLists = result.getBranchLists();
                                if (Validator.isValid(branchLists) && !branchLists.isEmpty()) {
                                    BranchInfoAdapter branchInfoAdapter = new BranchInfoAdapter(branchLists, PromosFragment.this, PromosFragment.this);
                                    recyclerView.setAdapter(branchInfoAdapter);

                                    RVItemTouchHelper rvItemTouchHelper = new RVItemTouchHelper(getContext(), branchInfoAdapter);
                                    rvItemTouchHelper.setSwipeEnabled(false);
                                    rvItemTouchHelper.setSwipeDirectionRight(false);

                                    itemTouchHelper = new ItemTouchHelper(rvItemTouchHelper);
                                    itemTouchHelper.attachToRecyclerView(recyclerView);
                                }
                            }
                        }
                    });
                } else {
                    ToastUtil.showToast(getContext(), UNABLE_TO_GET_CURRENT_LOCATION);
                }
            }
        });
    }
}
