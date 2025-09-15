-- Change the table companies_phones
ALTER TABLE companies_phones DROP FOREIGN KEY fk__companies_phones__companies_id__companies__id;
ALTER TABLE companies_phones DROP PRIMARY KEY;
ALTER TABLE companies_phones ADD PRIMARY KEY (companies_id, phone);
ALTER TABLE companies_phones ADD CONSTRAINT fk__companies_phones__companies_id__companies__id
  FOREIGN KEY (companies_id) REFERENCES companies(id)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

-- Change the table companies_emails
ALTER TABLE companies_emails DROP FOREIGN KEY fk__companies_emails__companies_id__companies__id;
ALTER TABLE companies_emails DROP PRIMARY KEY;
ALTER TABLE companies_emails ADD PRIMARY KEY (companies_id, email);
ALTER TABLE companies_emails ADD CONSTRAINT fk__companies_emails__companies_id__companies__id
  FOREIGN KEY (companies_id) REFERENCES companies(id)
  ON DELETE CASCADE
  ON UPDATE CASCADE;