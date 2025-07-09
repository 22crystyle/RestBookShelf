package org.example.utils.data;

import org.example.dto.request.BookRequest;
import org.example.dto.request.BookUpdateRequest;
import org.example.dto.response.AuthorResponse;
import org.example.dto.response.BookResponse;
import org.example.entity.Author;
import org.example.entity.Book;

public class BookData {
    public static final Book DEFAULT_ENTITY = entity().build();
    public static final BookRequest DEFAULT_REQUEST = request().build();
    public static final BookResponse DEFAULT_RESPONSE = response().build();
    public static final BookUpdateRequest DEFAULT_UPDATE_REQUEST = updateRequest().build();

    private BookData() {
    }

    public static BookEntityBuilder entity() {
        return new BookEntityBuilder();
    }

    public static BookRequestBuilder request() {
        return new BookRequestBuilder();
    }

    public static BookUpdateRequestBuilder updateRequest() {
        return new BookUpdateRequestBuilder();
    }

    public static BookResponseBuilder response() {
        return new BookResponseBuilder();
    }

    public static class BookEntityBuilder extends BaseBookBuilder<BookEntityBuilder> {
        private Long id = 1L;
        private Author author = AuthorData.DEFAULT_ENTITY;

        private BookEntityBuilder() {
        }

        public BookEntityBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public BookEntityBuilder withAuthor(Author author) {
            this.author = author;
            return this;
        }

        @Override
        protected BookEntityBuilder self() {
            return this;
        }

        public Book build() {
            Book b = new Book();
            b.setId(id);
            b.setTitle(title);
            b.setAuthor(author);
            b.setPublishedYear(publishedYear);
            b.setGenre(genre);
            return b;
        }
    }

    public static class BookRequestBuilder extends BaseBookBuilder<BookRequestBuilder> {
        private Long authorId = AuthorData.DEFAULT_ENTITY.getId();

        private BookRequestBuilder() {
        }

        public BookRequestBuilder withAuthorId(Long authorId) {
            this.authorId = authorId;
            return this;
        }

        @Override
        protected BookRequestBuilder self() {
            return this;
        }

        public BookRequest build() {
            return new BookRequest(title, authorId, publishedYear, genre);
        }
    }

    public static class BookResponseBuilder extends BaseBookBuilder<BookResponseBuilder> {
        private Long id = 1L;
        private AuthorResponse author = AuthorData.DEFAULT_RESPONSE;

        private BookResponseBuilder() {
        }

        public BookResponseBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public BookResponseBuilder withAuthor(AuthorResponse author) {
            this.author = author;
            return this;
        }

        @Override
        protected BookResponseBuilder self() {
            return this;
        }

        public BookResponse build() {
            return new BookResponse(id, title, author, publishedYear, genre);
        }
    }

    public static class BookUpdateRequestBuilder extends BaseBookBuilder<BookUpdateRequestBuilder> {
        private Long authorId = AuthorData.DEFAULT_ENTITY.getId();

        private BookUpdateRequestBuilder() {
        }

        public BookUpdateRequestBuilder withAuthorId(Long authorId) {
            this.authorId = authorId;
            return this;
        }

        @Override
        protected BookUpdateRequestBuilder self() {
            return this;
        }

        public BookUpdateRequest build() {
            return new BookUpdateRequest(title, authorId, publishedYear, genre);
        }
    }
}
