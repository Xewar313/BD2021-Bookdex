package com.example.bd2021bookdex.apiconnection;

import com.example.bd2021bookdex.BookSearcher;
import com.example.bd2021bookdex.database.DatabaseModifier;
import com.example.bd2021bookdex.database.DatabaseSearcher;
import com.example.bd2021bookdex.database.entities.AuthorEntity;
import com.example.bd2021bookdex.database.entities.BookEntity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Component
public class ApiSearcher extends BookSearcher {

    DatabaseModifier DM;
    DatabaseSearcher SE;
    @Autowired
    public ApiSearcher(DatabaseModifier x, DatabaseSearcher s) {
        DM = x;
        SE = s;
    }
    
    public List<BookEntity> getBooks(int x) throws IOException {
        Gson gson = new Gson();
        int i = 0;
        List<BookEntity> toReturn = new LinkedList<>();
        while (toReturn.size() < x) {
            JsonObject body = gson.fromJson(getBooksAsJson(i * 40), JsonObject.class);
            JsonArray books;
            try {
                books = body.get("items").getAsJsonArray();
            } catch (Exception e) {
                return toReturn;
            }
            for (JsonElement book : books) {
                BookEntity toAdd;
                JsonObject bookObject = book.getAsJsonObject();
                String googleId = bookObject.get("id").getAsString();
                toAdd = SE.getByGoogleId(googleId);
                if (toAdd != null) {
                    continue;
                }
                JsonObject volumeObject = bookObject.get("volumeInfo").getAsJsonObject();
                String title;
                if (volumeObject.get("title") == null)
                    continue;
                title = volumeObject.get("title").getAsString();
                List<String> authors = new LinkedList<>();
                if (volumeObject.get("authors") != null)
                    for (JsonElement e : volumeObject.get("authors").getAsJsonArray()) {
                        authors.add(e.getAsString());
                    }

                String desc = null;
                if (volumeObject.get("description") != null) {
                    desc = volumeObject.get("description").getAsString();
                    desc = desc.substring(0, Math.min(3999, desc.length()));
                }
                String link = null;
                if (volumeObject.get("imageLinks") != null && volumeObject.get("imageLinks").getAsJsonObject().get("thumbnail") != null)
                    link = volumeObject.get("imageLinks").getAsJsonObject().get("thumbnail").getAsString();
                String category = null;
                if (volumeObject.get("categories") != null) {
                    category = volumeObject.get("categories").getAsJsonArray().get(0).getAsString();
                }
                int pageCount = 0;
                if (volumeObject.get("pageCount") != null) {
                    pageCount = volumeObject.get("pageCount").getAsInt();
                }
                Set<AuthorEntity> authorsEntity = new HashSet<>();

                for (String n : authors) {
                    AuthorEntity temp = SE.getAuthor(n);
                    if (temp == null) {
                        temp = new AuthorEntity(n);
                        DM.addToDb(temp);
                    }
                    authorsEntity.add(temp);
                }
                toAdd = new BookEntity(title, link, pageCount, googleId, category, null, desc, authorsEntity);

                DM.addToDb(toAdd);
                toReturn.add(toAdd);
                if (toReturn.size() == x)
                    return toReturn;
            }
            i++;
        }
        return toReturn;
    }
    
    public String getBooksAsJson(int start) throws IOException{
        StringBuilder urlCreator = new StringBuilder();
        urlCreator.append("https://www.googleapis.com/books/v1/volumes?q=");
        if (titles.isEmpty() && authors.isEmpty() && categories.isEmpty()) {
            return null;
        }
        if (!titles.isEmpty()) {
            urlCreator.append("intitle:");
            for (String title : titles) {
                urlCreator.append(title).append('+');
            }
        }
        if (!authors.isEmpty()) {
            urlCreator.append("inauthor:");
            for (String author : authors) {
                urlCreator.append(author).append("+");
            }
        }

        if (!categories.isEmpty()) {
            urlCreator.append("subject:");
            for (String category : categories) {
                urlCreator.append(category).append("+");
            }
        }
        urlCreator.deleteCharAt(urlCreator.length() - 1);
        urlCreator.append("&startIndex=").append(start).append("&maxResults=40");
        URL url = new URL(urlCreator.toString());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        con.disconnect();
        return content.toString();
    }
}
