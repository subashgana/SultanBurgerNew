package com.sultanburger.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sultanburger.AppBaseFragment;
import com.sultanburger.R;
import com.sultanburger.activity.handler.DashBoardHandler;
import com.sultanburger.data.OptionSelectionType;
import com.sultanburger.data.output.ListMenu;
import com.sultanburger.data.output.ListMenuOutput;
import com.sultanburger.data.output.MenuCategory;
import com.sultanburger.fragment.adapter.MenuTabDataAdapter;
import com.sultanburger.helper.imagecache.ImageUtil;
import com.sultanburger.helper.imagecache.PauseOnScrollListener;
import com.sultanburger.helper.recyclerview.RVGridSpacingDecoration;
import com.sultanburger.helper.recyclerview.RVItemDragCallback;
import com.sultanburger.helper.recyclerview.RVItemTouchHelper;
import com.sultanburger.utils.DataReceiver;
import com.sultanburger.utils.Validator;

import java.util.List;

public class MenuTabDataFragment extends AppBaseFragment implements View.OnClickListener, RVItemDragCallback {

    private static final String TAG = MenuTabDataFragment.class.getSimpleName();

    private RecyclerView recyclerViewMenus;
    private ItemTouchHelper itemTouchHelper;

    private String branchId;
    private MenuCategory menuCategoryOutput;
    private DashBoardHandler dashBoardHandler;

    public MenuTabDataFragment() {

    }

    public static MenuTabDataFragment newInstance() {
        MenuTabDataFragment menuFragment = new MenuTabDataFragment();
        return menuFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_tab_data, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerViewMenus = view.findViewById(R.id.menuTabData_recyclerView_menus);
        recyclerViewMenus.setLayoutManager(linearLayoutManager);
        recyclerViewMenus.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMenus.addItemDecoration(new RVGridSpacingDecoration(1, 30, true));
        recyclerViewMenus.setHasFixedSize(true);
        recyclerViewMenus.addOnScrollListener(new PauseOnScrollListener(ImageUtil.getImageLoader(getContext()), false, false));

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
    public void onClick(View view) {
        int id = view.getId();
    }

    @Override
    public void onDrag(RecyclerView.ViewHolder viewHolder) {

    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public void setMenuCategoryOutput(MenuCategory menuCategoryOutput) {
        this.menuCategoryOutput = menuCategoryOutput;
    }

    private void populateRecyclerView() {
        int orderType = 1;

        String optionSelectionTypeStr = getAppBaseActivity().getPreferenceHelper().getString(PREF_OPTION_SELECTION_TYPE);
        if (Validator.isValid(optionSelectionTypeStr)) {
            OptionSelectionType optionSelectionType = OptionSelectionType.valueOf(optionSelectionTypeStr);
            switch (optionSelectionType) {
                case PICKUP:
                    orderType = 1;
                    break;

                case DELIVERY:
                    orderType = 2;
                    break;
            }
        }

        getAppBaseActivity().getListMenu(branchId, menuCategoryOutput.getMenuCategoryId(), orderType, "1", new DataReceiver<ListMenuOutput>() {
            @Override
            public void receiveData(ListMenuOutput result) {
                if (Validator.isValid(result)) {
                    String imageBaseUrl = result.getImageBaseUrl();

                    List<ListMenu> menuLists = result.getMenuLists();
                    if (Validator.isValid(menuLists) && !menuLists.isEmpty()) {
                        for (ListMenu listMenu : menuLists) {
                            String menuImageUrl = listMenu.getMenuImageUrl();
                            listMenu.setMenuImageUrl(imageBaseUrl + menuImageUrl);
                        }

                        MenuTabDataAdapter menuTabDataAdapter = new MenuTabDataAdapter(menuLists, dashBoardHandler, MenuTabDataFragment.this);
                        recyclerViewMenus.setAdapter(menuTabDataAdapter);

                        RVItemTouchHelper rvItemTouchHelper = new RVItemTouchHelper(getContext(), menuTabDataAdapter);
                        rvItemTouchHelper.setSwipeEnabled(false);
                        rvItemTouchHelper.setSwipeDirectionRight(false);

                        itemTouchHelper = new ItemTouchHelper(rvItemTouchHelper);
                        itemTouchHelper.attachToRecyclerView(recyclerViewMenus);
                    }
                }
            }
        });
    }
}
