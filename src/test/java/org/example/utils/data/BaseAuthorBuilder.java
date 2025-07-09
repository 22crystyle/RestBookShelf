package org.example.utils.data;

public abstract class BaseAuthorBuilder<T extends BaseAuthorBuilder<T>> {
    protected Long id = 1L;
    protected String name = "Author";
    protected Integer birthYear = 1970;

    public T withId(Long id) {
        this.id = id;
        return self();
    }

    public T withName(String name) {
        this.name = name;
        return self();
    }

    public T withBirthYear(Integer year) {
        this.birthYear = year;
        return self();
    }

    protected abstract T self();
}
