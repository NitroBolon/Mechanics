package com.example.mechanics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Book> books;
    public static final String AUTHOR = "AUTHOR";
    public static final String TITLE = "TITLE";
    public static final String IMAGE = "IMAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
        recyclerView = findViewById(R.id.recyclerview);
    }

    private void fetchBooksData(String query)
    {
        String finalQuery = prepareQuery(query);
        BookService bookService = RetrofitInstance.getRetrofitInstance().create(BookService.class);

        Call<BookContainer> booksApiCall = bookService.findBooks(finalQuery);

        booksApiCall.enqueue(new Callback<BookContainer>()
        {

            @Override
            public void onResponse(Call<BookContainer> call, Response<BookContainer> response) {
                setupBookListVIew(response.body().getBookList());
            }

            @Override
            public void onFailure(Call<BookContainer> call, Throwable t) {
                Snackbar.make(findViewById(R.id.main_view), "Something went wrong... Please try later", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private String prepareQuery(String query)
    {
        String[] queryParts = query.split("\\s+");
        return TextUtils.join("+", queryParts);
    }

    private void setupBookListVIew(List <Book> books)
    {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final BookAdapter adapter = new BookAdapter(books);
        adapter.setBooks(books);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public boolean checkNullOrEmpty(String text)
    {
        return text != null && !TextUtils.isEmpty(text);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.vin_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {

            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchBooksData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public class BookAdapter extends RecyclerView.Adapter<BookHolder>
    {
        private BookAdapter(List<Book> booksx)
        {
            books = booksx;
        }

        @NonNull
        @Override
        public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            LayoutInflater inflator = LayoutInflater.from(APIActivity.this);
            return new BookHolder(inflator,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull BookHolder holder, int position)
        {
            if(books != null)
            {
                Book book = books.get(position);
                holder.bind(book);
            }
            else
            {
                Log.d("APIActivity", "No books");
            }
        }
        void setBooks(List<Book> bookss)
        {
            books = bookss;
            notifyDataSetChanged();
        }
        @Override
        public int getItemCount()
        {
            return books.size();
        }
    }
    private class BookHolder extends RecyclerView.ViewHolder //implements View.OnClickListener
    {
        private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";

        private TextView bookTitleTextView;
        private TextView bookAuthorTextView;
        private ImageView bookCover;
        Book book;
        private BookHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.vin_list_item,parent,false));
            bookTitleTextView = itemView.findViewById(R.id.book_title);
            bookAuthorTextView = itemView.findViewById(R.id.book_author);
            bookCover = itemView.findViewById(R.id.img_cover);
            //itemView.setOnClickListener(this);
        }
        private void bind(Book book)
        {
            this.book = book;
            if(book != null && checkNullOrEmpty(book.getTitle()) && book.getAuthors() != null)
            {
                bookTitleTextView.setText(book.getTitle());
                bookAuthorTextView.setText(TextUtils.join(", ", book.getAuthors()));
                if(book.getCover() != null)
                {
                    Picasso.with(itemView.getContext())
                            .load(IMAGE_URL_BASE + book.getCover() + "-L.jpg")
                            .placeholder(R.drawable.ic_book_black_24dp).into(bookCover);
                }
                else
                {
                    bookCover.setImageResource(R.drawable.ic_book_black_24dp);
                }
            }
        }

        /*@Override
        public void onClick(View v)
        {
            String x=TextUtils.join(", ", book.getAuthors());
            Intent intent = new Intent(APIActivity.this, DetailsActivity.class);
            intent.putExtra(AUTHOR, x);
            intent.putExtra(TITLE,book.getTitle());
            intent.putExtra(IMAGE, book.getCover());
            Log.d("MainActivity", ""+ book.getCover());
            startActivity(intent);
        }*/
    }
}