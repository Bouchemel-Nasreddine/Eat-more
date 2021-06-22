package com.exemple.eatmore;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MenuAdapter.OnMenuListener, CategoriesAdapter.OnCategoryListener {

    private static final String TAG = "TAG";
    private static ArrayList<Category> categoriesList;
    private static ArrayList<MenuItem> menuList, burgerMenuList, pizzaMenuList, chickenMenuList;
    private static LinearLayoutManager categoryManager, menuManager;
    private DrawerLayout drawer;
    private NavigationView navView;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private RecyclerView categoriesRecyclerView, menuRecyclerView;
    private CategoriesAdapter categoryAdapter;
    private MenuAdapter menuAdapter;
    private ShapeableImageView profile;
    private ExtendedFloatingActionButton bag;
    private TextView selectedCategoryName;

    public static void deselectCategoryRecycleViewItems(int position) {
        for (int i = 0; i < categoriesList.size(); i++) {
            Category item = categoriesList.get(i);
            if (!item.isClicked() || i == position) continue;
            item.changeClicked();
            ConstraintLayout layout = (ConstraintLayout) categoryManager.findViewByPosition(i);
            TextView name = layout.findViewById(R.id.category_name);
            ImageView image = layout.findViewById(R.id.category_picture);
            name.setTextColor(ContextCompat.getColor(layout.getContext(), R.color.text_grey));
            image.setImageResource(item.getDrawable_ID());
            layout.setBackground(ContextCompat.getDrawable(layout.getContext(), R.drawable.categorie_shadow));

        }
    }

    public static void deselectMenuRecyclerViewItems(int position) {
        MenuItem item;
        for (int i = 0; i < menuList.size(); i++) {
            item = menuList.get(i);
            if ((!item.isClicked() || i == position) && position != -1) continue;
            item.changeClicked();
            ConstraintLayout layout = (ConstraintLayout) menuManager.findViewByPosition(position);
            layout.setBackground(ContextCompat.getDrawable(layout.getContext(), R.drawable.categorie_shadow));
            TextView tv = layout.findViewById(R.id.menu_item_name);
            tv.setTextColor(ContextCompat.getColor(layout.getContext(), R.color.text_blue_dark));
            tv = layout.findViewById(R.id.menu_item_price);
            tv.setTextColor(ContextCompat.getColor(layout.getContext(), R.color.text_blue_dark));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("");

        FirebaseUtils.setUser();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.background_grey));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        menuList = new ArrayList<>();

        categoriesList = new ArrayList<>();
        categoriesList.add(new Category("burger", R.drawable.burger, R.drawable.burger_pink));
        categoriesList.add(new Category("pizza", R.drawable.pizza, R.drawable.pizza_pink));
        categoriesList.add(new Category("chicken", R.drawable.chicken, R.drawable.chicken_pink));

        burgerMenuList = new ArrayList<>();
        burgerMenuList.add(new MenuItem("Zinger burger", R.drawable.burger_zinger, 12, 4));
        burgerMenuList.add(new MenuItem("Chicken burger", R.drawable.burger_chicken, 15, 3));
        burgerMenuList.add(new MenuItem("Cheese burger", R.drawable.burger_cheese, 12, 4));
        burgerMenuList.add(new MenuItem("Cheese burger", R.drawable.burger_cheese, 12, 4));


        pizzaMenuList = new ArrayList<>();
        pizzaMenuList.add(new MenuItem("Buffalo Pizza", R.drawable.pizza_buffalo, 18, 5));
        pizzaMenuList.add(new MenuItem("Sea Fruits Pizza", R.drawable.pizza_sea_fruits, 23, 3));
        pizzaMenuList.add(new MenuItem("Vegetables Pizza", R.drawable.pizza_vegetables, 17, 4));

        chickenMenuList = new ArrayList<>();
        chickenMenuList.add(new MenuItem("Chicken Shawarma", R.drawable.chicken_shawarma, 20, 4));
        chickenMenuList.add(new MenuItem("Chicken Tandori", R.drawable.chicken_tandoori, 28, 5));
        chickenMenuList.add(new MenuItem("Chicken Nuggets", R.drawable.chiken_nuggets, 13, 4));


        copyArrayList(menuList, burgerMenuList);

        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.navigation_drawer);
        toolbar = findViewById(R.id.toolbar);
        categoriesRecyclerView = findViewById(R.id.categories_recycler_view);
        menuRecyclerView = findViewById(R.id.category_menu_recycler_view);
        profile = findViewById(R.id.main_page_profile);
        bag = findViewById(R.id.shopping_bag);
        selectedCategoryName = findViewById(R.id.selected_category_name);

        setSupportActionBar(toolbar);
        navView.bringToFront();
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.toggle);

        categoryAdapter = new CategoriesAdapter(categoriesList, this);
        categoriesRecyclerView.setAdapter(categoryAdapter);
        categoryManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        categoriesRecyclerView.setLayoutManager(categoryManager);

        menuAdapter = new MenuAdapter(menuList, this);
        menuRecyclerView.setAdapter(menuAdapter);
        menuManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        menuRecyclerView.setLayoutManager(menuManager);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });

        bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Glide.with(getApplicationContext())
                .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                .placeholder(R.drawable.loading)
                .centerCrop()
                .signature(new ObjectKey(2000))
                .into(profile);

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START);
        else {
            finish();
            System.exit(0);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        switch (item.getTitle().toString()) {
            case "Log Out":
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                finish();
                startActivity(new Intent(MainActivity.this, AuthenticationActivity.class));
        }
        return true;
    }

    @Override
    public void onMenuClick(int position) {
        ConstraintLayout layout = (ConstraintLayout) menuManager.findViewByPosition(position);
        MenuItem item = menuList.get(position);
        if (!item.isClicked()) {
            layout.setBackground(ContextCompat.getDrawable(this, R.drawable.menu_selected_background));
            TextView tv = layout.findViewById(R.id.menu_item_name);
            tv.setTextColor(ContextCompat.getColor(this, R.color.white));
            tv = layout.findViewById(R.id.menu_item_price);
            tv.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            layout.setBackground(ContextCompat.getDrawable(this, R.drawable.categorie_shadow));
            TextView tv = layout.findViewById(R.id.menu_item_name);
            tv.setTextColor(ContextCompat.getColor(this, R.color.text_blue_dark));
            tv = layout.findViewById(R.id.menu_item_price);
            tv.setTextColor(ContextCompat.getColor(this, R.color.text_blue_dark));
        }

        menuList.get(position).changeClicked();

    }

    @Override
    public void onCategoryClick(int position) {
        Category item = categoriesList.get(position);
        MainActivity.deselectCategoryRecycleViewItems(position);
        ConstraintLayout layout = (ConstraintLayout) categoryManager.findViewByPosition(position);
        TextView name = layout.findViewById(R.id.category_name);
        ImageView picture = layout.findViewById(R.id.category_picture);
        if (!item.isClicked()) {
            name.setTextColor(ContextCompat.getColor(layout.getContext(), R.color.pink));
            picture.setImageResource(item.getDrawable_pink_ID());
            layout.setBackground(ContextCompat.getDrawable(layout.getContext(), R.drawable.categorie_shadow_pink));
            selectedCategoryName.setText(name.getText().toString());


            int b;
            switch (position) {
                case 0:
                    copyArrayList(menuList, burgerMenuList);
                    menuAdapter = new MenuAdapter(menuList, this);
                    menuRecyclerView.setAdapter(menuAdapter);
                    menuRecyclerView.setLayoutManager(menuManager);
                    break;
                case 1:
                    copyArrayList(menuList, pizzaMenuList);
                    menuAdapter = new MenuAdapter(menuList, this);
                    menuRecyclerView.setAdapter(menuAdapter);
                    menuRecyclerView.setLayoutManager(menuManager);
                    break;
                case 2:
                    copyArrayList(menuList, chickenMenuList);
                    menuAdapter = new MenuAdapter(menuList, this);
                    menuRecyclerView.setAdapter(menuAdapter);
                    menuRecyclerView.setLayoutManager(menuManager);
                    break;

            }

        }
        item.changeClicked();


    }

    public void copyArrayList(ArrayList<MenuItem> menuList, ArrayList<MenuItem> list) {

        menuList.clear();
        for (MenuItem item : list
        ) {
            menuList.add(item);
        }
    }

    public class Category {
        private final String name;
        private final int drawable_ID;
        private final int drawable_pink_ID;
        private boolean clicked;

        public Category(String name, int drawable_ID, int drawable_pink_ID) {
            this.name = name;
            this.drawable_ID = drawable_ID;
            this.drawable_pink_ID = drawable_pink_ID;
            this.clicked = false;
        }

        public boolean isClicked() {
            return clicked;
        }

        public String getName() {
            return name;
        }

        public int getDrawable_ID() {
            return drawable_ID;
        }

        public int getDrawable_pink_ID() {
            return drawable_pink_ID;
        }

        public void changeClicked() {
            this.clicked = !this.clicked;
        }
    }

    public class MenuItem {
        private final String name;
        private final int price;
        private final int rating;
        private final int drawable_ID;
        private boolean clicked;

        public MenuItem(String name, int drawable_ID, int price, int rating) {
            this.name = name;
            this.drawable_ID = drawable_ID;
            this.price = price;
            this.rating = rating;
            this.clicked = false;
        }

        public int getDrawable_ID() {
            return drawable_ID;
        }

        public String getName() {
            return name;
        }

        public int getPrice() {
            return price;
        }

        public int getRating() {
            return rating;
        }

        public boolean isClicked() {
            return clicked;
        }

        public void changeClicked() {
            this.clicked = !this.clicked;
        }
    }

}