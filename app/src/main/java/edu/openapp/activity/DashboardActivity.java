package edu.openapp.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.openapp.R;
import edu.openapp.fragments.DashboardFragment;

/**
 * Created by Ankit on 19/01/18.
 */

public class DashboardActivity extends BaseActivity {

    @Bind(R.id.navigation_view)
    NavigationView navigationView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawer)
    DrawerLayout drawerLayout;
    private MenuItem previousMenuItem;
    private View header;
    private static Fragment resultFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        overridePendingTransition(0, 0);
        header = navigationView.getHeaderView(0);
        final CircleImageView profilePhoto = (CircleImageView) header.findViewById(R.id.profile_image);
        TextView txtWelcome = (TextView) header.findViewById(R.id.txtWelcome);
        TextView txtName = (TextView) header.findViewById(R.id.txtName);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if (previousMenuItem != null)
                    previousMenuItem.setChecked(false);

                menuItem.setCheckable(true);
                menuItem.setChecked(true);

                previousMenuItem = menuItem;

                drawerLayout.closeDrawers();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (menuItem.getItemId()) {
                    case R.id.list:

                        DashboardFragment dashboardFragment = new DashboardFragment();
                        fragmentTransaction.replace(R.id.frame, dashboardFragment);
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle((R.string.list));
                        BaseActivity.currentFragment = dashboardFragment;
                        return true;

                    case R.id.add:
                        startActivity(openActivity(DashboardActivity.this,AddMemberActivity.class));
                        return true;
                    case R.id.gallery:
                        startActivity(openActivity(DashboardActivity.this,GalleryActivity.class));

                        return true;

                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });


        navigationView.getMenu().getItem(0).setChecked(true);
        DashboardFragment fragment = new DashboardFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle(R.string.list);


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        if (BaseActivity.currentFragment != null && !(BaseActivity.currentFragment instanceof DashboardFragment)) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            DashboardFragment dashboardFragment = new DashboardFragment();
            fragmentTransaction.replace(R.id.frame, dashboardFragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(R.string.list);
            BaseActivity.currentFragment = dashboardFragment;
            return;
        }
        super.onBackPressed();
        finish();
    }


}
