CREATE TABLE "user"
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    email      VARCHAR(255),
    name       VARCHAR(255),
    user_group VARCHAR(10),
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE "user"
    ADD CONSTRAINT uc_user_email UNIQUE (email);

-- Location

CREATE TABLE location
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat     DOUBLE PRECISION                        NOT NULL,
    lon     DOUBLE PRECISION                        NOT NULL,
    address VARCHAR(255),
    CONSTRAINT pk_location PRIMARY KEY (id)
);

-- Event

CREATE TABLE event (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   title VARCHAR(120) NOT NULL,
   annotation VARCHAR(2000) NOT NULL,
   description VARCHAR(7000),
   category_id BIGINT NOT NULL,
   initiator_id BIGINT NOT NULL,
   location_id BIGINT NOT NULL,
   paid BOOLEAN NOT NULL,
   created_on TIMESTAMP WITHOUT TIME ZONE,
   published_on TIMESTAMP WITHOUT TIME ZONE,
   event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   participant_limit INTEGER NOT NULL,
   request_moderation BOOLEAN NOT NULL,
   state INTEGER,
   views BIGINT NOT NULL,
   CONSTRAINT pk_event PRIMARY KEY (id)
);

ALTER TABLE event ADD CONSTRAINT FK_EVENT_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE event ADD CONSTRAINT FK_EVENT_ON_INITIATOR FOREIGN KEY (initiator_id) REFERENCES "user" (id);

ALTER TABLE event ADD CONSTRAINT FK_EVENT_ON_LOCATION FOREIGN KEY (location_id) REFERENCES location (id);

-- Request to event

CREATE TABLE event_request
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id     BIGINT,
    organizer_id BIGINT,
    visitor_id   BIGINT,
    request      BOOLEAN                                 NOT NULL,
    CONSTRAINT pk_eventrequest PRIMARY KEY (id)
);

ALTER TABLE event_request
    ADD CONSTRAINT FK_EVENTREQUEST_ON_EVENT FOREIGN KEY (event_id) REFERENCES event (id);

ALTER TABLE event_request
    ADD CONSTRAINT FK_EVENTREQUEST_ON_ORGANIZER FOREIGN KEY (organizer_id) REFERENCES "user" (id);

ALTER TABLE event_request
    ADD CONSTRAINT FK_EVENTREQUEST_ON_VISITOR FOREIGN KEY (visitor_id) REFERENCES "user" (id);

-- Category

CREATE TABLE category
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(50),
    CONSTRAINT pk_category PRIMARY KEY (id)
);

-- Compilation

CREATE TABLE compilation
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN                                 NOT NULL,
    title  VARCHAR(255),
    CONSTRAINT pk_compilation PRIMARY KEY (id)
);

CREATE TABLE compilation_events
(
    compilation_id BIGINT NOT NULL,
    events_id      BIGINT NOT NULL
);

ALTER TABLE compilation_events
    ADD CONSTRAINT fk_comeve_on_compilation FOREIGN KEY (compilation_id) REFERENCES compilation (id);

ALTER TABLE compilation_events
    ADD CONSTRAINT fk_comeve_on_event FOREIGN KEY (events_id) REFERENCES event (id);