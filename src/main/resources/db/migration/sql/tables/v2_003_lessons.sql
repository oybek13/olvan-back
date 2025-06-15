ALTER TABLE olvan_lessons
ALTER COLUMN date_begin TYPE DATE USING date_begin::DATE,
ALTER COLUMN date_begin SET NOT NULL;
