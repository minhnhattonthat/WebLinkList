package com.nathan.app.weblinklist.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;

import com.nathan.app.weblinklist.R;
import com.nathan.app.weblinklist.databinding.ActivityMainBinding;
import com.nathan.app.weblinklist.model.WebLink;
import com.nathan.app.weblinklist.network.RetrofitClient;
import com.nathan.app.weblinklist.utils.HtmlParser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ObservableInt selectedNumber = new ObservableInt(0);

    private ActivityMainBinding binding;
    private AlertDialog alertDialog;
    private LinkAdapter linkAdapter;
    private SelectionTracker<Long> tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> tracker.clearSelection());

        binding.setSelectedNumber(selectedNumber);

        binding.fab.setOnClickListener(view -> {
            if (alertDialog == null) {
                initDialog();
            }
            alertDialog.show();
        });

        setupRecyclerView();

        addWebLink("https://www.channelnewsasia.com");
        addWebLink("https://sg.yahoo.com");
        addWebLink("https://www.google.com");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sort:
                linkAdapter.setAscending(!linkAdapter.getAscending());
                return true;
            case R.id.action_delete:
                linkAdapter.deleteSelected();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
        @SuppressLint("InflateParams")
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add, null);
        EditText editText = dialogView.findViewById(R.id.link_text);
        alertDialog = alertBuilder.setTitle(R.string.dialog_title_add)
                .setView(dialogView)
                .setPositiveButton(R.string.dialog_button_add, (dialog, which) -> {
                    String link = editText.getText().toString();
                    if (URLUtil.isValidUrl(link)) {
                        addWebLink(link);
                    } else {
                        Toast.makeText(this, R.string.error_invalid_url, Toast.LENGTH_SHORT).show();
                    }
                    editText.setText("");
                }).create();
    }

    private void setupRecyclerView() {
        linkAdapter = new LinkAdapter();

        binding.webList.setAdapter(linkAdapter);

        tracker = new SelectionTracker.Builder<>(
                getString(R.string.selection_id),
                binding.webList,
                new WebLinkKeyProvider(binding.webList),
                new WebLinkDetailsLookup(binding.webList),
                StorageStrategy.createLongStorage())
                .withSelectionPredicate(SelectionPredicates.createSelectAnything())
                .build();

        tracker.addObserver(new SelectionTracker.SelectionObserver() {
            @Override
            public void onSelectionChanged() {
                super.onSelectionChanged();
                int size = tracker.getSelection().size();
                selectedNumber.set(size);
                Log.d(TAG, "onSelectionChanged: " + size);
                if (size == 0) {
                    binding.toolbar.getMenu().findItem(R.id.action_sort).setVisible(true);
                    binding.toolbar.getMenu().findItem(R.id.action_settings).setVisible(true);
                    binding.toolbar.getMenu().findItem(R.id.action_delete).setVisible(false);
                } else {
                    binding.toolbar.getMenu().findItem(R.id.action_sort).setVisible(false);
                    binding.toolbar.getMenu().findItem(R.id.action_settings).setVisible(false);
                    binding.toolbar.getMenu().findItem(R.id.action_delete).setVisible(true);
                }
            }
        });
        linkAdapter.setTracker(tracker);
    }

    private void addWebLink(String link) {
        Call<String> call = RetrofitClient.getApiService().getWebLink(link);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                String url = call.request().url().toString();
                String title = HtmlParser.getPageTitle(response.body());
                String imgUrl = HtmlParser.getImageUrl(url, response.body());
                WebLink webLink = new WebLink(url, imgUrl, title);
                int itemPosition = linkAdapter.add(webLink);
                binding.webList.scrollToPosition(itemPosition);
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
