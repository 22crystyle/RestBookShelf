package org.example.utils.data;

public abstract class BaseBookBuilder<T extends BaseBookBuilder<T>> {
    protected String title = "Title";
    protected Integer publishedYear = 1970;
    protected String genre = "Unknown";

    public T withTitle(String title) {
        this.title = title;
        return self();
    }

    public T withPublishedYear(Integer year) {
        this.publishedYear = year;
        return self();
    }

    public T withGenre(String genre) {
        this.genre = genre;
        return self();
    }

    protected abstract T self();
}
