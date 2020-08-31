package com.inventrax_pepsi.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inventrax_pepsi.R;
import com.inventrax_pepsi.adapters.NavigationDrawerAdapter;
import com.inventrax_pepsi.application.AppController;
import com.inventrax_pepsi.common.Log.Logger;
import com.inventrax_pepsi.common.SFACommon;
import com.inventrax_pepsi.model.NavDrawerItem;
import com.inventrax_pepsi.sfa.pojos.User;
import com.inventrax_pepsi.util.DialogUtils;
import com.inventrax_pepsi.util.ProgressDialogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nareshp on 05/01/2016.
 */
public class DrawerFragment extends Fragment implements View.OnClickListener {

    private static String TAG = DrawerFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    private FragmentDrawerListener drawerListener;
    private View layout;
    private TextView txtLoginUser;
    private User user;
    private AppCompatActivity appCompatActivity;
    private List<NavDrawerItem> menuItemList;
    private IntentFilter mIntentFilter;
    private CounterBroadcastReceiver counterBroadcastReceiver;
    private SFACommon sfaCommon;


    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        appCompatActivity = (AppCompatActivity) getActivity();

        menuItemList = new ArrayList<>();

        sfaCommon = new SFACommon();

        loadFormControls();

        return layout;
    }

    public void loadFormControls() {
        try {


            mIntentFilter = new IntentFilter();
            mIntentFilter.addAction("com.inventrax.broadcast.counter");

            counterBroadcastReceiver = new CounterBroadcastReceiver();

            menuItemList = sfaCommon.getMenuItemsByUserType(AppController.getUser().getUserTypeId());

            new ProgressDialogUtils(getContext());

            user = AppController.getUser();

            recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
            txtLoginUser = (TextView) layout.findViewById(R.id.txtLoginUser);

            if (user != null) {
                txtLoginUser.setText(user.getLoginUserId() + " [ " + user.getUserType() + " ] ");
            }

            adapter = new NavigationDrawerAdapter(getActivity(), menuItemList);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
                @Override
                public void onClick(View view, int position) {

                    NavigationDrawerAdapter.setSelectedItemPosition(position);

                    recyclerView.getAdapter().notifyDataSetChanged();

                    drawerListener.onDrawerItemSelected(view, position, menuItemList.get(position));
                    mDrawerLayout.closeDrawer(containerView);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));


        } catch (Exception ex) {
            Logger.Log(DrawerFragment.class.getName(), ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while loading menu list");
            return;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*try
        {
            if ( user != null ) {

                appCompatActivity.getSupportActionBar().setTitle(StringUtils.toCamelCase(user.getFirstName()));
                appCompatActivity.getSupportActionBar().setSubtitle(user.getUserType().toUpperCase() + "  " + new SimpleDateFormat("dd-MM-yyyy").format(new Date()) );

            }

        }catch (Exception ex){
            Logger.Log(DrawerFragment.class.getName(),ex);
            DialogUtils.showAlertDialog(getActivity(), "Error while loading menu list");
            return;
        }*/

    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {

        try {
            containerView = getActivity().findViewById(fragmentId);
            mDrawerLayout = drawerLayout;
            mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    getActivity().invalidateOptionsMenu();
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    getActivity().invalidateOptionsMenu();
                }

                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    super.onDrawerSlide(drawerView, slideOffset);
                    toolbar.setAlpha(1 - slideOffset / 2);
                }
            };

            mDrawerLayout.setDrawerListener(mDrawerToggle);
            mDrawerLayout.post(new Runnable() {
                @Override
                public void run() {
                    mDrawerToggle.syncState();
                }
            });
        }catch (Exception ex){
            Logger.Log(DrawerFragment.class.getName(),ex);
            return;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(counterBroadcastReceiver, mIntentFilter);

    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(counterBroadcastReceiver);
        super.onPause();

    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public interface FragmentDrawerListener {
        void onDrawerItemSelected(View view, int position, NavDrawerItem menuItem);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            try {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                    clickListener.onClick(child, rv.getChildPosition(child));
                }
                return false;

            }catch(Exception ex){
                Logger.Log(DrawerFragment.class.getName(),ex);
                return false;

            }
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public class CounterBroadcastReceiver extends BroadcastReceiver {

        public CounterBroadcastReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {

            try {

                if (menuItemList != null)
                    menuItemList.clear();

                menuItemList = sfaCommon.getMenuItemsByUserType(AppController.getUser().getUserTypeId());

                adapter.setNavigationDrawerAdapter(getActivity(), menuItemList);
                adapter.notifyDataSetChanged();

            }catch (Exception ex){
                Logger.Log(DrawerFragment.class.getName(),ex);
                return;
            }

        }

    }


}