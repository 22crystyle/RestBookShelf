package org.example.utils.data;

import org.example.dto.request.AuthorRequest;
import org.example.dto.response.AuthorResponse;
import org.example.entity.Author;

public class AuthorData {
    public static final Author DEFAULT_ENTITY = entity().build();
    public static final AuthorRequest DEFAULT_REQUEST = request().build();
    public static final AuthorResponse DEFAULT_RESPONSE = response().build();

    private AuthorData() {
    }

    public static AuthorEntityBuilder entity() {
        return new AuthorEntityBuilder();
    }

    public static AuthorRequestBuilder request() {
        return new AuthorRequestBuilder();
    }

    public static AuthorResponseBuilder response() {
        return new AuthorResponseBuilder();
    }

    public static class AuthorEntityBuilder extends BaseAuthorBuilder<AuthorEntityBuilder> {
        private AuthorEntityBuilder() {
        }

        @Override
        protected AuthorEntityBuilder self() {
            return this;
        }

        public Author build() {
            Author a = new Author();
            a.setId(id);
            a.setName(name);
            a.setBirthYear(birthYear);
            return a;
        }
    }

    public static class AuthorRequestBuilder extends BaseAuthorBuilder<AuthorRequestBuilder> {
        private AuthorRequestBuilder() {
        }

        @Override
        protected AuthorRequestBuilder self() {
            return this;
        }

        public AuthorRequest build() {
            return new AuthorRequest(name, birthYear);
        }
    }

    public static class AuthorResponseBuilder extends BaseAuthorBuilder<AuthorResponseBuilder> {
        private AuthorResponseBuilder() {
        }

        @Override
        protected AuthorResponseBuilder self() {
            return this;
        }

        public AuthorResponse build() {
            return new AuthorResponse(id, name, birthYear);
        }
    }
}
