CREATE INDEX IF NOT EXISTS event_publication_serialized_event_hash_idx ON event_publication USING hash(serialized_event);
CREATE INDEX IF NOT EXISTS event_publication_by_completion_date_idx ON event_publication (completion_date);

CREATE INDEX IF NOT EXISTS event_publication_archive_serialized_event_hash_idx ON event_publication_archive USING hash(serialized_event);
CREATE INDEX IF NOT EXISTS event_publication_archive_by_completion_date_idx ON event_publication_archive (completion_date);

ALTER TABLE event_publication ADD COLUMN IF NOT EXISTS completion_attempts INT;
ALTER TABLE event_publication ADD COLUMN IF NOT EXISTS last_resubmission_date TIMESTAMP WITH TIME ZONE;
ALTER TABLE event_publication ADD COLUMN IF NOT EXISTS status TEXT;

ALTER TABLE event_publication_archive ADD COLUMN IF NOT EXISTS completion_attempts INT;
ALTER TABLE event_publication_archive ADD COLUMN IF NOT EXISTS last_resubmission_date TIMESTAMP WITH TIME ZONE;
ALTER TABLE event_publication_archive ADD COLUMN IF NOT EXISTS status TEXT;
