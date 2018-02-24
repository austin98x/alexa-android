package com.austin.android.alexa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        OnMenuItemClickListener,
        OnMenuItemLongClickListener,
        FragmentManager.OnBackStackChangedListener {

    private static final String TAG = MainActivity.class.getName();

    private FragmentManager fragmentManager;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private Menu mMenu;
    private Toolbar mToolbar;
    private TextView mTitle;
    private long mLastClickTime = 0;
    private boolean mShowMenu = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);
        initToolbar();
        initMenu();
        //TODO first run show login activity
        addFragment(new MainFragment(), false, R.id.container);
    }

    private void initMenu() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(true);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
        mMenuDialogFragment.setItemLongClickListener(this);
    }

    private List<MenuObject> getMenuObjects() {
        List<MenuObject> menuObjects = new ArrayList<>();
        MenuObject help = new MenuObject(getString(R.string.menu_help));
        help.setResource(R.drawable.icn_1);
        MenuObject about = new MenuObject(getString(R.string.menu_about));
        about.setResource(R.drawable.icn_4);
        MenuObject share = new MenuObject(getString(R.string.menu_share));
        share.setResource(R.drawable.icn_2);
        MenuObject logout = new MenuObject(getString(R.string.menu_logout));
        logout.setResource(R.drawable.icn_5);
        menuObjects.add(help);
        menuObjects.add(about);
        menuObjects.add(share);
        menuObjects.add(logout);
        return menuObjects;
    }

    private void initToolbar() {
        mTitle = (TextView) findViewById(R.id.text_view_toolbar_title);
        mTitle.setText(R.string.home);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected void addFragment(Fragment fragment, boolean addToBackStack, int containerId) {
        invalidateOptionsMenu();
        String backStackName = fragment.getClass().getName();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStackName, 0);
        if (!fragmentPopped) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.add(containerId, fragment, backStackName);
            if (addToBackStack)
                transaction.addToBackStack(backStackName);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mShowMenu) {
            mMenu.findItem(R.id.context_menu).setVisible(true);
        } else {
            mMenu.findItem(R.id.context_menu).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mMenuDialogFragment != null && mMenuDialogFragment.isAdded()) {
            mMenuDialogFragment.dismiss();
        }

        if (fragmentManager.getBackStackEntryCount() > 0) {
            showHome();
        } else {
            if ((System.currentTimeMillis() - mLastClickTime) > 2000) {
                Toast.makeText(this, R.string.leave_app_text, Toast.LENGTH_SHORT).show();
                mLastClickTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        //Toast.makeText(this, "Clicked on position: " + position, Toast.LENGTH_SHORT).show();
        MenuAction(position);
    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {
        //Toast.makeText(this, "Long clicked on position: " + position, Toast.LENGTH_SHORT).show();
        MenuAction(position);
    }

    private void MenuAction(int position) {
        switch (position) {
            case 0:
                showHelp();
                break;
            case 1:
                showAbout();
                break;
            case 2:
                shareApp();
                break;
            case 3:
                logoutApp();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp() {
        boolean showBack = (fragmentManager.getBackStackEntryCount() > 0);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(showBack);
            if (showBack) {
                mToolbar.setNavigationIcon(R.drawable.btn_back);
            }
        }
    }

    public void showHome() {
        fragmentManager.popBackStack();
        mShowMenu = true;
        mMenu.findItem(R.id.context_menu).setVisible(true);
        mTitle.setText(R.string.home);
    }

    public void showHelp() {
        mShowMenu = false;
        mTitle.setText(R.string.help);
        addFragment(new HelpFragment(), true, R.id.container);
    }

    public void showAbout() {
        mShowMenu = false;
        mTitle.setText(R.string.about);
        addFragment(new AboutFragment(), true, R.id.container);
    }

    public void shareApp() {
        String shareTittle = getString(R.string.share_tittle);
        String shareContent = getString(R.string.share_content);
        String shareUrl = getString(R.string.share_url);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareTittle + "\n" + shareContent + "\n" + shareUrl);
        sendIntent.setType("text/plain");
        startActivityForResult(sendIntent, 0);
    }

    public void logoutApp() {
        Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
    }

}
