package com.example.wereadv10.ui;

import androidx.annotation.NonNull;

import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.books.Book;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class bookSearch {
    private String searchInput; // , authorName;
    private List<Book> bookList;
    private dbSetUp dbSetUp = new dbSetUp();

    public bookSearch(String bookTitle) {
        this.searchInput = bookTitle;
        bookList = new ArrayList<Book>();
        search();
    }

    public String getSearchInput() {
        return searchInput;
    }

    public void setSearchInput(String searchInput) {
        this.searchInput = searchInput;
    }

    private List<Book> search(){
        dbSetUp.db.collection("books")
                .whereEqualTo("book_title", searchInput)
                .whereEqualTo("author", searchInput)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                             Book book = document.toObject(Book.class);
                             System.out.println("AMANI"+ book.getBook_title());
                             bookList.add(book);

                        }
                    }
                }
                });
        return bookList;
    }
}
