INSERT INTO users(email,password,certificate,active)VALUES('stefan','$2a$10$nMmyl6RrEES4RkxJgsplwOWWt8NaCPVHMfhxDZo9ZaKRqHTvZQlM.',NULL,true)
INSERT INTO users(email,password,certificate,active)VALUES('boris','$2a$10$tma4RAW5S/MlxPbGjxAG..azzeWOsHIxzJ3y8YPiobMnfzsULjlPu',NULL,true)

INSERT INTO authority(name)VALUES('ADMIN')
INSERT INTO authority(name)VALUES('REGULAR')

INSERT INTO user_authority(user_id,authority_id)VALUES(1,1)
INSERT INTO user_authority(user_id,authority_id)VALUES(2,2)