CREATE TABLE inventory_items (
    sku        VARCHAR(32) PRIMARY KEY,
    available  INT         NOT NULL CHECK (available >= 0),   -- the database is the last line of defence
    reserved   INT         NOT NULL DEFAULT 0 CHECK (reserved >= 0),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    version    BIGINT      NOT NULL DEFAULT 0
);

CREATE TABLE reservations (
    id         UUID        PRIMARY KEY,
    order_id   VARCHAR(64) NOT NULL,
    sku        VARCHAR(32) NOT NULL REFERENCES inventory_items (sku),
    quantity   INT         NOT NULL CHECK (quantity > 0),
    status     VARCHAR(16) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX idx_reservations_order ON reservations (order_id, status);

INSERT INTO inventory_items (sku, available) VALUES
  ('KB-001', 100), ('MS-002', 250), ('MN-003', 20), ('HD-004', 40),
  ('LP-005', 75), ('CB-006', 500), ('WC-007', 0), ('GPU-008', 5);

-- Transactional outbox (see com.shopflow.common.outbox.OutboxWriter)
CREATE TABLE outbox (
    id           UUID         PRIMARY KEY,
    topic        VARCHAR(128) NOT NULL,
    event_key    VARCHAR(128) NOT NULL,
    payload      TEXT         NOT NULL,
    created_at   TIMESTAMPTZ  NOT NULL,
    published_at TIMESTAMPTZ,
    attempts     INT          NOT NULL DEFAULT 0,
    last_error   TEXT,
    trace_id     VARCHAR(64),
    span_id      VARCHAR(32)
);
-- Partial index: the relay only ever scans unpublished rows, so keep that index tiny.
CREATE INDEX idx_outbox_unpublished ON outbox (created_at) WHERE published_at IS NULL;

-- Idempotent consumer bookkeeping (see IdempotentConsumerGuard)
CREATE TABLE processed_events (
    event_id     VARCHAR(64) NOT NULL,
    consumer     VARCHAR(64) NOT NULL,
    processed_at TIMESTAMPTZ NOT NULL,
    PRIMARY KEY (event_id, consumer)
);
