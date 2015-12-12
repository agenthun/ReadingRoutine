package com.agenthun.readingroutine.datastore;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/11/30 上午8:19.
 */
public class Book implements Parcelable {
    public static final String TAG = "BOOK";

    private String id;
    private String title;
    private String author;
    private String authorInfo;
    private String publisher;
    private String publishDate;
    private String price;
    private String page;
    private double rate;
    private String tag;
    private String content;
    private String summary;
    private String bitmap;
    private int reviewCount;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorInfo() {
        return authorInfo;
    }

    public void setAuthorInfo(String authorInfo) {
        this.authorInfo = authorInfo;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static Creator<Book> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            Book book = new Book();
            book.id = in.readString();
            book.title = in.readString();
            book.author = in.readString();
            book.authorInfo = in.readString();
            book.publisher = in.readString();
            book.publishDate = in.readString();
            book.price = in.readString();
            book.page = in.readString();
            book.rate = in.readDouble();
            book.tag = in.readString();
            book.content = in.readString();
            book.summary = in.readString();
            book.bitmap = in.readString();
            book.reviewCount = in.readInt();
            book.url = in.readString();
            return book;
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(authorInfo);
        dest.writeString(publisher);
        dest.writeString(publishDate);
        dest.writeString(price);
        dest.writeString(page);
        dest.writeDouble(rate);
        dest.writeString(tag);
        dest.writeString(content);
        dest.writeString(summary);
        dest.writeString(bitmap);
        dest.writeInt(reviewCount);
        dest.writeString(url);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", authorInfo='" + authorInfo + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", price='" + price + '\'' +
                ", page='" + page + '\'' +
                ", rate=" + rate +
                ", tag='" + tag + '\'' +
                ", content='" + content + '\'' +
                ", summary='" + summary + '\'' +
                ", bitmap='" + bitmap + '\'' +
                ", reviewCount=" + reviewCount +
                ", url='" + url + '\'' +
                '}';
    }
}
